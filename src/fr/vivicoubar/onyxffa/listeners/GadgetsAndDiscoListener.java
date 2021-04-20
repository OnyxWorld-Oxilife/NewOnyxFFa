package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Random;

public class GadgetsAndDiscoListener implements Listener {
    private OnyxFFaMain main;
    private long time;

    public GadgetsAndDiscoListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
        time = System.currentTimeMillis();
    }

    @EventHandler
    public void onFishEvent(PlayerFishEvent playerFishEvent) {
        Player player = playerFishEvent.getPlayer();
        FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, player);
        PlayerFishEvent.State state = playerFishEvent.getState();
        ItemStack air = new ItemStack(Material.AIR);

        // Permet d'empêcher l'utilisation "classique" de la canne
        if (state == PlayerFishEvent.State.FISHING) {
            fFaPlayer.setFishHook(playerFishEvent.getHook());
            fFaPlayer.setFishing(true);
            // main.fishingPlayers.addPlayer(player, playerFishEvent.getHook());
        } else {
            fFaPlayer.setFishing(false);
            fFaPlayer.setFishHook(null);
            // main.fishingPlayers.removePlayer(player);
        }

        if (state == PlayerFishEvent.State.CAUGHT_ENTITY && playerFishEvent.getCaught() instanceof Player && playerFishEvent.getCaught() != player) {
            Player caughtPlayer = (Player) playerFishEvent.getCaught();
            if (player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD) {
                player.getInventory().setItemInMainHand(air);
            } else {
                player.getInventory().setItemInOffHand(air);
            }
            caughtPlayer.sendTitle("§eCapturé !", "", 5, 10, 5);
            caughtPlayer.setVelocity(player.getLocation().toVector().subtract(caughtPlayer.getLocation().toVector()).setY(0).normalize().setY(0.1).multiply(1.25));
        }

    }

    @EventHandler
    public void onUseSwitchSnowball(EntityDamageByEntityEvent onUseSwitchSnowball) {
        if (onUseSwitchSnowball.getDamager() instanceof Egg) {
            Egg snowball = (Egg) onUseSwitchSnowball.getDamager();
            if (snowball.getShooter() instanceof Player) {
                Player shooter = (Player) snowball.getShooter();
                if (onUseSwitchSnowball.getEntity() instanceof Player) {
                    Player victim = (Player) onUseSwitchSnowball.getEntity();
                    Location victimloc = victim.getLocation();
                    victim.teleport(shooter);
                    shooter.teleport(victimloc);
                    shooter.sendTitle("§eSwap!", "", 5, 10, 5);
                    victim.sendTitle("§eSwap!", "", 5, 10, 5);
                    shooter.playSound(shooter.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 5);
                    victim.playSound(shooter.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 5);
                }
            }
        }
    }

    @EventHandler
    public void eggSpawn(PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

    @EventHandler
    public void onDisco(PlayerMoveEvent discoEvent) {
        if (System.currentTimeMillis() - time > 500) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("Offa.disco") && player.getGameMode() == GameMode.SURVIVAL) {
                    time = System.currentTimeMillis();
                    if (player.getInventory().getHelmet() != null) {
                        if (player.getInventory().getHelmet().getType() == Material.LEATHER_HELMET) {
                            ItemStack helmet = player.getInventory().getHelmet();
                            LeatherArmorMeta itemmeta = (LeatherArmorMeta) helmet.getItemMeta();
                            int red = new Random().nextInt(256);
                            int green = new Random().nextInt(256);
                            int blue = new Random().nextInt(256);
                            itemmeta.setColor(Color.fromRGB(red, green, blue));
                            helmet.setItemMeta(itemmeta);
                            player.getInventory().setHelmet(helmet);
                        }
                    }
                    if (player.getInventory().getLeggings() != null) {
                        if (player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
                            ItemStack leggings = player.getInventory().getLeggings();
                            LeatherArmorMeta itemmeta = (LeatherArmorMeta) leggings.getItemMeta();
                            int red = new Random().nextInt(256);
                            int green = new Random().nextInt(256);
                            int blue = new Random().nextInt(256);
                            itemmeta.setColor(Color.fromRGB(red, green, blue));
                            leggings.setItemMeta(itemmeta);
                            player.getInventory().setLeggings(leggings);
                        }
                    }
                    if (player.getInventory().getChestplate() != null) {
                        if (player.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) {
                            ItemStack chestplate = player.getInventory().getChestplate();
                            LeatherArmorMeta itemmeta = (LeatherArmorMeta) chestplate.getItemMeta();
                            int red = new Random().nextInt(256);
                            int green = new Random().nextInt(256);
                            int blue = new Random().nextInt(256);
                            itemmeta.setColor(Color.fromRGB(red, green, blue));
                            chestplate.setItemMeta(itemmeta);
                            player.getInventory().setChestplate(chestplate);
                        }
                    }
                    if (player.getInventory().getBoots() != null) {
                        if (player.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) {
                            ItemStack boots = player.getInventory().getBoots();
                            LeatherArmorMeta itemmeta = (LeatherArmorMeta) boots.getItemMeta();
                            int red = new Random().nextInt(256);
                            int green = new Random().nextInt(256);
                            int blue = new Random().nextInt(256);
                            itemmeta.setColor(Color.fromRGB(red, green, blue));
                            boots.setItemMeta(itemmeta);
                            player.getInventory().setBoots(boots);
                        }
                    }
                }
            }
        }
    }
}
