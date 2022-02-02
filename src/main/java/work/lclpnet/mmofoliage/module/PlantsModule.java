package work.lclpnet.mmofoliage.module;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofoliage.MMOFoliage;

public class PlantsModule implements IModule {

    @Override
    public void register() {
        new MMOBlockRegistrar(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS))
                .register(MMOFoliage.identifier("bush"));
    }
}
