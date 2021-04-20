package fr.vivicoubar.onyxffa.events.wanted;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.events.OnyxEvent;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

public class WantedEvent extends OnyxEvent {

    private FFaPlayer target;
    private int timer;

    public void playerJoinOnyxEvent(FFaPlayer fFaPlayer) {
        eventPlayers.add(fFaPlayer);
    }

    public void playerQuitOnyxEvent(FFaPlayer fFaPlayer) {
        eventPlayers.remove(fFaPlayer);
    }



    public boolean isTarget(FFaPlayer fFaPlayer) {
        if(target != null) {
            return fFaPlayer.getUniqueID() == target.getUniqueID();
        }else{
            return false;
        }
    }

    public void setRandomTarget(){

        FFaPlayer newTarget = eventPlayers.get(new Random().nextInt(eventPlayers.size()));
        int count =0;
        while(!newTarget.isInArena() && count <= 5){
           newTarget= eventPlayers.get(new Random().nextInt(eventPlayers.size()));
           count ++;
        }
        if(count >= 5){
            stopEvent();
            return;
        }
        setTarget(newTarget);
    }


    public void setTarget(FFaPlayer newTarget) {
        if(newTarget.isInArena()) {
            target = newTarget;
            Bukkit.broadcastMessage("§e[§cWanted§e] La nouvelle cible est §c" + target.getPlayer().getName() + "§e!");
            main.potionEffectManager.addPotionEffect(target.getPlayer(), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999, 0, true));
            main.potionEffectManager.addPotionEffect(target.getPlayer(), new PotionEffect(PotionEffectType.GLOWING, 9999, 0, true));
            main.potionEffectManager.addPotionEffect(target.getPlayer(), new PotionEffect(PotionEffectType.SLOW, 9999, 1, true));
        }else{
            Bukkit.broadcastMessage("§e[§cWanted§e]La cible a disparu, désignation d'une nouvelle cible...!");
            new BukkitRunnable() {
                int timer2 = 5;
                @Override
                public void run() {
                    if(timer2 <= 0) {
                        cancel();
                        setRandomTarget();
                    }
                    if(timer2 > 0) {
                        Bukkit.broadcastMessage("§e[§cWanted§e] La cible sera désignée dans §c" + timeParser(timer2) + " §e!");
                    }
                    timer2--;
                }
            }.runTaskTimer(main,100,20);
        }
    }
    public void stopEvent(){
        state = EventState.WAITING;
        Bukkit.broadcastMessage("§b[§eOnyxFFa§b] Plus de joueurs en vie, L'event Wanted est annulé!");
    }

    public void startEvent() {
        state = EventState.STARTING;
        eventPlayers.clear();
        for (FFaPlayer fFaPlayer : main.getFFaPlayerManager().getfFaPlayerList()) {
            if (!eventPlayers.contains(fFaPlayer)) {
                this.playerJoinOnyxEvent(fFaPlayer);
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
                    Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted va commencer dans §c" + timeParser(timer) + " !");
                } else if (timer <= 0) {
                    cancel();
                    Bukkit.broadcastMessage("§b[§eOnyxFFa§b] L'event Wanted commence!");
                    for (FFaPlayer fFaPlayer : eventPlayers) {
                        if(fFaPlayer.isInArena() || fFaPlayer.getState() == FFaPlayerStates.SPECTATOR) {
                            fFaPlayer.spawnInArena();
                        }
                    }

                    timer = 310;
                    Bukkit.broadcastMessage("§e[§cWanted§e] La cible sera désignée dans §c" + timeParser(timer-300) + " §e!");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(state == EventState.WAITING){
                                cancel();
                            }
                            if(timer == 301 || timer == 302 || timer == 303 || timer == 305){
                                Bukkit.broadcastMessage("§e[§cWanted§e] La cible sera désignée dans §c" + timeParser(timer-300) + "  §e!");
                            }
                            if(timer == 300){
                                setRandomTarget();
                            }
                            if (timer == 300|| timer == 60 || timer == 30 || timer == 15 || timer == 10 || timer == 3 || timer == 2 || timer == 1) {
                                Bukkit.broadcastMessage("§e[§cWanted§e] La cible est §c" + target.getPlayer().getName() + "§e! Il reste "+ timeParser(timer) + " !");
                            } else if (timer <= 0) {
                                state = EventState.STOPPING;
                                cancel();
                                FFaPlayer winner = target;
                                Rank oldWinnerRank = winner.getStats().getRank();
                                Bukkit.broadcastMessage("§e[§cWanted§e] Le vainqueur est §c"+ winner.getPlayer().getName() + "§e! §eBravo!");
                                try {
                                    target.getStats().setPoints(target.getStats().getScore().get(2) + 200);
                                    target.updateStats();
                                    main.potionEffectManager.clearAllPotionEffect(target.getPlayer());
                                    Rank newWinnerrank = winner.getStats().getRank();

                                    if (oldWinnerRank != newWinnerrank) {
                                        for (String command : newWinnerrank.getCommandOnGoToRank()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", winner.getPlayer().getName()));
                                        }
                                        for (String command : oldWinnerRank.getCommandOnLeaveRank()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", winner.getPlayer().getName()));
                                        }
                                        target = null;

                                    }
                                    state = EventState.WAITING;
                                    target = null;
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
