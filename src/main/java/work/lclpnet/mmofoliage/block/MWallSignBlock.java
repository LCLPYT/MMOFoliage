package work.lclpnet.mmofoliage.block;

import net.minecraft.block.WallSignBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SignType;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;

public class MWallSignBlock extends WallSignBlock implements IMMOBlock {

    public MWallSignBlock(Settings settings, SignType signType) {
        super(settings, signType);
    }

    @Override
    public @Nullable BlockItem provideBlockItem(Item.Settings settings) {
        return null;
    }
}
