package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.managers.SpawnManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemListener implements Listener {
    private OnyxFFaMain main;
    private List<String> description = new ArrayList<>();

    public ItemListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @EventHandler
    private void onClickFFaItem(PlayerInteractEvent interactEvent) {
        if (interactEvent.getPlayer().getGameMode() == GameMode.ADVENTURE && !main.getfFaPlayerManager().getFFaPlayer(main ,interactEvent.getPlayer()).isInArena()) {
            if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK && interactEvent.getAction() != Action.RIGHT_CLICK_AIR) {
                return;
            }
            if (interactEvent.getItem() == null) {
                return;
            }
            if (interactEvent.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(main.getConfigConfiguration().getString("NewOnyxFFa.Config.Menu.Item.Name"))) {
                FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(this.main, interactEvent.getPlayer());
                fFaPlayer.getPlayer().closeInventory();
                fFaPlayer.spawnInArena();
            }
        }
    }
}
