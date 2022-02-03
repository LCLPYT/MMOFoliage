package work.lclpnet.mmofoliage.client;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ClientModInitializer;
import work.lclpnet.mmofoliage.client.module.AdditionalWoodClientModule;
import work.lclpnet.mmofoliage.client.module.IClientModule;
import work.lclpnet.mmofoliage.client.module.PlantsClientModule;

import java.util.Set;

public class MMOFoliageClient implements ClientModInitializer {

    private static Set<IClientModule> modules = ImmutableSet.of(
            new PlantsClientModule(),
            new AdditionalWoodClientModule()
    );

    @Override
    public void onInitializeClient() {
        modules.forEach(IClientModule::register);
        modules = null;
    }
}
