package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

//OPTIONS : Enchantments, Color, isUnbreakble, Data, Lore, Name,
public class ItemBuilder {
    private OnyxFFaMain main;

    public ItemBuilder(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    public ItemStack buildItem(String configpath) {
        ItemStack itemStack;
        FileConfiguration kitConfig = main.getKitsConfiguration();
        if (kitConfig.get(configpath + ".Color") == null) {

            ItemMeta itemMeta;
            //DATA
            try {
                itemStack = new ItemStack(Material.getMaterial(kitConfig.getString(configpath + ".Material")), kitConfig.getInt(configpath + ".Quantity"), (byte) kitConfig.getInt(configpath + ".Data"));
            } catch (Exception e) {
                if (main.getKitsConfiguration().get(configpath + ".Data") != null) {
                    e.printStackTrace();
                }
            }
            //QUANTITY
            try {
                itemStack = new ItemStack(Material.getMaterial(kitConfig.getString(configpath + ".Material")), kitConfig.getInt(configpath + ".Quantity"));
            } catch (Exception e) {
                if (kitConfig.getStringList(configpath + ".Quantity") != null) {
                    e.printStackTrace();
                }
                itemStack = new ItemStack(Material.getMaterial(kitConfig.getString(configpath + ".Material")), 1);
            }
            itemMeta = itemStack.getItemMeta();
            //ENCHANTS
            try {
                for (String enchantmentPath : kitConfig.getConfigurationSection(configpath + ".Enchantments").getKeys(false)) {
                    itemMeta.addEnchant(Enchantment.getByName(kitConfig.getString(configpath + ".Enchantments." + enchantmentPath + ".Type")), kitConfig.getInt(configpath + ".Enchantments." + enchantmentPath + ".Amplifier"), true);
                }
            } catch (Exception ignored) {
            }
            //ENCHANTED
            try {
                if (kitConfig.getBoolean(configpath + ".Enchanted")) {
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            } catch (Exception e) {
                if (kitConfig.get(configpath + ".Enchanted") != null) {
                    e.printStackTrace();
                }
            }
            //Lore
            try {
                itemMeta.setLore(kitConfig.getStringList(configpath + ".Lore"));
            } catch (Exception e) {
                if (kitConfig.getStringList(configpath + ".Lore") != null) {
                    e.printStackTrace();
                }
            }
            //Name
            try {
                itemMeta.setDisplayName(kitConfig.getString(configpath + ".Name"));
            } catch (Exception e) {
                if (kitConfig.getStringList(configpath + ".Name") != null) {
                    e.printStackTrace();
                }
            }
            //UNBREAKABLE
            try {
                if (kitConfig.getBoolean(configpath + ".isUnbreakable")) {
                    itemMeta.setUnbreakable(true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                }
            } catch (Exception e) {
                if (kitConfig.get(configpath + ".isUnbreakable") != null) {
                    e.printStackTrace();
                }
            }

            itemStack.setItemMeta(itemMeta);


        } else {
            LeatherArmorMeta itemMeta;
            //DATA
            try {
                itemStack = new ItemStack(Material.getMaterial(kitConfig.getString(configpath + ".Material")), kitConfig.getInt(configpath + ".Quantity"), (byte) kitConfig.getInt(configpath + ".Data"));
            } catch (Exception e) {
                if (main.getKitsConfiguration().get(configpath + ".Data") != null) {
                    e.printStackTrace();
                }
            }
            //QUANTITY
            try {
                itemStack = new ItemStack(Material.getMaterial(kitConfig.getString(configpath + ".Material")), kitConfig.getInt(configpath + ".Quantity"));
            } catch (Exception e) {
                if (kitConfig.getStringList(configpath + ".Quantity") != null) {
                    e.printStackTrace();
                }
                itemStack = new ItemStack(Material.getMaterial(kitConfig.getString(configpath + ".Material")), 1);
            }
            itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            //ENCHANTS
            try {
                for (String enchantmentPath : kitConfig.getConfigurationSection(configpath + ".Enchantments").getKeys(false)) {
                    itemMeta.addEnchant(Enchantment.getByName(kitConfig.getString(configpath + ".Enchantments." + enchantmentPath + ".Type")), kitConfig.getInt(configpath + ".Enchantments." + enchantmentPath + ".Amplifier"), true);
                }
            } catch (Exception ignored) {
            }
            //ENCHANTED
            try {
                if (kitConfig.getBoolean(configpath + ".Enchanted")) {
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            } catch (Exception e) {
                if (kitConfig.get(configpath + ".Enchanted") != null) {
                    e.printStackTrace();
                }
            }
            //Lore
            try {
                itemMeta.setLore(kitConfig.getStringList(configpath + ".Lore"));
            } catch (Exception e) {
                if (kitConfig.getStringList(configpath + ".Lore") != null) {
                    e.printStackTrace();
                }
            }
            //Name
            try {
                itemMeta.setDisplayName(kitConfig.getString(configpath + ".Name"));
            } catch (Exception e) {
                if (kitConfig.getStringList(configpath + ".Name") != null) {
                    e.printStackTrace();
                }
            }
            //UNBREAKABLE
            try {
                if (kitConfig.getBoolean(configpath + ".isUnbreakable")) {
                    itemMeta.setUnbreakable(true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                }
            } catch (Exception e) {
                if (kitConfig.get(configpath + ".isUnbreakable") != null) {
                    e.printStackTrace();
                }
            }

            //COLOR
            try {
                int red = kitConfig.getInt(configpath + ".Color.R");
                int green = kitConfig.getInt(configpath + ".Color.G");
                int blue = kitConfig.getInt(configpath + ".Color.B");
                itemMeta.setColor(Color.fromRGB(red, green, blue));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            } catch (Exception e) {
                if (kitConfig.get(configpath + ".Color") != null) {
                    e.printStackTrace();
                }
                itemStack.setItemMeta(itemMeta);
            }
        }
        return itemStack;
    }
}
