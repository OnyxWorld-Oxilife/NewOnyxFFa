package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaManager {
    private final List<Arena> arenaList = new ArrayList<>();

    public void addArena(Location loc1, Location loc2, String name){
        Arena arena = new Arena(loc1,loc2,name);
        arenaList.add(arena);
    }

    public Arena nextArena(){
        for(Arena arena : arenaList){
            if(arena.getArenaState() == ArenaState.AVAILABLE){
                return arena;
            }
        }
        return null;
    }

}
