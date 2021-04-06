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
