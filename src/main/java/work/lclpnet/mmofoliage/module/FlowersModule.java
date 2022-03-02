package work.lclpnet.mmofoliage.module;

import com.google.common.base.Functions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.MMOPottedPlantUtil;
import work.lclpnet.mmocontent.block.ext.MMOTallFlowerBlock;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.MFlowerBlock;

import java.util.function.Function;

public class FlowersModule implements IModule {

    public static MFlowerBlock
            violet,
            lavender,
            wildflower,
            pinkDaffodil,
            pinkHibiscus,
            glowflower,
            wiltedLily;

    public static FlowerPotBlock
            pottedViolet,
            pottedLavender,
            pottedWildflower,
            pottedPinkDaffodil,
            pottedPinkHibiscus,
            pottedGlowflower,
            pottedWiltedLily;

    public static MMOTallFlowerBlock blueHydrangea, goldenrod;

    @Override
    public void register() {
        final RegisteredFlower violet = registerFlower("violet", StatusEffects.NAUSEA, 10);
        FlowersModule.violet = violet.plant;
        pottedViolet = violet.potted;

        final RegisteredFlower lavender = registerFlower("lavender", StatusEffects.HEALTH_BOOST, 5, MapColor.MAGENTA);
        FlowersModule.lavender = lavender.plant;
        pottedLavender = lavender.potted;

        final RegisteredFlower wildflower = registerFlower("wildflower", StatusEffects.HUNGER, 10);
        FlowersModule.wildflower = wildflower.plant;
        pottedWildflower = wildflower.potted;

        final RegisteredFlower pinkDaffodil = registerFlower("pink_daffodil", StatusEffects.INVISIBILITY, 7);
        FlowersModule.pinkDaffodil = pinkDaffodil.plant;
        pottedPinkDaffodil = pinkDaffodil.potted;

        final RegisteredFlower pinkHibiscus = registerFlower("pink_hibiscus", StatusEffects.REGENERATION, 5);
        FlowersModule.pinkHibiscus = pinkHibiscus.plant;
        pottedPinkHibiscus = pinkHibiscus.potted;

        final RegisteredFlower glowflower = registerFlower("glowflower", StatusEffects.GLOWING, 10, MapColor.DARK_GREEN,
                settings -> settings.luminance(state -> 9));
        FlowersModule.glowflower = glowflower.plant;
        pottedGlowflower = glowflower.potted;

        final RegisteredFlower wiltedLily = registerFlower("wilted_lily", StatusEffects.UNLUCK, 5);
        FlowersModule.wiltedLily = wiltedLily.plant;
        pottedWiltedLily = wiltedLily.potted;

        blueHydrangea = registerTallFlower("blue_hydrangea");
        goldenrod = registerTallFlower("goldenrod");
    }

    private RegisteredFlower registerFlower(String name, StatusEffect effect, int duration) {
        return registerFlower(name, effect, duration, MapColor.DARK_GREEN);
    }

    private RegisteredFlower registerFlower(String name, StatusEffect effect, int duration, MapColor color) {
        return registerFlower(name, effect, duration, color, Functions.identity());
    }

    private RegisteredFlower registerFlower(String name, StatusEffect effect, int duration, MapColor color,
                                            Function<AbstractBlock.Settings, AbstractBlock.Settings> transformer) {
        final AbstractBlock.Settings settings = AbstractBlock.Settings.of(Material.PLANT, color)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS);

        final MFlowerBlock block = new MFlowerBlock(effect, duration, transformer.apply(settings));

        new MMOBlockRegistrar(block).register(MMOFoliage.identifier(name), MMOFoliage.ITEM_GROUP);

        FlowerPotBlock potted = MMOPottedPlantUtil.addPottedPlant(block, name, transformer, MMOFoliage::identifier);

        return new RegisteredFlower(block, potted);
    }

    private MMOTallFlowerBlock registerTallFlower(String name) {
        final MMOTallFlowerBlock block = new MMOTallFlowerBlock(AbstractBlock.Settings.of(Material.PLANT)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS));

        new MMOBlockRegistrar(block).register(MMOFoliage.identifier(name), MMOFoliage.ITEM_GROUP);

        return block;
    }

    public static class RegisteredFlower {
        public final MFlowerBlock plant;
        public final FlowerPotBlock potted;

        public RegisteredFlower(MFlowerBlock plant, FlowerPotBlock potted) {
            this.plant = plant;
            this.potted = potted;
        }
    }
}
