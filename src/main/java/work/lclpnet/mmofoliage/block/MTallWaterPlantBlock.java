package work.lclpnet.mmofoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class MTallWaterPlantBlock extends MTallPlantBlock implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public MTallWaterPlantBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) throw new IllegalStateException("Placement state is null");

        return state.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.get(WATERLOGGED))
            world.createAndScheduleBlockTick(pos, this, Fluids.WATER.getTickRate(world));

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            BlockPos posBelow = pos.down();
            BlockState existingState = world.getBlockState(pos);
            Block existingBlock = existingState.getBlock();
            return (existingBlock == this || existingState.getMaterial() == Material.WATER) && this.isExposed(world, pos.up()) && world.getBlockState(posBelow).isSideSolidFullSquare(world, posBelow, Direction.UP);
        } else {
            BlockState blockstate = world.getBlockState(pos.down());
            if (state.getBlock() != this) {
                return world.isAir(pos);
            } else {
                return this.isExposed(world, pos) && blockstate.getBlock() == this && blockstate.get(HALF) == DoubleBlockHalf.LOWER && (Boolean)blockstate.get(WATERLOGGED);
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        boolean water = world.getFluidState(pos.up()).getFluid() == Fluids.WATER;
        world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(WATERLOGGED, water), 3);
    }

    protected boolean isExposed(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == this ? !(Boolean)state.get(WATERLOGGED) : world.isAir(pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }
}
