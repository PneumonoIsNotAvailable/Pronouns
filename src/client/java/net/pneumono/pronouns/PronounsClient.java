package net.pneumono.pronouns;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.pneumono.pronouns.event.JoinServerEvent;
import net.pneumono.pronouns.network.DistributePronounsS2CPacket;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsClientApi;

public class PronounsClient implements ClientModInitializer {
	public static PlayerPronouns loadedPronouns;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(Pronouns.DISTRIBUTE_PRONOUNS_ID, DistributePronounsS2CPacket::receive);
		ClientPlayConnectionEvents.JOIN.register(new JoinServerEvent());

		PronounsClientApi.readPronouns();
	}
}