package fr.itsnotm33.celestialarmor;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

@Mod(CelestialArmorMod.MODID)
public final class CelestialArmorMod {
    public static final String MODID = "celestialarmor";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final ResourceKey<EquipmentAsset> CELESTIAL_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            ResourceLocation.fromNamespaceAndPath(MODID, "celestial_crystal")
    );

    public static final ArmorMaterial CELESTIAL_MATERIAL = new ArmorMaterial(
            37,
            Map.of(
                    ArmorType.BOOTS, 3,
                    ArmorType.LEGGINGS, 6,
                    ArmorType.CHESTPLATE, 8,
                    ArmorType.HELMET, 3
            ),
            18,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            3.0F,
            0.1F,
            ItemTags.REPAIRS_DIAMOND_ARMOR,
            CELESTIAL_ASSET
    );

    public static final DeferredItem<Item> CELESTIAL_HELMET = ITEMS.registerItem(
            "celestial_helmet",
            props -> new Item(CELESTIAL_MATERIAL.humanoidProperties(props, ArmorType.HELMET))
    );
    public static final DeferredItem<Item> CELESTIAL_CHESTPLATE = ITEMS.registerItem(
            "celestial_chestplate",
            props -> new Item(CELESTIAL_MATERIAL.humanoidProperties(props, ArmorType.CHESTPLATE))
    );
    public static final DeferredItem<Item> CELESTIAL_LEGGINGS = ITEMS.registerItem(
            "celestial_leggings",
            props -> new Item(CELESTIAL_MATERIAL.humanoidProperties(props, ArmorType.LEGGINGS))
    );
    public static final DeferredItem<Item> CELESTIAL_BOOTS = ITEMS.registerItem(
            "celestial_boots",
            props -> new Item(CELESTIAL_MATERIAL.humanoidProperties(props, ArmorType.BOOTS))
    );

    public CelestialArmorMod(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(CELESTIAL_HELMET);
            event.accept(CELESTIAL_CHESTPLATE);
            event.accept(CELESTIAL_LEGGINGS);
            event.accept(CELESTIAL_BOOTS);
        }
    }
}
