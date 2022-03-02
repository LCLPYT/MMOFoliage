package work.lclpnet.mmofoliage.module;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.FleshBlock;
import work.lclpnet.mmofoliage.block.MMushroomBlock;
import work.lclpnet.mmofoliage.block.MudBlock;

import static work.lclpnet.mmofoliage.MMOFoliage.ITEM_GROUP;
import static work.lclpnet.mmofoliage.MMOFoliage.identifier;

public class SoilModule implements IModule {

    public static Tag<Block> dirt;
    public static MMushroomBlock glowshroom_block;
    public static MMushroomBlock toadstool_block;

    @Override
    public void register() {
        new MMOBlockRegistrar(new MudBlock())
                .register(MMOFoliage.identifier("mud"), MMOFoliage.ITEM_GROUP);

        new MMOBlockRegistrar(new FleshBlock())
                .register(MMOFoliage.identifier("flesh"), MMOFoliage.ITEM_GROUP);

        dirt =TagFactory.BLOCK.create(MMOFoliage.identifier("dirt"));

        new MMOBlockRegistrar(glowshroom_block = new MMushroomBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.DIAMOND_BLUE)
                .strength(0.2F)
                .sounds(BlockSoundGroup.WOOD)
                .luminance(state -> 10)))
                .register(identifier("glowshroom_block"), ITEM_GROUP);

        new MMOBlockRegistrar(toadstool_block = new MMushroomBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.ORANGE)
                .strength(0.2F)
                .sounds(BlockSoundGroup.WOOD)))
                .register(identifier("toadstool_block"), ITEM_GROUP);
    }
}
