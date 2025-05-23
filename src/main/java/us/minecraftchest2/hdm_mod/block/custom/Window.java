package us.minecraftchest2.hdm_mod.block.custom;

// Import statements...

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import us.minecraftchest2.hdm_mod.world.dimension.ModDimensions;
import us.minecraftchest2.hdm_mod.world.dimension.SimpleTeleporter;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Custom window block class, serving as a dimensional portal between worlds.
 * Inherits directional placement capabilities from HorizontalBlock.
 */
public class Window extends HorizontalBlock {
    /**
     * Constructor for Window block; forwards the properties to the parent constructor.
     */
    public Window(Properties builder) {
        super(builder);
    }

    // VoxelShapes for visually and physically shaping the block based on orientation.
    // Each direction (N/E/S/W) has a custom composition of rectangular regions.

    /** Shape for the block facing NORTH. */
    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(5, 11, 5, 6, 13, 11),  // Window frame elements (left, etc.)
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),   // Base of window
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),   // Lower window frame
            Block.makeCuboidShape(6, 2, 6, 10, 10, 10),  // Glass pane
            Block.makeCuboidShape(5, 10, 4, 11, 11, 12), // Top frame
            Block.makeCuboidShape(5, 11, 4, 11, 12, 5),  // Decorative details
            Block.makeCuboidShape(5, 11, 11, 11, 14, 12),// Upper right details
            Block.makeCuboidShape(10, 11, 5, 11, 13, 11) // Right vertical side
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    /** Shape for the block facing EAST (rotated). */
    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(5, 11, 5, 11, 13, 6),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(6, 2, 6, 10, 10, 10),
            Block.makeCuboidShape(4, 10, 5, 12, 11, 11),
            Block.makeCuboidShape(11, 11, 5, 12, 12, 11),
            Block.makeCuboidShape(4, 11, 5, 5, 14, 11),
            Block.makeCuboidShape(5, 11, 10, 11, 13, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    /** Shape for the block facing SOUTH. */
    private static final VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(10, 11, 5, 11, 13, 11),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(6, 2, 6, 10, 10, 10),
            Block.makeCuboidShape(5, 10, 4, 11, 11, 12),
            Block.makeCuboidShape(5, 11, 11, 11, 12, 12),
            Block.makeCuboidShape(5, 11, 4, 11, 14, 5),
            Block.makeCuboidShape(5, 11, 5, 6, 13, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    /** Shape for the block facing WEST. */
    private static final VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(5, 11, 10, 11, 13, 11),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(6, 2, 6, 10, 10, 10),
            Block.makeCuboidShape(4, 10, 5, 12, 11, 11),
            Block.makeCuboidShape(4, 11, 5, 5, 12, 11),
            Block.makeCuboidShape(11, 11, 5, 12, 14, 11),
            Block.makeCuboidShape(5, 11, 5, 11, 13, 6)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    /**
     * Handles when a player right-clicks/interacts with this block.
     * Triggers portal teleportation if conditions are met.
     */
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(
            BlockState state,
            World worldIn,
            BlockPos pos,
            PlayerEntity player,
            Hand handIn,
            BlockRayTraceResult hit
    ) {
  if (!worldIn.isRemote() && !player.isCrouching()) {
      MinecraftServer server = worldIn.getServer();
      if (server == null) return ActionResultType.PASS;

      boolean goingToCustom = worldIn.getDimensionKey() != ModDimensions.World1;

      ServerWorld targetWorld = (worldIn.getDimensionKey() == ModDimensions.World1)
              ? server.getWorld(World.OVERWORLD)
              : server.getWorld(ModDimensions.World1);

      if (worldIn.getDimensionKey() == ModDimensions.World1) {
          ServerWorld overWorld = server.getWorld(World.OVERWORLD);
          if (overWorld != null) {
              player.changeDimension(overWorld, new SimpleTeleporter(pos, false));
          }
      }

      if (targetWorld != null) {
          SimpleTeleporter teleporter = new SimpleTeleporter(pos, goingToCustom);
          player.changeDimension(targetWorld, teleporter);
      }

  System.out.println("[DEBUG] Portal activated on server!");
  player.sendMessage(new StringTextComponent("Teleport logic running!"), player.getUniqueID());
      player.sendMessage(new StringTextComponent("Teleport attempted!"), player.getUniqueID());
      return ActionResultType.SUCCESS;

  }
  return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    /**
     * Returns the block's physical outline for rendering and collision.
     * The shape depends on the current facing direction of the block.
     */
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(HORIZONTAL_FACING)) {
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            case EAST:
                return SHAPE_E;
            default:
                return SHAPE_N;
        }
    }

    /**
     * Registers the horizontal facing property with the block state container,
     * enabling the block to store which direction it is facing.
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    /**
     * Determines the block's initial facing based on the placement context.
     * The block will face opposite to the player's direction when placed.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}