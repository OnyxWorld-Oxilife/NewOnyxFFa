package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.OnyxFFaMain;

import java.util.HashMap;
import java.util.List;

public class ArenaManager {
    private OnyxFFaMain main;
    private HashMap<DuelArena, ArenaState> arenaList = new HashMap<>();

    public ArenaManager(OnyxFFaMain onyxFFaMain) {
        main = onyxFFaMain;
        loadArena();
    }

    public void loadArena(){
        //TODO ARENE EN CONFIG?
    }

    public HashMap<DuelArena, ArenaState> getArenaList() {
        return arenaList;
    }
}
