package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class NMS {

    public void sendBreakPacket(final Location location, final int data, final Block block) {
        final int dimension = ((CraftWorld)block.getWorld()).getHandle().dimension;
        final BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        int id = 0;
        if (this.uniqueLocationId.containsKey(location)) {
            id = this.uniqueLocationId.get(location);
        }
        else {
            id = new Random().nextInt(Integer.MAX_VALUE);
            this.uniqueLocationId.put(location, id);
        }
        final PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, blockPosition, data);
        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby((EntityHuman)null, (double)block.getX(), (double)block.getY(), (double)block.getZ(), 120.0, dimension, (Packet)packet);
    }
    public static NMS instance;
    public Map<Location, Integer> uniqueLocationId;
    public Map<Long, Block> placedBlocks;
    public Map<Block, Material> placedBlockTypes;
    public Map<Long, Block> brokenBlocks;
    public Map<Block, Material> brokenBlockTypes;
    public Map<Location, Block> placedTotalBlocks;

    static {
        NMS.instance = new NMS();
    }

    public NMS() {
        this.uniqueLocationId = new HashMap<Location, Integer>();
        this.placedBlocks = new HashMap<Long, Block>();
        this.placedBlockTypes = new HashMap<Block, Material>();
        this.brokenBlocks = new HashMap<Long, Block>();
        this.brokenBlockTypes = new HashMap<Block, Material>();
        this.placedTotalBlocks = new HashMap<Location, Block>();
        Bukkit.getScheduler().runTaskTimer((Plugin) OnyxFFaMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<Long, Block>> iterator = NMS.this.placedBlocks.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<Long, Block> entry = iterator.next();
                    if (!NMS.this.placedBlockTypes.containsKey(entry.getValue())) {
                        NMS.this.sendBreakPacket(entry.getValue().getLocation(), 0, entry.getValue());
                        iterator.remove();
                    }
                    else {
                        final Block newBlock = entry.getValue().getWorld().getBlockAt(entry.getValue().getLocation());
                        if (!newBlock.getType().equals((Object)NMS.this.placedBlockTypes.get(entry.getValue()))) {
                            NMS.this.sendBreakPacket(entry.getValue().getLocation(), 0, entry.getValue());
                            iterator.remove();
                            NMS.this.placedBlockTypes.remove(entry.getValue());
                        }
                        else if (entry.getKey() < System.currentTimeMillis()) {
                            entry.getValue().setType(Material.AIR);
                            NMS.this.sendBreakPacket(entry.getValue().getLocation(), -1, entry.getValue());
                            iterator.remove();
                            NMS.this.placedBlockTypes.remove(entry.getValue());
                        }
                        else {
                            final double id = (10 - (int)(entry.getKey() - System.currentTimeMillis()) / 1000) * 0.8;
                            NMS.this.sendBreakPacket(entry.getValue().getLocation(), (int)id, entry.getValue());
                        }
                    }
                }
                iterator = NMS.this.brokenBlocks.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<Long, Block> entry = iterator.next();
                    if (!NMS.this.brokenBlockTypes.containsKey(entry.getValue())) {
                        iterator.remove();
                    }
                    else {
                        if (entry.getKey() >= System.currentTimeMillis()) {
                            continue;
                        }
                        NMS.this.sendBreakPacket(entry.getValue().getLocation(), -1, entry.getValue());
                        entry.getValue().getWorld().getBlockAt(entry.getValue().getLocation()).setType((Material)NMS.this.brokenBlockTypes.get(entry.getValue()));
                        iterator.remove();
                        NMS.this.brokenBlockTypes.remove(entry.getValue());
                    }
                }
            }
        }, 20L, 20L);
    }
}
