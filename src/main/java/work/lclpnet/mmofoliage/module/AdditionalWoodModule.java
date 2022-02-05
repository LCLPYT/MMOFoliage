package work.lclpnet.mmofoliage.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.SignItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.SignType;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.ext.MMOBlock;
import work.lclpnet.mmocontent.block.ext.MMOLeavesBlock;
import work.lclpnet.mmocontent.block.ext.MMOPillarBlock;
import work.lclpnet.mmocontent.block.ext.MMOSaplingBlock;
import work.lclpnet.mmocontent.item.MMOItemRegistrar;
import work.lclpnet.mmofoliage.block.MAdditionalSigns;
import work.lclpnet.mmofoliage.block.MSaplingBlock;
import work.lclpnet.mmofoliage.block.MSignBlock;
import work.lclpnet.mmofoliage.block.MWallSignBlock;
import work.lclpnet.mmofoliage.entity.MBoatEntity;
import work.lclpnet.mmofoliage.entity.MBoatType;
import work.lclpnet.mmofoliage.item.MBoatItem;
import work.lclpnet.mmofoliage.worldgen.feature.BasicTreeFeature;
import work.lclpnet.mmofoliage.worldgen.feature.BigTreeFeature;
import work.lclpnet.mmofoliage.worldgen.feature.TaigaTreeFeature;
import work.lclpnet.mmofoliage.worldgen.sapling.DeadSaplingGenerator;
import work.lclpnet.mmofoliage.worldgen.sapling.FirSaplingGenerator;
import work.lclpnet.mmofoliage.worldgen.sapling.PinkCherrySaplingGenerator;
import work.lclpnet.mmofoliage.worldgen.sapling.WhiteCherrySaplingGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static work.lclpnet.mmocontent.util.MMOUtil.registerStrippedBlock;
import static work.lclpnet.mmofoliage.MMOFoliage.ITEM_GROUP;
import static work.lclpnet.mmofoliage.MMOFoliage.identifier;

public class AdditionalWoodModule implements IModule {

    public static EntityType<MBoatEntity> boatEntityType;

    public static WoodGroupHolder fir, cherry, dead;
    public static MSaplingBlock whiteCherrySapling, pinkCherrySapling;

    public static Feature<TreeFeatureConfig> FIR_TREE_SMALL,
            FIR_TREE,
            FIR_TREE_LARGE,
            WHITE_CHERRY_TREE,
            BIG_WHITE_CHERRY_TREE,
            PINK_CHERRY_TREE,
            BIG_PINK_CHERRY_TREE,
            SMALL_DEAD_TREE,
            DYING_TREE;

    @Override
    public void register() {
        boatEntityType = Registry.register(Registry.ENTITY_TYPE,
                identifier("boat"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<MBoatEntity>) MBoatEntity::new)
                        .dimensions(EntityDimensions.changing(1.375F, 0.5625F))
                        .trackRangeChunks(10)
                        .build()
        );

        // fir

        fir = registerWoodGroup("fir", MaterialColor.WHITE_TERRACOTTA, MaterialColor.LIGHT_GRAY_TERRACOTTA, new FirSaplingGenerator(), true);

        FIR_TREE_SMALL = register("fir_tree_small", new TaigaTreeFeature.Builder()
                .log(fir.log.getDefaultState())
                .leaves(fir.leaves.getDefaultState())
                .minHeight(5)
                .maxHeight(11)
                .create());
        FIR_TREE = register("fir_tree", new TaigaTreeFeature.Builder()
                .log(fir.log.getDefaultState())
                .leaves(fir.leaves.getDefaultState())
                .minHeight(5)
                .maxHeight(28)
                .create());
        FIR_TREE_LARGE = register("fir_tree_large", new TaigaTreeFeature.Builder()
                .log(fir.log.getDefaultState())
                .leaves(fir.leaves.getDefaultState())
                .minHeight(20)
                .maxHeight(40)
                .trunkWidth(2)
                .create());

        // cherry

        final String whiteCherry = "white_cherry", pinkCherry = "pink_cherry";

        MMOLeavesBlock whiteCherryLeaves = registerLeaves(whiteCherry, MaterialColor.WHITE);
        whiteCherrySapling = registerSapling(whiteCherry, new WhiteCherrySaplingGenerator());

        MMOLeavesBlock pinkCherryLeaves = registerLeaves(pinkCherry, MaterialColor.PINK);
        pinkCherrySapling = registerSapling(pinkCherry, new PinkCherrySaplingGenerator());

        cherry = registerWoodGroup("cherry", MaterialColor.RED, MaterialColor.RED_TERRACOTTA, null, false);

        WHITE_CHERRY_TREE = register("white_cherry_tree", new BasicTreeFeature.Builder()
                .log(cherry.log.getDefaultState())
                .leaves(whiteCherryLeaves.getDefaultState())
                .create());

        BIG_WHITE_CHERRY_TREE = register("big_white_cherry_tree", new BigTreeFeature.Builder()
                .log(cherry.log.getDefaultState())
                .leaves(whiteCherryLeaves.getDefaultState())
                .create());

        PINK_CHERRY_TREE = register("pink_cherry_tree", new BasicTreeFeature.Builder()
                .log(cherry.log.getDefaultState())
                .leaves(pinkCherryLeaves.getDefaultState())
                .create());

