package work.lclpnet.mmofoliage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.mmocontent.block.ext.MMOBlock;

public class FleshBlock extends MMOBlock {

    public FleshBlock() {
        super(FabricBlockSettings.of(Material.SPONGE, MaterialColor.RED_TERRACOTTA)
                .strength(0.6F, 0.6F)
                .breakByTool(FabricToolTags.AXES, 0)
                .sounds(new BlockSoundGroup(1.0F, 0.5F, SoundEvents.BLOCK_CORAL_BLOCK_BREAK, SoundEvents.BLOCK_CORAL_BLOCK_STEP, SoundEvents.BLOCK_CORAL_BLOCK_PLACE, SoundEvents.BLOCK_CORAL_BLOCK_HIT, SoundEvents.BLOCK_CORAL_BLOCK_FALL)));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        entity.setVelocity(entity.getVelocity().multiply(0.95D, 1.0D, 0.95D));
    }
}
