package net.oldhaven.mixins.entity;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderManager;
import net.oldhaven.customs.alexskins.CustomRenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    /*
      ToDo: Fix skins breaking in MultiPlayer servers
      This could be a bit tricky as it seems the model is somehow is active on all players
      even though the code that I'm doing shouldn't mess with others players at all... We'll see
      how this goes in the future I guess.
     */
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 10))
    private Object renderPlayer(Map map, Object key, Object value) {
        map.remove(key);
        map.put(EntityPlayer.class, new CustomRenderPlayer());
        return map;
    }
}
