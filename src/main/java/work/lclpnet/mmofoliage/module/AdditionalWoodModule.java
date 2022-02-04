package work.lclpnet.mmofoliage.module;

import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.ext.MMOBlock;
import work.lclpnet.mmocontent.block.ext.MMOLeavesBlock;
import work.lclpnet.mmocontent.block.ext.MMOPillarBlock;
import work.lclpnet.mmocontent.block.ext.MMOSaplingBlock;
import work.lclpnet.mmofoliage.block.MSaplingBlock;
import work.lclpnet.mmofoliage.worldgen.TaigaTreeFeature;
import work.lclpnet.mmofoliage.worldgen.sapling.FirSaplingGenerator;

import java.util.Objects;

import static work.lclpnet.mmocontent.util.MMOUtil.registerStrippedBlock;
import static work.lclpnet.mmofoliage.MMOFoliage.ITEM_GROUP;
import static work.lclpnet.mmofoliage.MMOFoliage.identifier;

public class AdditionalWoodModule implements IModule {

    public static WoodGroupHolder fir;

    public static Feature<TreeFeatureConfig> FIR_TREE_SMALL;
    public static Feature<TreeFeatureConfig> FIR_TREE;
    public static Feature<TreeFeatureConfig> FIR_TREE_LARGE;

    @Override
    public void register() {
        fir = registerWoodGroup("fir", MaterialColor.WHITE_TERRACOTTA, MaterialColor.LIGHT_GRAY_TERRACOTTA, new FirSaplingGenerator());

        FIR_TREE_SMALL = Registry.register(Registry.FEATURE, identifier("fir_tree_small"), new TaigaTreeFeature.Builder()
                .log(fir.log.getDefaultState())
                .leaves(fir.leaves.getDefaultState())
                .minHeight(5)
                .maxHeight(11)
                .create());
        FIR_TREE = Registry.register(Registry.FEATURE, identifier("fir_tree"), new TaigaTreeFeature.Builder()
                .log(fir.log.getDefaultState())
                .leaves(fir.leaves.getDefaultState())
                .minHeight(5)
                .maxHeight(28)
                .create());
        FIR_TREE_LARGE = Registry.register(Registry.FEATURE, identifier("fir_tree_large"), new TaigaTreeFeature.Builder()
                .log(fir.log.getDefaultState())
                .leaves(fir.leaves.getDefaultState())
                .minHeight(20)
                .maxHeight(40)
                .trunkWidth(2)
                .create());
    }

    private static WoodGroupHolder registerWoodGroup(String name, MaterialColor woodTopColor, MaterialColor woodSideColor, SaplingGenerator saplingGenerator) {
        MMOLeavesBlock leaves = new MMOLeavesBlock(AbstractBlock.Settings.of(Material.LEAVES)
                .strength(0.2F)
                .ticksRandomly()
                .sounds(BlockSoundGroup.GRASS)
                .nonOpaque());
        new MMOBlockRegistrar(leaves).register(identifier("%s_leaves", name), ITEM_GROUP);

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

        MSaplingBlock sapling = new MSaplingBlock(saplingGenerator, AbstractBlock.Settings.of(Material.PLANT)
                .noCollision()
                .ticksRandomly()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS));
        new MMOBlockRegistrar(sapling).register(identifier("%s_sapling", name), ITEM_GROUP);

        return new WoodGroupHolder(sapling, leaves, log, wood, strippedLog, strippedWood, planks, result);
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

        public WoodGroupHolder(MMOSaplingBlock sapling, MMOLeavesBlock leaves, MMOPillarBlock log, MMOPillarBlock wood,
                               MMOPillarBlock strippedLog, MMOPillarBlock strippedWood, MMOBlock planks,
                               MMOBlockRegistrar.Result result) {
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
        }
    }
}
