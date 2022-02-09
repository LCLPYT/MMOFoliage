package work.lclpnet.mmofoliage.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmocontent.block.ext.MMOBlock;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Map;

public class DeadBranchBlock extends MMOBlock {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 16.0D),
            Direction.SOUTH, Block.createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 12.0D),
            Direction.WEST, Block.createCuboidShape(4.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D),
            Direction.EAST, Block.createCuboidShape(0.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    ));

    public DeadBranchBlock() {
        super(Settings.of(Material.WOOD, MaterialColor.GRAY)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.WOOD));
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        final Direction direction = state.get(FACING).getOpposite();
        final BlockPos anchor = pos.offset(direction);
        Block block = world.getBlockState(anchor).getBlock();
        return AdditionalWoodModule.dead.log.equals(block) || AdditionalWoodModule.dead.wood.equals(block);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        final WorldView worldView = ctx.getWorld();
        final BlockPos blockPos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockState = blockState.with(FACING, direction);
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }
}
