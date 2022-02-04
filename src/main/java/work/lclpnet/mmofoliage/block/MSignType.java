package work.lclpnet.mmofoliage.block;

import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

public class MSignType extends SignType {

    protected final Identifier id;

    public MSignType(Identifier id) {
        super(id.getPath());
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }
}
