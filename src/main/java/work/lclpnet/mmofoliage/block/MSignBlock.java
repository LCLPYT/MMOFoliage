package work.lclpnet.mmofoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.SignBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SignType;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;

import javax.annotation.Nullable;

public class MSignBlock extends SignBlock implements IMMOBlock {

    public MSignBlock(Settings settings, SignType signType) {
        super(settings, signType);
    }

    @Override
    public @Nullable BlockItem provideBlockItem(Item.Settings settings) {
        return null;
    }
}
