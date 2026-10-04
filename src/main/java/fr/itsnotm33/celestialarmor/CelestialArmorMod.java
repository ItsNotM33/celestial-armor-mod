package fr.itsnotm33.celestialarmor;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;

@Mod(CelestialArmorMod.MODID)
public final class CelestialArmorMod {
    public static final String MODID = "celestialarmor";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final ArmorMaterial CELESTIAL_MATERIAL = new ArmorMaterial(
            37,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 3);
                map.put(ArmorType.LEGGINGS, 6);
                map.put(ArmorType.CHESTPLATE, 8);
                map.put(ArmorType.HELMET, 3);
                map.put(ArmorType.BODY, 8);
            }),
            18,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            3.0F,
            0.1F,
            ItemTags.REPAIRS_DIAMOND_ARMOR,
            ResourceLocation.fromNamespaceAndPath(MODID, "celestial_crystal")
    );

    public static final DeferredItem<Item> CELESTIAL_HELMET = ITEMS.registerItem(
            "celestial_helmet",
            properties -> new Item(CELESTIAL_MATERIAL.humanoidProperties(properties, ArmorType.HELMET))
    );

    public static final DeferredItem<Item> CELESTIAL_CHESTPLATE = ITEMS.registerItem(
            "celestial_chestplate",
            properties -> new Item(CELESTIAL_MATERIAL.humanoidProperties(properties, ArmorType.CHESTPLATE))
    );

    public static final DeferredItem<Item> CELESTIAL_LEGGINGS = ITEMS.registerItem(
            "celestial_leggings",
            properties -> new Item(CELESTIAL_MATERIAL.humanoidProperties(properties, ArmorType.LEGGINGS))
    );

    public static final DeferredItem<Item> CELESTIAL_BOOTS = ITEMS.registerItem(
            "celestial_boots",
            properties -> new Item(CELESTIAL_MATERIAL.humanoidProperties(properties, ArmorType.BOOTS))
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
