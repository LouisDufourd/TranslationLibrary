package com.plaglefleau.translate;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a translation management class that handles reading, writing, and updating language translations.
 */
public class Translation {

    // Holds the translation data for different languages
    private final Map<String, Map<String, String>> TRANSLATION = new HashMap<>();

    // Represents the type of the language translations
    private final Type LANG_TYPE = new TypeToken<Map<String, String>>() {}.getType();

    // Path to the folder containing language files
    private final String LANG_FOLDER_PATH = "./lang";

    /**
     * Initializes a new Translation instance and updates languages by reading translation files from the lang folder.
     *
     * @throws IOException if an I/O error occurs while updating languages
     */
    public Translation() throws IOException {
        updateLanguages();
    }

    /**
     * Reads all language translations from the lang folder, processing multiple files.
     *
     * @throws IOException if an I/O error occurs while updating languages
     */
    public void updateLanguages() throws IOException {
        for (String langPath : FileEditing.getFolderFileNames(LANG_FOLDER_PATH, "json")) {
            updateLanguage(langPath);
        }
    }

    /**
     * Writes all language translations to the lang folder, processing multiple files.
     *
     * @throws IOException if an I/O error occurs while saving languages
     */
    public void saveLanguages() throws IOException {
        for (String langKey : TRANSLATION.keySet()) {
            saveLanguage(langKey);
        }
    }

    /**
     * Updates the language translation by reading a specific file from the lang folder.
     *
     * @param langName the language file name (without extension)
     * @throws IOException if an I/O error occurs while updating the language
     */
    public void updateLanguage(String langName) throws IOException {
        String path = "%1s/%2s.json".formatted(LANG_FOLDER_PATH, langName);
        Map<String, String> lang = FileEditing.readJsonFile(Path.of(path), LANG_TYPE);
        TRANSLATION.put(langName, lang);
    }

    /**
     * Saves the language translation by writing to a specific file in the lang folder.
     *
     * @param langName the language file name (without extension)
     * @throws IOException if an I/O error occurs while saving the language
     */
    public void saveLanguage(String langName) throws IOException {
        Map<String, String> lang = TRANSLATION.get(langName);
        String path = "%1s/%2s.json".formatted(LANG_FOLDER_PATH, langName);
        FileEditing.writeJsonFile(Path.of(path), lang);
    }

    /**
     * Sets the language translation during runtime and updates the corresponding language file.
     *
     * @param langName the language file name (without extension)
     * @param lang     a map with translation keys and values
     * @throws IOException if an I/O error occurs while setting the language
     */
    public void setLanguage(String langName, Map<String, String> lang) throws IOException {
        TRANSLATION.put(langName, lang);
        saveLanguage(langName);
        updateLanguage(langName);
    }

    /**
     * Retrieves the translation for a specific language.
     *
     * @param langName the language file name (without extension)
     * @return a map with translation keys and values
     */
    public Map<String, String> getLang(String langName) {
        return TRANSLATION.get(langName);
    }

    /**
     * Sets a specific translation for a specific language and updates the corresponding language file.
     *
     * @param langName        the language file name (without extension)
     * @param translationKey  the translation key
     * @param translation     the translation
     * @throws IOException if an I/O error occurs while setting the translation
     */
    public void setTraduction(String langName, String translationKey, String translation) throws IOException {
        if (TRANSLATION.get(langName) == null) {
            Map<String, String> lang = new HashMap<>();
            lang.put(translationKey, translation);
            TRANSLATION.put(langName, lang);
        } else {
            TRANSLATION.get(langName).put(translationKey, translation);
        }
        saveLanguage(langName);
        updateLanguage(langName);
    }

    /**
     * Sets a specific translation for all specified languages and updates the corresponding language files.
     *
     * @param translationKey the translation key
     * @param translations   a map with language names as keys and translations as values
     * @throws IOException if an I/O error occurs while setting the translations
     */
    public void setTraduction(String translationKey, @NotNull Map<String, String> translations) throws IOException {
        translations.forEach((langName, translation) -> {
            try {
                setTraduction(langName, translationKey, translation);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        saveLanguages();
        updateLanguages();
    }

    /**
     * Retrieves a specific translation for a specific language.
     *
     * @param langName        the language file name (without extension)
     * @param translationKey  the translation key
     * @return the specific translation
     */
    public String getTraduction(String langName, String translationKey) {
        return TRANSLATION.get(langName).get(translationKey);
    }
}
