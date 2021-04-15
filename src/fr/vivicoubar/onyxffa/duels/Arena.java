package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private Location spawnPlayer1;
    private Location spawnPlayer2;
    private List<FFaPlayer> players;
    private String name;
    private ArenaState arenaState;


    public Arena(Location spawn1, Location spawn2, String arenaName){
        spawnPlayer1 = spawn1;
        spawnPlayer2 = spawn2;
        name = arenaName;
        arenaState = ArenaState.AVAILABLE;
        players = new ArrayList<>();
    }

    public void setArenaState(ArenaState arenaState) {
        this.arenaState = arenaState;
    }
    public ArenaState getArenaState(){
        return arenaState;
    }

    public void spawnPlayers(FFaPlayer player1, FFaPlayer player2){
        player1.getPlayer().teleport(spawnPlayer1);
        player2.getPlayer().teleport(spawnPlayer2);
    }


}
