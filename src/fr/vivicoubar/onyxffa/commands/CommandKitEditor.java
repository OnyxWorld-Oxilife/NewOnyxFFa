package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandKitEditor implements CommandExecutor {

    private OnyxFFaMain main;
    public CommandKitEditor(OnyxFFaMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if(command.getName().equalsIgnoreCase("kiteditor") && commandSender instanceof Player && strings.length > 0) {
            FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, ((Player) commandSender).getPlayer());
            switch(strings[0].toLowerCase()) {
                case "edit":
                    if (fFaPlayer.getState() == FFaPlayerStates.WAITING) {
                        commandSender.sendMessage("§eTu peux à présent éditer ton kit !");
                        commandSender.sendMessage("§eFais §3/kiteditor save §epour sauvegarder ton kit !");
                        main.getKitsManager().giveKit(fFaPlayer, true);
                        fFaPlayer.setState(FFaPlayerStates.EDITING);
                    } else {
                        if (fFaPlayer.getState() == FFaPlayerStates.EDITING) {
                            commandSender.sendMessage("§cUsage: /kiteditor save");
                        } else {
                            commandSender.sendMessage("§eTu dois être au spawn pour éditer ton kit !");
                        }
                    }
                    break;
                case "save":
                    if (fFaPlayer.getState() == FFaPlayerStates.EDITING) {
                        main.getKitsManager().saveKit(fFaPlayer);
                        commandSender.sendMessage("§eTon kit a bien été sauvegardé !");
                        ItemStack joinItem = main.getItemBuilder().buildItem(main.getConfigConfiguration(), "NewOnyxFFa.Config.Menu.Item");
                        fFaPlayer.getPlayer().getInventory().setItem(4, joinItem);
                        fFaPlayer.getPlayer().getInventory().setHeldItemSlot(4);
                        fFaPlayer.setState(FFaPlayerStates.WAITING);
                    } else {
                        commandSender.sendMessage("§cUsage : /kiteditor edit");
                    }
                    break;
                case "give":
                    if (fFaPlayer.getPlayer().hasPermission("admin.admin")) {
                        main.getKitsManager().giveKit(fFaPlayer, false);
                        commandSender.sendMessage("given");
                    } else {
                        commandSender.sendMessage(command.getUsage());
                    }
                    break;
                default:
                    commandSender.sendMessage(command.getUsage());
            }
            return true;
        }
        return false;
    }

}
