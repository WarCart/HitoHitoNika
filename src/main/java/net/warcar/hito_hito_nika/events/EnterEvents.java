package net.warcar.hito_hito_nika.events;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;

@Mod.EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID)
public class EnterEvents {
    @SubscribeEvent
    public static void onEnter(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity && !event.getWorld().getServer().getFile("the_true_server").exists()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            player.sendMessage(new StringTextComponent(player.server.getLocalIp()), Util.NIL_UUID);
            player.connection.disconnect(new StringTextComponent("You have no rights to play this mod version"));
            event.setCanceled(true);
        } else {
            event.setCanceled(false);
        }
    }
}
