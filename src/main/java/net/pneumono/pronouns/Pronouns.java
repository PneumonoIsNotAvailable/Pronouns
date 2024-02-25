package net.pneumono.pronouns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.pneumono.pneumonocore.config.Configs;
import net.pneumono.pronouns.event.LeaveServerEvent;
import net.pneumono.pronouns.network.InformPronounsC2SPacket;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
		ServerPlayConnectionEvents.DISCONNECT.register(new LeaveServerEvent());

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(literal("pronouns")
				.requires(source -> source.hasPermissionLevel(4))
				.then(argument("player", EntityArgumentType.player())
					.executes(context -> {
						ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
						PlayerPronouns pronouns = PronounsApi.getPlayerPronouns(player);
						context.getSource().sendMessage(PronounsApi.getTranslatableTextWithPronouns("pronouns.test_message", pronouns, player.getName().getString()).formatted(Formatting.GOLD));

						return 1;
					})
				)
			)
		);
	}
}