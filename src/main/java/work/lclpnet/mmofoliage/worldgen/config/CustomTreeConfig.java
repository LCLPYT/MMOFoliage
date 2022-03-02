package work.lclpnet.mmofoliage.worldgen.config;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

import java.util.List;

public class CustomTreeConfig extends TreeFeatureConfig {

    public static final Codec<CustomTreeConfig> TYPE_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_provider").forGetter((instance) -> instance.trunkProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("foliage_provider").forGetter((instance) -> instance.foliageProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("vine_provider").forGetter((instance) -> instance.vineProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("hanging_provider").forGetter((instance) -> instance.hangingProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("trunk_fruit_provider").forGetter((instance) -> instance.trunkFruitProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("alt_foliage_provider").forGetter((instance) -> instance.altFoliageProvider),
            Codec.INT.fieldOf("min_height").forGetter((instance) -> instance.minHeight),
            Codec.INT.fieldOf("max_height").forGetter((instance) -> instance.maxHeight),
            TreeDecorator.TYPE_CODEC.listOf().fieldOf("decorators").forGetter((instance) -> instance.decorators)
    ).apply(builder, CustomTreeConfig::new));

    public final BlockStateProvider vineProvider;
    public final BlockStateProvider hangingProvider;
    public final BlockStateProvider trunkFruitProvider;
    public final BlockStateProvider altFoliageProvider;
    public final int minHeight;
    public final int maxHeight;

    protected CustomTreeConfig(BlockStateProvider trunkProvider, BlockStateProvider foliageProvider, BlockStateProvider vineProvider, BlockStateProvider hangingProvider, BlockStateProvider trunkFruitProvider, BlockStateProvider altFoliageProvider, int minHeight, int maxHeight, List<TreeDecorator> decorators) {
        super(trunkProvider, null, foliageProvider, null, null, null, decorators, false, false);
        this.vineProvider = vineProvider;
        this.hangingProvider = hangingProvider;
        this.trunkFruitProvider = trunkFruitProvider;
        this.altFoliageProvider = altFoliageProvider;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends CustomTreeConfig.Builder<T>> {
        protected BlockStateProvider trunkProvider;
        protected BlockStateProvider foliageProvider;
        protected BlockStateProvider vineProvider;
        protected BlockStateProvider hangingProvider;
        protected BlockStateProvider trunkFruitProvider;
        protected BlockStateProvider altFoliageProvider;
        protected List<TreeDecorator> decorators;
        protected int minHeight;
        protected int maxHeight;

        public Builder() {
            this.trunkProvider = BlockStateProvider.of(Blocks.OAK_LOG.getDefaultState());
            this.foliageProvider = BlockStateProvider.of(Blocks.OAK_LEAVES.getDefaultState());
            this.vineProvider = BlockStateProvider.of(Blocks.AIR.getDefaultState());
            this.hangingProvider = BlockStateProvider.of(Blocks.AIR.getDefaultState());
            this.trunkFruitProvider = BlockStateProvider.of(Blocks.AIR.getDefaultState());
            this.altFoliageProvider = BlockStateProvider.of(Blocks.AIR.getDefaultState());
            this.minHeight = 4;
            this.maxHeight = 7;
            this.decorators = Lists.newArrayList();
        }

        public T trunk(BlockStateProvider provider) {
            this.trunkProvider = provider;
            return (T) this;
        }

        public T foliage(BlockStateProvider provider) {
            this.foliageProvider = provider;
            return (T) this;
        }

        public T vine(BlockStateProvider provider) {
            this.vineProvider = provider;
            return (T) this;
        }

        public T hanging(BlockStateProvider provider) {
            this.hangingProvider = provider;
            return (T) this;
        }

        public T trunkFruit(BlockStateProvider provider) {
            this.trunkFruitProvider = provider;
            return (T) this;
        }

        public T altFoliage(BlockStateProvider a) {
            this.altFoliageProvider = a;
            return (T) this;
        }

        public T minHeight(int a) {
            this.minHeight = a;
            return (T) this;
        }

        public T maxHeight(int a) {
            this.maxHeight = a;
            return (T) this;
        }

        public T decorator(TreeDecorator decorator) {
            this.decorators.add(decorator);
            return (T) this;
        }

        public CustomTreeConfig build() {
            return new CustomTreeConfig(this.trunkProvider, this.foliageProvider, this.vineProvider, this.hangingProvider, this.trunkFruitProvider, this.altFoliageProvider, this.minHeight, this.maxHeight, this.decorators);
        }
    }
}
