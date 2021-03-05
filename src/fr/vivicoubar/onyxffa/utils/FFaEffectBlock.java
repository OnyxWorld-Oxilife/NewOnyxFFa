package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FFaEffectBlock{
    private Location block;
    private OnyxFFaMain main;

    public FFaEffectBlock(OnyxFFaMain onyxFFaMain, Location blockLocation) {
        this.main = onyxFFaMain;
        this.block = blockLocation;

        block.getBlock().getLocation();
        final Firework firework = (Firework) block.getWorld().spawn(block.getBlock().getLocation(), (Class<Firework>)Firework.class);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        final FireworkEffect.Builder builder2 = FireworkEffect.builder();
        fireworkMeta.addEffect(builder2.flicker(true).withColor(Color.RED).build());
        fireworkMeta.addEffect(builder2.trail(true).build());
        fireworkMeta.addEffect(builder2.withFade(Color.BLUE).build());
        fireworkMeta.addEffect(builder2.with(FireworkEffect.Type.BURST).build());
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
        new BukkitRunnable() {
            int timer = main.getBlockFileConfiguration().getInt("NewOnyxFFa.Config.EffectBlock.Cooldown");
            @Override
            public void run() {

                if (timer == (main.getBlockFileConfiguration().getInt("NewOnyxFFa.Config.EffectBlock.Cooldown"))){
                    block.getBlock().setType(Material.BEDROCK);
                }
                if(timer == 0){
                    int random = (int)(Math.random() * ((main.getBlockEffectList().size() - 1) + 1));
                    block.getBlock().setType(Material.getMaterial(main.getBlockEffectList().get(random)));
                    cancel();
                }
                timer --;
            }

     }.runTaskTimer(this.main,0,20);
}

    public Location getBlock() {
        return block;
    }
}

