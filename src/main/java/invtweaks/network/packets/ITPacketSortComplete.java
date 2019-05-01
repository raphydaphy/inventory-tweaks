package invtweaks.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ITPacketSortComplete implements ITPacket {
    @Override
    public void readBytes(ByteBuf bytes) {

    }

    @Override
    public void writeBytes(ByteBuf bytes) {

    }

    @Override
    public void handle(PacketListener handler) {
        if(handler instanceof ServerPlayNetworkHandler) {
            @NotNull ServerPlayNetworkHandler serverHandler = (ServerPlayNetworkHandler) handler;
            ServerPlayerEntity player = serverHandler.player;

            player.sendContainerToPlayer(player.container);
        }
    }
}
