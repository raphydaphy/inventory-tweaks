package invtweaks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

/**
 * Icon-size button, which get drawns in a specific way to fit its small size.
 *
 * @author Jimeo Wan
 */
public class InvTweaksGuiIconButton extends InvTweaksGuiTooltipButton {

    @NotNull
    private static Identifier resourceButtonCustom = new Identifier("inventorytweaks",
            "textures/gui/button10px.png");
    @NotNull
    private static Identifier resourceButtonDefault = new Identifier("textures/gui/widgets.png");
    protected InvTweaksConfigManager cfgManager;
    private boolean useCustomTexture;

    public InvTweaksGuiIconButton(InvTweaksConfigManager cfgManager_, int id_, int x, int y, int w, int h,
                                  String displayString_, String tooltip, boolean useCustomTexture_) {
        super(id_, x, y, w, h, displayString_, tooltip);
        cfgManager = cfgManager_;
        useCustomTexture = useCustomTexture_;
    }

    @Override
    public void drawButton(@NotNull MinecraftClient mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        // Draw background (use the 4 corners of the texture to fit best its small size)
        int k = getHoverState(isMouseOverButton(mouseX, mouseY));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if(useCustomTexture) {
            mc.getTextureManager().bindTexture(resourceButtonCustom);
            drawTexturedModalRect(x, y, (k - 1) * 10, 0, width, height);
        } else {
            mc.getTextureManager().bindTexture(resourceButtonDefault);
            drawTexturedModalRect(x, y, 1, 46 + k * 20 + 1, width / 2,
                    height / 2);
            drawTexturedModalRect(x, y + height / 2, 1,
                    46 + k * 20 + 20 - height / 2 - 1, width / 2, height / 2);
            drawTexturedModalRect(x + width / 2, y, 200 - width / 2 - 1,
                    46 + k * 20 + 1, width / 2, height / 2);
            drawTexturedModalRect(x + width / 2, y + height / 2,
                    200 - width / 2 - 1, 46 + k * 20 + 19 - height / 2,
                    width / 2, height / 2);
        }

    }

}