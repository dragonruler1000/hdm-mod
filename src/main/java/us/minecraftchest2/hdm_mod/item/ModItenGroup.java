package us.minecraftchest2.hdm_mod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItenGroup {
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
            return null;
        }
    };
}
