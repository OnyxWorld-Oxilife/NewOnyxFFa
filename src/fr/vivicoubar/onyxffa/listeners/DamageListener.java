package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DamageListener implements Listener {
    private final OnyxFFaMain main;

    public DamageListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    private final Map<String, String> lastHitters = new HashMap<>();

    @EventHandler
    public void onTakeDamage(EntityDamageByEntityEvent takeDamageEvent) {
        if(takeDamageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            if(takeDamageEvent.getEntity() instanceof Firework){
                takeDamageEvent.setCancelled(true);
            }
        }
        if (takeDamageEvent.getDamager() instanceof Player) {
            if (takeDamageEvent.getEntity() instanceof Player) {
                for (PotionEffect potionEffect : ((Player) takeDamageEvent.getEntity()).getActivePotionEffects()) {
                    if (potionEffect.getType().equals(PotionEffectType.GLOWING)) {
                        takeDamageEvent.setCancelled(true);
                    }
                }
                for (PotionEffect potionEffect2 : ((Player) takeDamageEvent.getDamager()).getActivePotionEffects()) {
                    if (potionEffect2.getType().equals(PotionEffectType.GLOWING)) {
                        takeDamageEvent.setCancelled(true);
                    }
                }
                if (((Player) takeDamageEvent.getEntity()).getGameMode() != GameMode.SURVIVAL) {
                    takeDamageEvent.setCancelled(true);
                }
                String damagerUuid = "" + takeDamageEvent.getDamager().getUniqueId();
                String victimUUid = "" + takeDamageEvent.getEntity().getUniqueId();
                lastHitters.put(victimUUid, damagerUuid);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (lastHitters.containsKey(victimUUid)) {
                            lastHitters.remove(victimUUid);
                        }
                    }
                }.runTaskLater(this.main, 20 * 10);
            }
        }
    }

    @EventHandler
    public void onBeKilled(EntityDamageByEntityEvent beKilledEvent) throws IOException {
        if (beKilledEvent.getDamager() instanceof Player) {
            if (beKilledEvent.getEntity() instanceof Player) {
                for (PotionEffect potionEffect : ((Player) beKilledEvent.getEntity()).getActivePotionEffects()) {
                    if (potionEffect.getType().equals(PotionEffectType.GLOWING)) {
                        beKilledEvent.setCancelled(true);
                    }
                }
                for (PotionEffect potionEffect2 : ((Player) beKilledEvent.getDamager()).getActivePotionEffects()) {
                    if (potionEffect2.getType().equals(PotionEffectType.GLOWING)) {
                        beKilledEvent.setCancelled(true);
                    }
                }
                if (((Player) beKilledEvent.getEntity()).getHealth() <= beKilledEvent.getFinalDamage()) {
                    beKilledEvent.setCancelled(true);
                    FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, (Player) beKilledEvent.getDamager());
                    FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, (Player) beKilledEvent.getEntity());
                    victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Kill.toVictim").replaceAll("%player%", damager.getPlayer().getName()));
                    damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Kill.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                    initKill(damager, victim);
                }
            }
        }
    }

    @EventHandler
    public void onQuitWhileFighting(PlayerQuitEvent quitWhileFightingEvent) throws IOException {
        FFaPlayer victim =  main.getfFaPlayerManager().getFFaPlayer(main, quitWhileFightingEvent.getPlayer());
        if (lastHitters.containsKey("" + quitWhileFightingEvent.getPlayer().getUniqueId())) {
            String damagerUUid = lastHitters.get("" + quitWhileFightingEvent.getPlayer().getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (("" + onlinePlayer.getUniqueId()).equalsIgnoreCase(damagerUUid)) {
                    lastHitters.remove("" + quitWhileFightingEvent.getPlayer().getUniqueId());
                    FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, onlinePlayer);
                    damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.QuitWhileFighting.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                    initKill(damager, victim);
                    break;
                }
            }
        }
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onDeathByFallDamage(EntityDamageEvent damageEvent) throws IOException {
        if (damageEvent.getEntity() instanceof Player) {
            if(main.getConfigConfiguration().getBoolean("NewOnyxFFa.Config.UseDeathByFallDamageRecognition")){
            if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (((Player) damageEvent.getEntity()).getGameMode() != GameMode.SURVIVAL) {
                    damageEvent.setCancelled(true);
                }
                if (((Player) damageEvent.getEntity()).getHealth() <= damageEvent.getFinalDamage()) {
                    damageEvent.setCancelled(true);
                    if (((Player) damageEvent.getEntity()).getGameMode() == GameMode.SURVIVAL) {
                        FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, (Player) damageEvent.getEntity());
                        if (lastHitters.containsKey("" + victim.getPlayer().getUniqueId())) {
                            String damagerUUid = lastHitters.get("" + victim.getPlayer().getUniqueId());
                            for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                if (damagerUUid.equalsIgnoreCase("" + onlineplayer.getUniqueId())) {
                                    lastHitters.remove("" + victim.getPlayer().getUniqueId());
                                    FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, onlineplayer);
                                    victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.KilledByFalling.toVictim").replaceAll("%player%", damager.getPlayer().getName()));
                                    damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.KilledByFalling.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                                    initKill(damager, victim);
                                    break;
                                }
                            }
                        } else {
                            victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.KilledByFalling.Suicide"));
                            initSuicide(victim);

                        }
                    }
                }
            }
        }
        }
    }

    @EventHandler
    public void onFallIntoTheVoid(PlayerMoveEvent fallIntoVoidEvent) throws IOException {
        if (fallIntoVoidEvent.getPlayer().getLocation().getBlockY() <= main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.MinY")) {
            if (lastHitters.containsKey("" + fallIntoVoidEvent.getPlayer().getUniqueId())) {
                String damagerUUid = lastHitters.get("" + fallIntoVoidEvent.getPlayer().getUniqueId());
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (("" + onlinePlayer.getUniqueId()).equals(damagerUUid)) {
                        lastHitters.remove("" + fallIntoVoidEvent.getPlayer().getUniqueId());
                        FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, fallIntoVoidEvent.getPlayer());
                        FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, onlinePlayer);
                        victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.FallenIntoTheVoid.toVictim").replaceAll("%player%", damager.getPlayer().getName()));
                        damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.FallenIntoTheVoid.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                        initKill(damager, victim);
                        break;
                    }
                }
            } else {
                FFaPlayer victim = new FFaPlayer(main, fallIntoVoidEvent.getPlayer());
                victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.FallenIntoTheVoid.Suicide"));
                initSuicide(victim);

            }
        }
    }

    public void initSuicide(FFaPlayer victim) throws IOException {
            victim.getPlayer().getVelocity().zero();
            victim.getPlayer().setHealth(20);
            victim.getPlayer().getInventory().clear();
            victim.getPlayer().setFallDistance(-5);
            for(PotionEffect potionEffect : victim.getPlayer().getActivePotionEffects()){
                victim.getPlayer().removePotionEffect(potionEffect.getType());
            }
            victim.getPlayer().setVelocity(victim.getPlayer().getVelocity().zero());
            victim.getAutoRespawnManager().askRespawn(victim);

            Rank oldVictimRank = victim.getStats().getRank();

            victim.getStats().iterateDeaths();

            double victimPoints = victim.getStats().getScore().get(2);

            victimPoints = victimPoints - (int) oldVictimRank.getRankNumber() * 20;
            if (victimPoints < 0) {
                victimPoints = 0;
            }
            victim.getStats().setPoints(victimPoints);
            victim.updateStats();
            Rank newVictimrank = victim.getStats().getRank();

            if (oldVictimRank != newVictimrank) {
                for (String command : newVictimrank.getCommandOnGoToRank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", victim.getPlayer().getName()));
                }
                for (String command : oldVictimRank.getCommandOnLeaveRank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", victim.getPlayer().getName()));
                }
            }
        //Commandes pour le Kill (Money)
        for (String command : main.getConfigConfiguration().getStringList("NewOnyxFFa.Config.onKillCommands.onFallIntoVoid")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%victim%", victim.getPlayer().getName()));
        }

        }

    public void initKill(FFaPlayer damager, FFaPlayer victim) throws IOException {
            victim.getPlayer().getVelocity().zero();
            double health = damager.getPlayer().getHealth() + main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.HealthBonusAfterKill");
            if (health > 20) {
                health = 20;
            }
            damager.getPlayer().setHealth(health);
            victim.getPlayer().setHealth(20);
            victim.getPlayer().getInventory().clear();
            victim.getPlayer().setFallDistance(-500);
            for(PotionEffect potionEffect : victim.getPlayer().getActivePotionEffects()){
                victim.getPlayer().removePotionEffect(potionEffect.getType());
            }
            victim.getPlayer().setVelocity(victim.getPlayer().getVelocity().zero());
            victim.getAutoRespawnManager().askRespawn(victim);

            Rank oldVictimRank = victim.getStats().getRank();
            Rank oldDamagerRank = damager.getStats().getRank();

            damager.getStats().iterateKills();
            victim.getStats().iterateDeaths();

            double victimPoints = victim.getStats().getScore().get(2);
            double damagerPoints = damager.getStats().getScore().get(2);

            victimPoints = victimPoints - (int) oldVictimRank.getRankNumber() * 20 / oldDamagerRank.getRankNumber();
            damagerPoints = damagerPoints + (int) oldVictimRank.getRankNumber() * 20 / oldDamagerRank.getRankNumber();
            if (victimPoints < 0) {
                victimPoints = 0;
            }
            damager.getStats().setPoints(damagerPoints);
            victim.getStats().setPoints(victimPoints);
            damager.updateStats();
            victim.updateStats();
            Rank newVictimrank = victim.getStats().getRank();
            Rank newDamagerank = damager.getStats().getRank();

            if (oldDamagerRank != newDamagerank) {
                for (String command : newDamagerank.getCommandOnGoToRank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", damager.getPlayer().getName()));
                }
                for (String command : oldDamagerRank.getCommandOnLeaveRank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", damager.getPlayer().getName()));
                }
            }
            if (oldVictimRank != newVictimrank) {
                for (String command : newVictimrank.getCommandOnGoToRank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", victim.getPlayer().getName()));
                }
                for (String command : oldVictimRank.getCommandOnLeaveRank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", victim.getPlayer().getName()));
                }
            }
            //Commandes pour le Kill (Money)
            for (String command : main.getConfigConfiguration().getStringList("NewOnyxFFa.Config.onKillCommands.onFight")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%victim%", victim.getPlayer().getName()).replaceAll("%killer%", damager.getPlayer().getName()));
            }
        }
    }