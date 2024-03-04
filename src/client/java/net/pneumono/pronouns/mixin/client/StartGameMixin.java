package net.pneumono.pronouns.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.pneumono.pronouns.pronouns.PronounsClientApi;
import net.pneumono.pronouns.screen.SetupPronounsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
@SuppressWarnings("unused")
public abstract class StartGameMixin {
    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "onInitFinished", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1), cancellable = true)
    private void setScreenPronounCreation(CallbackInfo info) {
        if (PronounsClientApi.getLoadedPronouns() == null) {
            setScreen(new SetupPronounsScreen());
            info.cancel();
        }
    }
}
