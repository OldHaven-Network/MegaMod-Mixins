package net.oldhaven.customs.options;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SavedShaders {
    public File shaderFolder;
    public void loadShaders() {
        if(!shaderFolder.exists()) {
            URL folderToURL = getClass().getResource("/shaders/");
            if(folderToURL == null || folderToURL.getFile() == null) {
                System.err.println("Can't find /shaders/ in MegaMod jar");
                return;
            }
            File newShaderFolder = new File(folderToURL.getFile());
            boolean b = shaderFolder.mkdir();
            if(!b) {
                System.err.println("Failed to create /shaders/ folder");
                return;
            }
            File[] files = newShaderFolder.listFiles();
            if(files == null) {
                System.err.println("/shaders/ in MegaMod jar is empty");
                return;
            }
            for (File file : files) {
                if(file.isFile())
                    continue;
                try {
                    File toCopy = new File(shaderFolder, file.getName() +"\\");
                    FileUtils.copyDirectory(file, toCopy);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
