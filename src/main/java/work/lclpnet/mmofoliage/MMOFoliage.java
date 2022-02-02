package work.lclpnet.mmofoliage;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmofoliage.module.IModule;
import work.lclpnet.mmofoliage.module.PlantsModule;

import java.util.Set;

public class MMOFoliage implements ModInitializer {

    public static final String MOD_ID = "lclpmmo";

    private static Set<IModule> modules = ImmutableSet.of(
            new PlantsModule()
    );

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            MMOFoliage.identifier("decorations"),
            () -> new ItemStack(Registry.BLOCK.get(MMOFoliage.identifier("bush")))
    );

    @Override
    public void onInitialize() {
        modules.forEach(IModule::register);
        modules = null;
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier identifier(String format, Object... substitutes) {
        return identifier(String.format(format, substitutes));
    }
}
