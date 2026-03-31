package com.mollin.loto.logic.main.grid;

import java.util.List;

/**
 * Définit les actions qu'un utilisateur peut effectuer sur une grille.
 *
 * @author MOLLIN Florian
 */
public interface LotoGridInterface {
    /**
     * Effectue un clic sur le nombre donné.
     *
     * @param number Le nombre sur lequel cliquer
     */
    void clic(int number);

    /**
     * Demarque la grille (la vide).
     */
    void clear();

    /**
     * Effectue un retour arriere dans l'historique.
     */
    void undo();

    /**
     * Renvoie la liste des 15 derniers numéros tirés (ordre antéchronologique).
     *
     * @return La liste des 15 derniers numéros.
     */
    List<Integer> getLast15DrawnNumbers();

    /**
     * Renvoie le nombre de numéros actuellement tirés.
     *
     * @return Le nombre de numéros tirés.
     */
    int getDrawnNumbersCount();
}
