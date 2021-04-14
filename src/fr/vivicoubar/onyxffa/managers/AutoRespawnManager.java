package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoRespawnManager {
    OnyxFFaMain main;

    public AutoRespawnManager(OnyxFFaMain main) {
        this.main = main;
    }

    public void askRespawn(FFaPlayer fFaPlayer) {
        if (fFaPlayer.isAutorespawnBoolean()) {
            fFaPlayer.getPlayer().closeInventory();

            Location temp = new Location(fFaPlayer.getPlayer().getWorld(), 738, 48, 625);

            fFaPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            fFaPlayer.getPlayer().teleport(temp);
            fFaPlayer.setState(FFaPlayerStates.SPECTATOR);
            new BukkitRunnable() {
                int timer = 3;

                @Override
                public void run() {

                    if (timer > 0 && fFaPlayer.getState() == FFaPlayerStates.SPECTATOR) {
                        fFaPlayer.getPlayer().sendTitle("§7Vous êtes §cMort !", "§7Respawn dans §c" + timer + "...", 5, 10, 5);
                        fFaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c/autorespawn §7pour désactiver !"));
                    } else if (timer == 0 && fFaPlayer.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                        cancel();
                        fFaPlayer.spawnInArena();
                    } else {
                        cancel();
                        return;
                    }

                    timer--;
                }
            }
                    .runTaskTimer(this.main, 0, 20L);
        } else {
            fFaPlayer.sendToSpawn();
        }
    }
}
