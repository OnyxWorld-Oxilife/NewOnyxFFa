package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoRespawnManager {
    OnyxFFaMain main;

    public AutoRespawnManager(OnyxFFaMain main) {
        this.main = main;
    }

    public void askRespawn(FFaPlayer fFaPlayer) {
        if (fFaPlayer.isAutorespawnBoolean()) {
            fFaPlayer.getPlayer().closeInventory();

            Location temp = new Location(fFaPlayer.getPlayer().getWorld(), 738, 48, 625);

            fFaPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            fFaPlayer.getPlayer().teleport(temp);

            new BukkitRunnable() {
                int timer = 3 ;
                @Override
                public void run() {

                    if(timer > 0 && fFaPlayer.getPlayer().getGameMode() == GameMode.SPECTATOR){
                        fFaPlayer.getPlayer().sendTitle("§7Vous êtes §cMort !", "§7Respawn dans §c"  + timer + "...", 5 , 10 ,5);
                        fFaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c/autorespawn §7pour désactiver !"));
                    } else if(timer == 0 && fFaPlayer.getPlayer().getGameMode() == GameMode.SPECTATOR){
                        cancel();
                        SpawnManager spawnManagerInstance = new SpawnManager(main);
                        spawnManagerInstance.respawnPlayer(fFaPlayer.getPlayer());
                    } else {
                        cancel();
                        return;
                    }

                timer--;
                }
            }
            .runTaskTimer(this.main, 0, 20L);
        } else {
            FileConfiguration configConfiguration = main.getConfigConfiguration();
            Player player = fFaPlayer.getPlayer();
            player.getInventory().setHeldItemSlot(4);
            player.setGameMode(GameMode.ADVENTURE);
            player.setFoodLevel(20);
            player.getActivePotionEffects().clear();
            //Téléportation au spawn

            player.teleport(main.getLocationBuilder().getLocation("NewOnyxFFa.Spawns.Lobby"));
            ItemStack menuSelector = new ItemStack(Material.getMaterial(configConfiguration.getString("NewOnyxFFa.Config.Menu.Item.Material")));
            ItemMeta menuMeta = menuSelector.getItemMeta();
            if (configConfiguration.getBoolean("NewOnyxFFa.Config.Menu.Item.Enchanted")) {
                menuMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            }
            menuMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
            menuMeta.setDisplayName(configConfiguration.getString("NewOnyxFFa.Config.Menu.Item.Name"));
            menuMeta.setLore(configConfiguration.getStringList("NewOnyxFFa.Config.Menu.Item.Lore"));
            menuSelector.setItemMeta(menuMeta);
            player.getInventory().setItem(4, menuSelector);
        }
    }
}
