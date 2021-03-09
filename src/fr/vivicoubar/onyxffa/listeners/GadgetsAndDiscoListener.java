package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSnowball;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
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
    public void onGrappin(PlayerFishEvent playerGrappinUse){
        if(playerGrappinUse.getCaught() instanceof Player && ((Player) playerGrappinUse.getCaught()).getPlayer() != playerGrappinUse.getPlayer()){
            playerGrappinUse.getPlayer().getInventory().setItem(playerGrappinUse.getPlayer().getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
            ((Player) playerGrappinUse.getCaught()).sendTitle("§eCapturé!", "", 5, 10, 5);
            playerGrappinUse.getCaught().setVelocity(playerGrappinUse.getPlayer().getLocation().toVector().subtract(playerGrappinUse.getCaught().getLocation().toVector()).normalize());
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
    public void eggSpawn(PlayerEggThrowEvent event){
        event.setHatching(false);
    }
    @EventHandler
    public void onDisco(PlayerMoveEvent discoEvent){
        if (System.currentTimeMillis() - time > 500) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("Offa.disco") && player.getGameMode() == GameMode.SURVIVAL) {
                    time = System.currentTimeMillis();
                    if(player.getInventory().getHelmet() != null){
                    if (player.getInventory().getHelmet().getType() == Material.LEATHER_HELMET) {
                        ItemStack helmet = player.getInventory().getHelmet();
                        LeatherArmorMeta itemmeta = (LeatherArmorMeta) helmet.getItemMeta();
                        int red = new Random().nextInt(256);
                        int green = new Random().nextInt(256);
                        int blue = new Random().nextInt(256);
                        itemmeta.setColor(Color.fromRGB(red, green, blue));
                        helmet.setItemMeta(itemmeta);
                        player.getInventory().setHelmet(helmet);
                    }}
                if(player.getInventory().getLeggings() != null){
                    if (player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
                        ItemStack leggings = player.getInventory().getLeggings();
                        LeatherArmorMeta itemmeta = (LeatherArmorMeta) leggings.getItemMeta();
                        int red = new Random().nextInt(256);
                        int green = new Random().nextInt(256);
                        int blue = new Random().nextInt(256);
                        itemmeta.setColor(Color.fromRGB(red, green, blue));
                        leggings.setItemMeta(itemmeta);
                        player.getInventory().setLeggings(leggings);
                    }}
                if(player.getInventory().getChestplate() != null){
                    if (player.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) {
                        ItemStack chestplate = player.getInventory().getChestplate();
                        LeatherArmorMeta itemmeta = (LeatherArmorMeta) chestplate.getItemMeta();
                        int red = new Random().nextInt(256);
                        int green = new Random().nextInt(256);
                        int blue = new Random().nextInt(256);
                        itemmeta.setColor(Color.fromRGB(red, green, blue));
                        chestplate.setItemMeta(itemmeta);
                        player.getInventory().setChestplate(chestplate);
                    }}
                if(player.getInventory().getBoots() != null){
                    if (player.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) {
                        ItemStack boots = player.getInventory().getBoots();
                        LeatherArmorMeta itemmeta = (LeatherArmorMeta) boots.getItemMeta();
                        int red = new Random().nextInt(256);
                        int green = new Random().nextInt(256);
                        int blue = new Random().nextInt(256);
                        itemmeta.setColor(Color.fromRGB(red, green, blue));
                        boots.setItemMeta(itemmeta);
                        player.getInventory().setBoots(boots);
                    }}
                }
            }
        }
    }
}
