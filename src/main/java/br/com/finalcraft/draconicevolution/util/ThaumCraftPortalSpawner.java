package br.com.finalcraft.draconicevolution.util;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.items.ItemDE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.WorldGenEldritchRing;
import thaumcraft.common.lib.world.dim.MazeThread;

public class ThaumCraftPortalSpawner extends ItemDE {

    private WorldGenEldritchRing gen;

    public ThaumCraftPortalSpawner() {
        this.setUnlocalizedName("portalInABox");
        this.setHasSubtypes(false);
        this.setCreativeTab(DraconicEvolution.tabBlocksItems);
        this.setMaxStackSize(1);
        ModItems.register(this);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            gen = new WorldGenEldritchRing();

            gen.chunkX = x;
            gen.chunkZ = z;
            int w = 11 + world.rand.nextInt(6) * 2;
            int h = 11 + world.rand.nextInt(6) * 2;
            gen.width = w;
            gen.height = h;

            if (gen.generate(world, world.rand, x, y, z))
            {
                ThaumcraftWorldGenerator.createRandomNodeAt(world, x, y+2, z, world.rand, false, true, false);
                Thread t = new Thread(new MazeThread(x, z, w, h, world.rand.nextLong()));
                t.start();
                stack.stackSize--;
                if (stack.stackSize <= 0)
                    stack = null;
                return true;
            }
        }

        return false;

    }


}
