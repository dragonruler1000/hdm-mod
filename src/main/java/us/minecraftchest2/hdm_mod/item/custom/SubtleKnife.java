package us.minecraftchest2.hdm_mod.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SubtleKnife extends Item {


    public SubtleKnife(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();

        if(!world.isRemote) {
            BlockPos another_pos;

        }
        return super.onItemUseFirst(stack, context);
    }

}
