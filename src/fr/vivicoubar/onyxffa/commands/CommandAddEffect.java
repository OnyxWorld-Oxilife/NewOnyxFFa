package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandAddEffect implements CommandExecutor {

    private OnyxFFaMain main = OnyxFFaMain.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (command.getName().equalsIgnoreCase("addeffect")) {

            if (strings.length >= 4) {
                if (Bukkit.getPlayer(strings[0]) != null &&Bukkit.getPlayer(strings[0]).isOnline()) {
                    main.potionEffectManager.addPotionEffect(Bukkit.getPlayer(strings[0]), new PotionEffect(PotionEffectType.getByName(strings[1]), Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), true));
                } else {
                    commandSender.sendMessage(command.getUsage());
                }
                return true;
            } else {
                commandSender.sendMessage(command.getUsage());
                return true;
            }

        }

        return false;
    }

}
