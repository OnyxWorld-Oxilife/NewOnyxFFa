package fr.vivicoubar.onyxffa;

import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class PlayerPAPIExpansion extends PlaceholderExpansion {

    private OnyxFFaMain main;


    public PlayerPAPIExpansion(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public boolean canRegister() {
        return true;
    }


    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().toString();
    }


    @Override
    public String getIdentifier() {
        return "onyxffa";
    }


    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, player);

        // %someplugin_placeholder1%
        if (identifier.equals("kills")) {
            return "" + fFaPlayer.getStats().getScore().get(0);
        }

        // %someplugin_placeholder2%
        if (identifier.equals("deaths")) {
            return "" + fFaPlayer.getStats().getScore().get(1);
        }

        if (identifier.equals("score")) {
            return "" + fFaPlayer.getStats().getScore().get(2);
        }

        if (identifier.equals("rank")) {
            return fFaPlayer.getStats().getRank().getName();
        }
        if (identifier.equals("color")) {
            return fFaPlayer.getStats().getRank().getColor();
        }
        if (identifier.equals("highscore")) {
            return "" + fFaPlayer.getStats().getScore().get(3);
        }
        if (identifier.equals("killstreak")) {
            return "" + main.killStreak.getValue(player);
        }
        if (identifier.equals("highestkillstreak")) {
            return "" + fFaPlayer.getStats().getHighestKillStreak();
        }
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided

        return null;
    }


}

