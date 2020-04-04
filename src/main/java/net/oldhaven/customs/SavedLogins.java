package net.oldhaven.customs;

import net.oldhaven.MegaMod;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SavedLogins {
    private MegaMod megaMod;
    public File savedLoginsFile;
    public SavedLogins(MegaMod megaMod) {
        this.megaMod = megaMod;
    }
    private void saveLogins() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(savedLoginsFile));
            for(Map.Entry<String, SavedLogin> entry : savedLoginMap.entrySet()) {
                Map<String, String> usernames = entry.getValue().usernames;
                StringBuilder list = new StringBuilder();
                int i = 0;
                for(Map.Entry<String, String> userEntry : usernames.entrySet()) {
                    list.append(userEntry.getKey()).append(" ").append(userEntry.getValue());
                    if(i+1 < usernames.size())
                        list.append(":<>:");
                    i++;
                }
                printwriter.println(entry.getKey() + "|" + list.toString());
            }
            printwriter.close();
        } catch(Exception exception) {
            System.out.println("Failed to save logins");
            exception.printStackTrace();
        }
    }
    public void readLogins() {
        if(savedLoginsFile.exists()) {
            try {
                BufferedReader bufferedreader = new BufferedReader(new FileReader(savedLoginsFile));
                for (String s = ""; (s = bufferedreader.readLine()) != null; ) {
                    String[] serverSplit = s.split("\\|");
                    String server = serverSplit[0];
                    SavedLogin savedLogin;
                    if(savedLoginMap.containsKey(server)) {
                        savedLogin = savedLoginMap.get(server);
                        savedLoginMap.remove(server);
                    } else
                        savedLogin = new SavedLogin();
                    String[] logins = serverSplit[1].split(":<>:");
                    for(String login : logins) {
                        String[] split = login.split(" ");
                        savedLogin.add(split[0], split[1]);
                    }
                    savedLoginMap.put(server, savedLogin);
                }
            } catch (Exception exception) {
                System.out.println("Failed to load options");
                exception.printStackTrace();
            }
        }
    }

    private Map<String /*server ip*/, SavedLogin> savedLoginMap = new HashMap<>();
    public class SavedLogin {
        private SavedLogin() {
            usernames = new HashMap<>();
        }
        protected Map<String /*usernames*/, String /*login code*/> usernames;
        public int getSize() {
            return usernames.size();
        }
        public String getIndex(int i) {
            return (String)usernames.keySet().toArray()[i];
        }
        public String /*login code*/ getName(String name) {
            if(usernames.containsKey(name))
                return usernames.get(name);
            return null;
        }
        public void add(String username, String login) {
            this.usernames.put(username, login);
        }
        public void remove(String username) {
            this.usernames.remove(username);
            saveLogins();
        }
    }
    public SavedLogin getSavedLoginsByIP(String ip) {
        if(savedLoginMap.containsKey(ip))
            return savedLoginMap.get(ip);
        return null;
    }
    public Map<String, SavedLogin> getSavedLoginsMap() {
        return savedLoginMap;
    }
    public int getSavedLoginsSize() {
        return savedLoginMap.size();
    }
    public void saveLogin(String username, String login) {
        String ip = megaMod.getConnectedServer();
        if(ip == null) {
            System.out.println("CAN'T SAVE LOGIN: Not connected to server");
            return;
        }
        if(savedLoginMap.containsKey(ip)) {
            SavedLogin savedLogin = savedLoginMap.get(ip);
            savedLogin.add(username, login);
            savedLoginMap.replace(ip, savedLogin);
        } else {
            SavedLogin savedLogin = new SavedLogin();
            savedLogin.add(username, login);
            savedLoginMap.put(ip, savedLogin);
        }
        saveLogins();
    }
    public void removeAllLogins(String ip) {
        savedLoginMap.remove(ip);
        saveLogins();
    }
}
