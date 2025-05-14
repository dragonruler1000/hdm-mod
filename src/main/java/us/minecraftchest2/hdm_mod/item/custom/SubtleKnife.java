package us.minecraftchest2.hdm_mod.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ScoreTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import us.minecraftchest2.hdm_mod.block.ModBlocks;


public class SubtleKnife extends Item {


    public SubtleKnife(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (player == null) return ActionResultType.PASS;

        // Get the block being clicked
        BlockPos blockPos = context.getPos();
        Direction face = context.getFace(); // The face the player clicked

        // Offset to the block face to place the block on the adjacent block (like right-clicking a wall places on it)
        BlockPos placePos = blockPos.offset(face);

        // Server-side logic only: place a block
        if (!world.isRemote) {
            world.setBlockState(placePos, ModBlocks.PORTAL_BLOCK.get().getDefaultState());
        }

        return ActionResultType.SUCCESS;
    }



}
