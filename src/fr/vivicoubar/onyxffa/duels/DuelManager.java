package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;

import java.util.ArrayList;
import java.util.List;

public class DuelManager {

    private static List<Duel> duels = new ArrayList<>();
    private OnyxFFaMain main = OnyxFFaMain.getInstance();

    public void addDuel(FFaPlayer asker, FFaPlayer asked) {
        Duel duel = new Duel(asker, asked);
        duels.add(duel);
    }

    public Duel getDuelByPlayer(FFaPlayer fFaPlayer) {
        for (Duel duel : this.duels) {
            if (duel.getAsker() == fFaPlayer || duel.getAsked() == fFaPlayer) {
                return duel;
            }
        }
        return null;
    }

    public void removeDuelByPlayer(FFaPlayer fFaPlayer) {
        for (Duel duel : this.duels) {
            if (duel.getAsker() == fFaPlayer || duel.getAsked() == fFaPlayer) {
                duels.remove(duel);
            }
        }
    }

}
//TODO 1) Joueur asker envoie demande > addDemand() création d'un duel en state Asked, ajout à la hashmap duelDemands
//TODO 2) Envoi d'un message au joueur demandé
//TODO 3) Joueur asked > Accepte ou refuse la demande > Si refuse message de refus aux 2, si accepte acceptDemand() > Launchduel
