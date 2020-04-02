package net.oldhaven.customs;

import net.oldhaven.MegaMod;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CustomKeybinds {
    public static class SavedKey {
        SavedKey(int keyType, int key, String name) {
            this.keyType = keyType;
            this.key = key;
            this.name = name;
        }
        private int keyType = 0;
        private int key = 0;
        private String name;
        public int getKeyType() {
            return keyType;
        }
        public int getKey() {
            return key;
        }
        public String getName() {
            return name;
        }
    }
    private Map<String, SavedKey> savedKeysMap;
    private interface OneVarRunnable {
        void run(boolean b);
    }
    private Map<String, OneVarRunnable> keyActions;
    public Map<Integer, String> keyIntegers;
    public File savedKeysFile;
    public CustomKeybinds() {
        savedKeysMap = new HashMap<>();
        keyActions = new HashMap<>();
        keyIntegers = new HashMap<>();
    }

    public static Map<String, Integer> defaultKeybinds = new HashMap<String, Integer>() {
        {
            put("Forward", Keyboard.KEY_W);
            put("Left", Keyboard.KEY_A);
            put("Right", Keyboard.KEY_D);
            put("Back", Keyboard.KEY_S);
            put("Jump", Keyboard.KEY_SPACE);
            put("Sneak", Keyboard.KEY_LSHIFT);
            put("Drop", Keyboard.KEY_Q);
            put("Inventory", Keyboard.KEY_E);
            put("Chat", Keyboard.KEY_T);
            put("Toggle Fog", Keyboard.KEY_F);
            put("PlayerList", Keyboard.KEY_TAB);
            put("Fly", Keyboard.KEY_R);
            put("Sprint", Keyboard.KEY_LCONTROL);
        }
    };

    public Map<String, SavedKey> getSavedKeys() {
        return savedKeysMap;
    }

    private long lastJump = 0;
    private boolean didLastJump = false;
    private void onKey_Jump(boolean b) {
        Integer i = MegaMod.getInstance().getCustomGameSettings().getOptionI("Double Jump to Fly");
        if(i != null && i == 1) {
            long jump = MegaMod.getSystemTime();
            if (jump < (lastJump + 1000)) {
                if (didLastJump) {
                    MegaMod.getInstance().isFlying = !MegaMod.getInstance().isFlying;
                    didLastJump = false;
                } else
                    didLastJump = true;
            } else
                didLastJump = false;
            lastJump = jump;
        }
    }
    private void onKey_Sprint(boolean b) {
        if(!b) {
            int i = MegaMod.getInstance().getCustomGameSettings().getOptionI("Hold Sprint");
            if(i != 1)
                return;
        }
        MegaMod.getInstance().isSprinting = b;
    }
    private void onKey_Fly(boolean b) {
        boolean flying = MegaMod.getInstance().isFlying;
        MegaMod.getInstance().isFlying = !flying;
    }

    private void onKey_PlayerList(boolean b) {
        MegaMod.getInstance().playerList = b;
    }

    public void onKey(String name, boolean b) {
        if(this.keyActions.containsKey(name))
            this.keyActions.get(name).run(b);
    }

    public boolean isKeyDisabled(String name) {
        return !keyActions.containsKey(name);
    }

    public void setKey(String name, int key) {
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        Integer i = gs.getOptionI("Disable "+name);
        if(i == null || i != 1)
            this.savedKeysMap.get(name).key = key;
        saveKeys();
    }

    public void saveKeys() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(savedKeysFile));
            for(Map.Entry<String, SavedKey> entry : savedKeysMap.entrySet()) {
                String keyName = entry.getKey();
                Integer keyId = entry.getValue().key;
                printwriter.println(keyId + ":" + keyName);
            }
            printwriter.close();
        } catch(Exception exception) {
            System.out.println("Failed to save servers");
            exception.printStackTrace();
        }
    }

    public void keyCheck() {
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        if(gs.getOptionI("Disable PlayerList") != 1)
            keyActions.put("PlayerList", this::onKey_PlayerList);
        else
            keyActions.remove("PlayerList");
        savedKeysMap.put("PlayerList", new SavedKey(1, Keyboard.KEY_TAB, "PlayerList"));
        if(gs.getOptionI("Disable Fly") != 1)
            keyActions.put("Fly", this::onKey_Fly);
        else
            keyActions.remove("Fly");
        savedKeysMap.put("Fly", new SavedKey(0, Keyboard.KEY_R, "Fly"));
        if(gs.getOptionI("Disable Sprint") != 1)
            keyActions.put("Sprint", this::onKey_Sprint);
        else
            keyActions.remove("Sprint");
        savedKeysMap.put("Sprint", new SavedKey(1, Keyboard.KEY_LCONTROL, "Sprint"));
        keyActions.put("Jump", this::onKey_Jump);
    }
    public void saveIntegers() {
        keyIntegers.clear();
        for(Map.Entry<String, SavedKey> keyEntry : savedKeysMap.entrySet()) {
            String name = keyEntry.getKey();
            SavedKey savedKey = keyEntry.getValue();
            int key = savedKey.getKey();
            keyIntegers.put(key, name);
        }
    }

    public void loadKeys() {
        keyCheck();
        if(savedKeysFile.exists()) {
            try {
                BufferedReader bufferedreader = new BufferedReader(new FileReader(savedKeysFile));
                for (String s = ""; (s = bufferedreader.readLine()) != null; ) {
                    String[] serverSplit = s.split(":");
                    int key = Integer.parseInt(serverSplit[0]);
                    String name = serverSplit[1];
                    if(!savedKeysMap.containsKey(name)) {
                        continue;
                    }
                    SavedKey savedKey = savedKeysMap.get(name);
                    savedKey.key = key;
                    savedKeysMap.put(name, savedKey);
                }
            } catch (Exception exception) {
                System.out.println("Failed to load options");
                exception.printStackTrace();
            }
        }
        saveIntegers();
        saveKeys();
    }
}
