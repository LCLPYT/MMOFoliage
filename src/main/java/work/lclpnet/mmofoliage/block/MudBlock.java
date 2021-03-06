package work.lclpnet.mmofoliage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.mmocontent.block.ext.MMOBlock;

public class MudBlock extends MMOBlock {

    public MudBlock() {
        super(FabricBlockSettings.of(Material.SOIL, MapColor.TERRACOTTA_BROWN)
                .strength(0.6F,0.6F)
                .sounds(new BlockSoundGroup(1.0F, 0.75F,
                        SoundEvents.BLOCK_SLIME_BLOCK_BREAK,
                        SoundEvents.BLOCK_SLIME_BLOCK_STEP,
                        SoundEvents.BLOCK_SLIME_BLOCK_PLACE,
                        SoundEvents.BLOCK_SLIME_BLOCK_HIT,
                        SoundEvents.BLOCK_SLIME_BLOCK_FALL
                )));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        entity.setVelocity(entity.getVelocity().multiply(0.5D, 1D, 0.5D));
    }
}
