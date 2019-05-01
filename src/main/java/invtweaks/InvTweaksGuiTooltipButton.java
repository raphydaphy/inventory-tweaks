package invtweaks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Icon-size button, which get drawns in a specific way to fit its small size.
 *
 * @author Jimeo Wan
 */
public class InvTweaksGuiTooltipButton extends ButtonWidget {
    public final static int LINE_HEIGHT = 11;

    private int hoverTime = 0;
    private long prevSystemTime = 0;

    @Nullable
    private String tooltip = null;
    @Nullable
    private String[] tooltipLines = null;
    private int tooltipWidth = -1;
    private boolean drawBackground = true;

    /**
     * Default size is 150, the common "GuiSmallButton" button size.
     */
    public InvTweaksGuiTooltipButton(int id_, int x, int y, @NotNull String displayString_, String tooltip_) {
        this(id_, x, y, 150, 20, displayString_, tooltip_);
    }

    public InvTweaksGuiTooltipButton(int id_, int x, int y, int w, int h, @NotNull String displayString_, @Nullable String tooltip_) {
        super(id_, x, y, w, h, displayString_);
        if(tooltip_ != null) {
            setTooltip(tooltip_);
        }
    }

    public InvTweaksGuiTooltipButton(int id_, int x, int y, int w, int h, @NotNull String displayString_, @Nullable String tooltip_,
                                     boolean drawBackground_) {
        super(id_, x, y, w, h, displayString_);
        if(tooltip_ != null) {
            setTooltip(tooltip_);
        }
        drawBackground = drawBackground_;
    }

    @Override
    public void drawButton(@NotNull MinecraftClient mc, int mouseX, int mouseY, float partialTicks) {
        if(this.drawBackground) {
            super.drawButton(mc, mouseX, mouseY, partialTicks);
        } else {
            this.drawString(mc.textRenderer, this.displayString, this.x,
                    this.y + (this.height - 8) / 2, 0x999999);
        }

        @NotNull InvTweaksObfuscation obf = new InvTweaksObfuscation(mc);

        if(tooltipLines != null) {
            // Compute hover time
            if(isMouseOverButton(mouseX, mouseY)) {
                long systemTime = System.currentTimeMillis();
                if(prevSystemTime != 0) {
                    hoverTime += systemTime - prevSystemTime;
                }
                prevSystemTime = systemTime;
            } else {
                hoverTime = 0;
                prevSystemTime = 0;
            }

            // Draw tooltip if hover time is long enough
            if(hoverTime > InvTweaksConst.TOOLTIP_DELAY && tooltipLines != null) {

                TextRenderer fontRenderer = obf.getFontRenderer();

                // Compute tooltip params
                int x = mouseX + 12, y = mouseY - LINE_HEIGHT * tooltipLines.length;
                if(tooltipWidth == -1) {
                    for(String line : tooltipLines) {
                        tooltipWidth = Math.max(fontRenderer.getStringWidth(line), tooltipWidth);
                    }
                }
                if(x + tooltipWidth > obf.getCurrentScreen().width) {
                    x = obf.getCurrentScreen().width - tooltipWidth;
                }

                // Draw background
                drawGradientRect(x - 3, y - 3, x + tooltipWidth + 3, y + LINE_HEIGHT * tooltipLines.length, 0xc0000000,
                        0xc0000000);

                // Draw lines
                int lineCount = 0;
                for(@NotNull String line : tooltipLines) {
                    int j1 = y + (lineCount++) * LINE_HEIGHT;
                    int k = -1;
                    fontRenderer.drawStringWithShadow(line, x, j1, k);
                }
            }
        }

    }

    protected boolean isMouseOverButton(int i, int j) {
        return i >= x && j >= y && i < (x + width) && j < (y + height);
    }

    protected int getTextColor(int i, int j) {

        int textColor = 0xffe0e0e0;
        if(!enabled) {
            textColor = 0xffa0a0a0;
        } else if(isMouseOverButton(i, j)) {
            textColor = 0xffffffa0;
        }
        return textColor;

    }

    @Nullable
    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(@NotNull String tooltip_) {
        tooltip_ = tooltip_.replace("\\n", "\n");
        tooltip = tooltip_;
        tooltipLines = tooltip.split("\n");
    }

}
