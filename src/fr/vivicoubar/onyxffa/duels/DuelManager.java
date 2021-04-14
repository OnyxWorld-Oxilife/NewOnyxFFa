package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.duels.Duel;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuelManager {
    private HashMap<FFaPlayer, Duel> duelsDemands = new HashMap<>();
    private List<Duel> duelsInProgress = new ArrayList<>();
    private OnyxFFaMain main;

    public DuelManager(OnyxFFaMain onyxFFaMain) {
        main = onyxFFaMain;
    }

    public void addDemand(FFaPlayer asker, FFaPlayer sender, DuelArena arena){
        Duel demandDuel = new Duel(asker, arena);
        duelsDemands.put(asker,demandDuel);
    }

    public void acceptDemand(FFaPlayer asker){
        Duel acceptedDuel = duelsDemands.get(asker);
        duelsDemands.remove(asker);
        duelsInProgress.add(acceptedDuel);
        acceptedDuel.launchDuel();

    }
}
//TODO 1) Joueur asker envoie demande > addDemand() création d'un duel en state Asked, ajout à la hashmap duelDemands
//TODO 2) Envoi d'un message au joueur demandé
//TODO 3) Joueur asked > Accepte ou refuse la demande > Si refuse message de refus aux 2, si accepte acceptDemand() > Launchduel
