package invtweaks.forge;

import invtweaks.*;
import invtweaks.api.IItemTreeListener;
import invtweaks.api.SortingMethod;
import invtweaks.api.container.ContainerSection;
import invtweaks.network.ITPacketHandlerClient;
import invtweaks.network.packets.ITPacketClick;
import invtweaks.network.packets.ITPacketSortComplete;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.container.Container;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
    public static final KeyBinding KEYBINDING_SORT = new KeyBinding("invtweaks.key.sort", Keyboard.KEY_R, "invtweaks.key.category");
    public boolean serverSupportEnabled = false;
    public boolean serverSupportDetected = false;
    private InvTweaks instance;

    @Override
    public void preInit(@NotNull FMLPreInitializationEvent e) {
        super.preInit(e);

        InvTweaks.log = e.getModLog();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        invtweaksChannel.get(Side.CLIENT).pipeline().addAfter("ITMessageToMessageCodec#0", "InvTweaks Handler Client", new ITPacketHandlerClient());

        MinecraftClient mc = FMLClientHandler.instance().getClient();
        // Instantiate mod core
        instance = new InvTweaks(mc);

        ClientRegistry.registerKeyBinding(KEYBINDING_SORT);
    }

    @SubscribeEvent
    public void onTick(@NotNull TickEvent.ClientTickEvent tick) {
        if(tick.phase == TickEvent.Phase.START) {
            MinecraftClient mc = FMLClientHandler.instance().getClient();
            if(mc.world != null && mc.player != null) {
                if(mc.currentScreen != null) {
                    instance.onTickInGUI(mc.currentScreen);
                } else {
                    instance.onTickInGame();
                }
            }
        }
    }

    @SubscribeEvent
    public void notifyPickup(PlayerEvent.ItemPickupEvent e) {
        instance.setItemPickupPending(true);
    }

    @Override
    public void setServerAssistEnabled(boolean enabled) {
        serverSupportEnabled = serverSupportDetected && enabled;
        //InvTweaks.log.info("Server has support: " + serverSupportDetected + " support enabled: " + serverSupportEnabled);
    }

    @Override
    public void setServerHasInvTweaks(boolean hasInvTweaks) {
        serverSupportDetected = hasInvTweaks;
        serverSupportEnabled = hasInvTweaks && !InvTweaks.getConfigManager().getConfig()
                .getProperty(InvTweaksConfig.PROP_ENABLE_SERVER_ITEMSWAP)
                .equals(InvTweaksConfig.VALUE_FALSE);
        //InvTweaks.log.info("Server has support: " + hasInvTweaks + " support enabled: " + serverSupportEnabled);
    }

    @Override
    public void slotClick(@NotNull ClientPlayerInteractionManager playerController, int windowId, int slot, int data, @NotNull SlotActionType action,
                          @NotNull PlayerEntity player) {
        //int modiferKeys = (shiftHold) ? 1 : 0 /* XXX Placeholder */;
        if(serverSupportEnabled) {
            player.container.onSlotClick(slot, data, action, player);

            invtweaksChannel.get(EnvType.CLIENT).writeOutbound(new ITPacketClick(slot, data, action, windowId));
        } else {
            playerController.windowClick(windowId, slot, data, action, player);
        }
    }

    @Override
    public void sortComplete() {
        if(serverSupportEnabled) {
            invtweaksChannel.get(Side.CLIENT).writeOutbound(new ITPacketSortComplete());
        }
    }

    @Override
    public void addOnLoadListener(IItemTreeListener listener) {
        InvTweaksItemTreeLoader.addOnLoadListener(listener);
    }

    @Override
    public boolean removeOnLoadListener(IItemTreeListener listener) {
        return InvTweaksItemTreeLoader.removeOnLoadListener(listener);
    }

    @Override
    public void setSortKeyEnabled(boolean enabled) {
        instance.setSortKeyEnabled(enabled);
    }

    @Override
    public void setTextboxMode(boolean enabled) {
        instance.setTextboxMode(enabled);
    }

    @Override
    public int compareItems(@NotNull ItemStack i, @NotNull ItemStack j) {
        return instance.compareItems(i, j);
    }

    @Override
    public void sort(ContainerSection section, SortingMethod method) {
        // TODO: This seems like something useful enough to be a util method somewhere.
        MinecraftClient mc = FMLClientHandler.instance().getClient();
        Container currentContainer = mc.player.playerContainer;
        if(InvTweaksObfuscation.isGuiContainer(mc.currentScreen)) {
            currentContainer = ((ContainerScreen) mc.currentScreen).container;
        }

        try {
            new InvTweaksHandlerSorting(mc, InvTweaks.getConfigManager().getConfig(), section, method, InvTweaksObfuscation.getSpecialChestRowSize(currentContainer)).sort();
        } catch(Exception e) {
            InvTweaks.logInGameErrorStatic("invtweaks.sort.chest.error", e);
            e.printStackTrace();
        }
    }

    @Override
    public void addClientScheduledTask(@NotNull Runnable task) {
        MinecraftClient.getInstance().addScheduledTask(task);
    }

    @SubscribeEvent
    public void onConnectionToServer(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        setServerHasInvTweaks(false);
    }
}
