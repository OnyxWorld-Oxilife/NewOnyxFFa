package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class FFaBlock{
    private Block block;
    private OnyxFFaMain main;

    public FFaBlock(OnyxFFaMain onyxFFaMain, Block block) {
        this.main = onyxFFaMain;
        this.block = block;
        new BukkitRunnable() {
            int timer = main.getBlockFileConfiguration().getInt("NewOnyxFFa.Config.Block.TimerUntilBreak");

            @Override
            public void run() {
                if(Bukkit.getWorld(block.getWorld().getName()).getBlockAt(block.getLocation()).getType() == Material.AIR){
                    cancel();

                }
                if(timer == 0) {
                    Bukkit.getWorld(block.getWorld().getName()).getBlockAt(block.getLocation()).setType(Material.AIR);
                    cancel();
                }
                if(timer == main.getBlockFileConfiguration().getDouble("NewOnyxFFa.Config.Block.ReplaceTime")) {
                    if(Bukkit.getWorld(block.getWorld().getName()).getBlockAt(block.getLocation()).getType() != Material.AIR){
                        Bukkit.getWorld(block.getWorld().getName()).getBlockAt(block.getLocation()).setType(Material.getMaterial(main.getBlockFileConfiguration().getString("NewOnyxFFa.Config.Block.BlockBeforeBreakState")));
                    }
                }
                timer--;
            }
            }.runTaskTimer(this.main,0 ,(long) (20L));
        }

    public Block getBlock() {
        return block;
    }
}
