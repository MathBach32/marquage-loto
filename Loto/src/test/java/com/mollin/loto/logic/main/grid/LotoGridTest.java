package com.mollin.loto.logic.main.grid;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de la LotoGrid
 *
 * @author MOLLIN Florian
 */
public class LotoGridTest {
    private static final int HISTORY_DEPTH = 42;
    private static final int MAX_NUMBER = 90;

    /**
     * Test un enchainement de retours arrieres inutiles.
     */
    @Test
    public void testWrongUndos() {
        final int NB_UNDO = 100;
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 0; i < NB_UNDO; i++) {
            grid.undo();
        }
    }

    /**
     * Test un depassement de la taille de l'historique.
     */
    @Test
    public void testHistoryOverflow() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 0; i < HISTORY_DEPTH * 2; i++) {
            grid.clic(MAX_NUMBER);
        }
    }

    /**
     * Test de clics successifs.
     */
    @Test
    public void testClic() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 1; i <= MAX_NUMBER; i++) {
            grid.clic(i);
            assertThat(grid.getActualGrid().getNumbers()).isEqualTo(rangeSet(1, i));
        }
    }

    /**
     * Remplit la grille puis effectue des retours arrieres successifs.
     */
    @Test
    public void testUndo() {
        LotoGrid grid = new LotoGrid(MAX_NUMBER + 1, MAX_NUMBER);
        for (int i = 1; i <= MAX_NUMBER; i++) {
            grid.clic(i);
        }
        for (int i = MAX_NUMBER; i >= 0; i--) {
            assertThat(grid.getActualGrid().getNumbers()).isEqualTo(rangeSet(1, i));
            grid.undo();
        }
    }

    /**
     * Test du demarquage de la grille.
     */
    @Test
    public void testClear() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 1; i <= MAX_NUMBER; i++) {
            grid.clic(i);
        }
        grid.clear();
        assertThat(grid.getActualGrid().isEmpty()).isTrue();
    }

    /**
     * Test du bon fonctionnement de l'historique lors d'un retour arriere apres
     * une demarque.
     */
    @Test
    public void testHistoryClear() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH * 2, MAX_NUMBER);
        for (int i = 1; i <= MAX_NUMBER; i++) {
            grid.clic(i);
        }
        grid.clear();
        grid.undo();
        assertThat(grid.getActualGrid().getNumbers()).isEqualTo(rangeSet(1, MAX_NUMBER));
    }

    /**
     * Test qu'un nombre s'efface bien lors d'un double clic.
     */
    @Test
    public void testDoubleClic() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 1; i <= MAX_NUMBER; i++) {
            grid.clic(i);
        }
        for (int i = MAX_NUMBER; i >= 1; i--) {
            assertThat(grid.getActualGrid().getNumbers()).isEqualTo(rangeSet(1, i));
            grid.clic(i);
        }
        assertThat(grid.getActualGrid().isEmpty()).isTrue();
    }

    /**
     * Test le comptage total des numéros tirés.
     */
    @Test
    public void testDrawnNumbersCount() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        assertThat(grid.getDrawnNumbersCount()).isEqualTo(0);
        for (int i = 1; i <= 5; i++) {
            grid.clic(i);
            assertThat(grid.getDrawnNumbersCount()).isEqualTo(i);
        }
    }

    /**
     * Test la liste partielle des derniers numéros tirés (moins de 15).
     */
    @Test
    public void testLast15DrawnNumbersPartial() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        grid.clic(10);
        grid.clic(20);
        grid.clic(30);

        List<Integer> last15 = grid.getLast15DrawnNumbers();
        assertThat(last15).containsExactly(30, 20, 10);
        assertThat(grid.getDrawnNumbersCount()).isEqualTo(3);
    }

    /**
     * Test la liste complète des 15 derniers numéros tirés (plus de 15 tirages).
     */
    @Test
    public void testLast15DrawnNumbersFull() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 1; i <= 20; i++) {
            grid.clic(i);
        }

        List<Integer> last15 = grid.getLast15DrawnNumbers();
        assertThat(last15).hasSize(15);
        assertThat(last15).containsExactly(20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6);
        assertThat(grid.getDrawnNumbersCount()).isEqualTo(20);
    }

    /**
     * Test l'historique et le compteur après une désélection (faux clic).
     */
    @Test
    public void testHistoryAndCountAfterUncheck() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        grid.clic(10);
        grid.clic(20);
        grid.clic(30);
        grid.clic(40);

        // Désélectionner 20
        grid.clic(20);

        List<Integer> last15 = grid.getLast15DrawnNumbers();
        assertThat(last15).containsExactly(40, 30, 10);
        assertThat(grid.getDrawnNumbersCount()).isEqualTo(3);
    }

    /**
     * Test l'historique et le compteur après un retour en arrière.
     */
    @Test
    public void testHistoryAndCountAfterUndo() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        grid.clic(10);
        grid.clic(20);
        grid.clic(30);

        grid.undo();

        List<Integer> last15 = grid.getLast15DrawnNumbers();
        assertThat(last15).containsExactly(20, 10);
        assertThat(grid.getDrawnNumbersCount()).isEqualTo(2);
    }

    /**
     * Test l'historique et le compteur après une démarque (clear).
     */
    @Test
    public void testHistoryAndCountAfterClear() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH, MAX_NUMBER);
        for (int i = 1; i <= 5; i++) {
            grid.clic(i);
        }

        grid.clear();

        assertThat(grid.getLast15DrawnNumbers()).isEmpty();
        assertThat(grid.getDrawnNumbersCount()).isEqualTo(0);
    }

    /**
     * Test l'historique lors d'un retour arrière après une démarque.
     */
    @Test
    public void testHistoryAndCountUndoAfterClear() {
        LotoGrid grid = new LotoGrid(HISTORY_DEPTH * 2, MAX_NUMBER);
        for (int i = 1; i <= MAX_NUMBER; i++) {
            grid.clic(i);
        }
        grid.clear();
        grid.undo();

        assertThat(grid.getDrawnNumbersCount()).isEqualTo(MAX_NUMBER);
        List<Integer> last15 = grid.getLast15DrawnNumbers();
        assertThat(last15).hasSize(15);
        assertThat(last15.get(0)).isEqualTo(MAX_NUMBER);
    }

    /**
     * Renvoit l'ensemble contenant tous les entiers naturels allant de 'a' a
     * 'b'.
     *
     * @param a Borne inférieure (comprise)
     * @param b Borne supérieure (comprise)
     * @return L'ensemble des entiers naturels entre 'a' et 'b' (inclus)
     */
    private static Set<Integer> rangeSet(int a, int b) {
        return IntStream.rangeClosed(a, b).boxed().collect(Collectors.toSet());
    }
}
