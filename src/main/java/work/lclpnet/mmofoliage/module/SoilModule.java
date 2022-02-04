package work.lclpnet.mmofoliage.module;

import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.FleshBlock;
import work.lclpnet.mmofoliage.block.MudBlock;

public class SoilModule implements IModule {

    @Override
    public void register() {
        new MMOBlockRegistrar(new MudBlock())
                .register(MMOFoliage.identifier("mud"), MMOFoliage.ITEM_GROUP);

        new MMOBlockRegistrar(new FleshBlock())
                .register(MMOFoliage.identifier("flesh"), MMOFoliage.ITEM_GROUP);
    }
}
