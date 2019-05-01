package invtweaks;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.Point;

import java.awt.*;
import java.net.URL;
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
public class InvTweaksGuiSettings extends InvTweaksGuiSettingsAbstract {
    private final static int ID_MIDDLE_CLICK = 1;
    private final static int ID_BEFORE_BREAK = 2;
    private final static int ID_SHORTCUTS = 3;
    private final static int ID_SHORTCUTS_HELP = 4;
    private final static int ID_AUTO_REFILL = 5;
    private final static int ID_MORE_OPTIONS = 6;
    private final static int ID_BUG_SORTING = 7;
    private final static int ID_EDITRULES = 100;
    private final static int ID_EDITTREE = 101;
    private final static int ID_HELP = 102;

    private static String labelMiddleClick;
    private static String labelShortcuts;
    private static String labelAutoRefill;
    private static String labelAutoRefillBeforeBreak;
    private static String labelMoreOptions;
    private static String labelBugSorting;

    @SuppressWarnings("unused")
    public InvTweaksGuiSettings(Screen parentScreen_) {
        this(MinecraftClient.getMinecraft(), parentScreen_, InvTweaks.getConfigManager().getConfig());
    }

    public InvTweaksGuiSettings(MinecraftClient mc_, Screen parentScreen_, InvTweaksConfig config_) {
        super(mc_, parentScreen_, config_);

        labelMiddleClick = I18n.translate("invtweaks.settings.middleclick");
        labelShortcuts = I18n.translate("invtweaks.settings.shortcuts");
        labelAutoRefill = I18n.translate("invtweaks.settings.autorefill");
        labelAutoRefillBeforeBreak = I18n.translate("invtweaks.settings.beforebreak");
        labelMoreOptions = I18n.translate("invtweaks.settings.moreoptions");
        labelBugSorting = I18n.translate("invtweaks.help.bugsorting");
    }

    @Override
    public void initGui() {
        super.initGui();

        List<ButtonWidget> controlList = buttonList;
        @NotNull java.awt.Point p = new java.awt.Point();
        int i = 0;

        // Create large buttons

        moveToButtonCoords(1, p);
        controlList.add(new ButtonWidget(ID_EDITRULES, p.getX() + 55, height / 6 + 96,
                I18n.format("invtweaks.settings.rulesfile")));
        controlList.add(new ButtonWidget(ID_EDITTREE, p.getX() + 55, height / 6 + 120,
                I18n.format("invtweaks.settings.treefile")));
        controlList.add(new ButtonWidget(ID_HELP, p.getX() + 55, height / 6 + 144,
                I18n.format("invtweaks.settings.onlinehelp")));

        // Create settings buttons

        moveToButtonCoords(i++, p);
        controlList.add(new InvTweaksGuiTooltipButton(ID_SHORTCUTS_HELP, p.getX() + 130, p.getY(), 20, 20, "?",
                "Shortcuts help"));
        @NotNull InvTweaksGuiTooltipButton shortcutsBtn = new InvTweaksGuiTooltipButton(ID_SHORTCUTS, p.getX(), p.getY(), 130,
                20, computeBooleanButtonLabel(
                InvTweaksConfig.PROP_ENABLE_SHORTCUTS, labelShortcuts),
                I18n.format(
                        "invtweaks.settings.shortcuts.tooltip"));
        controlList.add(shortcutsBtn);

        moveToButtonCoords(i++, p);
        @NotNull InvTweaksGuiTooltipButton beforeBreakBtn = new InvTweaksGuiTooltipButton(ID_BEFORE_BREAK, p.getX(), p.getY(),
                computeBooleanButtonLabel(
                        InvTweaksConfig.PROP_AUTO_REFILL_BEFORE_BREAK,
                        labelAutoRefillBeforeBreak),
                I18n.format(
                        "invtweaks.settings.beforebreak.tooltip"));
        controlList.add(beforeBreakBtn);

        moveToButtonCoords(i++, p);
        @NotNull InvTweaksGuiTooltipButton autoRefillBtn = new InvTweaksGuiTooltipButton(ID_AUTO_REFILL, p.getX(), p.getY(),
                computeBooleanButtonLabel(
                        InvTweaksConfig.PROP_ENABLE_AUTO_REFILL,
                        labelAutoRefill), I18n
                .format(
                        "invtweaks.settings.autorefill.tooltip"));
        controlList.add(autoRefillBtn);

        moveToButtonCoords(i++, p);
        controlList.add(new InvTweaksGuiTooltipButton(ID_MORE_OPTIONS, p.getX(), p.getY(), labelMoreOptions,
                I18n.format(
                        "invtweaks.settings.moreoptions.tooltip")));

        controlList.add(new InvTweaksGuiTooltipButton(ID_BUG_SORTING, 5, this.height - 20, 100, 20,
                labelBugSorting, null, false));

        //noinspection UnusedAssignment
        moveToButtonCoords(i++, p);
        @NotNull InvTweaksGuiTooltipButton middleClickBtn = new InvTweaksGuiTooltipButton(ID_MIDDLE_CLICK, p.getX(), p.getY(),
                computeBooleanButtonLabel(
                        InvTweaksConfig.PROP_ENABLE_MIDDLE_CLICK,
                        labelMiddleClick),
                I18n.format(
                        "invtweaks.settings.middleclick.tooltip"));
        controlList.add(middleClickBtn);

        // Check if links to files are supported, if not disable the buttons
        if(!Desktop.isDesktopSupported()) {
            controlList.stream().filter(InvTweaksObfuscation::isGuiButton).forEach(o -> {
                if(o.id >= ID_EDITRULES && o.id <= ID_HELP) {
                    o.enabled = false;
                }
            });
        }

        // Save control list
        buttonList = controlList;

    }

