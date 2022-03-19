package work.lclpnet.mmofoliage.module;

import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.item.SignItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.SignType;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import work.lclpnet.mmocontent.block.MMOAdditionalSigns;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.MMOPottedPlantUtil;
import work.lclpnet.mmocontent.block.ext.*;
import work.lclpnet.mmocontent.entity.MMOBoatEntity;
import work.lclpnet.mmocontent.entity.MMOBoatType;
import work.lclpnet.mmocontent.item.MMOBoatItem;
import work.lclpnet.mmocontent.item.MMOItemRegistrar;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.MSaplingBlock;
import work.lclpnet.mmofoliage.worldgen.config.*;
import work.lclpnet.mmofoliage.worldgen.feature.*;
import work.lclpnet.mmofoliage.worldgen.sapling.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static work.lclpnet.mmocontent.util.MMOUtil.registerStrippedBlock;
import static work.lclpnet.mmofoliage.MMOFoliage.ITEM_GROUP;
import static work.lclpnet.mmofoliage.MMOFoliage.identifier;

public class AdditionalWoodModule implements IModule {

    public static WoodGroupHolder fir, cherry, dead, hellbark, jacaranda, palm, willow;
    public static MSaplingBlock
            whiteCherrySapling,
            pinkCherrySapling,
            yellowAutumnSapling,
            orangeAutumnSapling,
            mapleSapling;
    public static FlowerPotBlock
            pottedWhiteCherrySapling,
            pottedPinkCherrySapling,
            pottedYellowAutumnSapling,
            pottedOrangeAutumnSapling,
            pottedMapleSapling;

    public static AbstractTreeFeature<?>
            BASIC_TREE,
            TAIGA_TREE,
            BIG_TREE,
            TWIGLET_TREE,
            BASE_PALM_TREE;

    public static ConfiguredFeature<TreeFeatureConfig, ?>
            FIR_TREE_SMALL,
            FIR_TREE,
            FIR_TREE_LARGE,
            WHITE_CHERRY_TREE,
            BIG_WHITE_CHERRY_TREE,
            PINK_CHERRY_TREE,
            BIG_PINK_CHERRY_TREE,
            SMALL_DEAD_TREE,
            DYING_TREE,
            HELLBARK_TREE,
            JACARANDA_TREE,
            BIG_JACARANDA_TREE,
            YELLOW_AUTUMN_TREE,
            BIG_YELLOW_AUTUMN_TREE,
            ORANGE_AUTUMN_TREE,
            BIG_ORANGE_AUTUMN_TREE,
            PALM_TREE,
            WILLOW_TREE,
            MAPLE_TREE,
            BIG_MAPLE_TREE;

