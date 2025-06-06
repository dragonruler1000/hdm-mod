package us.minecraftchest2.hdm_mod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.minecraftchest2.hdm_mod.block.ModBlocks;
import us.minecraftchest2.hdm_mod.item.ModItems;
//import us.minecraftchest2.hdm_mod.init.ItemInit;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("hdm_mod")
public class Hdm_mod {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "hdm_mod";

    public Hdm_mod() {
        // Register the setup method for modloading
        IEventBus modEventBus1 = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus1);
        ModBlocks.register(modEventBus1);

        modEventBus1.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modEventBus1.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus1.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modEventBus1.addListener(this::doClientStuff);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfig.GENERAL_SPEC, "modconfig.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        final IEventBus modEventBus = modEventBus1;

        modEventBus.addListener(this::setup);
        //ItemInit.ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("hdm_mod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