    @Override
    protected void actionPerformed(@NotNull ButtonWidget guibutton) {
        super.actionPerformed(guibutton);

        // GuiButton
        switch(guibutton.id) {
            // Toggle middle click shortcut
            case ID_MIDDLE_CLICK:
                toggleBooleanButton(guibutton, InvTweaksConfig.PROP_ENABLE_MIDDLE_CLICK, labelMiddleClick);
                break;

            // Toggle auto-refill
            case ID_AUTO_REFILL:
                toggleBooleanButton(guibutton, InvTweaksConfig.PROP_ENABLE_AUTO_REFILL, labelAutoRefill);
                break;

            // Toggle auto-refill before tool break
            case ID_BEFORE_BREAK:
                toggleBooleanButton(guibutton, InvTweaksConfig.PROP_AUTO_REFILL_BEFORE_BREAK,
                        labelAutoRefillBeforeBreak);
                break;

            // Toggle shortcuts
            case ID_SHORTCUTS:
                toggleBooleanButton(guibutton, InvTweaksConfig.PROP_ENABLE_SHORTCUTS, labelShortcuts);
                break;

            // Shortcuts help
            case ID_SHORTCUTS_HELP:
                obf.displayGuiScreen(new InvTweaksGuiShortcutsHelp(mc, this, config));
                break;

            // More options screen
            case ID_MORE_OPTIONS:
                obf.displayGuiScreen(new InvTweaksGuiSettingsAdvanced(mc, parentScreen, config));
                break;

            // Sorting bug help screen
            case ID_BUG_SORTING:
                obf.displayGuiScreen(new InvTweaksGuiModNotWorking(mc, parentScreen, config));
                break;

            // Open rules configuration in external editor
            case ID_EDITRULES:
                try {
                    Desktop.getDesktop().open(InvTweaksConst.CONFIG_RULES_FILE);
                } catch(Exception e) {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.rulesfile.error", e);
                }
                break;

            // Open tree configuration in external editor
            case ID_EDITTREE:
                try {
                    Desktop.getDesktop().open(InvTweaksConst.CONFIG_TREE_FILE);
                } catch(Exception e) {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.treefile.error", e);
                }
                break;

            // Open help in browser
            case ID_HELP:
                try {
                    Desktop.getDesktop().browse(new URL(InvTweaksConst.HELP_URL).toURI());
                } catch(Exception e) {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.onlinehelp.error", e);
                }
                break;

        }

    }
}
