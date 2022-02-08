package work.lclpnet.mmofoliage.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;
import work.lclpnet.mmofoliage.module.PlantsModule;

public class MFoliageBlock extends PlantBlock implements IMMOBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public MFoliageBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (!world.isClient && stack.getItem() == Items.SHEARS) {
            player.incrementStat(Stats.MINED.getOrCreateStat(this));
            player.addExhaustion(0.005F);
            dropStack(world, pos, new ItemStack(this));
        } else {
            super.afterBreak(world, player, pos, state, blockEntity, stack);
        }
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        final BlockState groundState = world.getBlockState(pos.down());
        final Block ground = groundState.getBlock();

        if (this.equals(PlantsModule.sprout)) {
            return groundState.isSideSolidFullSquare(world, pos.down(), Direction.UP) || super.canPlaceAt(state, world, pos);
        } else if (this.equals(PlantsModule.duneGrass)) {
            return ground instanceof SandBlock;
        } else if (this.equals(PlantsModule.deadGrass)) {
            return ground instanceof SandBlock || ground instanceof GravelBlock;
        }

        return super.canPlaceAt(state, world, pos);
    }
}
