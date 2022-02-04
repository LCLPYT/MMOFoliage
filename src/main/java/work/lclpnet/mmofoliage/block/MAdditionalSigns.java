package work.lclpnet.mmofoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.SignType;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.asm.mixin.common.BlockEntityTypeAccessor;
import work.lclpnet.mmofoliage.asm.mixin.common.SignTypeAccessor;

import java.util.Set;

public class MAdditionalSigns {

    public static void registerAdditionalSign(SignBlock standing, WallSignBlock wall) {
        Set<Block> blocks = ((BlockEntityTypeAccessor) BlockEntityType.SIGN).getBlocks();
        // blocks is made mutable by MixinBlockEntityType for SIGN

        blocks.add(standing);
        blocks.add(wall);
    }

    public static SignType registerSignType(String name) {
        return SignTypeAccessor.invokeRegister(new MSignType(MMOFoliage.identifier(name)));
    }
}
