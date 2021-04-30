package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitsManager {

    private static OnyxFFaMain main;
    private FileConfiguration kitsConfiguration;

    public KitsManager(OnyxFFaMain main) {
        this.main = main;
        this.kitsConfiguration = main.getKitsConfiguration();
    }

    public void giveKit(FFaPlayer fFaPlayer, Boolean edition) {
        List <String> kitList = fFaPlayer.getKitList();
        for (String string : kitList) {
            String kitIndex = string.split(":")[1];
            Integer inventorySlot = Integer.parseInt(string.split(":")[0]);
            for (String path : kitsConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + fFaPlayer.getStats().getRank().getName() + ".Items").getKeys(false)) {
                if (path.equalsIgnoreCase(kitIndex)) {
                    String objectPath = "NewOnyxFFa.Ffa." + fFaPlayer.getStats().getRank().getName() + ".Items." + path;
                    ItemStack itemStack = main.getItemBuilder().buildItem(kitsConfiguration, objectPath);
                    if (edition) {
                        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
                        NBTTagCompound itemStackCompound = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
                        itemStackCompound.set("kitIndex", new NBTTagInt(Integer.parseInt(kitIndex)));
                        nmsItemStack.setTag(itemStackCompound);
                        itemStack = CraftItemStack.asBukkitCopy(nmsItemStack);
                        itemStack.setAmount(1);
                    }
                    fFaPlayer.getPlayer().getInventory().setItem(inventorySlot, itemStack);
                }
            }
        }
    }

    public void saveKit(FFaPlayer fFaPlayer) {
        List<String> list = new ArrayList<>();
        for (int inventorySlot = 0; inventorySlot < fFaPlayer.getPlayer().getInventory().getSize(); inventorySlot++) {
            if (fFaPlayer.getPlayer().getInventory().getItem(inventorySlot) != null) {
                net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(fFaPlayer.getPlayer().getInventory().getItem(inventorySlot));
                NBTTagCompound itemStackCompound = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
                int kitIndex = itemStackCompound.getInt("kitIndex");
                list.add(inventorySlot + ":" + kitIndex);
            }
        }
        fFaPlayer.setKitList(list);
        fFaPlayer.getPlayer().getInventory().clear();
    }

}
