package br.com.finalcraft.draconicevolution.util;


import com.brandon3055.draconicevolution.common.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class FCArmorHandler {

    // Armor nanoUmArmor                == 96                   |/\ 1.2
    // Armor neutroniumt1Armor          == 144                  |/\ 1.4
    // Armor spectralArmor              == 180                  |/\ 1.8
    // Armor zeusArmor                  == 228                  |/\ 2.0
    // Armor neutroniumt2Armor          == 252 --> 325          |/\ 2.3 -->2,5

    private static int protectionNeutroniumt2Armor = 325 / 6;
    private static double defenceRateNeutroniumt2Armor = 2.5 / 6;

    public static void onPlayerHurt(LivingHurtEvent event){

        final EntityPlayer player = (EntityPlayer) event.entityLiving;

        int protection = 0; //protection que sera retirada do dano base
        double defenceRate = 0; //defenceRate é um numero pelo qual o restante do dano será dividido
        int armorParts = 0; //armorParts é o contador que permitirá saber qual armadura o jogador esta vestindo

        final boolean helmet = player.inventory.armorInventory[3] != null;
        final boolean chest = player.inventory.armorInventory[2] != null;
        final boolean legs = player.inventory.armorInventory[1] != null;
        final boolean boots = player.inventory.armorInventory[0] != null;

        if (helmet){
            if (player.inventory.armorInventory[3].getItem() == ModItems.neutroniumT2Helm){
                protection += protectionNeutroniumt2Armor;
                defenceRate += defenceRateNeutroniumt2Armor;
            }
        }
        if (chest){
            if (player.inventory.armorInventory[2].getItem() == ModItems.neutroniumT2Chest){
                protection += protectionNeutroniumt2Armor * 2;
                defenceRate += defenceRateNeutroniumt2Armor * 2;
            }
        }
        if (legs){
            if (player.inventory.armorInventory[1].getItem() == ModItems.neutroniumT2Leggs){
                protection += protectionNeutroniumt2Armor * 2;
                defenceRate += defenceRateNeutroniumt2Armor * 2;
            }
        }
        if (boots){
            if (player.inventory.armorInventory[0].getItem() == ModItems.neutroniumT2Boots){
                protection += protectionNeutroniumt2Armor;
                defenceRate += defenceRateNeutroniumt2Armor;
            }
        }

        double dmg = event.ammount;
        if (protection != 0){
            dmg = dmg - protection;
            if (dmg > 0){
                if (defenceRate > 1.0){
                    dmg = dmg / defenceRate;
                }
            }
            else {
                dmg = 0;
            }
            event.ammount = (float) dmg;
        }
    }
}
