package fr.vivicoubar.onyxffa.events.wanted;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.managers.SpawnManager;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WantedEvent {

    protected OnyxFFaMain main;
    protected EventState state = EventState.WAITING;
    protected ArrayList<FFaPlayer> eventPlayers = new ArrayList<>();
    protected FFaPlayer target;
    private int timer;
    public WantedEvent(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    public void playerJoinWanted(FFaPlayer fFaPlayer) {
        eventPlayers.add(fFaPlayer);
    }

    public void playerQuitWanted(FFaPlayer fFaPlayer) {
        eventPlayers.remove(fFaPlayer);
    }

    public ArrayList<FFaPlayer> getEventPlayers() {
        return eventPlayers;
    }

    public boolean isTarget(FFaPlayer fFaPlayer) {
        return fFaPlayer.getUniqueID() == target.getUniqueID();
    }

    public EventState getState() {
        return state;
    }

    public FFaPlayer getTarget() {
        return target;
    }

    public void setRandomTarget(){
        setTarget(eventPlayers.get(new Random().nextInt(eventPlayers.size())));
    }

    public void setTarget(FFaPlayer newTarget) {
        target = newTarget;
        Bukkit.broadcastMessage("§e[§cWanted§e] La nouvelle cible est §c" + target.getPlayer().getName() + "§e!")  ;
        target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999 , 2, true));
        target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 9999 , 2, true));
        target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999 , 1, true));
    }


    public void startWanted() {
        state = EventState.STARTING;
        eventPlayers.clear();
        for (FFaPlayer fFaPlayer : main.getfFaPlayerManager().getfFaPlayerList()) {
            if (!eventPlayers.contains(fFaPlayer)) {
                this.playerJoinWanted(fFaPlayer);
            }
        }
        Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted va bientôt commencer!");
        timer = 60;
        state = EventState.PLAYING;
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println(timer);
                if (timer == 30 || timer == 15 || timer == 10 || timer == 3 || timer == 2 || timer == 1) {
                    Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted va commencer dans §c" + timer + "sec !");
                } else if (timer <= 0) {
                    cancel();
                    Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted commence!");
                    for (FFaPlayer fFaPlayer : eventPlayers) {
                        SpawnManager spawnManagerInstance = new SpawnManager(main);
                        spawnManagerInstance.respawnPlayer(fFaPlayer.getPlayer());
                    }
                    setRandomTarget();
                    timer = 300;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            System.out.println(timer);
                            if (timer == 295 || timer == 60 || timer == 30 || timer == 15 || timer == 10 || timer == 3 || timer == 2 || timer == 1) {
                                Bukkit.broadcastMessage("§e[§cWanted§e] La cible est §c" + target.getPlayer().getName() + "§e! Il reste "+ timer + "sec!");
                            } else if (timer <= 0) {
                                state = EventState.STOPPING;
                                cancel();
                                FFaPlayer winner = target;
                                Rank oldWinnerRank = winner.getStats().getRank();
                                Bukkit.broadcastMessage("§e[§cWanted§e] Le vainqueur est §c"+ winner.getPlayer().getName() + " §eBravo!");
                                try {
                                    target.getStats().setPoints(target.getStats().getScore().get(2) + 200);
                                    target.updateStats();
                                    Rank newWinnerrank = winner.getStats().getRank();

                                    if (oldWinnerRank != newWinnerrank) {
                                        for (String command : newWinnerrank.getCommandOnGoToRank()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", winner.getPlayer().getName()));
                                        }
                                        for (String command : oldWinnerRank.getCommandOnLeaveRank()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", winner.getPlayer().getName()));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            timer--;
                        }
                    }.runTaskTimer(main,0,20);
                }
                timer--;
            }
        }.runTaskTimer(main,0,20);

    }
}
