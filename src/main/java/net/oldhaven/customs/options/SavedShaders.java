package net.oldhaven.customs.options;

import net.oldhaven.MegaMod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SavedShaders {
    public File shaderFolder;

    private static List<String> shadersLoc = new ArrayList<String>() {
        {
            add("default/base");
            add("default/base");
            add("default/composite");
            add("default/final");
            add("default/gbuffers_basic");
            add("default/gbuffers_hand");
            add("default/gbuffers_terrain");
            add("default/gbuffers_textured");
            add("default/gbuffers_textured_lit");
            add("default/gbuffers_water");
            add("default/gbuffers_weather");

        }
    };
    private static List<String> shaderTypes = new ArrayList<String>() {
        {
            add(".fsh");
            add(".vsh");
        }
    };

    public void loadShaders() {
        Class<MegaMod> mm = MegaMod.class;
        URL folderToURL = mm.getResource("/shaders/");
        if (folderToURL == null || folderToURL.getFile() == null) {
            System.err.println("Can't find /shaders/ in MegaMod jar");
            return;
        }
        if(!shaderFolder.exists()) {
            boolean b = shaderFolder.mkdir();
            if (!b) {
                System.err.println("Failed to create /shaders/ folder");
                return;
            }
        }
        // TODO: Optimize this into a List<Map<?,?>>
        for(String shader : shadersLoc) {
            String[] split = shader.split("/");
            String folder = split[0];
            String fileName = split[1];
            File selfFolder = new File(shaderFolder, folder);
            if(!selfFolder.exists()) {
                boolean b = selfFolder.mkdirs();
                if(!b)
                    continue;
            } else
                continue;
            for(String type : shaderTypes) {
                URL url = mm.getResource("/shaders/"+shader+type);
                Path path;
                try {
                    path = Paths.get(url.toURI());
                    List<String> lines = Files.readAllLines(path);
                    File self = new File(selfFolder, fileName+type);
                    if(!self.exists()) {
                        boolean b = self.createNewFile();
                        if(!b)
                            continue;
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(self));
                    for(String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                    writer.close();
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
