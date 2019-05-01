package invtweaks.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.container.SlotActionType;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ITPacketClick implements ITPacket {
    public int slot;
    public int data;
    public SlotActionType action;
    public int window;

    @SuppressWarnings("unused")
    public ITPacketClick() {
    }

    public ITPacketClick(int _slot, int _data, SlotActionType _action, int _window) {
        slot = _slot;
        data = _data;
        action = _action;
        window = _window;
    }

    @Override
    public void readBytes(@NotNull ByteBuf bytes) {
        slot = bytes.readInt();
        data = bytes.readInt();
        action = SlotActionType.values()[bytes.readInt()];
        window = bytes.readByte();
    }

    @Override
    public void writeBytes(@NotNull ByteBuf bytes) {
        bytes.writeInt(slot);
        bytes.writeInt(data);
        bytes.writeInt(action.ordinal());
        bytes.writeByte(window);
    }

    @Override
    public void handle(PacketListener handler) {
        if(handler instanceof ServerPlayNetworkHandler) {
            @NotNull ServerPlayNetworkHandler serverHandler = (ServerPlayNetworkHandler) handler;
            ServerPlayerEntity player = serverHandler.player;

            if(player.container.syncId == window) {
                player.container.slotClick(slot, data, action, player);
            }
            // TODO: Might want to set a flag to ignore all packets until next sortcomplete even if client window changes.
        }
    }
}
