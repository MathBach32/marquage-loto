package com.mollin.loto.logic.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mollin.loto.logic.main.grid.LotoGrid;
import com.mollin.loto.logic.main.utils.Constants;
import com.mollin.loto.logic.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Gestion du chargement/sauvegarde la grille
 *
 * @author MOLLIN Florian
 */
public class GridStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(GridStorage.class);

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Constructeur privé car classe utilitaire
     */
    private GridStorage() {
    }

    /**
     * Sauvegarde de la grille
     *
     * @param grid        La grille
     * @param profileName Le nom du profil
     */
    public static void saveGrid(LotoGrid grid, String profileName) {
        try {
            Path filePath = Paths.get(Constants.File.PROFILES_FOLDER_PATH, profileName, Constants.File.GRID_FILE_NAME);
            String json = GSON.toJson(grid, LotoGrid.class);
            boolean success = FileUtils.write(filePath, json);
            if (!success) {
                LOGGER.error("Impossible de sauvegarder la grille pour le profil : {}", profileName);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur inattendue lors de la sauvegarde de la grille pour le profil : {}", profileName, e);
        }
    }

    /**
     * Chargement de la grille
     *
     * @param profileName Le nom du profil
     * @return La grille chargé (ou une grille par défaut si erreur)
     */
    public static LotoGrid loadGrid(String profileName) {
        try {
            Path filePath = Paths.get(Constants.File.PROFILES_FOLDER_PATH, profileName, Constants.File.GRID_FILE_NAME);
            if (filePath == null) {
                LOGGER.warn("Le chemin de la grille est null pour le profil : {}", profileName);
                return getDefaultGrid(profileName);
            }
            List<String> fileContent = FileUtils.read(filePath);
            if (fileContent == null) {
                LOGGER.info("Aucun fichier de grille trouvé ou lecture impossible, utilisation d'une grille par défaut pour : {}", profileName);
                return getDefaultGrid(profileName);
            }
            String json = String.join("", fileContent);
            return GSON.fromJson(json, LotoGrid.class);
        } catch (Exception e) {
            LOGGER.error("Erreur inattendue lors du chargement de la grille pour le profil : {}. Utilisation de la grille par défaut.", profileName, e);
            return getDefaultGrid(profileName);
        }
    }

    /**
     * Renvoi la grille par défaut (grille vierge)
     *
     * @param profileName le nom du profil
     * @return La grille
     */
    private static LotoGrid getDefaultGrid(String profileName) {
        return new LotoGrid(Constants.Size.HISTORY_DEPTH, Constants.Number.MAX_NUMBER, profileName);
    }
}
