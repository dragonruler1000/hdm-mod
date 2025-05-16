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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import us.minecraftchest2.hdm_mod.world.dimension.ModDimensions;
import us.minecraftchest2.hdm_mod.world.dimension.SimpleTeleporter;

import javax.annotation.Nullable;
import java.util.stream.Stream;



/**
 * A custom window block that acts as a dimensional portal when activated
 * Extends HorizontalBlock to support directional placement
 */
public class Window extends HorizontalBlock {
    public Window(Properties builder) {
        super(builder);
    }

    // VoxelShapes define the physical shape of the block for each direction (NORTH, EAST, SOUTH, WEST)
    // These shapes are made up of multiple cubic sections combined
    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(5, 11, 5, 6, 13, 11),  // Window frame pieces
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),   // Base
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),   // Lower frame
            Block.makeCuboidShape(6, 2, 6, 10, 10, 10),  // Main window pane
            Block.makeCuboidShape(5, 10, 4, 11, 11, 12), // Upper frame
            Block.makeCuboidShape(5, 11, 4, 11, 12, 5),  // Top decorative elements
            Block.makeCuboidShape(5, 11, 11, 11, 14, 12),
            Block.makeCuboidShape(10, 11, 5, 11, 13, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    // Similar shape definitions for other directions...
    // (SHAPE_E, SHAPE_S, SHAPE_W follow the same pattern but rotated)

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
     * Handles player interaction with the window block
     * @param state Current block state
     * @param worldIn World instance
     * @param pos Block position
     * @param player Player who activated the block
     * @param handIn Hand used for interaction
     * @param hit Hit result information
     * @return Result of the interaction
     */
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                         PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        String message = "blockActivated";
        ITextComponent msg = new StringTextComponent(message);
        player.sendMessage(msg, player.getUniqueID());

        // Client-side: Only show a status message
        if (worldIn.isRemote()) {
            player.sendStatusMessage(new StringTextComponent("Client: Block activated!"), true);
            return ActionResultType.SUCCESS;
        }

        // Server-side logic below
        player.sendMessage(new StringTextComponent("blockActivated"), player.getUniqueID());

        // Prevent action if sneaking (crouching)
        if (player.isCrouching()) {
            return ActionResultType.PASS;
        }

        MinecraftServer server = worldIn.getServer();
        if (server == null) {
            return ActionResultType.FAIL;
        }

        boolean goingToCustom = worldIn.getDimensionKey() != ModDimensions.World1;
        ServerWorld targetWorld = goingToCustom
            ? server.getWorld(ModDimensions.World1)
            : server.getWorld(World.OVERWORLD);

        if (targetWorld != null) {
            SimpleTeleporter teleporter = new SimpleTeleporter(pos, goingToCustom);
            player.changeDimension(targetWorld, teleporter);

            if (teleporter.wasSuccessful()) {
                player.sendMessage(new StringTextComponent("Teleportation successful!"), player.getUniqueID());
            } else {
                player.sendMessage(new StringTextComponent("Teleportation failed: no safe location found."), player.getUniqueID());
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }


    /**
     * Returns the appropriate shape for the block based on its facing direction
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
     * Adds the horizontal facing property to the block's state container
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    /**
     * Determines the block's facing direction when placed
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}