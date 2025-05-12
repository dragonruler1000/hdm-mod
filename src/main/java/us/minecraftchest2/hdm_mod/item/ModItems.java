package us.minecraftchest2.hdm_mod.item;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import us.minecraftchest2.hdm_mod.Hdm_mod;
import us.minecraftchest2.hdm_mod.item.custom.SubtleKnife;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Hdm_mod.MOD_ID);

    public static final RegistryObject<Item> DUST = ITEMS.register("dust",
            () -> new Item(new Item.Properties().isImmuneToFire().maxStackSize(42).group(ModItemGroup.HDM_ITEM_GROUP)));


    public static final RegistryObject<Item> OMELET = ITEMS.register("omelet",
            () -> new Item(new Item.Properties().group(ModItemGroup.HDM_ITEM_GROUP)));

    public static final RegistryObject<Item> KNIFE = ITEMS.register("subtle_knife",
            () -> new SubtleKnife(new Item.Properties().maxStackSize(1).group(ModItemGroup.HDM_ITEM_GROUP).maxDamage(2000)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
