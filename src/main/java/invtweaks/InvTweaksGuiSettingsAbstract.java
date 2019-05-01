package invtweaks;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;

/**
 * The inventory and chest settings menu.
 *
 * @author Jimeo Wan
 */
public abstract class InvTweaksGuiSettingsAbstract extends Screen {

    protected static final Logger log = InvTweaks.log;
    protected final static int ID_DONE = 200;
    protected static String ON;
    protected static String OFF;
    protected static String LABEL_DONE;
    protected InvTweaksObfuscation obf;
    protected InvTweaksConfig config;
    protected Screen parentScreen;

    public InvTweaksGuiSettingsAbstract(MinecraftClient mc_, Screen parentScreen_, InvTweaksConfig config_) {

        LABEL_DONE = I18n.format("invtweaks.settings.exit");
        ON = ": " + I18n.format("invtweaks.settings.on");
        OFF = ": " + I18n.format("invtweaks.settings.off");

        mc = mc_;
        obf = new InvTweaksObfuscation(mc_);
        parentScreen = parentScreen_;
        config = config_;
    }

    @Override
    public void initGui() {
        List<ButtonWidget> controlList = buttonList;
        @NotNull Point p = new Point();
        moveToButtonCoords(1, p);
        controlList.add(new ButtonWidget(ID_DONE, p.getX() + 55, height / 6 + 168, LABEL_DONE)); // GuiButton

        // Save control list
        buttonList = controlList;

    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(obf.getFontRenderer(), I18n.format("invtweaks.settings.title"),
                width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(@NotNull ButtonWidget guibutton) {
        // GuiButton
        if(guibutton.id == ID_DONE) {
            obf.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char c, int keyCode) {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            obf.displayGuiScreen(parentScreen);
        }
    }

    protected void moveToButtonCoords(int buttonOrder, @NotNull Point p) {
        p.setX(width / 2 - 155 + ((buttonOrder + 1) % 2) * 160);
        p.setY(height / 6 + (buttonOrder / 2) * 24);
    }

    protected void toggleBooleanButton(@NotNull ButtonWidget guibutton, @NotNull String property, String label) {
        @NotNull Boolean enabled = !Boolean.valueOf(config.getProperty(property));
        config.setProperty(property, enabled.toString());
        guibutton.displayString = computeBooleanButtonLabel(property, label);
    }

    @NotNull
    protected String computeBooleanButtonLabel(@NotNull String property, String label) {
        @NotNull String propertyValue = config.getProperty(property);
        Boolean enabled = Boolean.valueOf(propertyValue);
        return label + ((enabled) ? ON : OFF);
    }

}
