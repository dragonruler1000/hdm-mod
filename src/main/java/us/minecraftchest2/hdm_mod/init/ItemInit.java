package us.minecraftchest2.hdm_mod.init;

import net.minecraft.item.Food;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import us.minecraftchest2.hdm_mod.Hdm_mod;
import net.minecraft.item.Item;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hdm_mod.MOD_ID);
    public static final RegistryObject<Item> KNIFE = ITEMS.register("knife", () -> new Item(new Item.Properties().tab(ModCreativeTab.Items)));
    public static final RegistryObject<Item> OMELET = ITEMS.register("omelet", () -> new Item(new Item.Properties().tab(ModCreativeTab.Items).food(new Food.Builder().nutrition(4).saturationMod(2).build())));

    public static class ModCreativeTab extends ItemGroup {
        public static final ModCreativeTab Items = new ModCreativeTab(ItemGroup.TABS.length, "HDM Mod Items");
        private ModCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(KNIFE.get());
        }
    }
}

