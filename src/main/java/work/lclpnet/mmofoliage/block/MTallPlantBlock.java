package work.lclpnet.mmofoliage.block;

import net.minecraft.block.TallPlantBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;

public class MTallPlantBlock extends TallPlantBlock implements IMMOBlock {

    public MTallPlantBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
