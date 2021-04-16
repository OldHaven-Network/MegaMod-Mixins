package net.oldhaven.customs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlinePlayer {
    private static final Map<String, OnlinePlayer> playerMap = new HashMap<>();
    private static final Map<String, List<OnlinePlayer>> objectiveMap = new HashMap<>();
    public static OnlinePlayer get(String name) {
        if(playerMap.containsKey(name))
            return playerMap.get(name);
        OnlinePlayer onlinePlayer = new OnlinePlayer(name);
        playerMap.put(name, onlinePlayer);
        return onlinePlayer;
    }
    public static void remove(String name) {
        playerMap.remove(name);
    }
    public static void refresh() {
        playerMap.clear();
    }

    public static List<OnlinePlayer> getObjectiveList(String listType) {
        if(objectiveMap.containsKey(listType))
            return objectiveMap.get(listType);
        List<OnlinePlayer> list = new ArrayList<>();
        objectiveMap.put(listType, list);
        return list;
    }
    private static void addToObjectiveList(String listType, OnlinePlayer onlinePlayer) {
        List<OnlinePlayer> onlinePlayers = getObjectiveList(listType);
        onlinePlayers.add(onlinePlayer);
        objectiveMap.replace(listType, onlinePlayers);
    }
    private static void removeFromObjectiveList(String listType, OnlinePlayer onlinePlayer) {
        List<OnlinePlayer> onlinePlayers = getObjectiveList(listType);
        onlinePlayers.remove(onlinePlayer);
        objectiveMap.replace(listType, onlinePlayers);
    }

    private boolean isTyping;
    private long typingAtMillis;

    private final String username;
    OnlinePlayer(String name) {
        this.username = name;
        this.isTyping = false;
    }

    public String getUsername() {
        return this.username;
    }
    public boolean isTyping() {
        long time = System.currentTimeMillis();
        long millis = time - this.typingAtMillis;
        if(millis > 15000) {
            this.setTyping(false);
            return false;
        }
        return this.isTyping;
    }
    public long getTypingAtMillis() {
        return this.typingAtMillis;
    }
    public void setTyping(boolean b) {
        this.isTyping = b;
        this.typingAtMillis = System.currentTimeMillis();
        if(b)
            addToObjectiveList("typing", this);
        else
            removeFromObjectiveList("typing", this);
    }
}
