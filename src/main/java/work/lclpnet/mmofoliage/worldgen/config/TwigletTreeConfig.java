package work.lclpnet.mmofoliage.worldgen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

import java.util.List;

public class TwigletTreeConfig extends CustomTreeConfig {

    public static final Codec<TwigletTreeConfig> TYPE_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_provider").forGetter((instance) -> instance.trunkProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("foliage_provider").forGetter((instance) -> instance.foliageProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("vine_provider").forGetter((instance) -> instance.vineProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("hanging_provider").forGetter((instance) -> instance.hangingProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_fruit_provider").forGetter((instance) -> instance.trunkFruitProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("alt_foliage_provider").forGetter((instance) -> instance.altFoliageProvider),
            Codec.INT.fieldOf("min_height").forGetter((instance) -> instance.minHeight),
            Codec.INT.fieldOf("max_height").forGetter((instance) -> instance.maxHeight),
            TreeDecorator.TYPE_CODEC.listOf().fieldOf("decorators").forGetter((instance) -> instance.decorators),
            Codec.FLOAT.fieldOf("leaf_chance_even").forGetter((instance) -> instance.leafChanceEven),
            Codec.FLOAT.fieldOf("leaf_chance_odd").forGetter((instance) -> instance.leafChanceOdd)
    ).apply(builder, TwigletTreeConfig::new));

    public final float leafChanceEven;
    public final float leafChanceOdd;

    protected TwigletTreeConfig(BlockStateProvider trunkProvider, BlockStateProvider foliageProvider, BlockStateProvider vineProvider, BlockStateProvider hangingProvider, BlockStateProvider trunkFruitProvider, BlockStateProvider altFoliageProvider, int minHeight, int maxHeight, List<TreeDecorator> decorators, float leafChanceEven, float leafChanceOdd) {
        super(trunkProvider, foliageProvider, vineProvider, hangingProvider, trunkFruitProvider, altFoliageProvider, minHeight, maxHeight, decorators);
        this.leafChanceEven = leafChanceEven;
        this.leafChanceOdd = leafChanceOdd;
    }

    public static class Builder extends CustomTreeConfig.Builder<Builder> {
        private float leafChanceEven;
        private float leafChanceOdd;

        public Builder() {
            this.minHeight = 2;
            this.maxHeight = 6;
            this.leafChanceEven = 0.2F;
            this.leafChanceOdd = 0.9F;
        }

        public Builder leafChance(float a, float b) {
            this.leafChanceEven = a;
            this.leafChanceOdd = b;
            return this;
        }

        public TwigletTreeConfig build() {
            return new TwigletTreeConfig(this.trunkProvider, this.foliageProvider, this.vineProvider, this.hangingProvider, this.trunkFruitProvider, this.altFoliageProvider, this.minHeight, this.maxHeight, this.decorators, this.leafChanceEven, this.leafChanceOdd);
        }
    }
}
