package us.minecraftchest2.hdm_mod.world.dimension;

// Imports omitted for brevity

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import us.minecraftchest2.hdm_mod.block.ModBlocks;
import us.minecraftchest2.hdm_mod.block.custom.Window;

import java.util.function.Function;

/**
 * Handles teleportation of entities between dimensions, with custom logic for portals and safety.
 */
public class SimpleTeleporter implements ITeleporter {
    // The destination position for the teleport
    public static BlockPos thisPos = BlockPos.ZERO;
    
    // True if teleporting into the custom dimension, false if to overworld
    public static boolean insideDimension = true;

    // Records if the most recent teleport was successful
    private boolean success = false;

    /**
     * @return True if the last teleportation was successful (a safe landing spot found), false otherwise
     */
    public boolean wasSuccessful() {
        return success;
    }

    /**
     * Constructor sets the target position and whether we're inside our custom dimension.
     * @param pos Destination position
     * @param insideDim Are we inside the custom dimension after teleport?
     */
    public SimpleTeleporter(BlockPos pos, boolean insideDim) {
        thisPos = pos;
        insideDimension = insideDim;
    }

    /**
     * Checks if a block position is unsafe (solid, unreplaceable, lava, or fire).
     * @param world The world to check in
     * @param pos The position to check
     * @return True if the position is unsafe, false if safe
     */
    private boolean isUnsafe(ServerWorld world, BlockPos pos){
        BlockState state = world.getBlockState(pos);
        Material material = state.getMaterial();
        // Not air AND not easily replaced (by water/etc) OR is lava or fire is considered unsafe
        return material != Material.AIR &&
                !state.isReplaceable(Fluids.WATER) &&
                !material.isReplaceable() ||
                material == Material.LAVA ||
                material == Material.FIRE;
    }

    /**
     * Handles the actual placing of the entity when teleporting between dimensions.
     * Will try to find a safe spot upwards, up to 25 tries.
     * Also, optionally places a portal block at the destination.
     */
    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destinationWorld,
                              float yaw, Function<Boolean, Entity> repositionEntity) {
        // Move entity to the origin position
        entity = repositionEntity.apply(false);

        // Default target y coordinate for the destination
        double y = 61;

        // If not going inside the custom dimension, use the saved Y
        if (!insideDimension) {
            y = thisPos.getY();
        }

        // Calculate the intended destination block position
        BlockPos destinationPos = new BlockPos(thisPos.getX(), y, thisPos.getZ());

        // Try to find a safe spot (air, replaceable, not lava/fire) by moving up, max 25 attempts
        int tries = 0;
        while ((isUnsafe(destinationWorld, destinationPos) || isUnsafe(destinationWorld, destinationPos.up())) && tries < 25) {
            destinationPos = destinationPos.up(2);
            tries++;
        }

        // If it couldn't find a safe spot after 25 attempts, fail teleport
        if (tries >= 25) {
            this.success = false;
            return entity;
        }

        // Move entity to found safe location
        entity.setPositionAndUpdate(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());
        this.success = true;

        // When entering a custom dimension, make a portal block at the destination unless one already exists nearby
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

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ResourceLocation dimensionKey = destinationWorld.getDimensionKey().getLocation();
            // Only send the welcome message if NOT the overworld
            if (!dimensionKey.getPath().equals("overworld")) {
                // (Same nice formatting as before)
                String path = dimensionKey.getPath();
                String dimensionName;
                switch (path) {
                    case "the_nether":
                        dimensionName = "the Nether";
                        break;
                    case "the_end":
                        dimensionName = "the End";
                        break;
                    default:
                        dimensionName =  path.replace('_', ' ');
                        dimensionName = dimensionName.substring(0, 1).toUpperCase() + dimensionName.substring(1);
                        break;
                }
                player.sendMessage(new StringTextComponent("Welcome to " + dimensionName + "!"), player.getUniqueID());
            }
        }

        return entity;
    }
}