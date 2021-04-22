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
import java.util.Random;

public class FFAAnimatedBlocksManager {

    private int id;
    public Map<Location, Long> animatedBlocks;
    public Map<Location, Integer> uniqueLocationId;
    public OnyxFFaMain main;

    public FFAAnimatedBlocksManager() {
        this.id = 0;
        this.main = OnyxFFaMain.getInstance();
        this.animatedBlocks = new HashMap<Location, Long>();
        this.uniqueLocationId = new HashMap<Location, Integer>();

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<Location, Long>> iterator = animatedBlocks.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<Location, Long> entry = iterator.next();
                    final int time = (int) ((System.currentTimeMillis() - entry.getValue())/1000);
                    if (time >= 20 || entry.getKey().getBlock().getType() == Material.AIR) {
                        sendBreakPacket(entry.getKey(), -1, entry.getKey().getBlock());
                        entry.getKey().getBlock().setType(Material.AIR);
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

    public void sendBreakPacket(final Location location, int breakState, final Block block) {

        final int dimension = ((CraftWorld) block.getWorld()).getHandle().dimension;
        final BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // id = id < 1000000 ? id + 1 : 0;

        if (this.uniqueLocationId.containsKey(location)) {
            id = this.uniqueLocationId.get(location);
        } else {
            id = id < 1000000 ? id + 1 : 0;
            this.uniqueLocationId.put(location, id);
        }
        final PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, blockPosition, breakState);
        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(null, block.getX(), block.getY(), block.getZ(), 120.0, dimension, packet);
    }
}
