package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Duel {
    private FFaPlayer asker;
    private FFaPlayer asked;
    private OnyxFFaMain main = OnyxFFaMain.getInstance();
    private ArenaManager arenaManager = main.getArenaManager();

    private Arena arena;
    private DuelState state;

    public Duel(FFaPlayer asker, FFaPlayer asked) {
        this.asker = asker;
        this.asked = asked;
        this.state = DuelState.PENDING;
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
            this.asker.setState(FFaPlayerStates.DUEL);
            this.asked.setState(FFaPlayerStates.DUEL);
            this.arena = arenaManager.nextArena();

            new BukkitRunnable() {
                int timer = 3;
                @Override
                public void run() {
                    if(timer <= 0) {
                        arena.spawnPlayers(asker, asked);
                        cancel();
                    }
                    if(timer > 0) {
                        asker.getPlayer().sendTitle("Vous allez être téléporté dans", String.valueOf(timer), 4, 16, 0);
                    }
                    timer--;
                }
            }.runTaskTimer(main,0,20);
            //TODO Téléporter à l'arène, donner kit, mettre compte à rebours (FFaPlayer en state Freeze duel?) , mettre FFaPlayer en Duel
        }
    }

    public void startDuel() {
        if(this.state == DuelState.LOADING) {
            new BukkitRunnable() {
                int timer = 5;
                @Override
                public void run() {
                    if(timer <= 0) {
                        arena.spawnPlayers(asker, asked);
                        cancel();
                    }
                    if(timer > 0) {
                        asker.getPlayer().sendTitle("Le combat commence dans", String.valueOf(timer), 4, 16, 0);
                    }
                    timer--;
                }
            }.runTaskTimer(main,0,20);
        }
    }

    public void endDuel(FFaPlayer winner) {
        FFaPlayer loser = winner == asked ? asker : asked;
        Bukkit.broadcastMessage(winner.getPlayer().getName() + " a gagné son combat contre " + loser.getPlayer().getName());
    }

}
