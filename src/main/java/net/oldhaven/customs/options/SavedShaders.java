package net.oldhaven.customs.options;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SavedShaders {
    public File shaderFolder;
    public void loadShaders() {
        URL folderToURL = getClass().getResource("/shaders/");
        if (folderToURL == null || folderToURL.getFile() == null) {
            System.err.println("Can't find /shaders/ in MegaMod jar");
            return;
        }
        File newShaderFolder = new File(folderToURL.getFile());
        if(!shaderFolder.exists()) {
            boolean b = shaderFolder.mkdir();
            if (!b) {
                System.err.println("Failed to create /shaders/ folder");
                return;
            }
        }
        File[] files = newShaderFolder.listFiles();
        if(files == null) {
            System.err.println("/shaders/ in MegaMod jar is empty");
            return;
        }
        for (File file : files) {
            if(file.isFile())
                continue;
            File toCopy = new File(shaderFolder, file.getName() +"\\");
            if(toCopy.exists())
                continue;
            try {
                FileUtils.copyDirectory(file, toCopy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
