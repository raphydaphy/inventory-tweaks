package invtweaks;

import invtweaks.api.container.ContainerSection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ingame.CreativePlayerInventoryScreen;
import net.minecraft.client.gui.ingame.EditSignScreen;
import net.minecraft.client.gui.ingame.PlayerInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Minecraft 1.3 Obfuscation layer
 *
 * @author Jimeo Wan
 */
public class InvTweaksObfuscation {

    private static final Logger log = InvTweaks.log;
    public MinecraftClient mc;

    public InvTweaksObfuscation(MinecraftClient mc_) {
        mc = mc_;
    }

    // Minecraft members

    @Nullable
    public static String getNamespacedID(@Nullable String id) {
        if(id == null) {
            return null;
        } else if(id.indexOf(':') == -1) {
            return "minecraft:" + id;
        }
        return id;
    }

    public static int getDisplayWidth() {
        return FMLClientHandler.instance().getClient().displayWidth;
    }

    public static int getDisplayHeight() {
        return FMLClientHandler.instance().getClient().displayHeight;
    }

    public static boolean areItemStacksEqual(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2) {
        return itemStack1.isItemEqual(itemStack2) && itemStack1.getCount() == itemStack2.getCount();
    }

    @NotNull
    public static ItemStack getSlotStack(@NotNull Container container, int i) {
        // Slot
        Slot slot = container.slotList.get(i);
        return (slot == null) ? ItemStack.EMPTY : slot.getStack(); // getStack
    }

