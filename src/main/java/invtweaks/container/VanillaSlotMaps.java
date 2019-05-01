package invtweaks.container;

import invtweaks.InvTweaksConst;
import invtweaks.api.container.ContainerSection;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ingame.CreativePlayerInventoryScreen;
import net.minecraft.container.Container;
import net.minecraft.container.HorseContainer;
import net.minecraft.container.Slot;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class VanillaSlotMaps {
    @NotNull
    public static Map<ContainerSection, List<Slot>> containerPlayerSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.CRAFTING_OUT, container.slotList.subList(0, 1));
        slotRefs.put(ContainerSection.CRAFTING_IN, container.slotList.subList(1, 5));
        slotRefs.put(ContainerSection.ARMOR, container.slotList.subList(5, 9));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(9, 45));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(9, 36));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(36, 45));

        return slotRefs;
    }

    @SideOnly(Side.CLIENT)
    public static boolean containerCreativeIsInventory(CreativePlayerInventoryScreen.CreativeContainer container) {
        @Nullable Screen currentScreen = FMLClientHandler.instance().getClient().currentScreen;
        return currentScreen instanceof CreativePlayerInventoryScreen && ((CreativePlayerInventoryScreen) currentScreen).getSelectedTabIndex() == CreativeTabs.INVENTORY.getTabIndex();
    }

    @NotNull
    @SideOnly(Side.CLIENT)
    public static Map<ContainerSection, List<Slot>> containerCreativeSlots(@NotNull CreativePlayerInventoryScreen.CreativeContainer container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.ARMOR, container.slotList.subList(5, 9));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(9, 45));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(9, 36));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(36, 45));

        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerChestDispenserSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        int size = container.slotList.size();

        slotRefs.put(ContainerSection.CHEST, container.slotList.subList(0, size - InvTweaksConst.INVENTORY_SIZE));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(size - InvTweaksConst.INVENTORY_SIZE, size));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(size - InvTweaksConst.INVENTORY_SIZE, size - InvTweaksConst.HOTBAR_SIZE));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(size - InvTweaksConst.HOTBAR_SIZE, size));

        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerHorseSlots(@NotNull HorseContainer container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        int size = container.slotList.size();

        if (container.entity instanceof AbstractDonkeyEntity && ((AbstractDonkeyEntity)container.entity).hasChest()) { // Chest slots are only added if chest is added. Saddle/armor slots always exist.
            slotRefs.put(ContainerSection.CHEST, container.slotList.subList(2, size - InvTweaksConst.INVENTORY_SIZE));
        }
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(size - InvTweaksConst.INVENTORY_SIZE, size));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(size - InvTweaksConst.INVENTORY_SIZE, size - InvTweaksConst.HOTBAR_SIZE));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(size - InvTweaksConst.HOTBAR_SIZE, size));

        return slotRefs;
    }

    public static boolean containerHorseIsInventory(@NotNull HorseContainer container) {
        return container.entity instanceof AbstractDonkeyEntity && ((AbstractDonkeyEntity)container.entity).hasChest();
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerFurnaceSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.FURNACE_IN, container.slotList.subList(0, 1));
        slotRefs.put(ContainerSection.FURNACE_FUEL, container.slotList.subList(1, 2));
        slotRefs.put(ContainerSection.FURNACE_OUT, container.slotList.subList(2, 3));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(3, 39));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(3, 30));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(30, 39));
        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerWorkbenchSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.CRAFTING_OUT, container.slotList.subList(0, 1));
        slotRefs.put(ContainerSection.CRAFTING_IN, container.slotList.subList(1, 10));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(10, 46));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(10, 37));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(37, 46));

        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerEnchantmentSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.ENCHANTMENT, container.slotList.subList(0, 1));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(2, 38));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(2, 29));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(29, 38));

        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerBrewingSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.BREWING_BOTTLES, container.slotList.subList(0, 3));
        slotRefs.put(ContainerSection.BREWING_INGREDIENT, container.slotList.subList(3, 4));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(4, 40));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(4, 31));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(31, 40));

        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> containerRepairSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        slotRefs.put(ContainerSection.CRAFTING_IN, container.slotList.subList(0, 2));
        slotRefs.put(ContainerSection.CRAFTING_OUT, container.slotList.subList(2, 3));
        slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(3, 39));
        slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(3, 30));
        slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(30, 39));

        return slotRefs;
    }

    @NotNull
    public static Map<ContainerSection, List<Slot>> unknownContainerSlots(@NotNull Container container) {
        @NotNull Map<ContainerSection, List<Slot>> slotRefs = new HashMap<>();

        int size = container.slotList.size();

        if(size >= InvTweaksConst.INVENTORY_SIZE) {
            // Assuming the container ends with the inventory, just like all vanilla containers.
            slotRefs.put(ContainerSection.CHEST,
                    container.slotList.subList(0, size - InvTweaksConst.INVENTORY_SIZE));
            slotRefs.put(ContainerSection.INVENTORY, container.slotList.subList(size - InvTweaksConst.INVENTORY_SIZE, size));
            slotRefs.put(ContainerSection.INVENTORY_NOT_HOTBAR, container.slotList.subList(size - InvTweaksConst.INVENTORY_SIZE, size - InvTweaksConst.HOTBAR_SIZE));
            slotRefs.put(ContainerSection.INVENTORY_HOTBAR, container.slotList.subList(size - InvTweaksConst.HOTBAR_SIZE, size));
        } else {
            slotRefs.put(ContainerSection.CHEST, container.slotList.subList(0, size));
        }

        return slotRefs;
    }
}