    @Override
    public void register() {
        MMOBoatEntity.enableMMOBoatIntegration();

        // base features
        BASIC_TREE = (BasicTreeFeature) register("basic_tree", new BasicTreeFeature(BasicTreeConfig.TYPE_CODEC));
        BIG_TREE = (BigTreeFeature) register("big_tree", new BigTreeFeature(BigTreeConfig.TYPE_CODEC));
        TAIGA_TREE = (TaigaTreeFeature) register("taiga_tree", new TaigaTreeFeature(TaigaTreeConfig.TYPE_CODEC));
        TWIGLET_TREE = (TwigletTreeFeature) register("twiglet_tree", new TwigletTreeFeature(TwigletTreeConfig.TYPE_CODEC));
        BASE_PALM_TREE = (PalmTreeFeature) register("palm_tree", new PalmTreeFeature(PalmTreeConfig.TYPE_CODEC));

        // fir
        fir = registerWoodGroup("fir", MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_LIGHT_GRAY, new FirSaplingGenerator(), true);

        FIR_TREE_SMALL = registerConfigured("fir_tree_small", TAIGA_TREE.configure(createFirBuilder().minHeight(5).maxHeight(11).build()));
        FIR_TREE = registerConfigured("fir_tree", TAIGA_TREE.configure(createFirBuilder().minHeight(5).maxHeight(28).build()));
        FIR_TREE_LARGE = registerConfigured("fir_tree_large", TAIGA_TREE.configure(createFirBuilder().minHeight(20).maxHeight(40).trunkWidth(2).build()));

        // cherry
        final String whiteCherry = "white_cherry", pinkCherry = "pink_cherry";

        MMOLeavesBlock whiteCherryLeaves = registerLeaves(whiteCherry, MapColor.WHITE);
        RegisteredSapling whiteCherryPlant = registerSapling(whiteCherry, new WhiteCherrySaplingGenerator());
        AdditionalWoodModule.whiteCherrySapling = whiteCherryPlant.sapling;
        AdditionalWoodModule.pottedWhiteCherrySapling = whiteCherryPlant.potted;

        MMOLeavesBlock pinkCherryLeaves = registerLeaves(pinkCherry, MapColor.PINK);
        RegisteredSapling pinkCherryPlant = registerSapling(pinkCherry, new PinkCherrySaplingGenerator());
        AdditionalWoodModule.pinkCherrySapling = pinkCherryPlant.sapling;
        AdditionalWoodModule.pottedPinkCherrySapling = pinkCherryPlant.potted;

        cherry = registerWoodGroup("cherry", MapColor.RED, MapColor.TERRACOTTA_RED, null, false);

        WHITE_CHERRY_TREE = registerConfigured("white_cherry_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(cherry.log.getDefaultState()))
                .foliage(BlockStateProvider.of(whiteCherryLeaves.getDefaultState()))
                .build()));

