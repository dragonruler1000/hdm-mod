//package us.minecraftchest2.hdm_mod.item.custom;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.*;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.feature.structure.Structure;
////import net.minecraft.world.gen.feature.structure.StructureFeature;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.server.ServerWorld;
//
//public class StructureLocatorItem extends Item {
//
//    public StructureLocatorItem(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
//        if (!world.isClientSide && world instanceof ServerWorld) {
//            ServerWorld serverWorld = (ServerWorld) world;
//            BlockPos playerPos = player.blockPosition();
//
//            // Replace this with your own structure, or any StructureFeature
//            Structure<?> structureToFind = Structure.STRONGHOLD;
//
//            BlockPos structurePos = serverWorld.getStructureLocation(
//                    structureToFind,
//                    playerPos,
//                    100, // search radius in chunks
//                    false
//            );
//
//            if (structurePos != null) {
//                player.sendMessage(
//                        new StringTextComponent("Nearest structure at: " + structurePos.getX() + ", " + structurePos.getY() + ", " + structurePos.getZ()),
//                        player.getUUID()
//                );
//            } else {
//                player.sendMessage(
//                        new StringTextComponent("No structure found nearby."),
//                        player.getUUID()
//                );
//            }
//        }
//
//        return ActionResult.resultSuccess(player.getItemInHand(hand));
//    }
//}
