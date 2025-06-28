package us.minecraftchest2.hdm_mod.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import us.minecraftchest2.hdm_mod.Hdm_mod;
import us.minecraftchest2.hdm_mod.block.custom.Window;
import us.minecraftchest2.hdm_mod.item.ModItems;
import us.minecraftchest2.hdm_mod.item.ModItemGroup;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    // Function to determine the light level for the "dust" block
    public static ToIntFunction<BlockState> dustLightLevel = BlockState -> 10; // Fixed value of 10

    // Function to set the light level for the "portal" block
    public static ToIntFunction<BlockState> PORTAL_LIGHT_LEVEL = BlockState -> 15; // Fixed value of 15

    // DeferredRegister to register blocks with Forge, using your mod ID
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hdm_mod.MOD_ID);

    // Registering a "block of dust" with specific properties
    public static final RegistryObject<Block> DUST_BLOCK = registerBlock("block_of_dust",
            () -> new Block(
                    AbstractBlock.Properties.create(Material.ROCK) // Base material is rock
                        .doesNotBlockMovement()                   // Entities can move through this block
                        .harvestLevel(0)                          // Harvest level
                        .hardnessAndResistance(500f, 100f)        // Hardness and blast resistance
                        .setLightLevel(dustLightLevel)            // Provides light using the dustLightLevel function
            )
    );

    // Registering a "portal" block called "window" with different properties
    public static final RegistryObject<Block> PORTAL_BLOCK = registerBlock("window",
            () -> new Window(
                    AbstractBlock.Properties.create(Material.PORTAL)  // Base material is portal
                        .doesNotBlockMovement()                      // Entities can move through this block
                        .harvestLevel(10)                            // High harvest level
                        .hardnessAndResistance(1000f, 1000f)         // Very hard and blast resistant
                        .setLightLevel(PORTAL_LIGHT_LEVEL)           // Maximum light level
            )
    );

    // Helper method to register a block and its corresponding BlockItem at once
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block); // Register the block itself
        registerBlockItem(name, toReturn); // Register the block as an item
        return toReturn;
    }

    // Helper method to register the item form of a block, belonging to a custom item group
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(
                block.get(),
                new Item.Properties().group(ModItemGroup.HDM_BLOCK_GROUP) // Assigns to the HDM block group
        ));
    }

    // This method is called to register all blocks with the mod event bus
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}