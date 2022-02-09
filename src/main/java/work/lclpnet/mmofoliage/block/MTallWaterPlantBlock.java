package work.lclpnet.mmofoliage.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;
import work.lclpnet.mmofoliage.util.FTF;

import java.util.Iterator;

public class MTallWaterPlantBlock extends TallPlantBlock implements IMMOBlock, Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public MTallWaterPlantBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
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
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.getBlock() != this) {
            return super.canPlaceAt(state, world, pos);
        } else {
            BlockState soil;
            if (state.get(HALF) != DoubleBlockHalf.UPPER) {
                soil = world.getBlockState(pos.down());

                if (!FTF.canSustainBeach(soil, world, pos.down())) {
                    return false;
                } else {
                    BlockPos down = pos.down();
                    Iterator<Direction> iterator = Direction.Type.HORIZONTAL.iterator();

                    BlockState blockState;
                    FluidState fluidState;
                    do {
                        if (!iterator.hasNext()) {
                            return false;
                        }

                        Direction dir = iterator.next();
                        blockState = world.getBlockState(down.offset(dir));
                        fluidState = world.getFluidState(down.offset(dir));
                    } while (!fluidState.isIn(FluidTags.WATER) && blockState.getBlock() != Blocks.FROSTED_ICE);

                    return true;
                }
            } else {
                soil = world.getBlockState(pos.down());
                return soil.getBlock() == this && soil.get(HALF) == DoubleBlockHalf.LOWER;
            }
        }
    }

    @Override
    public void placeAt(WorldAccess world, BlockPos pos, int flags) {
        boolean water = world.getFluidState(pos).getFluid() == Fluids.WATER;
        world.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(WATERLOGGED, water), flags);

        final BlockPos up = pos.up();
        water = world.getFluidState(up).getFluid() == Fluids.WATER;
        world.setBlockState(up, this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(WATERLOGGED, water), flags);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        boolean water = world.getFluidState(pos.up()).getFluid() == Fluids.WATER;
        world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(WATERLOGGED, water), 3);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }
}