    public static int getSlotNumber(Slot slot) {
        try {
            // Creative slots don't set the "slotNumber" property, serve as a proxy for true slots
            if(slot instanceof CreativePlayerInventoryScreen.CreativeSlot) {
                Slot underlyingSlot = ((CreativePlayerInventoryScreen.CreativeSlot) slot).slot;
                if(underlyingSlot != null) {
                    return underlyingSlot.id;
                } else {
                    log.warn("Creative inventory: Failed to get real slot");
                }
            }
        } catch(Exception e) {
            log.warn("Failed to access creative slot number");
        }
        return slot.id;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public static Slot getSlotAtMousePosition(@Nullable ContainerScreen guiContainer) {
        // Copied from GuiContainer
        if(guiContainer != null) {
            Container container = guiContainer.container;

            int x = getMouseX(guiContainer);
            int y = getMouseY(guiContainer);
            for(int k = 0; k < container.slotList.size(); k++) {
                Slot slot = container.slotList.get(k);
                if(getIsMouseOverSlot(guiContainer, slot, x, y)) {
                    return slot;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static boolean getIsMouseOverSlot(@Nullable ContainerScreen guiContainer, @NotNull Slot slot, int x, int y) {
        // Copied from GuiContainer
        if(guiContainer != null) {
            x -= guiContainer.left;
            y -= guiContainer.top;
            return x >= slot.xPosition - 1 && x < slot.xPosition + 16 + 1 && y >= slot.yPosition - 1 && y < slot.yPosition + 16 + 1;
        } else {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    private static int getMouseX(@NotNull ContainerScreen guiContainer) {
        return (Mouse.getEventX() * guiContainer.width) / getDisplayWidth();
    }

    @SideOnly(Side.CLIENT)
    private static int getMouseY(@NotNull ContainerScreen guiContainer) {
        return guiContainer.height -
                (Mouse.getEventY() * guiContainer.height) / getDisplayHeight() - 1;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static int getSpecialChestRowSize(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$rowSize()"
        return 0;
    }

    // EntityPlayer members

    // Static access
    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isValidChest(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$validChest()"
        return false;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isLargeChest(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$largeChest()"
        return false;
    }

    // InventoryPlayer members

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isValidInventory(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$validInventory()"
        return false;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean showButtons(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$showButtons()"
        return false;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static Map<ContainerSection, List<Slot>> getContainerSlotMap(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$slotMap()"
        return null;
    }

    public static boolean isGuiContainer(@Nullable Object o) { // GuiContainer (abstract class)
        return o != null && o instanceof ContainerScreen;
    }

    public static boolean isGuiInventoryCreative(@Nullable Object o) { // GuiInventoryCreative
        return o != null && o.getClass().equals(CreativePlayerInventoryScreen.class);
    }

    public static boolean isGuiInventory(@Nullable Object o) { // GuiInventory
        return o != null && o.getClass().equals(PlayerInventoryScreen.class);
    }

    public static boolean isGuiButton(@Nullable Object o) { // GuiButton
        return o != null && o instanceof ButtonWidget;
    }

    // FontRenderer members

    public static boolean isGuiEditSign(@Nullable Object o) {
        return o != null && o.getClass().equals(EditSignScreen.class);
    }

    public static boolean isItemArmor(@Nullable Object o) { // ItemArmor
        return o != null && o instanceof ArmorItem;
    }

    public static boolean isBasicSlot(@Nullable Object o) { // Slot
        return o != null && (o.getClass()
                .equals(Slot.class) || o.getClass().equals(CreativePlayerInventoryScreen.CreativeSlot.class));
    }

    // Container members

    public static Container getCurrentContainer() {
        MinecraftClient mc = FMLClientHandler.instance().getClient();
        Container currentContainer = mc.player.playerContainer;
        if(InvTweaksObfuscation.isGuiContainer(mc.currentScreen)) {
            currentContainer = ((ContainerScreen) mc.currentScreen).container;
        }

        return currentContainer;
    }

    // Slot members

    public static boolean areSameItemType(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2) {
        return !itemStack1.isEmpty() && !itemStack2.isEmpty() &&
                (itemStack1.isItemEqual(itemStack2) ||
                        (itemStack1.isItemStackDamageable() && itemStack1.getItem() == itemStack2.getItem()));
    }

    public static boolean areItemsStackable(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2) {
        return !itemStack1.isEmpty() && !itemStack2.isEmpty() && itemStack1.isItemEqual(itemStack2) &&
                itemStack1.isStackable() &&
                (!itemStack1.getHasSubtypes() || itemStack1.getItemDamage() == itemStack2.getItemDamage()) &&
                ItemStack.areItemStackTagsEqual(itemStack1, itemStack2);
    }

    public void addChatMessage(@NotNull String message) {
        if(mc.inGameHud != null) {
            mc.inGameHud.getChatGUI().printChatMessage(new StringTextComponent(message));
        }
    }

    public PlayerEntity getThePlayer() {
        return mc.player;
    }

    public ClientPlayerInteractionManager getPlayerController() {
        return mc.interactionManager;
    }

    @Nullable
    public Screen getCurrentScreen() {
        return mc.currentScreen;
    }

    public TextRenderer getFontRenderer() {
        return mc.textRenderer;
    }

    public void displayGuiScreen(Screen parentScreen) {
        mc.displayGuiScreen(parentScreen);
    }

    public GameSettings getGameSettings() {
        return mc.options;
    }

    public int getKeyBindingForwardKeyCode() {
        return getGameSettings().keyBindForward.keyCode;
    }

    // Classes

    public int getKeyBindingBackKeyCode() {
        return getGameSettings().keyBindBack.keyCode;
    }

    public PlayerInventory getInventoryPlayer() { // InventoryPlayer
        return getThePlayer().inventory;
    }

    public DefaultedList<ItemStack> getMainInventory() {
        return getInventoryPlayer().main;
    }

    @NotNull
    public ItemStack getHeldStack() {
        return getInventoryPlayer().getItemStack(); // getItemStack
    }

    @NotNull
    public ItemStack getFocusedStack() {
        return getInventoryPlayer().getCurrentItem(); // getCurrentItem
    }

    public int getFocusedSlot() {
        return getInventoryPlayer().selectedSlot; // currentItem
    }

    public boolean hasTexture(@NotNull Identifier texture) {
        try {
            mc.getResourceManager().getResource(texture);
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    @NotNull
    public ItemStack getOffhandStack() {
        return getInventoryPlayer().offHand.get(0);
    }
}
