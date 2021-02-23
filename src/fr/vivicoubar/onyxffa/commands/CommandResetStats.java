package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandResetStats implements CommandExecutor {
    private OnyxFFaMain main;

    public CommandResetStats(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("resetstats")){
            if(sender instanceof Player) {
                FFaPlayer fFaPlayer = new FFaPlayer(main, (Player) sender);
                fFaPlayer.getStats().reset();
                fFaPlayer.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommand.Success"));
            }

        }
        return false;
    }
}
