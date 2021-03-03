package fr.vivicoubar.onyxffa;

import fr.vivicoubar.onyxffa.commands.*;
import fr.vivicoubar.onyxffa.listeners.BlockListener;
import fr.vivicoubar.onyxffa.listeners.DamageListener;
import fr.vivicoubar.onyxffa.listeners.FFaPlayerListener;
import fr.vivicoubar.onyxffa.listeners.ItemListener;
import fr.vivicoubar.onyxffa.utils.RanksManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OnyxFFaMain extends JavaPlugin{

private File configFile;
private FileConfiguration configConfiguration;
private File statsFile;
private FileConfiguration statsConfiguration;
private File ranksFile;
private FileConfiguration ranksConfiguration;
private File spawnsFile;
private FileConfiguration spawnsConfiguration;
private File messagesFile;
private FileConfiguration messagesConfiguration;
private File kitsFile;
private FileConfiguration kitsConfiguration;
private File blockFile;
private FileConfiguration blockFileConfiguration;
private RanksManager ranksManager;
private final List<String> commandsList = new ArrayList<>();
private final List<String> lore = new ArrayList<>();
private final List<String> SpawnInWait = new ArrayList<>();
private final List<Location> spawnsList = new ArrayList<>();
private final List<String> blockEffectList= new ArrayList<>();
private final List<String> jumpadsBlocks = new ArrayList<>();

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlayerPAPIExpansion(this).register();
        }

        System.out.println(" ");
        System.out.println("************************");
        System.out.println("[NewOnyxFFA] > DEMARRAGE");
        System.out.println("************************");
        System.out.println(" ");

        PluginManager pluginManager = Bukkit.getPluginManager();
        getCommand("offa").setExecutor(new CommandOffa(this));
        getCommand("stats").setExecutor(new CommandStats(this));
        getCommand("spawn").setExecutor(new CommandSpawn(this));
        getCommand("resetStats").setExecutor(new CommandResetStats(this));
        getCommand("points").setExecutor(new CommandPoints(this));
        pluginManager.registerEvents(new FFaPlayerListener(this), this);
        pluginManager.registerEvents(new BlockListener(this), this);
        pluginManager.registerEvents(new ItemListener(this), this);
        pluginManager.registerEvents(new DamageListener(this), this);
        pluginManager.registerEvents(new DamageListener(this), this);

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            configFile = new File(this.getDataFolder(), "config.yml");
            messagesFile = new File(this.getDataFolder(), "messages.yml");
            kitsFile = new File(this.getDataFolder(), "kits.yml");
            statsFile = new File(this.getDataFolder(), "stats.yml");
            ranksFile = new File(this.getDataFolder(), "ranks.yml");
            spawnsFile = new File(this.getDataFolder(), "spawns.yml");
            blockFile = new File(this.getDataFolder(), "blocks.yml");

            if (!configFile.exists()) {
                configFile.createNewFile();
                configConfiguration = YamlConfiguration.loadConfiguration(configFile);
                lore.clear();
                lore.add("§aCliquer Pour");
                lore.add("§aRejoindre le FFa");
                configConfiguration.set("NewOnyxFFa.Config.minY", 50);
                configConfiguration.set("NewOnyxFFa.Config.Menu.Item.Material", "GOLD_AXE");
                configConfiguration.set("NewOnyxFFa.Config.Menu.Item.Name", "RushFFa");
                configConfiguration.set("NewOnyxFFa.Config.Menu.Item.Enchanted", true);
                configConfiguration.set("NewOnyxFFa.Config.Menu.Item.Lore", lore.toArray());
                configConfiguration.set("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation", 10);
                lore.clear();
                lore.add("msg %killer% test");
                lore.add("msg %victim% test");
                configConfiguration.set("NewOnyxFFa.Config.onKillCommands.onFight", lore.toArray());
                lore.clear();
                lore.add("msg %victim% test");
                lore.add("msg %victim% test");
                configConfiguration.set("NewOnyxFFa.Config.onKillCommands.onFallIntoVoid", lore.toArray());
                configConfiguration.set("NewOnyxFFa.Config.HealthBonusAfterKill", 5);
                lore.clear();
                configConfiguration.save(configFile);
            }

            if (!blockFile.exists()) {
                blockFile.createNewFile();
                blockFileConfiguration = YamlConfiguration.loadConfiguration(blockFile);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.TimerUntilBreak", 5);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockBeforeBreakState", "BRICK");
                lore.clear();
                lore.add("BRICK");
                lore.add("SANDSTONE");
                lore.add("GRASS");
                lore.add("GLOWSTONE");
                lore.add("WOOL");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.AllowedBlocktoBreak", lore.toArray());
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockPlacedByPlayers", lore.toArray());
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Material", "LAPIS_BLOCK");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.EffectType", "Potion");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Effect.1.PotionEffect", "HEAL");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Effect.1.Amplifier", 1);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Effect.1.Duration", 20);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Effect.2.PotionEffect", "INCREASE_DAMAGE");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Effect.2.Amplifier", 20);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.1.Effect.2.Duration", 20);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.2.Material", "GOLD_BLOCK");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.2.EffectType", "HealthBonus");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.2.HeartBonus", 2);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Material", "DIAMOND_BLOCK");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.EffectType", "Item");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Item.1.Material", "IRON_SWORD");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Item.1.Quantity", 1);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Item.1.IsEnchanted", true);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Item.1.Enchantments.1.Type", "PROTECTION_ENVIRONMENTAL");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Item.1.Enchantments.1.Amplifier", 2);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.3.Item.1.isUnbreakable", true);
                blockFileConfiguration.set("NewOnyxFFa.Config.EffectBlock.Cooldown", 20);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.4.Material", "REDSTONE_BLOCK");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.4.EffectType", "CommandBlock");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.JumpadBlock.1.Material", "EMERALD_BLOCK");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.JumpadBlock.1.VectorCoords.High", 2);
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.JumpadBlock.1.VectorCoords.EyeLocationDirectionMovementMultiplier", 2);
                lore.clear();
                lore.add("give %player% golden_apple 1");
                lore.add("give %player% stick 1");
                blockFileConfiguration.set("NewOnyxFFa.Config.Block.BlockWithEffects.4.Commands", lore.toArray());
                lore.clear();
                blockFileConfiguration.save(blockFile);
            }

            if (!statsFile.exists()) {
                statsFile.createNewFile();
                statsConfiguration = YamlConfiguration.loadConfiguration(statsFile);
                statsConfiguration.set("NewOnyxFFa.Description", "You can use this file to change players stats, using their UUIDS");
                statsConfiguration.save(statsFile);
            }

            if (!ranksFile.exists()) {
                ranksFile.createNewFile();
                ranksConfiguration = YamlConfiguration.loadConfiguration(ranksFile);
                commandsList.add("msg %player% test");
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.Name", "Cuivre");
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.Color", "&e");
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.UpperBound", 1000);
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.LowerBound", 0);
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.RankNumber", 1);
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.CommandsOnGoToRank", commandsList.toArray());
                ranksConfiguration.set("NewOnyxFFa.Ranks.Cuivre.CommandsOnLeaveRank", commandsList.toArray());
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.Name", "Argent");
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.Color", "&b");
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.UpperBound", 2000);
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.LowerBound", 1000);
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.RankNumber", 2);
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.CommandsOnGoToRank", commandsList.toArray());
                ranksConfiguration.set("NewOnyxFFa.Ranks.Argent.CommandsOnLeaveRank", commandsList.toArray());
                ranksConfiguration.save(ranksFile);
            }

            if (!spawnsFile.exists()) {
                spawnsFile.createNewFile();
                spawnsConfiguration = YamlConfiguration.loadConfiguration(spawnsFile);
                spawnsConfiguration.set("NewOnyxFFa.Spawns.Lobby.WorldName", "world");
                spawnsConfiguration.set("NewOnyxFFa.Spawns.Lobby.x", 0);
                spawnsConfiguration.set("NewOnyxFFa.Spawns.Lobby.y", 100.0);
                spawnsConfiguration.set("NewOnyxFFa.Spawns.Lobby.z", 0);
                spawnsConfiguration.set("NewOnyxFFa.Spawns.Lobby.yaw", 0f);
                spawnsConfiguration.set("NewOnyxFFa.Spawns.Lobby.pitch", 0f);
                spawnsConfiguration.save(spawnsFile);
            }
            if (!kitsFile.exists()) {
                kitsFile.createNewFile();
                kitsConfiguration = YamlConfiguration.loadConfiguration(kitsFile);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Material", "ColoredLeather");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Color.R", 255);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Color.G", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Color.B", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Helmet.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Helmet.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Helmet.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Chestplate.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Chestplate.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Chestplate.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Leggings.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Leggings.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Leggings.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Boots.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Boots.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Boots.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Items.1.Type", "Enchanted");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Items.1.Enchantments.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Items.1.Enchantments.1.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Items.1.Enchantments.2.Type", "Enchantment.DAMAGE_ALL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Armor.Items.1.Enchantments.2.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Material", "GOLD_AXE");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Type", "Enchanted");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Slot.Type", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Quantity.Type", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Enchantments.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Enchantments.1.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Enchantments.2.Type", "DURABILITY");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Argent.Items.1.Enchantments.2.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Material", "Diamond");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Helmet.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Helmet.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Helmet.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Chestplate.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Chestplate.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Chestplate.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Leggings.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Leggings.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Leggings.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Boots.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Boots.Enchantements.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Armor.Boots.Enchantements.1.Amplifier", 2);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Material", "GOLD_AXE");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Enchantments.1.Type", "PROTECTION_ENVIRONMENTAL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Enchantments.1.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Enchantments.2.Type", "DAMAGE_ALL");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Enchantments.2.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.isDataItem", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.isUnbreakable", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Data", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Slot", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.1.Quantity", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Material", "DIAMOND_PICKAXE");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Enchanted", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Enchantments.1.Type", "DURABILITY");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Enchantments.1.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Enchantments.2.Type", "DIG_SPEED");
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Enchantments.2.Amplifier", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.isDataItem", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.isUnbreakable", true);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Data", 0);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Slot", 1);
                kitsConfiguration.set("NewOnyxFFa.Ffa.Cuivre.Items.2.Quantity", 1);
                kitsConfiguration.save(kitsFile);
            }
            if (!messagesFile.exists()) {
                messagesFile.createNewFile();
                messagesConfiguration = YamlConfiguration.loadConfiguration(messagesFile);
                messagesConfiguration.set("NewOnyxFFa.Messages.Welcome.NewPlayer", "Bienvenue à toi %player% sur le RushFFa d'Onyxworld!");
                messagesConfiguration.set("NewOnyxFFa.Messages.Welcome.Player", "%player% a rejoint la mêlée! ");
                messagesConfiguration.set("NewOnyxFFa.Messages.Welcome.HiddenStaff", "Arrivée incognito sur le Rushffa! Chut...");
                messagesConfiguration.set("NewOnyxFFa.Messages.SpawnCommand.Wait", "Téléportation dans %timer% secondes ... Ne bougez pas!");
                messagesConfiguration.set("NewOnyxFFa.Messages.SpawnCommand.Error", "Vous avez bougé! Téléportation annulée");
                messagesConfiguration.set("NewOnyxFFa.Messages.StatsCommand.Stats", "Voici vos stats: \n Rang : %rank% \n Kills: %kills% \n Morts : %deaths% \n Score: %points%");
                messagesConfiguration.set("NewOnyxFFa.Messages.StatsCommand.OtherStats", "Voici les stats de %player%: \n Rang : %rank% \n Kills: %kills% \n Morts : %deaths% \n Score: %points%");
                messagesConfiguration.set("NewOnyxFFa.Messages.StatsCommand.OtherNotFound", "Le joueur %player% n'a pas été trouvé...");
                messagesConfiguration.set("NewOnyxFFa.Messages.Offa.Spawn.Success", "Point de Spawn ajouté en: %x%, %y%, %z%, %yaw% , %pitch%");
                messagesConfiguration.set("NewOnyxFFa.Messages.Offa.Break.Success", "Le Bloc a été cassé avec succès");
                messagesConfiguration.set("NewOnyxFFa.Messages.Offa.Break.Error", "Erreur, aucun bloc ciblé!");
                messagesConfiguration.set("NewOnyxFFa.Messages.Points.Error", "Erreur. La commande est '/points <arg> <player> <number>");
                messagesConfiguration.set("NewOnyxFFa.Messages.Points.PlayerOffline", "Erreur. Le joueur %player% n'est pas connecté.");
                messagesConfiguration.set("NewOnyxFFa.Messages.Points.Success", "Le score de %player% est maintenant %score%. Son meilleur score est %highScore%");
                messagesConfiguration.set("NewOnyxFFa.Messages.ResetStatsCommand.Success", "Vos statistiques ont été remises à zéro!");
                messagesConfiguration.set("NewOnyxFFa.Messages.Kill.toKiller", "Vous avez éliminé: %player%");
                messagesConfiguration.set("NewOnyxFFa.Messages.Kill.toVictim", "Vous avez été vaincu par: %player%");
                messagesConfiguration.set("NewOnyxFFa.Messages.FallenIntoTheVoid.Suicide", "Vous avez essayé le saut à l'élastique... Sans élastique.");
                messagesConfiguration.set("NewOnyxFFa.Messages.FallenIntoTheVoid.toVictim", "%player% vous a poussé dans le vide!");
                messagesConfiguration.set("NewOnyxFFa.Messages.FallenIntoTheVoid.toKiller", "Vous avez poussé %player% dans le vide!");
                messagesConfiguration.set("NewOnyxFFa.Messages.QuitWhileFighting.toKiller", "%player% a fuit le combat... Quel lâche!");
                messagesConfiguration.set("NewOnyxFFa.Messages.KilledByFalling.toKiller", "%player% s'est écrasé quelques mètres plus bas.");
                messagesConfiguration.set("NewOnyxFFa.Messages.KilledByFalling.toVictim", "%player% vous a fait mordre la poussière... littéralement.");
                messagesConfiguration.set("NewOnyxFFa.Messages.KilledByFalling.Suicide", "Vous êtes tombés trop bas!");
                messagesConfiguration.save(messagesFile);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        statsConfiguration = YamlConfiguration.loadConfiguration(statsFile);
        messagesConfiguration = YamlConfiguration.loadConfiguration(messagesFile);
        spawnsConfiguration = YamlConfiguration.loadConfiguration(spawnsFile);
        ranksConfiguration = YamlConfiguration.loadConfiguration(ranksFile);
        configConfiguration = YamlConfiguration.loadConfiguration(configFile);
        kitsConfiguration = YamlConfiguration.loadConfiguration(kitsFile);
        blockFileConfiguration = YamlConfiguration.loadConfiguration(blockFile);

        ranksManager = new RanksManager(this);

        for (String spawnName : spawnsConfiguration.getConfigurationSection("NewOnyxFFa.Spawns").getKeys(false)) {
            if (!spawnName.equalsIgnoreCase("lobby")) {
                spawnsList.add(new Location(Bukkit.getWorld(spawnsConfiguration.getString("NewOnyxFFa.Spawns.Lobby.WorldName")),
                        spawnsConfiguration.getDouble("NewOnyxFFa.Spawns." + spawnName + ".x"),
                        spawnsConfiguration.getDouble("NewOnyxFFa.Spawns." + spawnName + ".y"),
                        spawnsConfiguration.getDouble("NewOnyxFFa.Spawns." + spawnName + ".z"),
                        (float) spawnsConfiguration.getDouble("NewOnyxFFa.Spawns." + spawnName + ".yaw"),
                        (float) spawnsConfiguration.getDouble("NewOnyxFFa.Spawns." + spawnName + ".pitch")
                ));
            }
        }
        for (String blockEffect : blockFileConfiguration.getConfigurationSection("NewOnyxFFa.Config.Block.BlockWithEffects").getKeys(false)) {
            blockEffectList.add(blockFileConfiguration.getString("NewOnyxFFa.Config.Block.BlockWithEffects." + blockEffect + ".Material"));
        }
        for (String jumpadBlocks : blockFileConfiguration.getConfigurationSection("NewOnyxFFa.Config.Block.JumpadBlock").getKeys(false)) {
            jumpadsBlocks.add(blockFileConfiguration.getString("NewOnyxFFa.Config.Block.JumpadBlock." + jumpadBlocks + ".Material"));
        }

    }
    /*@Override
    public void onDisable(){
        try {
            statsConfiguration.save(statsFile);
            configConfiguration.save(configFile);
            ranksConfiguration.save(ranksFile);
            spawnsConfiguration.save(spawnsFile);
            kitsConfiguration.save(kitsFile);
            messagesConfiguration.save(messagesFile);
            blockFileConfiguration.save(blockFile);
            System.out.println(" ");
            System.out.println("************************");
            System.out.println("[NewOnyxFFA] > ARRET SECURISE");
            System.out.println("************************");
            System.out.println(" ");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" ");
            System.out.println("************************");
            System.out.println("[NewOnyxFFA] > ERREUR DE SAUVEGARDE!");
            System.out.println("************************");
            System.out.println(" ");
        }
    }
*/
    public FileConfiguration getStatsConfiguration() {
        return statsConfiguration;
    }
    public FileConfiguration getRanksConfiguration() {
        return  ranksConfiguration;
    }
    public FileConfiguration getConfigConfiguration() {
        return  configConfiguration;
    }
    public FileConfiguration getSpawnsConfiguration() {
        return  spawnsConfiguration;
    }
    public FileConfiguration getKitsConfiguration() {
        return  kitsConfiguration;
    }
    public FileConfiguration getMessagesConfiguration() {
        return  messagesConfiguration;
    }
    public FileConfiguration getBlockFileConfiguration() {
        return blockFileConfiguration;
    }
    public List<String> getSpawnsInWait() {
        return SpawnInWait;
    }
    public File getStatsFile() {
        return statsFile;
    }
    public File getSpawnsFile() {
        return spawnsFile;
    }
    public List<String> getBlockEffectList() {
        return blockEffectList;
    }
    public List<Location> getSpawnsList() {
        return spawnsList;
    }
    public File getRanksFile() { return ranksFile; }
    public RanksManager getRanksManager(){return ranksManager;}

    public List<String> getJumpadsBlocks() {
        return jumpadsBlocks;
    }
}