        BIG_PINK_CHERRY_TREE = register("big_pink_cherry_tree", new BigTreeFeature.Builder()
                .log(cherry.log.getDefaultState())
                .leaves(pinkCherryLeaves.getDefaultState())
                .create());

        // dead
        dead = registerWoodGroup("dead", MaterialColor.STONE, MaterialColor.STONE, new DeadSaplingGenerator(), true);

        SMALL_DEAD_TREE = register("small_dead_tree", new BasicTreeFeature.Builder()
                .log(dead.log.getDefaultState())
                .leaves(dead.leaves.getDefaultState())
                .create());

        DYING_TREE = register("dying_tree", new BigTreeFeature.Builder()
                .log(dead.log.getDefaultState())
                .leaves(dead.leaves.getDefaultState())
                .maxHeight(10)
                .foliageHeight(2)
                .create());
    }

    private static Feature<TreeFeatureConfig> register(String name, Feature<TreeFeatureConfig> feature) {
        return Registry.register(Registry.FEATURE, identifier(name), feature);
    }

    private static WoodGroupHolder registerWoodGroup(String name, MaterialColor woodTopColor, MaterialColor woodSideColor, @Nullable SaplingGenerator saplingGenerator, boolean registerLeaves) {
        MMOLeavesBlock leaves = null;
        if (registerLeaves) leaves = registerLeaves(name, Material.LEAVES.getColor());

        MSaplingBlock sapling = null;
        if (saplingGenerator != null) {
            sapling = registerSapling(name, saplingGenerator);
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

        SignType signType = MAdditionalSigns.registerSignType(name);

        MSignBlock sign = new MSignBlock(AbstractBlock.Settings.of(Material.WOOD)
                .noCollision()
                .strength(1.0F)
                .sounds(BlockSoundGroup.WOOD), signType);
        new MMOBlockRegistrar(sign)
                .register(identifier("%s_sign", name));

        MWallSignBlock wallSign = new MWallSignBlock(AbstractBlock.Settings.of(Material.WOOD)
                .noCollision()
                .strength(1.0F)
                .sounds(BlockSoundGroup.WOOD)
                .dropsLike(sign), signType);
        new MMOBlockRegistrar(wallSign)
                .register(identifier("%s_wall_sign", name));

        MAdditionalSigns.registerAdditionalSign(sign, wallSign);

        new MMOItemRegistrar(settings -> new SignItem(settings.maxCount(16), sign, wallSign))
                .register(identifier("%s_sign", name), ITEM_GROUP);

        MBoatType boatType = MBoatType.register(identifier(name), planks, () -> AdditionalWoodModule.fir.boatItem);

        MBoatItem boatItem = (MBoatItem) new MMOItemRegistrar(settings -> new MBoatItem(boatType, settings.maxCount(1)))
                .register(identifier("%s_boat", name), ITEM_GROUP);

        return new WoodGroupHolder(sapling, leaves, log, wood, strippedLog, strippedWood, planks, result, boatType, boatItem);
    }

    @Nonnull
    private static MSaplingBlock registerSapling(String name, @Nonnull SaplingGenerator saplingGenerator) {
        MSaplingBlock sapling;
        sapling = new MSaplingBlock(saplingGenerator, AbstractBlock.Settings.of(Material.PLANT)
                .noCollision()
                .ticksRandomly()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS));
        new MMOBlockRegistrar(sapling).register(identifier("%s_sapling", name), ITEM_GROUP);
        return sapling;
    }

    @Nonnull
    private static MMOLeavesBlock registerLeaves(String name, MaterialColor color) {
        MMOLeavesBlock leaves;
        leaves = new MMOLeavesBlock(AbstractBlock.Settings.of(Material.LEAVES, color)
                .strength(0.2F)
                .ticksRandomly()
                .sounds(BlockSoundGroup.GRASS)
                .nonOpaque());
        new MMOBlockRegistrar(leaves).register(identifier("%s_leaves", name), ITEM_GROUP);
        return leaves;
    }

    private static MMOPillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor) {
        return new MMOPillarBlock(AbstractBlock.Settings.of(Material.WOOD,
                        (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor)
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD));
    }

    public static class WoodGroupHolder {
        public final MMOSaplingBlock sapling;
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
        public final MBoatType boatType;
        public final MBoatItem boatItem;

        public WoodGroupHolder(MMOSaplingBlock sapling, MMOLeavesBlock leaves, MMOPillarBlock log, MMOPillarBlock wood,
                               MMOPillarBlock strippedLog, MMOPillarBlock strippedWood, MMOBlock planks,
                               MMOBlockRegistrar.Result result, MBoatType boatType, MBoatItem boatItem) {
            this.leaves = leaves;
            this.sapling = sapling;
            this.log = log;
            this.wood = wood;
            this.strippedLog = strippedLog;
            this.strippedWood = strippedWood;
            this.planks = planks;
            this.stairs = Objects.requireNonNull(result.stairs).block;
            this.slab = Objects.requireNonNull(result.slab).block;
            this.fence = Objects.requireNonNull(result.fence).block;
            this.fenceGate = Objects.requireNonNull(result.fenceGate).block;
            this.door = Objects.requireNonNull(result.door).block;
            this.trapdoor = Objects.requireNonNull(result.trapdoor).block;
            this.pressurePlate = Objects.requireNonNull(result.pressurePlate).block;
            this.button = Objects.requireNonNull(result.button).block;
            this.boatType = boatType;
            this.boatItem = boatItem;
        }
    }
}
