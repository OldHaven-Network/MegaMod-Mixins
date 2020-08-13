package net.oldhaven.customs;

import net.minecraft.src.Block;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
    private static HashMap<Integer, Block> blockMapById = new HashMap<>();
    private static HashMap<String, Block> blockMapByStr = new HashMap<>();
    public static void addBlock(Block block) {
        System.out.println(block.getBlockName());
        blockMapById.put(block.blockID, block);
        blockMapByStr.put(block.getBlockName(), block);
    }
    public static Block isBlockById(int id) {
        if(blockMapById.containsKey(id))
            return blockMapById.get(id);
        return null;
    }
    public static Block isBlockByStr(String blockName) {
        for(Map.Entry<String, Block> entry : blockMapByStr.entrySet()) {
            if(entry.getKey().startsWith(blockName))
                return entry.getValue();
            else if(entry.getKey().contains(blockName))
                return entry.getValue();
        }
        return null;
    }
}
