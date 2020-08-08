package com.brandon3055.draconicevolution.integration;

import com.brandon3055.draconicevolution.common.utills.LogHelper;
import com.gamerforea.eventhelper.util.EventUtils;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class ModHookEventHelper {

   private static boolean initialized = false;

   public static void initialize() {
      initialized = Loader.isModLoaded("EventHelper");
      if(initialized) {
         LogHelper.info("[FCEventHelper DirectInjection] - EventHelper has been enabled on DradonicEvolution!");
      }
   }

   public static boolean isInitialized() {
      return initialized;
   }

   public static boolean cantBreak(@Nonnull EntityPlayer player, double x, double y, double z){
      if (initialized){
         return EventUtils.cantBreak(player,x,y,z);
      }
      return false;
   }

   public static boolean cantAttack(Entity attacker, Entity victim){
      if (true){
         return EventUtils.cantDamage(attacker,victim);
      }
      return false;
   }


}
