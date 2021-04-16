package net.oldhaven.customs;

import net.minecraft.src.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ItemKeep {
    public static class ItemFulfill {
        public final int id;
        public final String name;
        public float hardness = -1;
        public float damage = -1;
        public final ItemStack stack;
        public ItemFulfill destroyWith = null;
        ItemFulfill(String name, int id) {
            this.stack = new ItemStack(id, 1, 0);
            this.name = name;
            this.id = id;
        }

        void setHardness(float f) {
            this.hardness = f;
            this.destroyWith = keepToDestroyWith(this);
        }

        public void setDamage(float damage) {
            this.damage = damage;
        }
    }
    private static final HashMap<String, ItemFulfill> listID = new HashMap<>();
    private static final HashMap<String, ItemFulfill> mapByStr = new HashMap<>();
    public static HashMap<String, ItemFulfill> getFullMap() {
        return mapByStr;
    }

    public static ItemFulfill add(String name, int id, float hardness, float damage) {
        if(name.equals("stonebrick"))
            name = "cobblestone";
        ItemFulfill fulfill = new ItemFulfill(name, id);
        if(hardness > 0)
            fulfill.setHardness(hardness);
        if(damage > 0)
            fulfill.setDamage(damage);
        mapByStr.put(name, fulfill);
        listID.put(id+"", fulfill);
        return fulfill;
    }
    public static int get(String s) {
        int id;
        if((id= getIdByStr(s)) != 0)
            return id;
        if((id= confirmID(s)) != 0)
            return id;
        return 0;
    }
    @Nullable
    public static ItemFulfill getByStr(String name) {
        if(mapByStr.containsKey(name))
            return mapByStr.get(name);
        return null;
    }
    public static int getIdByStr(String name) {
        if(mapByStr.containsKey(name))
            return mapByStr.get(name).id;
        return 0;
    }
    public static int confirmID(String id) {
        if(listID.containsKey(id))
            return listID.get(id).id;
        return 0;
    }

    private static ItemFulfill keepToDestroyWith(ItemFulfill current) {
        switch(current.id) {
            case 1:
            case 4:
                return getByStr("pickaxeWood");
            case 5:
            case 47:
                return getByStr("hatchetwood");
            default:
                return null;
        }
    }
}
