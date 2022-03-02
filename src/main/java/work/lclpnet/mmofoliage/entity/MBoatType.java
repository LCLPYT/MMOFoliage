package work.lclpnet.mmofoliage.entity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MBoatType {

    public final Identifier identifier;
    public final Block baseBlock;
    public Item boatItem = Items.OAK_BOAT;

    protected MBoatType(Identifier identifier, Block baseBlock) {
        this.identifier = Objects.requireNonNull(identifier);
        this.baseBlock = Objects.requireNonNull(baseBlock);
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

    public void toTag(NbtCompound tag) {
        tag.putString("MBoatType", this.identifier.toString());
    }

    private static final Map<Identifier, MBoatType> types = new HashMap<>();

    public static MBoatType register(Identifier identifier, Block baseBlock) {
        MBoatType type = new MBoatType(identifier, baseBlock);
        types.put(identifier, type);
        return type;
    }

    @Nullable
    public static MBoatType get(Identifier identifier) {
        return types.get(identifier);
    }

    @Nullable
    public static MBoatType fromTag(NbtCompound tag) {
        if (!tag.contains("MBoatType")) return null;

        Identifier id = new Identifier(tag.getString("MBoatType"));
        return types.get(id);
    }
}
