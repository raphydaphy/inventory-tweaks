package invtweaks.network;

import invtweaks.network.packets.ITPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

public class ITPacketHandlerClient extends SimpleChannelInboundHandler<ITPacket> {
    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull ITPacket msg) throws Exception {
        @NotNull final ClientPlayNetworkHandler handler = (ClientPlayNetworkHandler) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        MinecraftClient.getMinecraft().addScheduledTask(() -> msg.handle(handler));
    }
}
