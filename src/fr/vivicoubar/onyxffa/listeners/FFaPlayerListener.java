package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class FFaPlayerListener implements Listener {

    private final OnyxFFaMain main;

    public FFaPlayerListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    /*@EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        FileConfiguration messagesConfiguration = main.getMessagesConfiguration();
        FFaPlayer ffaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, joinEvent.getPlayer());
        joinEvent.setJoinMessage(null);
        if (!ffaPlayer.getPlayer().hasPermission("OnyxFfa.mod.hideJoinMessage")) {
            if (!statsConfiguration.contains("NewOnyxFFa." + ffaPlayer.getPlayer().getUniqueId())) {
                ffaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceFirst("%player%", ffaPlayer.getPlayer().getDisplayName())));
                Bukkit.broadcastMessage(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceFirst("%player%", ffaPlayer.getPlayer().getDisplayName()));
            }
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.Player").replaceFirst("%player%", ffaPlayer.getPlayer().getDisplayName())));
            }
        } else {
            ffaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.HiddenStaff").replaceFirst("%player%", ffaPlayer.getPlayer().getDisplayName())));
        }
        CommandSpawn spawnInstance = new CommandSpawn(main);
        spawnInstance.sendPlayerToSpawn(joinEvent.getPlayer());
    }*/

    @EventHandler
    public void onMoveAfterSpawn(PlayerMoveEvent playerMoveAfterSpawnEvent) {
        if (playerMoveAfterSpawnEvent.getFrom().getBlockX() != playerMoveAfterSpawnEvent.getTo().getBlockX() || playerMoveAfterSpawnEvent.getFrom().getBlockZ() != playerMoveAfterSpawnEvent.getTo().getBlockZ()) {
            if (main.getSpawnsInWait().contains(playerMoveAfterSpawnEvent.getPlayer().getUniqueId().toString())) {
                main.getSpawnsInWait().remove(playerMoveAfterSpawnEvent.getPlayer().getUniqueId().toString());
                playerMoveAfterSpawnEvent.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.SpawnCommand.Error"));
            }
        }
    }

    @EventHandler
    public void onTryToMoveItem(InventoryClickEvent onTryToMoveItemEvent) {
        if (!onTryToMoveItemEvent.getWhoClicked().hasPermission("NewOnyxFFa.inventory.bypass") && onTryToMoveItemEvent.getWhoClicked().getGameMode() != GameMode.SURVIVAL) {
            onTryToMoveItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent dropItemEvent) {
        if (!dropItemEvent.getPlayer().hasPermission("NewOnyxFFa.drop.bypass")) {
            dropItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onLoseFood(FoodLevelChangeEvent onLoseFoodEvent) {
        onLoseFoodEvent.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent onWeatherChangeEvent) {
        onWeatherChangeEvent.setCancelled(true);
    }
}
