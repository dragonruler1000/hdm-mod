package us.minecraftchest2.hdm_mod.world.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import us.minecraftchest2.hdm_mod.Hdm_mod;

public class ModDimensions {
    public static RegistryKey<World> TestDim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,
            new ResourceLocation(Hdm_mod.MOD_ID, "testdim"));
    public static RegistryKey<World> World1 = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,
            new ResourceLocation(Hdm_mod.MOD_ID, "world1"));
}
