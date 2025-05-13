package us.minecraftchest2.hdm_mod.item.custom;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.PlayerEntity;
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
//        BlockPos pos =
        PlayerEntity player = context.getPlayer();

        if(world.isRemote) {
//            world.setBlockState()
        }
        return super.onItemUseFirst(stack, context);
    }


}
