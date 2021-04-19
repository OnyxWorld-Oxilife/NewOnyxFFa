package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class NMS {

    public static NMS instance = new NMS();
    public Map<Location, Integer> uniqueLocationId;
    public Map<Long, Block> placedBlocks;
    public Map<Block, Material> placedBlockTypes;


    public NMS() {
        this.uniqueLocationId = new HashMap<Location, Integer>();
        this.placedBlocks = new HashMap<Long, Block>();
        this.placedBlockTypes = new HashMap<Block, Material>();

        Bukkit.getScheduler().runTaskTimer(OnyxFFaMain.instance, new Runnable() {
            @Override
            public void run() {
                //Transformation de la HashMap en liste d'entrées de tableaux pour en faire un itérateur
                Iterator<Map.Entry<Long, Block>> iterator = NMS.this.placedBlocks.entrySet().iterator();
                //Tant que l'itérateur peut sélectionner une nouvelle entrée, le plugin continue
                while (iterator.hasNext()) {
                    //Sélection de l'entrée
                    final Map.Entry<Long, Block> entry = iterator.next();
                    //Si dans la liste des types de bloc il y a le bloc de la liste des blocs
                    if (!NMS.this.placedBlockTypes.containsKey(entry.getValue())) {
                        //Utilisation de la méthode sendPacket
                        NMS.this.sendBreakPacket(entry.getValue().getLocation(), 0, entry.getValue());
                        //Suppression de l'itérateur
                        iterator.remove();
                    }
                    //Sinon
                    else {
                        //Sélection du block à partir de sa position
                        final Block newBlock = entry.getValue().getWorld().getBlockAt(entry.getValue().getLocation());
                        //Si le type du bloc est le même que celui dans la iste des blocks
                        if (!newBlock.getType().equals(NMS.this.placedBlockTypes.get(entry.getValue()))) {
                            //Utilisation de la méthode sendPacket
                            NMS.this.sendBreakPacket(entry.getValue().getLocation(), -1, entry.getValue());
                            //Supression de l'itérateur et du bloc de la liste des types de blocks
                            iterator.remove();
                            NMS.this.placedBlockTypes.remove(entry.getValue());
                        }
                        //Si le temps du bloc (clé random) et plus petit que le temps machine > Le bloc devient de l'air.
                        else if (entry.getKey() < System.currentTimeMillis()) {
                            NMS.this.sendBreakPacket(entry.getValue().getLocation(), -1, entry.getValue());
                            entry.getValue().setType(Material.AIR);
                            iterator.remove();
                            NMS.this.placedBlockTypes.remove(entry.getValue());
                        } else {
                            //Sinon il récupère le nombre de secondes restantes et update son niveau de dégâts.
                            final double breakLevel_id = (10 - (int) (entry.getKey() - System.currentTimeMillis()) / 1000) * 0.8;
                            NMS.this.sendBreakPacket(entry.getValue().getLocation(), (int) breakLevel_id, entry.getValue());
                        }
                    }
                }
            }
        }, 0L, 10L);
    }

    public void sendBreakPacket(final Location location, int data, final Block block) {
        //Nether, END , OverWorld
        final int dimension = ((CraftWorld) block.getWorld()).getHandle().dimension;
        //Récupère la position du block
        final BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        //Récupère si il existe l'id du packet du bloc
        int id;
        if (this.uniqueLocationId.containsKey(location)) {
            id = this.uniqueLocationId.get(location);
        } else {
            id = new Random().nextInt(Integer.MAX_VALUE);
            this.uniqueLocationId.put(location, id);
        }
        this.uniqueLocationId.put(location, id);
        if (location.getBlock().getType() == Material.AIR) {
            data = -1;
        }
        final PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, blockPosition, data);
        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(null, block.getX(), block.getY(), block.getZ(), 120.0, dimension, packet);
    }
}

