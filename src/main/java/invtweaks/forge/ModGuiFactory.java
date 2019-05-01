package invtweaks.forge;

import invtweaks.InvTweaksGuiSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraftforge.fml.client.IModGuiFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@SuppressWarnings("unused")
public class ModGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(MinecraftClient minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @NotNull
    @Override
    public Screen createConfigGui(Screen parentScreen) {
        // TODO: Find out if we can just cache this?
        return new InvTweaksGuiSettings(parentScreen);
    }

    @Nullable
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
