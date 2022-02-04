package work.lclpnet.mmofoliage.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import work.lclpnet.mmocontent.networking.MMONetworking;
import work.lclpnet.mmofoliage.asm.mixin.common.BoatEntityAccessor;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import javax.annotation.Nullable;

public class MBoatEntity extends BoatEntity {

    private static final TrackedData<CompoundTag> TYPE = DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

    public MBoatEntity(World world, double x, double y, double z) {
        this(AdditionalWoodModule.boatEntityType, world);
        this.updatePosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public MBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, new CompoundTag());
    }

    @Nullable
    public MBoatType getMBoatType() {
        return MBoatType.fromTag(this.dataTracker.get(TYPE));
    }

    public void setMBoatType(MBoatType type) {
        final CompoundTag tag = new CompoundTag();
        type.toTag(tag);
        this.dataTracker.set(TYPE, tag);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        BoatEntityAccessor accessor = (BoatEntityAccessor) this;
        accessor.setFallVelocity(this.getVelocity().y);

        if (!this.hasVehicle()) {
            if (onGround) {
                if (this.fallDistance > 3.0F) {
                    if (accessor.getLocation() != BoatEntity.Location.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.handleFallDamage(this.fallDistance, 1.0F);
                    if (!this.world.isClient && !this.removed) {
                        this.remove();
                        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            int j;

                            final MBoatType boatType = getMBoatType();
                            if (boatType != null) {
                                for (j = 0; j < 3; ++j) {
                                    this.dropItem(boatType.baseBlock);
                                }
                            }

                            for(j = 0; j < 2; ++j) {
                                this.dropItem(Items.STICK);
                            }
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.world.getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0D) {
                this.fallDistance = (float)((double)this.fallDistance - heightDifference);
            }
        }
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        MBoatType type = getMBoatType();
        if (type != null) type.toTag(tag);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        MBoatType type = MBoatType.fromTag(tag);
        if (type != null) setMBoatType(type);
    }

    @Override
    public Item asItem() {
        final MBoatType type = getMBoatType();
        return type != null ? type.boatItem.get() : super.asItem();
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return MMONetworking.createMMOSpawnPacket(this);
    }
}
