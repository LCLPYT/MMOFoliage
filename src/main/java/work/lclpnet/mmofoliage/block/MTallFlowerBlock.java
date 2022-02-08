package work.lclpnet.mmofoliage.block;

import net.minecraft.block.TallFlowerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;

public class MTallFlowerBlock extends TallFlowerBlock implements IMMOBlock {

    public MTallFlowerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
