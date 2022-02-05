package work.lclpnet.mmofoliage.entity;

import net.minecraft.block.Block;
import net.minecraft.item.BoatItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class MBoatType {

    public final Identifier identifier;
    public final Block baseBlock;
    public final Supplier<BoatItem> boatItem;

    protected MBoatType(Identifier identifier, Block baseBlock, Supplier<BoatItem> boatItem) {
        this.identifier = Objects.requireNonNull(identifier);
        this.baseBlock = Objects.requireNonNull(baseBlock);
        this.boatItem = Objects.requireNonNull(boatItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MBoatType mBoatType = (MBoatType) o;
        return Objects.equals(identifier, mBoatType.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    public void toTag(CompoundTag tag) {
        tag.putString("MBoatType", this.identifier.toString());
    }

    private static final Map<Identifier, MBoatType> types = new HashMap<>();

    public static MBoatType register(Identifier identifier, Block baseBlock, Supplier<BoatItem> item) {
        MBoatType type = new MBoatType(identifier, baseBlock, item);
        types.put(identifier, type);
        return type;
    }

    @Nullable
    public static MBoatType get(Identifier identifier) {
        return types.get(identifier);
    }

    @Nullable
    public static MBoatType fromTag(CompoundTag tag) {
        if (!tag.contains("MBoatType")) return null;

        Identifier id = new Identifier(tag.getString("MBoatType"));
        return types.get(id);
    }
}