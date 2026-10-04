package fr.itsnotm33.celestialarmor.client;

import fr.itsnotm33.celestialarmor.CelestialArmorMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentModel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = CelestialArmorMod.MODID, value = Dist.CLIENT)
public final class CelestialArmorClient {
    private CelestialArmorClient() {}

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public Model getGenericArmorModel(ItemStack stack, EquipmentModel.LayerType layerType, Model original) {
                if (original instanceof HumanoidModel<?> humanoid) {
                    return new CelestialArmorModel(humanoid, stack.getItem());
                }
                return original;
            }
        },
                CelestialArmorMod.CELESTIAL_HELMET.get(),
                CelestialArmorMod.CELESTIAL_CHESTPLATE.get(),
                CelestialArmorMod.CELESTIAL_LEGGINGS.get(),
                CelestialArmorMod.CELESTIAL_BOOTS.get()
        );
    }
}
