package work.lclpnet.mmofoliage.worldgen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

import java.util.List;

public class BasicTreeConfig extends CustomTreeConfig {

    public static final Codec<BasicTreeConfig> TYPE_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_provider").forGetter((instance) -> instance.trunkProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("foliage_provider").forGetter((instance) -> instance.foliageProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("vine_provider").forGetter((instance) -> instance.vineProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("hanging_provider").forGetter((instance) -> instance.hangingProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_fruit_provider").forGetter((instance) -> instance.trunkFruitProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("alt_foliage_provider").forGetter((instance) -> instance.altFoliageProvider),
            Codec.INT.fieldOf("min_height").forGetter((instance) -> instance.minHeight),
            Codec.INT.fieldOf("max_height").forGetter((instance) -> instance.maxHeight),
            TreeDecorator.TYPE_CODEC.listOf().fieldOf("decorators").forGetter((instance) -> instance.decorators),
            Codec.INT.fieldOf("leaf_layers").forGetter((instance) -> instance.leafLayers),
            Codec.INT.fieldOf("leaves_offset").forGetter((instance) -> instance.leavesOffset),
            Codec.INT.fieldOf("max_leaves_radius").forGetter((instance) -> instance.maxLeavesRadius),
            Codec.INT.fieldOf("leaves_layer_height").forGetter((instance) -> instance.leavesLayerHeight),
            Codec.FLOAT.fieldOf("hanging_chance").forGetter((instance) -> instance.hangingChance)
    ).apply(builder, BasicTreeConfig::new));

    public final int leafLayers;
    public final int leavesOffset;
    public final int maxLeavesRadius;
    public final int leavesLayerHeight;
    public final float hangingChance;

    protected BasicTreeConfig(BlockStateProvider trunkProvider, BlockStateProvider foliageProvider, BlockStateProvider vineProvider, BlockStateProvider hangingProvider, BlockStateProvider trunkFruitProvider, BlockStateProvider altFoliageProvider, int minHeight, int maxHeight, List<TreeDecorator> decorators, int leafLayers, int leavesOffset, int maxLeavesRadius, int leavesLayerHeight, float hangingChance) {
        super(trunkProvider, foliageProvider, vineProvider, hangingProvider, trunkFruitProvider, altFoliageProvider, minHeight, maxHeight, decorators);
        this.leafLayers = leafLayers;
        this.leavesOffset = leavesOffset;
        this.maxLeavesRadius = maxLeavesRadius;
        this.leavesLayerHeight = leavesLayerHeight;
        this.hangingChance = hangingChance;
    }

    public static class Builder extends CustomTreeConfig.Builder<Builder> {
        protected int leafLayers;
        protected int leavesOffset;
        protected int maxLeavesRadius;
        protected int leavesLayerHeight;
        protected float hangingChance;

        public Builder leafLayers(int a) {
            this.leafLayers = a;
            return this;
        }

        public Builder leavesOffset(int a) {
            this.leavesOffset = a;
            return this;
        }

        public Builder leavesLayerHeight(int a) {
            this.leavesLayerHeight = a;
            return this;
        }

        public Builder maxLeavesRadius(int a) {
            this.maxLeavesRadius = a;
            return this;
        }

        public Builder hangingChance(float a) {
            this.hangingChance = a;
            return this;
        }

        public Builder() {
            this.minHeight = 4;
            this.maxHeight = 7;
            this.leafLayers = 4;
            this.leavesOffset = 1;
            this.maxLeavesRadius = 1;
            this.leavesLayerHeight = 2;
            this.hangingChance = 0.0F;
        }

        public BasicTreeConfig build() {
            return new BasicTreeConfig(this.trunkProvider, this.foliageProvider, this.vineProvider, this.hangingProvider, this.trunkFruitProvider, this.altFoliageProvider, this.minHeight, this.maxHeight, this.decorators, this.leafLayers, this.leavesOffset, this.maxLeavesRadius, this.leavesLayerHeight, this.hangingChance);
        }
    }
}
