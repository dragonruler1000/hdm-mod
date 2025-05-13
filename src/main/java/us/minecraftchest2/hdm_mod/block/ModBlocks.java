package us.minecraftchest2.hdm_mod.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import us.minecraftchest2.hdm_mod.Hdm_mod;
import us.minecraftchest2.hdm_mod.item.ModItems;
import us.minecraftchest2.hdm_mod.item.ModItemGroup;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static ToIntFunction<BlockState> dustLightLevel = BlockState -> 10; // Dust Light Level
    public static ToIntFunction<BlockState> PORTAL_LIGHT_LEVEL = BlockState ->15;
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hdm_mod.MOD_ID);


    public static final RegistryObject<Block> DUST_BLOCK = registerBlock("block_of_dust",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK).doesNotBlockMovement().harvestLevel(0)
                    .hardnessAndResistance(500f, 100f).setLightLevel(dustLightLevel)));

    public static final RegistryObject<Block> PORTAL_BLOCK = registerBlock("window",
            () -> new Block(AbstractBlock.Properties.create(Material.PORTAL).doesNotBlockMovement().harvestLevel(10)
                    .hardnessAndResistance(1000f, 1000f).setLightLevel(PORTAL_LIGHT_LEVEL)));


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.HDM_BLOCK_GROUP)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
