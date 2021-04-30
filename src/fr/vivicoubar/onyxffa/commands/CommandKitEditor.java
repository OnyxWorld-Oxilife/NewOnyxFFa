package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.KitsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKitEditor implements CommandExecutor {

    private OnyxFFaMain main;
    public CommandKitEditor(OnyxFFaMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if(command.getName().equalsIgnoreCase("kiteditor") && commandSender instanceof Player && strings.length > 0) {
            FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, ((Player) commandSender).getPlayer());
            if (strings[0].equalsIgnoreCase("edit")) {
                main.getKitsManager().giveKit(fFaPlayer, true);
                commandSender.sendMessage("edition");
            } else if (strings[0].equalsIgnoreCase("save")) {
                main.getKitsManager().saveKit(fFaPlayer);
                commandSender.sendMessage("saved");
            } else if (strings[0].equalsIgnoreCase("give")) {
                main.getKitsManager().giveKit(fFaPlayer, false);
                commandSender.sendMessage("given");
            } else {
                commandSender.sendMessage("error");
            }
            return true;
        }
        return false;
    }

}
