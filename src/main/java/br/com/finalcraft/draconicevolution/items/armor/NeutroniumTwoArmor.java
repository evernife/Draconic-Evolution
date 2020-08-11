package br.com.finalcraft.draconicevolution.items.armor;

import br.com.finalcraft.evernifeworldrpg.common.items.armors.data.ArmorUtil;
import br.com.finalcraft.evernifeworldrpg.common.items.armors.data.IFCArmor;
import codechicken.lib.math.MathHelper;
import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.lib.DelayedTask;
import com.brandon3055.brandonscore.utils.InventoryUtils;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.api.itemconfig.BooleanConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.IntegerConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.integration.ModHelper;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.items.armor.WyvernArmor;
import com.brandon3055.draconicevolution.items.tools.ToolStats;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Collection;

import static com.brandon3055.draconicevolution.api.itemconfig.IItemConfigField.EnumControlType.SLIDER;
import static net.minecraft.inventory.EntityEquipmentSlot.*;

/**
 * Created by EverNife on 08/08/2020.
 */
public class NeutroniumTwoArmor extends WyvernArmor implements IFCArmor {

    private static ArmorMaterial neutroniumMaterial = EnumHelper.addArmorMaterial("neutroniumttwoArmor", "draconicevolution:neutroniumtwo_armor", -1, new int[]{3, 6, 8, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);

    public NeutroniumTwoArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(neutroniumMaterial, renderIndexIn, equipmentSlotIn);
    }

