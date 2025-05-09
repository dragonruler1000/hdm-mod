package us.minecraftchest2.hdm_mod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import us.minecraftchest2.hdm_mod.block.ModBlocks;

public class ModItemGroup {
    public static final ItemGroup HDM_ITEM_GROUP = new ItemGroup("hdmModItemTab") {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.DUST.get());
        }
    };
    public static final ItemGroup HDM_BLOCK_GROUP = new ItemGroup("hdmModBlockTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.DUST_BLOCK.get());
        }
    };
}
