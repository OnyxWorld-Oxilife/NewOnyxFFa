package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.commands.CommandSpawn;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    OnyxFFaMain main = OnyxFFaMain.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        OnyxFFaMain.getInstance().killStreak.addPlayer(player);
        joinEvent.setJoinMessage(null);

        FileConfiguration messagesConfiguration = main.getMessagesConfiguration();
        if (player.hasPermission("OnyxFfa.mod.hideJoinMessage")) {
            if (!player.hasPlayedBefore()) {
                main.messages.actionBarMessage(player, messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceAll("%player%", player.getName()));
                Bukkit.broadcastMessage(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceAll("%player%", player.getName()));
            }
            main.messages.broadcastActionBarMessage(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.Player").replaceAll("%player%", player.getName()));

        } else {
            main.messages.actionBarMessage(player, messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.HiddenStaff").replaceAll("%player%", player.getName()));
        }
        CommandSpawn spawnInstance = new CommandSpawn(main);
        spawnInstance.sendPlayerToSpawn(player);
    }

}
