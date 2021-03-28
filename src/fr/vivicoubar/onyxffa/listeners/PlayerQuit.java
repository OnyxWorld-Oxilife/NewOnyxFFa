package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.commands.CommandSpawn;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    OnyxFFaMain main = OnyxFFaMain.getInstance();

    // public PlayerQuit() {}

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        OnyxFFaMain.getInstance().killStreak.removePlayer(player);
        quitEvent.setQuitMessage(null);

        FileConfiguration messagesConfiguration = main.getMessagesConfiguration();
        if (player.hasPermission("OnyxFfa.mod.hideJoinMessage")) {
            if (!player.hasPlayedBefore()) {
                main.messages.actionBarMessage(player, messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceAll("%player%", player.getDisplayName()));
                Bukkit.broadcastMessage(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceAll("%player%", player.getDisplayName()));
            }
            main.messages.broadcastActionBarMessage(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.Player").replaceAll("%player%", player.getDisplayName()));

        } else {
            main.messages.actionBarMessage(player, messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.HiddenStaff").replaceAll("%player%", player.getDisplayName()));
        }
        CommandSpawn spawnInstance = new CommandSpawn(main);
        spawnInstance.sendPlayerToSpawn(player);
    }

}
