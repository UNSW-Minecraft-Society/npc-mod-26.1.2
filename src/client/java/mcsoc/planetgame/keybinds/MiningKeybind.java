package mcsoc.planetgame.keybinds;

import org.lwjgl.glfw.GLFW;

import mcsoc.planetgame.networking.TriggerSecondAbilityC2SPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class MiningKeybind {
    private MiningKeybind() { /* delete */ }

    private static KeyBinding mining_keybind;

    public static void registerBindings() {
        mining_keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "mcsoc.planetgame.second_ability_keybind", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_R, // The keycode of the key
            "mcsoc.planetgame.keybinds_category" // The translation key of the keybinding's category.
        ));
    }

    public static void registerBindEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (mining_keybind.wasPressed()) {
                ClientPlayNetworking.send(new TriggerSecondAbilityC2SPayload());
            }
        });
    }

    public static void register() {
        registerBindings();
        registerBindEvents();
    }
}
