package net.oldhaven.customs;

import net.oldhaven.MegaMod;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SavedServers {
    private Map<String, String> savedServersMap;
    public File savedServersFile;
    public SavedServers(MegaMod megaMod) {
        savedServersMap = new HashMap<>();
    }
    public void saveServers() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(savedServersFile));
            for(Map.Entry<String, String> entry : savedServersMap.entrySet()) {
                String ip = entry.getKey();
                String serverName = entry.getValue();
                printwriter.println(ip + "|" + serverName);
            }
            printwriter.close();
        } catch(Exception exception) {
            System.out.println("Failed to save servers");
            exception.printStackTrace();
        }
    }
    public void readServers() {
        if(savedServersFile.exists()) {
            try {
                BufferedReader bufferedreader = new BufferedReader(new FileReader(savedServersFile));
                for (String s = ""; (s = bufferedreader.readLine()) != null; ) {
                    String[] serverSplit = s.split("\\|");
                    String ip = serverSplit[0];
                    String server = serverSplit[1];
                    savedServersMap.put(ip, server);
                }
            } catch (Exception exception) {
                System.out.println("Failed to load options");
                exception.printStackTrace();
            }
        }
    }

    public void saveServer(String ip, String serverName) {
        savedServersMap.put(ip, serverName);
        saveServers();
    }
    public void removeServer(String ip) {
        savedServersMap.remove(ip);
        saveServers();
    }
    public Map<String, String> getSavedServersMap() {
        return savedServersMap;
    }
    public int selectedServer = -1;
}
