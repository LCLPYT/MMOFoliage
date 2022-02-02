package work.lclpnet.mmofoliage.module;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.ext.MMOBlock;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.MFoliageBlock;

public class PlantsModule implements IModule {

    public static MFoliageBlock bush;

    @Override
    public void register() {
        bush = new MFoliageBlock(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS));

        new MMOBlockRegistrar(bush)
                .register(MMOFoliage.identifier("bush"));
    }
}
