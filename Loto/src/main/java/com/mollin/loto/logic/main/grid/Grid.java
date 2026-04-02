package com.mollin.loto.logic.main.grid;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Une grille représente les nombres deja tirés lors d'une partie de loto.<br>
 * Ne stocke pas d'historique.
 *
 * @author MOLLIN Florian
 */
public class Grid {
    /**
     * Représente les nombres tirés de la grille.
     */
    private final Set<Integer> numbers;

    /**
     * Historique chronologique des tirages dans cette grille.
     */
    private final List<Integer> chronologicalDraws;

    /**
     * Constructeur d'une grille vide.
     */
    public Grid() {
        this.numbers = new LinkedHashSet<>();
        this.chronologicalDraws = new ArrayList<>();
    }

    /**
     * Constructeur d'une grille avec un ensemble de nombres déjà tiré.
     *
     * @param numbers L'ensemble de nombres déjà tiré
     */
    public Grid(Set<Integer> numbers) {
        this.numbers = numbers;
        this.chronologicalDraws = new ArrayList<>(numbers);
    }

    /**
     * Constructeur privé pour cloner une grille en incluant l'ordre chronologique.
     *
     * @param numbers L'ensemble de nombres déjà tirés.
     * @param chronologicalDraws L'historique chronologique des nombres tirés.
     */
    private Grid(Set<Integer> numbers, List<Integer> chronologicalDraws) {
        this.numbers = numbers;
        this.chronologicalDraws = chronologicalDraws;
    }

    /**
     * Constructeur d'une grille copiant une autre grille.
     *
     * @param grid La grille à copier
     */
    public Grid(Grid grid) {
        this(new LinkedHashSet<>(grid.numbers), new ArrayList<>(grid.chronologicalDraws));
    }

    /**
     * Test si une grille est vide.
     *
     * @return vrai si la grill est vide
     */
    public boolean isEmpty() {
        return this.numbers.isEmpty();
    }

    /**
     * Test si la grille contient un nombre donné.
     *
     * @param number Le nombre à tester.
     * @return Vrai si le nombre est dans la grille.
     */
    public boolean contains(int number) {
        return this.numbers.contains(number);
    }

    /**
     * Renvoie l'ensemble des nombres tirés de la grille.
     *
     * @return L'ensemble des nombres tirés de la grille.
     */
    public Set<Integer> getNumbers() {
        return this.numbers;
    }

    /**
     * Renvoie la liste chronologique des numéros tirés.
     *
     * @return La liste des numéros tirés par ordre chronologique.
     */
    public List<Integer> getChronologicalDraws() {
        return new ArrayList<>(this.chronologicalDraws);
    }

    /**
     * Inverse un nombre dans la grille.<br>
     * Si le nombre est présent, il est enlevé, sinon il est ajouté.
     *
     * @param number Le nombre à inverser
     */
    private void switchNumber(int number) {
        if (this.numbers.contains(number)) {
            this.numbers.remove(number);
            this.chronologicalDraws.remove(Integer.valueOf(number));
        } else {
            this.numbers.add(number);
            this.chronologicalDraws.add(number);
        }
    }

    /**
     * Crée une nouvelle grille identique avec le nombre inversé donné.
     *
     * @param number Le nombre à inverser sur la nouvelle grille.
     * @return La nouvelle grille.
     */
    public Grid derive(int number) {
        Grid resultGrid = new Grid(this);
        resultGrid.switchNumber(number);
        return resultGrid;
    }
}
