package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FFAAnimatedBlocksManager {

    private int id;
    public Map<Location, Long> animatedBlocks;
    public OnyxFFaMain main;

    public FFAAnimatedBlocksManager() {
        Bukkit.broadcastMessage("Initialized");
        this.id = 0;
        this.main = OnyxFFaMain.getInstance();
        this.animatedBlocks = new HashMap<Location, Long>();

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<Location, Long>> iterator = animatedBlocks.entrySet().iterator();

                while (iterator.hasNext()) {
                    Bukkit.broadcastMessage("hasNext");
                    Map.Entry<Location, Long> entry = iterator.next();
                    final int time = (int) ((System.currentTimeMillis() - entry.getValue())/1000);
                    Bukkit.broadcastMessage(String.valueOf(time));
                    if (time >= 20 || entry.getKey().getBlock().getType() == Material.AIR) {
                        entry.getKey().getBlock().setType(Material.AIR);
                        sendBreakPacket(entry.getKey(), -1, entry.getKey().getBlock());
                        iterator.remove();
                    } else if (time >= 10) {
                        sendBreakPacket(entry.getKey(), time - 10 , entry.getKey().getBlock());
                    }
                }
            }
        }.runTaskTimer(main, 0, 10);
    }

    public void addBlock(final Location location) {
        animatedBlocks.put(location, System.currentTimeMillis());
    }

    public void removeBlock(final Location location) {
        animatedBlocks.remove(location);
    }

    void sendBreakPacket(final Location location, int breakState, final Block block) {

        final int dimension = ((CraftWorld) block.getWorld()).getHandle().dimension;
        final BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        id = id < 1000000 ? id + 1  : 0;
        final PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, blockPosition, breakState);
        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(null, block.getX(), block.getY(), block.getZ(), 120.0, dimension, packet);
    }
}
