package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Duel {
    private FFaPlayer asker;
    private FFaPlayer asked;
    private OnyxFFaMain main = OnyxFFaMain.getInstance();
    private ArenaManager arenaManager = main.getArenaManager();
    private FileConfiguration messagesConfig = main.getMessagesConfiguration();

    private Arena arena;
    private DuelState state;

    public Duel(FFaPlayer asker, FFaPlayer asked) {
        this.asker = asker;
        this.asked = asked;
        this.state = DuelState.PENDING;
        this.asker.setState(FFaPlayerStates.DUEL);
        this.asked.setState(FFaPlayerStates.DUEL);
    }

    public FFaPlayer getAsker() {
        return this.asker;
    }

    public FFaPlayer getAsked() {
        return this.asked;
    }

    public DuelState getState() {
        return this.state;
    }

    public void teleportDuel() {
        if(this.state == DuelState.PENDING){
            this.state = DuelState.LOADING;
            this.arena = arenaManager.nextArena();
            this.asker.getPlayer().getInventory().clear();
            this.asked.getPlayer().getInventory().clear();

            new BukkitRunnable() {
                int timer = 3;
                @Override
                public void run() {
                    if(timer <= 0) {
                        arena.setArenaState(ArenaState.LOADING);
                        arena.spawnPlayers(asker, asked);
                        startDuel();
                        cancel();
                    }
                    if(timer > 0) {
                        asker.getPlayer().sendTitle("Vous allez être téléporté dans", String.valueOf(timer), 4, 16, 0);
                        asked.getPlayer().sendTitle("Vous allez être téléporté dans", String.valueOf(timer), 4, 16, 0);
                    }
                    timer--;
                }
            }.runTaskTimer(main,0,20);
        }
    }

    public void startDuel() {
        asker.setFrozen(true);
        asked.setFrozen(true);
        asker.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.DIAMOND_AXE, 1));
        asked.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.DIAMOND_AXE, 1));
        //TODO Kits
        if(this.state == DuelState.LOADING) {
            new BukkitRunnable() {
                int timer = 5;
                @Override
                public void run() {
                    if(timer <= 0) {
                        arena.setArenaState(ArenaState.USED);
                        asker.setFrozen(false);
                        asked.setFrozen(false);
                        setState(DuelState.PLAYING);
                        cancel();
                    }
                    if(timer > 0) {
                        asker.getPlayer().sendTitle("Le combat commence dans", String.valueOf(timer), 4, 16, 0);
                        asked.getPlayer().sendTitle("Le combat commence dans", String.valueOf(timer), 4, 16, 0);
                    }
                    timer--;
                }
            }.runTaskTimer(main,0,20);
        }
    }

    public void winDuel(FFaPlayer winner) {
        FFaPlayer loser = winner == asked ? asker : asked;
        Bukkit.broadcastMessage(messagesConfig.getString("NewOnyxFFa.Messages.Duel..End.Broadcast").replaceAll("%winner%", winner.getPlayer().getName()).replaceAll("%loser%", loser.getPlayer().getName()));
        loser.getPlayer().sendMessage("§b[§eDuel§b]: Il restait §c" + winner.getPlayer().getHealth()+  "§bcoeurs à  ");
        new BukkitRunnable() {
            int timer = 5;
            @Override
            public void run() {
                if(timer <= 0) {
                    arena.setArenaState(ArenaState.AVAILABLE);
                    loser.setFrozen(false);
                    ending();
                    cancel();
                }
                if(timer > 0) {
                    winner.getPlayer().sendTitle("§aVous avez gagné !", "Retour au spawn dans " + timer, 0, 20, 0);
                    loser.getPlayer().sendTitle("§cVous avez perdu !", "Retour au spawn dans " + timer, 0, 20, 0);
                }
                timer--;
            }
        }.runTaskTimer(main,0,20);
    }
    public void loseDuel(FFaPlayer loser){
        if (this.state == DuelState.PLAYING) {
            winDuel(loser == asked ? asker : asked);
        } else if (this.state == DuelState.PENDING || this.state == DuelState.LOADING) {
            (loser == asked ? asker : asked).getPlayer().sendMessage(loser.getPlayer().getName() + " s'est déconnecté, le duel est annulé");
            ending();
        }
    }

    public void ending() {
        asker.setState(FFaPlayerStates.WAITING);
        asked.setState(FFaPlayerStates.WAITING);
        if (this.state != DuelState.PENDING) {
            asker.sendToSpawn();
            asked.sendToSpawn();
        }
        asker = null;
        asked = null;
        state = DuelState.ENDING;
        main.getDuelManager().removeDuel(this);
    }

    public void setState(DuelState duelState){
        this.state = duelState;
    }

}
