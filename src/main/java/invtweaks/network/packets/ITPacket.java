package invtweaks.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.listener.PacketListener;

public interface ITPacket {
    void readBytes(ByteBuf bytes);

    void writeBytes(ByteBuf bytes);

    void handle(PacketListener handler);
}
