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
        if(target != null) {
            return fFaPlayer.getUniqueID() == target.getUniqueID();
        }else{
            return false;
        }
    }

    public EventState getState() {
        return state;
    }

    public void setRandomTarget(){

        FFaPlayer newtarget = eventPlayers.get(new Random().nextInt(eventPlayers.size()));
        int count =0;
        while(!newtarget.isInArena() && count <= 5){
           newtarget= eventPlayers.get(new Random().nextInt(eventPlayers.size()));
           count ++;
        }
        if(count >= 5){
            stopWanted();
            return;
        }
        setTarget(newtarget);
    }

    public void setTarget(FFaPlayer newTarget) {
        target = newTarget;
        Bukkit.broadcastMessage("§e[§cWanted§e] La nouvelle cible est §c" + target.getPlayer().getName() + "§e!")  ;
        main.potionEffectManager.addPotionEffect(target.getPlayer(), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999 , 2, true));
        main.potionEffectManager.addPotionEffect(target.getPlayer(), new PotionEffect(PotionEffectType.GLOWING, 9999 , 2, true));
        main.potionEffectManager.addPotionEffect(target.getPlayer(), new PotionEffect(PotionEffectType.SLOW, 9999 , 1, true));
    }
    public void stopWanted(){
        state = EventState.WAITING;
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
                if(state == EventState.WAITING){
                    cancel();
                }
                if (timer == 30 || timer == 15 || timer == 10 || timer == 3 || timer == 2 || timer == 1) {
                    Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted va commencer dans §c" + timer + "sec !");
                } else if (timer <= 0) {
                    cancel();
                    Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted commence!");
                    for (FFaPlayer fFaPlayer : eventPlayers) {
                        SpawnManager spawnManagerInstance = new SpawnManager(main);
                        spawnManagerInstance.respawnPlayer(fFaPlayer.getPlayer());
                    }

                    timer = 305;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(state == EventState.WAITING){
                                cancel();
                            }
                            if(timer == 300){
                                setRandomTarget();
                            }
                            if (timer == 300|| timer == 60 || timer == 30 || timer == 15 || timer == 10 || timer == 3 || timer == 2 || timer == 1) {
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
                                    target.getPlayer().getActivePotionEffects().clear();
                                    Rank newWinnerrank = winner.getStats().getRank();

                                    if (oldWinnerRank != newWinnerrank) {
                                        for (String command : newWinnerrank.getCommandOnGoToRank()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", winner.getPlayer().getName()));
                                        }
                                        for (String command : oldWinnerRank.getCommandOnLeaveRank()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", winner.getPlayer().getName()));
                                        }
                                        target = null;
                                        state = EventState.WAITING;
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