        BIG_WHITE_CHERRY_TREE = registerConfigured("big_white_cherry_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .trunk(BlockStateProvider.of(cherry.log.getDefaultState()))
                .foliage(BlockStateProvider.of(whiteCherryLeaves.getDefaultState()))
                .build()));

        PINK_CHERRY_TREE = registerConfigured("pink_cherry_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(cherry.log.getDefaultState()))
                .foliage(BlockStateProvider.of(pinkCherryLeaves.getDefaultState()))
                .build()));

        BIG_PINK_CHERRY_TREE = registerConfigured("big_pink_cherry_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .trunk(BlockStateProvider.of(cherry.log.getDefaultState()))
                .foliage(BlockStateProvider.of(pinkCherryLeaves.getDefaultState()))
                .build()));

        // dead
        dead = registerWoodGroup("dead", MapColor.STONE_GRAY, MapColor.STONE_GRAY, new DeadSaplingGenerator(), true);

        SMALL_DEAD_TREE = registerConfigured("small_dead_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(dead.log.getDefaultState()))
                .foliage(BlockStateProvider.of(dead.leaves.getDefaultState()))
                .build()));

        DYING_TREE = registerConfigured("dying_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .trunk(BlockStateProvider.of(dead.log.getDefaultState()))
                .foliage(BlockStateProvider.of(dead.leaves.getDefaultState()))
                .maxHeight(10)
                .foliageHeight(2)
                .build()));

        // hellbark
        hellbark = registerWoodGroup("hellbark", MapColor.TERRACOTTA_GRAY, MapColor.LIGHT_GRAY, new HellbarkSaplingGenerator(), true);

        HELLBARK_TREE = registerConfigured("hellbark_tree", TWIGLET_TREE.configure(new TwigletTreeConfig.Builder()
                .trunk(BlockStateProvider.of(hellbark.log.getDefaultState()))
                .foliage(BlockStateProvider.of(hellbark.leaves.getDefaultState()))
                .build()));

        // jacaranda
        jacaranda = registerWoodGroup("jacaranda", MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_LIGHT_GRAY, new JacarandaSaplingGenerator(), true);

        JACARANDA_TREE = registerConfigured("jacaranda_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(jacaranda.log.getDefaultState()))
                .foliage(BlockStateProvider.of(jacaranda.leaves.getDefaultState()))
                .build()));

        BIG_JACARANDA_TREE = registerConfigured("big_jacaranda_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .trunk(BlockStateProvider.of(jacaranda.log.getDefaultState()))
                .foliage(BlockStateProvider.of(jacaranda.leaves.getDefaultState()))
                .build()));

        // palm
        palm = registerWoodGroup("palm", MapColor.TERRACOTTA_YELLOW, MapColor.TERRACOTTA_BROWN, new PalmSaplingGenerator(), true);

        PALM_TREE = registerConfigured("palm_tree", BASE_PALM_TREE.configure(new PalmTreeConfig.Builder()
                .trunk(BlockStateProvider.of(palm.log.getDefaultState()))
                .foliage(BlockStateProvider.of(palm.leaves.getDefaultState().with(LeavesBlock.PERSISTENT, true)))
                .build()));

        // willow
        willow = registerWoodGroup("willow", MapColor.TERRACOTTA_LIME, MapColor.TERRACOTTA_LIME, new WillowSaplingGenerator(), true);

        WILLOW_TREE = registerConfigured("willow_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(willow.log.getDefaultState()))
                .foliage(BlockStateProvider.of(willow.leaves.getDefaultState()))
                .vine(BlockStateProvider.of(PlantsModule.willowVine))
                .minHeight(6)
                .maxHeight(10)
                .maxLeavesRadius(2)
                .leavesOffset(0)
                .build()));

        // yellow autumn
        final String yellowAutumn = "yellow_autumn";
        MMOLeavesBlock yellowAutumnLeaves = registerLeaves(yellowAutumn, MapColor.TERRACOTTA_YELLOW);

        RegisteredSapling yellowAutumnPlant = registerSapling(yellowAutumn, new YellowAutumnSaplingGenerator());
        yellowAutumnSapling = yellowAutumnPlant.sapling;
        pottedYellowAutumnSapling = yellowAutumnPlant.potted;

        YELLOW_AUTUMN_TREE = registerConfigured("yellow_autumn_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(Blocks.BIRCH_LOG.getDefaultState()))
                .foliage(BlockStateProvider.of(yellowAutumnLeaves.getDefaultState()))
                .minHeight(5)
                .maxHeight(8)
                .build()));

        BIG_YELLOW_AUTUMN_TREE = registerConfigured("big_yellow_autumn_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .trunk(BlockStateProvider.of(Blocks.BIRCH_LOG.getDefaultState()))
                .foliage(BlockStateProvider.of(yellowAutumnLeaves.getDefaultState()))
                .build()));

        // orange autumn
        final String orangeAutumn = "orange_autumn";
        MMOLeavesBlock orangeAutumnLeaves = registerLeaves(orangeAutumn, MapColor.TERRACOTTA_ORANGE);

        RegisteredSapling orangeAutumnPlant = registerSapling(orangeAutumn, new OrangeAutumnSaplingGenerator());
        orangeAutumnSapling = orangeAutumnPlant.sapling;
        pottedOrangeAutumnSapling = orangeAutumnPlant.potted;

        ORANGE_AUTUMN_TREE = registerConfigured("orange_autumn_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .trunk(BlockStateProvider.of(Blocks.DARK_OAK_LOG.getDefaultState()))
                .foliage(BlockStateProvider.of(orangeAutumnLeaves.getDefaultState()))
                .minHeight(5)
                .maxHeight(8)
                .build()));

        BIG_ORANGE_AUTUMN_TREE = registerConfigured("big_orange_autumn_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .trunk(BlockStateProvider.of(Blocks.DARK_OAK_LOG.getDefaultState()))
                .foliage(BlockStateProvider.of(orangeAutumnLeaves.getDefaultState()))
                .build()));

        // maple
        final String maple = "maple";
        MMOLeavesBlock mapleLeaves = registerLeaves(maple, MapColor.RED);

        final RegisteredSapling maplePlant = registerSapling(maple, new MapleSaplingGenerator());
        mapleSapling = maplePlant.sapling;
        pottedMapleSapling = maplePlant.potted;

        MAPLE_TREE = registerConfigured("maple_tree", BASIC_TREE.configure(new BasicTreeConfig.Builder()
                .foliage(BlockStateProvider.of(mapleLeaves.getDefaultState()))
                .minHeight(5)
                .maxHeight(10)
                .build()));

        BIG_MAPLE_TREE = registerConfigured("big_maple_tree", BIG_TREE.configure(new BigTreeConfig.Builder()
                .foliage(BlockStateProvider.of(mapleLeaves.getDefaultState()))
                .build()));
    }

    private TaigaTreeConfig.Builder createFirBuilder() {
        return new TaigaTreeConfig.Builder()
                .trunk(BlockStateProvider.of(fir.log.getDefaultState()))
                .foliage(BlockStateProvider.of(fir.leaves.getDefaultState()));
    }

    private static <C extends FeatureConfig> Feature<C> register(String name, Feature<C> feature) {
        return Registry.register(Registry.FEATURE, identifier(name), feature);
    }

    private static <C extends FeatureConfig> ConfiguredFeature<C, ?> registerConfigured(String name, ConfiguredFeature<C, ?> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier(name), feature);
    }

    private static WoodGroupHolder registerWoodGroup(String name, MapColor woodTopColor, MapColor woodSideColor, @Nullable SaplingGenerator saplingGenerator, boolean registerLeaves) {
        MMOLeavesBlock leaves = null;
        if (registerLeaves) leaves = registerLeaves(name, Material.LEAVES.getColor());

        MSaplingBlock sapling = null;
        FlowerPotBlock pottedSapling = null;
        if (saplingGenerator != null) {
            RegisteredSapling plant = registerSapling(name, saplingGenerator);
            sapling = plant.sapling;
            pottedSapling = plant.potted;
        }

        MMOPillarBlock log = createLogBlock(woodTopColor, woodSideColor);
        new MMOBlockRegistrar(log).register(identifier("%s_log", name), ITEM_GROUP);

        MMOPillarBlock wood = new MMOPillarBlock(AbstractBlock.Settings.of(Material.WOOD, woodTopColor)
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD));
        new MMOBlockRegistrar(wood).register(identifier("%s_wood", name), ITEM_GROUP);

        MMOPillarBlock strippedLog = createLogBlock(woodTopColor, woodTopColor);
        new MMOBlockRegistrar(strippedLog).register(identifier("stripped_%s_log", name), ITEM_GROUP);

        MMOPillarBlock strippedWood = new MMOPillarBlock(AbstractBlock.Settings.of(Material.WOOD, woodTopColor)
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD));
        new MMOBlockRegistrar(strippedWood).register(identifier("stripped_%s_wood", name), ITEM_GROUP);

        registerStrippedBlock(log, strippedLog);
        registerStrippedBlock(wood, strippedWood);

        MMOBlock planks = new MMOBlock(AbstractBlock.Settings.of(Material.WOOD, woodTopColor)
                .strength(2.0F, 3.0F)
                .sounds(BlockSoundGroup.WOOD));
        MMOBlockRegistrar.Result result = new MMOBlockRegistrar(planks)
                .withStairs().withSlab().withVerticalSlab().withFence().withFenceGate().withDoor().withTrapdoor()
                .withPressurePlate(PressurePlateBlock.ActivationRule.EVERYTHING).withButton(true)
                .register(identifier(name), ITEM_GROUP, basePath -> basePath.concat("_planks"));

        SignType signType = MMOAdditionalSigns.registerSignType(name, MMOFoliage::identifier);

        MMOSignBlock sign = new MMOSignBlock(AbstractBlock.Settings.of(Material.WOOD)
                .noCollision()
                .strength(1.0F)
                .sounds(BlockSoundGroup.WOOD), signType);
        new MMOBlockRegistrar(sign)
                .register(identifier("%s_sign", name));

        MMOWallSignBlock wallSign = new MMOWallSignBlock(AbstractBlock.Settings.of(Material.WOOD)
                .noCollision()
                .strength(1.0F)
                .sounds(BlockSoundGroup.WOOD)
                .dropsLike(sign), signType);
        new MMOBlockRegistrar(wallSign)
                .register(identifier("%s_wall_sign", name));

        MMOAdditionalSigns.registerAdditionalSign(sign, wallSign);

        new MMOItemRegistrar(settings -> new SignItem(settings.maxCount(16), sign, wallSign))
                .register(identifier("%s_sign", name), ITEM_GROUP);

        MMOBoatType boatType = MMOBoatType.register(identifier(name), planks);

        final MMOBoatItem boatItem = (MMOBoatItem) new MMOItemRegistrar(settings -> new MMOBoatItem(boatType, settings.maxCount(1)))
                .register(identifier("%s_boat", name), ITEM_GROUP);

        boatType.boatItem = boatItem;

        return new WoodGroupHolder(sapling, pottedSapling, leaves, log, wood, strippedLog, strippedWood, planks, result, boatType, boatItem);
    }

    @Nonnull
    private static RegisteredSapling registerSapling(String name, @Nonnull SaplingGenerator saplingGenerator) {
        MSaplingBlock sapling;
        sapling = new MSaplingBlock(saplingGenerator, AbstractBlock.Settings.of(Material.PLANT)
                .noCollision()
                .ticksRandomly()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS));

        new MMOBlockRegistrar(sapling).register(identifier("%s_sapling", name), ITEM_GROUP);

        FlowerPotBlock potted = MMOPottedPlantUtil.addPottedPlant(sapling, String.format("%s_sapling", name), MMOFoliage::identifier);
        return new RegisteredSapling(sapling, potted);
    }

    @Nonnull
    private static MMOLeavesBlock registerLeaves(String name, MapColor color) {
        MMOLeavesBlock leaves;
        leaves = new MMOLeavesBlock(AbstractBlock.Settings.of(Material.LEAVES, color)
                .strength(0.2F)
                .ticksRandomly()
                .sounds(BlockSoundGroup.GRASS)
                .nonOpaque());
        new MMOBlockRegistrar(leaves).register(identifier("%s_leaves", name), ITEM_GROUP);
        return leaves;
    }

    private static MMOPillarBlock createLogBlock(MapColor topMaterialColor, MapColor sideMaterialColor) {
        return new MMOPillarBlock(AbstractBlock.Settings.of(Material.WOOD,
                        (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor)
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD));
    }

    public record RegisteredSapling(MSaplingBlock sapling, FlowerPotBlock potted) {}

    public static class WoodGroupHolder {
        public final MMOSaplingBlock sapling;
        public final FlowerPotBlock pottedSapling;
        public final MMOLeavesBlock leaves;
        public final MMOPillarBlock log, wood, strippedLog, strippedWood;
        public final MMOBlock planks;
        public final StairsBlock stairs;
        public final SlabBlock slab;
        public final FenceBlock fence;
        public final FenceGateBlock fenceGate;
        public final DoorBlock door;
        public final TrapdoorBlock trapdoor;
        public final PressurePlateBlock pressurePlate;
        public final AbstractButtonBlock button;
        public final MMOBoatType boatType;
        public final MMOBoatItem boatItem;

        public WoodGroupHolder(MMOSaplingBlock sapling, FlowerPotBlock pottedSapling, MMOLeavesBlock leaves, MMOPillarBlock log, MMOPillarBlock wood,
                               MMOPillarBlock strippedLog, MMOPillarBlock strippedWood, MMOBlock planks,
                               MMOBlockRegistrar.Result result, MMOBoatType boatType, MMOBoatItem boatItem) {
            this.pottedSapling = pottedSapling;
            this.leaves = leaves;
            this.sapling = sapling;
            this.log = log;
            this.wood = wood;
            this.strippedLog = strippedLog;
            this.strippedWood = strippedWood;
            this.planks = planks;
            this.stairs = Objects.requireNonNull(result.stairs()).block();
            this.slab = Objects.requireNonNull(result.slab()).block();
            this.fence = Objects.requireNonNull(result.fence()).block();
            this.fenceGate = Objects.requireNonNull(result.fenceGate()).block();
            this.door = Objects.requireNonNull(result.door()).block();
            this.trapdoor = Objects.requireNonNull(result.trapdoor()).block();
            this.pressurePlate = Objects.requireNonNull(result.pressurePlate()).block();
            this.button = Objects.requireNonNull(result.button()).block();
            this.boatType = boatType;
            this.boatItem = boatItem;
        }
    }
}
