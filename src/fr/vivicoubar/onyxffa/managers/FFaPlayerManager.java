package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FFaPlayerManager {
    private List<FFaPlayer> fFaPlayerList;
    private final OnyxFFaMain main;

    public FFaPlayerManager(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
        this.fFaPlayerList = new ArrayList<>();
        for (Player player :Bukkit.getOnlinePlayers()){
            fFaPlayerList.add(getFFaPlayer(main,player));
        }
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

    public void removeFFaPlayer(Player player){
        fFaPlayerList.removeIf(fFaPlayer -> fFaPlayer.getUniqueID() == player.getUniqueId());
    }
}
