package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.managers.AutoRespawnManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.UUID;

// Player Object for the whole plugin
public class FFaPlayer {
    private final Player player;
    private final Inventory inventory;
    private Stats stats;
    private FFaPlayerStates state = FFaPlayerStates.WAITING;
    private boolean autorespawnBoolean = true;
    private final OnyxFFaMain main;
    private String lasthitter = "";
    public long timeWhenLastHitted = 0;
    private final AutoRespawnManager autoRespawnManager;
    private final UUID uniqueID;
    private int killStreak = 0;
    private Boolean fishing = false;
    private FishHook fishHook;
    private Boolean frozen = false;

    public FFaPlayer(OnyxFFaMain onyxFFaMain, Player player) {
        this.main = onyxFFaMain;
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        if (!statsConfiguration.contains("NewOnyxFFa." + player.getUniqueId())) {
            try {
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Kills", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Deaths", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Points", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".HighestScore", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".HighestKillStreak", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Pseudo", player.getName());
                statsConfiguration.save(main.getStatsFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.uniqueID = player.getUniqueId();
        this.autoRespawnManager = new AutoRespawnManager(this.main);
        this.player = player;
        this.inventory = player.getInventory();
        this.stats = new Stats(this, main);
        this.main.getFFaPlayerManager().getfFaPlayerList().add(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public Inventory getInventory() {
        return this.player.getInventory();
    }

    public Stats getStats() {
        return this.stats;
    }

    public void updateStats() {
        this.stats = new Stats(this, main);
    }

    public void setState(FFaPlayerStates state) {
        this.state = state;
    }
    public FFaPlayerStates getState() {
        return this.state;
    }
    public boolean isInArena(){
        return state == FFaPlayerStates.PLAYING || state == FFaPlayerStates.INVINCIBLE;
    }
    public boolean isInArenaOrDuel() {
        return isInArena() || state == FFaPlayerStates.DUEL;
    }
    public boolean isPlaying(){return state == FFaPlayerStates.PLAYING;}
    public void setHealth(double health) {
        this.player.setHealth(health);
    }

    public boolean isAutorespawnBoolean() {
        return this.autorespawnBoolean;
    }

    public String getLasthitter() {
        return this.lasthitter;
    }

    public long getTimeWhenLastHitted() {
        return this.timeWhenLastHitted;
    }
    public void setTimeWhenLastHitted(long time){
        this.timeWhenLastHitted = time;
    }
    public void setLasthitter(String lasthitter) {
        this.lasthitter = lasthitter;
    }

    public AutoRespawnManager getAutoRespawnManager() {
        return this.autoRespawnManager;
    }

    public void setLocation(Location location) {
        this.player.teleport(location);
    }

    public void setAutorespawnBoolean() {
        this.autorespawnBoolean = !autorespawnBoolean;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public void incrementKillStreak() {
        this.killStreak++;
    }

    public void resetKillStreak() {
        this.killStreak = 0;
    }

    public Boolean isFishing() {
        return fishing;
    }

    public void setFishing(Boolean value) {
        this.fishing = value;
    }

    public void setFishHook(FishHook hook) {
        this.fishHook = hook;
    }

    public FishHook getFishHook() {
        return fishHook;
    }

    public void resetMaxHealth() {
        AttributeInstance attribute = this.player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(20);
    }

    public void clearEffects() {
        for (PotionEffect effect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(effect.getType());
        }
    }

    // Encore un peu dégueu, à mettre dans l'ItemBuilder
    public void sendToSpawn() {
        FileConfiguration configConfiguration = main.getConfigConfiguration();
        this.player.setGameMode(GameMode.ADVENTURE);
        this.setState(FFaPlayerStates.WAITING);
        resetMaxHealth();
        this.player.setHealth(20);
        this.player.getInventory().clear();
        this.player.teleport(main.getLocationBuilder().getLocation("NewOnyxFFa.Spawns.Lobby", main.getSpawnsConfiguration()));
        ItemStack joinItem = main.getItemBuilder().buildItem(configConfiguration, "NewOnyxFFa.Config.Menu.Item");
        this.player.getInventory().setItem(4, joinItem);
        this.player.getInventory().setHeldItemSlot(4);
        clearEffects();
    }

    public void spawnInArena() {
        main.getSpawnManager().respawnPlayer(this.player);
    }

    public void setFrozen(Boolean condition) {
        this.frozen = condition;
    }

    public Boolean isFrozen() {
        return this.frozen;
    }

}
