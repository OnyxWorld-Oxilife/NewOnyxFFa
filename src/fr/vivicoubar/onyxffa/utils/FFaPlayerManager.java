package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FFaPlayerManager {
    private List<FFaPlayer> fFaPlayerList;
    private OnyxFFaMain main;

    public FFaPlayerManager(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
        this.fFaPlayerList = new ArrayList<>();
    }

    public List<FFaPlayer> getfFaPlayerList() {
        return fFaPlayerList;
    }
    public FFaPlayer getFFaPlayer(OnyxFFaMain onyxFFaMain, Player player){
        for(FFaPlayer fFaPlayer : fFaPlayerList){
            if(fFaPlayer.getUniqueID() == player.getUniqueId()){
                return fFaPlayer;
            }
        }
        return new FFaPlayer(main, player);
    }
}
