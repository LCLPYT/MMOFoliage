package work.lclpnet.mmofoliage.worldgen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

import java.util.List;

public class TaigaTreeConfig extends CustomTreeConfig {

    public static final Codec<TaigaTreeConfig> TYPE_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_provider").forGetter((instance) -> instance.trunkProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("foliage_provider").forGetter((instance) -> instance.foliageProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("vine_provider").forGetter((instance) -> instance.vineProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("hanging_provider").forGetter((instance) -> instance.hangingProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_fruit_provider").forGetter((instance) -> instance.trunkFruitProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("alt_foliage_provider").forGetter((instance) -> instance.altFoliageProvider),
            Codec.INT.fieldOf("min_height").forGetter((instance) -> instance.minHeight),
            Codec.INT.fieldOf("max_height").forGetter((instance) -> instance.maxHeight),
            TreeDecorator.TYPE_CODEC.listOf().fieldOf("decorators").forGetter((instance) -> instance.decorators),
            Codec.INT.fieldOf("trunk_width").forGetter((instance) -> instance.trunkWidth)
    ).apply(builder, TaigaTreeConfig::new));

    public final int trunkWidth;

    protected TaigaTreeConfig(BlockStateProvider trunkProvider, BlockStateProvider foliageProvider, BlockStateProvider vineProvider, BlockStateProvider hangingProvider, BlockStateProvider trunkFruitProvider, BlockStateProvider altFoliageProvider, int minHeight, int maxHeight, List<TreeDecorator> decorators, int trunkWidth) {
        super(trunkProvider, foliageProvider, vineProvider, hangingProvider, trunkFruitProvider, altFoliageProvider, minHeight, maxHeight, decorators);
        this.trunkWidth = trunkWidth;
    }

    public static class Builder extends CustomTreeConfig.Builder<Builder> {
        private int trunkWidth;

        public Builder() {
            this.minHeight = 6;
            this.maxHeight = 12;
            this.trunkProvider = BlockStateProvider.of(Blocks.SPRUCE_LOG.getDefaultState());
            this.foliageProvider = BlockStateProvider.of(Blocks.SPRUCE_LEAVES.getDefaultState());
            this.vineProvider = BlockStateProvider.of(Blocks.VINE.getDefaultState());
            this.trunkWidth = 1;
        }

        public Builder trunkWidth(int a) {
            this.trunkWidth = a;
            return this;
        }

        public TaigaTreeConfig build() {
            return new TaigaTreeConfig(this.trunkProvider, this.foliageProvider, this.vineProvider, this.hangingProvider, this.trunkFruitProvider, this.altFoliageProvider, this.minHeight, this.maxHeight, this.decorators, this.trunkWidth);
        }
    }
}
