package work.lclpnet.mmofoliage.block;

import net.minecraft.block.MushroomBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;

public class MMushroomBlock extends MushroomBlock implements IMMOBlock {

    public MMushroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
