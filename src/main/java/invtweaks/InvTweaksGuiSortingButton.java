package invtweaks;

import invtweaks.api.SortingMethod;
import invtweaks.api.container.ContainerSection;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

/**
 * Chest sorting button
 *
 * @author Jimeo Wan
 */
public class InvTweaksGuiSortingButton extends InvTweaksGuiIconButton {

    private final ContainerSection section = ContainerSection.CHEST;

    private SortingMethod algorithm;
    private int rowSize;

    public InvTweaksGuiSortingButton(InvTweaksConfigManager cfgManager_, int id_, int x, int y, int w, int h,
                                     String displayString_, String tooltip, SortingMethod algorithm_, int rowSize_,
                                     boolean useCustomTexture) {
        super(cfgManager_, id_, x, y, w, h, displayString_, tooltip, useCustomTexture);
        algorithm = algorithm_;
        rowSize = rowSize_;
    }

    @Override
    public void drawButton(@NotNull MinecraftClient mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        // Display symbol
        int textColor = getTextColor(mouseX, mouseY);
        switch(displayString) {
            case "h":
                drawRect(x + 3, y + 3, x + width - 3, y + 4,
                        textColor);
                drawRect(x + 3, y + 6, x + width - 3, y + 7,
                        textColor);
                break;
            case "v":
                drawRect(x + 3, y + 3, x + 4, y + height - 3,
                        textColor);
                drawRect(x + 6, y + 3, x + 7, y + height - 3,
                        textColor);
                break;
            default:
                drawRect(x + 3, y + 3, x + width - 3, y + 4,
                        textColor);
                drawRect(x + 5, y + 4, x + 6, y + 5, textColor);
                drawRect(x + 4, y + 5, x + 5, y + 6, textColor);
                drawRect(x + 3, y + 6, x + width - 3, y + 7,
                        textColor);
                break;
        }
    }

    /**
     * Sort container
     */
    @Override
    public boolean mousePressed(MinecraftClient minecraft, int i, int j) {
        if(super.mousePressed(minecraft, i, j)) {
            try {
                new InvTweaksHandlerSorting(minecraft, cfgManager.getConfig(), section, algorithm, rowSize).sort();
            } catch(Exception e) {
                InvTweaks.logInGameErrorStatic("invtweaks.sort.chest.error", e);
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }

    }

}
