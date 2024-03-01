package net.pneumono.pronouns.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.pronouns.PlayerPronouns;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DistributePronounsS2CPacket {
    @SuppressWarnings("unused")
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Map<UUID, PlayerPronouns> map = new HashMap<>();

        JsonArray array = JsonParser.parseString(buf.readString()).getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            String uuid = object.getAsJsonPrimitive("uuid").getAsString();
            JsonObject pronouns = object.getAsJsonObject("pronouns");

            map.put(UUID.fromString(uuid), PlayerPronouns.fromJson(pronouns));
        }

        Pronouns.uuidPronounsMap = map;
    }
}
