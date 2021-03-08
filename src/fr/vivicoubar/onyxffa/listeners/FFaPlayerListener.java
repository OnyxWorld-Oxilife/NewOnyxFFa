package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.managers.ItemBuilder;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class FFaPlayerListener implements Listener {

    private final OnyxFFaMain main;
    public FFaPlayerListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        FileConfiguration messagesConfiguration = main.getMessagesConfiguration();
        FFaPlayer ffaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, joinEvent.getPlayer());
        joinEvent.setJoinMessage(null);
        if(!ffaPlayer.getPlayer().hasPermission("OnyxFfa.mod.hideJoinMessage"))
        {
            if(!statsConfiguration.contains("NewOnyxFFa."+ ffaPlayer.getPlayer().getUniqueId())){
                ffaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceFirst("%player%",ffaPlayer.getPlayer().getDisplayName())));
                Bukkit.broadcastMessage(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.NewPlayer").replaceFirst("%player%",ffaPlayer.getPlayer().getDisplayName()));
            }
            for(Player player : Bukkit.getServer().getOnlinePlayers()){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.Player").replaceFirst("%player%",ffaPlayer.getPlayer().getDisplayName())));
            }
        }else{
            ffaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messagesConfiguration.getString("NewOnyxFFa.Messages.Welcome.HiddenStaff").replaceFirst("%player%",ffaPlayer.getPlayer().getDisplayName())));
        }
        Player player = ffaPlayer.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        AttributeInstance attribute = player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(20);
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.teleport(main.getLocationBuilder().getLocation("NewOnyxFFa.Spawns.Lobby"));

        ItemStack menuSelector = main.getItemBuilder().buildItem("NewOnyxFFa.Config.Menu.Item");
        player.getInventory().setItem(4,menuSelector);
        player.getInventory().setHeldItemSlot(4);
    }
    @EventHandler
    public void onMoveAfterSpawn(PlayerMoveEvent playerMoveAfterSpawnEvent){
        if(playerMoveAfterSpawnEvent.getFrom().getBlockX() != playerMoveAfterSpawnEvent.getTo().getBlockX() || playerMoveAfterSpawnEvent.getFrom().getBlockZ() != playerMoveAfterSpawnEvent.getTo().getBlockZ()){
            if(main.getSpawnsInWait().contains(playerMoveAfterSpawnEvent.getPlayer().getUniqueId().toString())){
                main.getSpawnsInWait().remove(playerMoveAfterSpawnEvent.getPlayer().getUniqueId().toString());
                playerMoveAfterSpawnEvent.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.SpawnCommand.Error"));
            }
        }
    }
    @EventHandler
    public void onTryToMoveItem(InventoryClickEvent onTryToMoveItemEvent){
        if(!onTryToMoveItemEvent.getWhoClicked().hasPermission("NewOnyxFFa.inventory.bypass") && onTryToMoveItemEvent.getWhoClicked().getGameMode() != GameMode.SURVIVAL){
            onTryToMoveItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent dropItemEvent){
        if(!dropItemEvent.getPlayer().hasPermission("NewOnyxFFa.drop.bypass")){
            dropItemEvent.setCancelled(true);
        }
    }
    @EventHandler
    public void onLoseFood(FoodLevelChangeEvent onLoseFoodEvent){
        onLoseFoodEvent.setCancelled(true);
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent onWeatherChangeEvent){
        onWeatherChangeEvent.setCancelled(true);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent){
        playerQuitEvent.setQuitMessage(null);
    }
}
