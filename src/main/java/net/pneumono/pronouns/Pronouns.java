package net.pneumono.pronouns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.pneumono.pneumonocore.config.Configs;
import net.pneumono.pronouns.network.InformPronounsC2SPacket;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pronouns implements ModInitializer {
	public static final String MOD_ID = "pronouns";
    public static final Logger LOGGER = LoggerFactory.getLogger("Pronouns");

	public static final Identifier DISTRIBUTE_PRONOUNS_ID = new Identifier(MOD_ID, "distribute_pronouns");
	public static final Identifier INFORM_PRONOUNS_ID = new Identifier(MOD_ID, "inform_pronouns");

	public static Map<UUID, PlayerPronouns> uuidPronounsMap = new HashMap<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Pronouns");
		Configs.reload(MOD_ID);

		ServerPlayNetworking.registerGlobalReceiver(INFORM_PRONOUNS_ID, InformPronounsC2SPacket::receive);
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			Pronouns.uuidPronounsMap.remove(handler.getPlayer().getUuid());
			PronounsApi.sendDistributePronounsPacket(server.getPlayerManager().getPlayerList(), uuidPronounsMap);
		});
	}
}