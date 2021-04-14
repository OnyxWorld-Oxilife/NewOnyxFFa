package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.utils.FFaPlayer;

public class Duel {
    private FFaPlayer player1;
    private FFaPlayer player2;

    private DuelArena arena;
    private DuelState state;

    public Duel(FFaPlayer asker, DuelArena askedarena){
        player1 = asker;
        state = DuelState.ASKED;
        arena = askedarena;
    }
    public void acceptDuel(FFaPlayer asked){
        player2 = asked;
        launchDuel();
    }
    public void launchDuel(){
        if(state == DuelState.ASKED){
            state = DuelState.LOADING;
            //TODO Téléporter à l'arène, donner kit, mettre compte à rebours (FFaPlayer en state Freeze duel?) , mettre FFaPlayer en Duel
        }
    }

    public void setWinner(FFaPlayer player){
        FFaPlayer winner;
        FFaPlayer loser;
        state = DuelState.ENDING;
        if(player.getUniqueID().equals(player1.getUniqueID())){
            winner = player1;
            loser = player2;
        }else{
            winner = player2;
            loser = player1;
        }
        //TODO Faire les wins/loose, téléporter au spawn, message

    }
}
