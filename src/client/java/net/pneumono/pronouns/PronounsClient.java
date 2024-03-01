package net.pneumono.pronouns;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.pneumono.pronouns.network.DistributePronounsS2CPacket;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsClientApi;
import net.pneumono.pronouns.screen.ServerPronounsScreen;
import org.lwjgl.glfw.GLFW;

public class PronounsClient implements ClientModInitializer {
	public static final KeyBinding pronounScreenKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.pronouns.screen",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_P,
			"category.pronouns.pronouns"
	));

	public static PlayerPronouns loadedPronouns;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(Pronouns.DISTRIBUTE_PRONOUNS_ID, DistributePronounsS2CPacket::receive);
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> PronounsClientApi.sendInformPronounsPacket(PronounsClient.loadedPronouns));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (pronounScreenKeybind.wasPressed()) {
				client.setScreen(new ServerPronounsScreen());
			}
		});

		PronounsClientApi.readPronouns();
	}
}