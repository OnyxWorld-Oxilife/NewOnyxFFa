package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;

import java.util.ArrayList;
import java.util.List;

public class DuelManager {

    private static final List<Duel> duels = new ArrayList<>();
    private final OnyxFFaMain main = OnyxFFaMain.getInstance();

    public void addDuel(FFaPlayer asker, FFaPlayer asked) {
        Duel duel = new Duel(asker, asked);
        duels.add(duel);
    }

    public Duel getDuelByPlayer(FFaPlayer fFaPlayer) {
        for (Duel duel : duels) {
            if (duel.getAsker() == fFaPlayer || duel.getAsked() == fFaPlayer) {
                return duel;
            }
        }
        return null;
    }

    public void removeDuelByPlayer(FFaPlayer fFaPlayer) {
        duels.removeIf(duel -> duel.getAsker() == fFaPlayer || duel.getAsked() == fFaPlayer);
    }

    public void removeDuel(Duel duel) {
        duels.remove(duel);
    }

}