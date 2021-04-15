package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFirework;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;


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
        if (takeDamageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            if (takeDamageEvent.getDamager() instanceof CraftFirework) {
                takeDamageEvent.setCancelled(true);
            }
        }
        if (takeDamageEvent.getDamager() instanceof Player || takeDamageEvent.getDamager() instanceof Egg) {
            if (takeDamageEvent.getEntity() instanceof Player) {
                if (takeDamageEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    if (takeDamageEvent.getDamager() instanceof Egg) {
                        Projectile projectile = (Projectile) takeDamageEvent.getDamager();
                        Player damager = (Player) projectile.getShooter();
                        String damagerUuid = "" + damager.getUniqueId();
                        FFaPlayer victim =  main.getfFaPlayerManager().getFFaPlayer(main, ((Player) takeDamageEvent.getEntity()).getPlayer());
                        victim.setLasthitter(damagerUuid);
                        victim.setTimeWhenLastHitted(System.currentTimeMillis());
                        takeDamageEvent.setCancelled(true);
                    } else {
                        String damagerUuid = "" + takeDamageEvent.getDamager().getUniqueId();
                        takeDamageEvent.setCancelled(true);
                        FFaPlayer victim =  main.getfFaPlayerManager().getFFaPlayer(main, ((Player) takeDamageEvent.getEntity()).getPlayer());
                        victim.setLasthitter(damagerUuid);
                        victim.setTimeWhenLastHitted(System.currentTimeMillis());
                    }
                } else {
                    if(!main.getfFaPlayerManager().getFFaPlayer(main , (Player) takeDamageEvent.getDamager()).isInArenaOrDuel() || !main.getfFaPlayerManager().getFFaPlayer(main, (Player) takeDamageEvent.getEntity()).isInArenaOrDuel()) {
                        takeDamageEvent.setCancelled(true);
                    }

                    String damagerUuid = "" + takeDamageEvent.getDamager().getUniqueId();
                    if (((Player) takeDamageEvent.getEntity()).getGameMode() == GameMode.SURVIVAL) {
                        FFaPlayer victim =  main.getfFaPlayerManager().getFFaPlayer(main, ((Player) takeDamageEvent.getEntity()).getPlayer());
                        victim.setLasthitter(damagerUuid);
                        victim.setTimeWhenLastHitted(System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        playerDeathEvent.setDeathMessage(null);
        playerDeathEvent.getDrops().clear();
        playerDeathEvent.getEntity().spigot().respawn();
        FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, playerDeathEvent.getEntity());
        try {
            initSuicide(victim);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBeKilled(EntityDamageByEntityEvent beKilledEvent) {
        if (beKilledEvent.getDamager() instanceof Player) {
            if (beKilledEvent.getEntity() instanceof Player) {
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
    public void onQuitWhileFighting(PlayerQuitEvent quitWhileFightingEvent) {
        FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, quitWhileFightingEvent.getPlayer());
        if (!victim.getLasthitter().equals("") && System.currentTimeMillis() - victim.getTimeWhenLastHitted() < 10000 && victim.getTimeWhenLastHitted() > 0 ) {
            String damagerUUid = victim.getLasthitter();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (("" + onlinePlayer.getUniqueId()).equalsIgnoreCase(damagerUUid)) {
                    victim.setTimeWhenLastHitted(0);
                    victim.setLasthitter("");
                    FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, onlinePlayer);
                    damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.QuitWhileFighting.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                    initKill(damager, victim);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeathByFallDamage(EntityDamageEvent damageEvent) {
        if (damageEvent.getEntity() instanceof Player) {
            if (main.getConfigConfiguration().getBoolean("NewOnyxFFa.Config.UseDeathByFallDamageRecognition")) {
                if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if (((Player) damageEvent.getEntity()).getGameMode() != GameMode.SURVIVAL) {
                        damageEvent.setCancelled(true);
                    }
                    if (((Player) damageEvent.getEntity()).getHealth() <= damageEvent.getFinalDamage()) {
                        damageEvent.setCancelled(true);
                        if (((Player) damageEvent.getEntity()).getGameMode() == GameMode.SURVIVAL) {
                            FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, (Player) damageEvent.getEntity());
                            if (!victim.getLasthitter().equals("") && System.currentTimeMillis() - victim.getTimeWhenLastHitted() > 10000 && victim.getTimeWhenLastHitted() > 0 ) {
                                String damagerUUid = victim.getLasthitter();
                                for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                    if (damagerUUid.equalsIgnoreCase("" + onlineplayer.getUniqueId())) {
                                        victim.setTimeWhenLastHitted(0);
                                        victim.setLasthitter("");
                                        FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, onlineplayer);
                                        victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.KilledByFalling.toVictim").replaceAll("%player%", damager.getPlayer().getName()));
                                        damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.KilledByFalling.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                                        initKill(damager, victim);
                                        break;
                                    }
                                }
                            } else {
                                victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.KilledByFalling.Suicide"));
                                try {
                                    initSuicide(victim);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFallIntoTheVoid(PlayerMoveEvent fallIntoVoidEvent) throws IOException {
        if (fallIntoVoidEvent.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (fallIntoVoidEvent.getPlayer().getLocation().getBlockY() <= main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.MinY")) {
            FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main, fallIntoVoidEvent.getPlayer());
            if (!victim.getLasthitter().equals("") && System.currentTimeMillis() - victim.getTimeWhenLastHitted() < 10000 && victim.getTimeWhenLastHitted() > 0 ) {
                String damagerUUid = victim.getLasthitter();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (("" + onlinePlayer.getUniqueId()).equals(damagerUUid)) {
                        victim.setTimeWhenLastHitted(0);
                        victim.setLasthitter("");
                        FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main, onlinePlayer);
                        victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.FallenIntoTheVoid.toVictim").replaceAll("%player%", damager.getPlayer().getName()));
                        damager.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.FallenIntoTheVoid.toKiller").replaceAll("%player%", victim.getPlayer().getName()));
                        initKill(damager, victim);
                        break;
                    }
                }
            } else {
                victim = main.getfFaPlayerManager().getFFaPlayer(main, fallIntoVoidEvent.getPlayer());
                victim.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.FallenIntoTheVoid.Suicide"));
                initSuicide(victim);

            }
        }
    }

    public void initSuicide(FFaPlayer victim) throws IOException {

        victim.resetKillStreak();

        if(main.wantedEvent.isTarget(victim)){
            main.wantedEvent.setRandomTarget();
        }

        victim.getPlayer().getVelocity().zero();
        victim.getPlayer().setHealth(20);
        // Added clear GENERIC_MAX_HEALTH
        AttributeInstance attribute = victim.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(20);
        victim.getPlayer().getInventory().clear();
        victim.getPlayer().setFallDistance(-5);
        victim.setTimeWhenLastHitted(0);
        victim.setLasthitter("");
        main.potionEffectManager.clearAllPotionEffect(victim.getPlayer());
        victim.getPlayer().setVelocity(victim.getPlayer().getVelocity().zero());
        victim.getAutoRespawnManager().askRespawn(victim);

        Rank oldVictimRank = victim.getStats().getRank();

        victim.getStats().iterateDeaths();

        double victimPoints = victim.getStats().getScore().get(2);

        // victimPoints = victimPoints - (int) oldVictimRank.getRankNumber() * 20;
        victimPoints -= 10;
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

    public void initKill(FFaPlayer damager, FFaPlayer victim) {

        if(main.wantedEvent.isTarget(victim)){
            main.wantedEvent.setTarget(damager);
        }
        // killStreak
        damager.incrementKillStreak();
        victim.resetKillStreak();

        if (damager.getStats().getHighestKillStreak() < damager.getKillStreak()) {
            damager.getStats().setHighestKillStreak(damager.getKillStreak());
        }

        if (damager.getKillStreak() % 5 == 0) {
            Bukkit.broadcastMessage("§3" + damager.getPlayer().getName() + "§e est dans une folie meurtrière ! Killstreak de §3" + damager.getKillStreak() + "§e !");
        }

        victim.getPlayer().getVelocity().zero();
        double health = damager.getPlayer().getHealth() + main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.HealthBonusAfterKill");
        double maxHealth = damager.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (health > maxHealth) {
            health = maxHealth;
        }
        damager.getPlayer().setHealth(health);
        victim.getPlayer().setHealth(20);
        // Added clear GENERIC_MAX_HEALTH
        AttributeInstance attribute = victim.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(20);
        victim.getPlayer().getInventory().clear();
        victim.getPlayer().setFallDistance(-500);
        main.potionEffectManager.clearAllPotionEffect(victim.getPlayer());
        victim.getPlayer().setVelocity(victim.getPlayer().getVelocity().zero());
        victim.getAutoRespawnManager().askRespawn(victim);

        Rank oldVictimRank = victim.getStats().getRank();
        Rank oldDamagerRank = damager.getStats().getRank();

        damager.getStats().iterateKills();
        victim.getStats().iterateDeaths();

        double victimPoints = victim.getStats().getScore().get(2);
        double damagerPoints = damager.getStats().getScore().get(2);

        // victimPoints = victimPoints - (int) oldVictimRank.getRankNumber() * 10 / oldDamagerRank.getRankNumber();
        victimPoints -= 10;
        // damagerPoints = damagerPoints + (int) oldVictimRank.getRankNumber() * 20 / oldDamagerRank.getRankNumber();
        damagerPoints += 20;
        if (victimPoints < 0) {
            victimPoints = 0;
        }
        try {
            damager.getStats().setPoints(damagerPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            victim.getStats().setPoints(victimPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (damager.isInArena()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%victim%", victim.getPlayer().getName()).replaceAll("%killer%", damager.getPlayer().getName()));
        }}
    }
}