package us.minecraftchest2.hdm_mod.world.dimension;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import us.minecraftchest2.hdm_mod.block.ModBlocks;
import us.minecraftchest2.hdm_mod.block.custom.Window;


import java.util.function.Function;

public class SimpleTeleporter implements ITeleporter {
    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideDimension = true;

    public SimpleTeleporter(BlockPos pos, boolean insideDim) {
        thisPos = pos;
        insideDimension = insideDim;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destinationWorld,
                              float yaw, Function<Boolean, Entity> repositionEntity) {
        entity = repositionEntity.apply(false);
        double y = 61;

        if (!insideDimension) {
            y = thisPos.getY();
        }

        BlockPos destinationPos = new BlockPos(thisPos.getX(), y, thisPos.getZ());


        int tries = 0;
        while (
                (
                        destinationWorld.getBlockState(destinationPos).getMaterial() != Material.AIR &&
                                !destinationWorld.getBlockState(destinationPos).isReplaceable(Fluids.WATER) &&
                                !destinationWorld.getBlockState(destinationPos).getMaterial().isReplaceable() ||
                                destinationWorld.getBlockState(destinationPos).getMaterial() == Material.LAVA ||
//                                destinationWorld.getBlockState(destinationPos).getBlock().isFire(destinationWorld, destinationPos, null) ||
                                destinationWorld.getBlockState(destinationPos.up()).getMaterial() != Material.AIR &&
                                        !destinationWorld.getBlockState(destinationPos.up()).isReplaceable(Fluids.WATER) &&
                                        !destinationWorld.getBlockState(destinationPos.up()).getMaterial().isReplaceable() ||
                                destinationWorld.getBlockState(destinationPos.up()).getMaterial() == Material.LAVA
//                                destinationWorld.getBlockState(destinationPos.up()).getBlock().isFire(destinationWorld, destinationPos.up(), null)
                ) && tries < 25
        )
        {
            destinationPos = destinationPos.up(2);
            tries++;
        }

        entity.setPositionAndUpdate(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());

        if (insideDimension) {
            boolean doSetBlock = true;
            for (BlockPos checkPos : BlockPos.getAllInBoxMutable(destinationPos.down(10).west(10), destinationPos.up(10).east(10))) {
                if (destinationWorld.getBlockState(checkPos).getBlock() instanceof Window) {
                    doSetBlock = false;
                    break;
                }
            }
            if (doSetBlock) {
                destinationWorld.setBlockState(destinationPos, ModBlocks.PORTAL_BLOCK.get().getDefaultState());
            }
        }

        return entity;
    }
}