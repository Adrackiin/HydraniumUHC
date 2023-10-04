package fr.adrackiin.hydranium.uhc.utils;

import java.io.File;

public class Files {

    public static void remove(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    remove(file);
                } else {
                    file.delete();
                }
            }
        }
    }
}
