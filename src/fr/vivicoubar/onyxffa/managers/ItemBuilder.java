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

    public ItemStack buildItem(FileConfiguration configuration, String configPath) {
        ItemStack itemStack;
        if (!configuration.getBoolean(configPath + ".RankColor", false)) {

            ItemMeta itemMeta;
            //DATA
            try {
                itemStack = new ItemStack(Material.getMaterial(configuration.getString(configPath + ".Material")), configuration.getInt(configPath + ".Quantity"), (byte) configuration.getInt(configPath + ".Data"));
            } catch (Exception e) {
                if (main.getKitsConfiguration().get(configPath + ".Data") != null) {
                    e.printStackTrace();
                }
            }
            //QUANTITY
            try {
                itemStack = new ItemStack(Material.getMaterial(configuration.getString(configPath + ".Material")), configuration.getInt(configPath + ".Quantity"));
            } catch (Exception e) {
                if (configuration.getStringList(configPath + ".Quantity") != null) {
                    e.printStackTrace();
                }
                itemStack = new ItemStack(Material.getMaterial(configuration.getString(configPath + ".Material")), 1);
            }
            itemMeta = itemStack.getItemMeta();
            //ENCHANTS
            try {
                for (String enchantmentPath : configuration.getConfigurationSection(configPath + ".Enchantments").getKeys(false)) {
                    itemMeta.addEnchant(Enchantment.getByName(configuration.getString(configPath + ".Enchantments." + enchantmentPath + ".Type")), configuration.getInt(configPath + ".Enchantments." + enchantmentPath + ".Amplifier"), true);
                }
            } catch (Exception ignored) {
            }
            //ENCHANTED
            try {
                if (configuration.getBoolean(configPath + ".Enchanted")) {
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            } catch (Exception e) {
                if (configuration.get(configPath + ".Enchanted") != null) {
                    e.printStackTrace();
                }
            }
            //Lore
            try {
                itemMeta.setLore(configuration.getStringList(configPath + ".Lore"));
            } catch (Exception e) {
                if (configuration.getStringList(configPath + ".Lore") != null) {
                    e.printStackTrace();
                }
            }
            //Name
            try {
                itemMeta.setDisplayName(configuration.getString(configPath + ".Name"));
            } catch (Exception e) {
                if (configuration.getStringList(configPath + ".Name") != null) {
                    e.printStackTrace();
                }
            }
            //UNBREAKABLE
            try {
                if (configuration.getBoolean(configPath + ".isUnbreakable")) {
                    itemMeta.setUnbreakable(true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                }
            } catch (Exception e) {
                if (configuration.get(configPath + ".isUnbreakable") != null) {
                    e.printStackTrace();
                }
            }

            itemStack.setItemMeta(itemMeta);


        } else {
            LeatherArmorMeta itemMeta;
            //DATA
            try {
                itemStack = new ItemStack(Material.getMaterial(configuration.getString(configPath + ".Material")), configuration.getInt(configPath + ".Quantity"), (byte) configuration.getInt(configPath + ".Data"));
            } catch (Exception e) {
                if (main.getKitsConfiguration().get(configPath + ".Data") != null) {
                    e.printStackTrace();
                }
            }
            //QUANTITY
            try {
                itemStack = new ItemStack(Material.getMaterial(configuration.getString(configPath + ".Material")), configuration.getInt(configPath + ".Quantity"));
            } catch (Exception e) {
                if (configuration.getStringList(configPath + ".Quantity") != null) {
                    e.printStackTrace();
                }
                itemStack = new ItemStack(Material.getMaterial(configuration.getString(configPath + ".Material")), 1);
            }
            itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            //ENCHANTS
            try {
                for (String enchantmentPath : configuration.getConfigurationSection(configPath + ".Enchantments").getKeys(false)) {
                    itemMeta.addEnchant(Enchantment.getByName(configuration.getString(configPath + ".Enchantments." + enchantmentPath + ".Type")), configuration.getInt(configPath + ".Enchantments." + enchantmentPath + ".Amplifier"), true);
                }
            } catch (Exception ignored) {
            }
            //ENCHANTED
            try {
                if (configuration.getBoolean(configPath + ".Enchanted")) {
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            } catch (Exception e) {
                if (configuration.get(configPath + ".Enchanted") != null) {
                    e.printStackTrace();
                }
            }
            //Lore
            try {
                itemMeta.setLore(configuration.getStringList(configPath + ".Lore"));
            } catch (Exception e) {
                if (configuration.getStringList(configPath + ".Lore") != null) {
                    e.printStackTrace();
                }
            }
            //Name
            try {
                itemMeta.setDisplayName(configuration.getString(configPath + ".Name"));
            } catch (Exception e) {
                if (configuration.getStringList(configPath + ".Name") != null) {
                    e.printStackTrace();
                }
            }
            //UNBREAKABLE
            try {
                if (configuration.getBoolean(configPath + ".isUnbreakable")) {
                    itemMeta.setUnbreakable(true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                }
            } catch (Exception e) {
                if (configuration.get(configPath + ".isUnbreakable") != null) {
                    e.printStackTrace();
                }
            }

            //COLOR
            try {
                int red = configuration.getInt(configPath + ".Color.R");
                int green = configuration.getInt(configPath + ".Color.G");
                int blue = configuration.getInt(configPath + ".Color.B");
                itemMeta.setColor(Color.fromRGB(red, green, blue));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            } catch (Exception e) {
                if (configuration.get(configPath + ".Color") != null) {
                    e.printStackTrace();
                }
                itemStack.setItemMeta(itemMeta);
            }
        }
        return itemStack;
    }
}