    public NeutroniumTwoArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
    }

    private final double[] protection = splitValueIn(ArmorUtil.PROTECTION_NEUTRONIUMT2, new int[]{3,3,3,2});
    private final double[] defenceHate = splitValueIn(ArmorUtil.DEFENCERATE_NEUTRONIUMT2, new int[]{3,3,3,2});
    @Override
    public double getArmorProtection(EntityEquipmentSlot equipmentSlot) {
        return protection[equipmentSlot.getIndex()];
    }

    @Override
    public double getArmorDefenceRatio(EntityEquipmentSlot equipmentSlot) {
        return defenceHate[equipmentSlot.getIndex()];
    }

    //region Upgrade

    @Override
    public int getMaxUpgradeLevel(ItemStack stack, String upgrade) {
        return 3;
    }

    //endregion

    //region Config

    @Override
    public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
        if (armorType == HEAD) {
            registry.register(stack, new BooleanConfigField("armorNV", false, "config.field.armorNV.description"));
            registry.register(stack, new BooleanConfigField("armorNVLock", false, "config.field.armorNVLock.description"));
            registry.register(stack, new BooleanConfigField("armorAutoFeed", false, "config.field.armorAutoFeed.description"));
            //TODO RE Integrate thaumcraft
        }
        if (armorType == CHEST) {
            registry.register(stack, new IntegerConfigField("armorFSpeedModifier", 0, 0, MathHelper.clip(DEConfig.flightSpeedLimit != -1 ? DEConfig.flightSpeedLimit : 600, 0, 1200), "config.field.armorFSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
//            registry.register(stack, new IntegerConfigField("armorVFSpeedModifier", 0, 0, 600, "config.field.armorVFSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
            registry.register(stack, new BooleanConfigField("armorInertiaCancel", false, "config.field.armorInertiaCancel.description"));
            registry.register(stack, new BooleanConfigField("armorFlightLock", false, "config.field.armorFlightLock.description"));
        }
        if (armorType == LEGS) {
            int u = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.MOVE_SPEED);
            int i = 200 + (100 * u) + (Math.max(u - 1, 0) * 100) + (Math.max(u - 2, 0) * 100);
            registry.register(stack, new IntegerConfigField("armorSpeedModifier", 0, 0, i, "config.field.armorSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
            registry.register(stack, new BooleanConfigField("armorSpeedFOVWarp", false, "config.field.armorSpeedFOVWarp.description"));
        }
        if (armorType == FEET) {
            int u = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.JUMP_BOOST);
            int i = 200 + (100 * u) + (Math.max(u - 1, 0) * 100) + (Math.max(u - 2, 0) * 100);
            registry.register(stack, new IntegerConfigField("armorJumpModifier", 0, 0, i, "config.field.armorSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
            registry.register(stack, new BooleanConfigField("armorHillStep", true, "config.field.armorHillStep.description"));
        }
        if (armorType == FEET || armorType == LEGS || armorType == CHEST) {
            registry.register(stack, new BooleanConfigField("sprintBoost", false, "config.field.sprintBoost.description"));
        }

        registry.register(stack, new BooleanConfigField("hideArmor", false, "config.field.hideArmor.description"));

        return registry;
    }

    //endregion

    //region Rendering

    @SideOnly(Side.CLIENT)
    public ModelBiped model_invisible;

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (ToolConfigHelper.getBooleanField("hideArmor", itemStack)) {
            if (model_invisible == null) {
                model_invisible = new ModelBiped() {
                    @Override
                    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                    }
                };
            }
            return model_invisible;
        }
        return _default;
    }

    @Override
    public float getProtectionPoints(ItemStack stack) {
        int upgradeLevel = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.SHIELD_CAPACITY);
        float points = ToolStats.NEUTRONIUMTIER2_BASE_SHIELD_CAPACITY * getProtectionShare() * (upgradeLevel + 1);
        return points;
    }
    
    @Override
    public float getRecoveryRate(ItemStack stack) {
        return (float)ToolStats.NEUTRONIUMTIER2_SHIELD_RECOVERY * (1F + UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.SHIELD_RECOVERY));//TODO Balance
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() == DEFeatures.neutroniumtwoHelm) {

            if (world.isRemote) {
                return;
            }

            if (this.getEnergyStored(stack) >= 5000 && clearNegativeEffects(player)) {
                this.modifyEnergy(stack, -5000);
            }

            FoodStats foodStats = player.getFoodStats();
            if (player.ticksExisted % 100 == 0 && ToolConfigHelper.getBooleanField("armorAutoFeed", stack) && foodStats.needFood() && this.getEnergyStored(stack) >= 500) {
                IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack candidate = handler.getStackInSlot(i);
                        if (!candidate.isEmpty() && candidate.getItem() instanceof ItemFood) {
                            ItemFood food = (ItemFood) candidate.getItem();
                            int amount = food.getHealAmount(candidate);
                            if (amount > 0 && food.getHealAmount(candidate) + foodStats.getFoodLevel() <= 20) {
                                candidate = candidate.copy();
                                ItemStack foodStack = handler.extractItem(i, candidate.getCount(), false);

                                if (ItemStack.areItemStacksEqual(foodStack, candidate)) {
                                    foodStats.addStats(food, foodStack);
                                    foodStack = food.onItemUseFinish(foodStack, world, player);
                                    if (world.rand.nextInt(3) == 0) {
                                        DelayedTask.run(20, () -> world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, .5F, world.rand.nextFloat() * 0.1F + 0.9F));
                                    }

                                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.5F + 0.5F * (float)world.rand.nextInt(2), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
                                    foodStack = handler.insertItem(i, foodStack, false);
                                    this.modifyEnergy(stack, -500);
                                    if (!foodStack.isEmpty()) {
                                        InventoryUtils.givePlayerStack(player, foodStack.copy());//I miss being able to just do setStackInSlot...
                                    }
                                    break;
                                }
                                else {
                                    foodStack = handler.insertItem(i, foodStack, false);
                                    if (!foodStack.isEmpty()) {
                                        InventoryUtils.givePlayerStack(player, foodStack.copy());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Potion nv = Potion.getPotionFromResourceLocation("night_vision");

            if (nv == null) {
                return;
            }

            PotionEffect active = player.getActivePotionEffect(nv);
            if (ToolConfigHelper.getBooleanField("armorNV", stack) && (player.world.getLightBrightness(new BlockPos((int) Math.floor(player.posX), (int) player.posY + 1, (int) Math.floor(player.posZ))) < 0.1F || ToolConfigHelper.getBooleanField("armorNVLock", stack))) {
                player.addPotionEffect(new PotionEffect(nv, 500, 0, false, false));
            }
            else if (active != null && ToolConfigHelper.getBooleanField("armorNVLock", stack)) {
                player.removePotionEffect(nv);
            }
        }

        super.onArmorTick(world, player, stack);
    }

    @SuppressWarnings("unchecked")
    public boolean clearNegativeEffects(Entity par3Entity) {
        boolean flag = false;
        if (par3Entity.ticksExisted % 20 == 0) {
            if (par3Entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) par3Entity;

                Collection<PotionEffect> potions = player.getActivePotionEffects();

                if (player.isBurning()) {
                    player.extinguish();
                }
                for (PotionEffect potion : potions) {
                    if (potion.getPotion().isBadEffect()) {
                        if (potion.getPotion() == MobEffects.MINING_FATIGUE && ModHelper.isHoldingCleaver(player)) {
                            continue;
                        }

                        player.removePotionEffect(potion.getPotion());
                        flag = true;

                        break;
                    }
                }
            }
        }
        return flag;
    }


    @Override
    public boolean hasHillStep(ItemStack stack, EntityPlayer player) {
        return ToolConfigHelper.getBooleanField("armorHillStep", stack);
    }

    @Override
    public float getFireResistance(ItemStack stack) {
        return 1F;
    }

    @Override
    public boolean[] hasFlight(ItemStack stack) {
        return new boolean[]{true, ToolConfigHelper.getBooleanField("armorFlightLock", stack), ToolConfigHelper.getBooleanField("armorInertiaCancel", stack)};
    }

    @Override
    public float getFlightSpeedModifier(ItemStack stack, EntityPlayer player) {
        int value = ToolConfigHelper.getIntegerField("armorFSpeedModifier", stack);
        if (DEConfig.flightSpeedLimit > -1 && value > DEConfig.flightSpeedLimit) {
            value = DEConfig.flightSpeedLimit;
        }

        float modifier = value / 100F;

        if (ToolConfigHelper.getBooleanField("sprintBoost", stack) && !BrandonsCore.proxy.isSprintKeyDown()) {
            modifier /= 5F;
        }

        return modifier;
    }

    @Override
    public float getFlightVModifier(ItemStack stack, EntityPlayer player) {
        float modifier = ToolConfigHelper.getIntegerField("armorVFSpeedModifier", stack) / 100F;

        if (ToolConfigHelper.getBooleanField("sprintBoost", stack) && !BrandonsCore.proxy.isSprintKeyDown()) {
            modifier /= 5F;
        }

        return modifier;
    }

    @Override
    public int getEnergyPerProtectionPoint() {
        return ToolStats.NEUTRONIUMTIER2_SHIELD_RECHARGE_COST;
    }

    //endregion

    //region Energy

    @Override
    protected int getCapacity(ItemStack stack) {
        int level = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.RF_CAPACITY);

        if (level == 0) {
            return ToolStats.NEUTRONIUMTIER2_BASE_CAPACITY;
        }
        else {
            return ToolStats.NEUTRONIUMTIER2_BASE_CAPACITY * (int) Math.pow(2, level + 1);
        }
    }

    @Override
    protected int getMaxReceive(ItemStack stack) {
        return ToolStats.NEUTRONIUMTIER2_MAX_RECIEVE;
    }

    //endregion
}
