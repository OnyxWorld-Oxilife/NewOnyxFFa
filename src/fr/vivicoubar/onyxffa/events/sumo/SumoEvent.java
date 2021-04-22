package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.events.OnyxEvent;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SumoEvent extends OnyxEvent {

    private final List<FFaPlayer> winners = new ArrayList<>();

    public SumoEvent(OnyxFFaMain onyxFFaMain){
        this.main= onyxFFaMain;
    }

    @Override
    protected void startEvent() {
        state = EventState.STARTING;
        Bukkit.broadcastMessage("§e[§cSumo§e] L'Event Sumo va commencer!");
        new BukkitRunnable(){
            int timer = 60;
            @Override
            public void run() {
                if(state != EventState.STARTING){
                    cancel();
                }
                if(timer == 60 || timer == 30 || timer == 15 ||timer == 5|| timer ==3 || timer == 2 || timer == 1){
                    Bukkit.broadcastMessage("§e[§cSumo§e] Début dans " + timeParser(timer));
                }else if(timer == 0){
                    if(eventPlayers.size() > 1) {
                        Bukkit.broadcastMessage("§e[§cSumo§e] L'Event Sumo commence!");
                        for (FFaPlayer fFaPlayer : eventPlayers) {
                            fFaPlayer.getPlayer().getInventory().clear();
                            ItemStack batonKb = new ItemStack(Material.STICK, 1);
                            batonKb.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
                            fFaPlayer.getInventory().setItem(4, batonKb);
                            fFaPlayer.getPlayer().getInventory().setHeldItemSlot(4);
                            fFaPlayer.getPlayer().teleport(main.sumoSpawnManager.getSelectedSpawn());
                        }
                        winners.addAll(eventPlayers);
                    }else{
                        cancel();
                        Bukkit.broadcastMessage("§e[§cSumo§e] Pas assez de joueurs pour débuter!");
                        stopEvent();
                    }
                }else if(timer < 0 && timer > -5){
                    for(FFaPlayer fFaPlayer: eventPlayers){
                        fFaPlayer.getPlayer().sendMessage("§e[§cSumo§e] Début du PVP dans " + timeParser(timer + 6));
                    }
                }
                if(timer <= -5 ){
                    cancel();
                    state = EventState.PLAYING;
                    timer = 300;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(state != EventState.PLAYING){
                                cancel();
                            }
                            if(timer == 300 ||timer == 240 || timer == 180 ||timer == 120 ||timer == 60 ||timer == 15 || timer == 3 ||timer == 2 ||timer == 1){
                                Bukkit.broadcastMessage("§e[§cSumo§e] Fin de l'event dans " + timeParser(timer));
                            }
                            if(timer <= 0 ){
                                cancel();
                                if(winners.size()> 0){
                                    StringBuilder winnersname = new StringBuilder();
                                    for(FFaPlayer winner : winners){
                                        winnersname.append(" ").append(winner.getPlayer().getName());
                                        try {
                                            winner.getStats().addPoints(200);
                                            winner.updateStats();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        winner.sendToSpawn();
                                    }
                                    Bukkit.broadcastMessage("§e[§cSumo§e] Fin de l'event! Bravo à§c " + winnersname + "§e qui sont restés en vie!");
                                }
                            }
                            timer --;
                        }
                    }.runTaskTimer(main, 0 , 20);
                }
                timer --;
            }
        }.runTaskTimer(main, 0 , 20);


    }

    @Override
    protected void stopEvent() {
        state = EventState.STOPPING;
        Bukkit.broadcastMessage("§e[§cSumo§e] Event Annulé!");
        for(FFaPlayer fFaPlayer : eventPlayers){
            fFaPlayer.sendToSpawn();
        }
        state = EventState.WAITING;
    }

    @Override
    protected void playerJoinOnyxEvent(FFaPlayer fFaPlayer) {
        fFaPlayer.getPlayer().sendMessage("§eTu as rejoint le Sumo! L'event va bientôt commencer...");
        eventPlayers.add(fFaPlayer);
        fFaPlayer.setState(FFaPlayerStates.SUMO);
    }

    @Override
    protected void playerQuitOnyxEvent(FFaPlayer fFaPlayer) {
        eventPlayers.remove(fFaPlayer);
        if(state == EventState.PLAYING){
            eliminatePlayer(fFaPlayer);
        }else {
            fFaPlayer.setState(FFaPlayerStates.WAITING);
            fFaPlayer.getPlayer().sendMessage("§eTu as quitté l'event Sumo!");
        }

    }
    public void eliminatePlayer(FFaPlayer fFaPlayer){
        eventPlayers.remove(fFaPlayer);
        winners.remove(fFaPlayer);
        fFaPlayer.sendToSpawn();
        fFaPlayer.setState(FFaPlayerStates.WAITING);
        fFaPlayer.getPlayer().sendMessage("§e[§cSumo§e] Vous êtes éliminé!");
        if(winners.size() == 1){
            FFaPlayer winner = winners.get(0);
            Bukkit.broadcastMessage("§e[§cSumo§e] Fin de l'event! Bravo à §c" + winner.getPlayer().getName() + "§e qui est resté en vie!");
            winner.sendToSpawn();
            state = EventState.WAITING;
            eventPlayers.clear();
            winners.clear();
            try {
                winner.getStats().addPoints(500);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
