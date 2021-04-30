package fr.vivicoubar.onyxffa;

import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;


public class PlayerPAPIExpansion extends PlaceholderExpansion {

    private OnyxFFaMain main;

    DecimalFormat df = new DecimalFormat("###.###");

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
        FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, player);

        // %someplugin_placeholder1%
        if (identifier.equals("kills")) {
            return "" + Math.round(fFaPlayer.getStats().getScore().get(0));
        }

        // %someplugin_placeholder2%
        if (identifier.equals("deaths")) {
            return "" + Math.round(fFaPlayer.getStats().getScore().get(1));
        }
        if(identifier.equals("glowcolor")){
            if(main.wantedEvent.isTarget(main.getFFaPlayerManager().getFFaPlayer(main, player))){
               return "§4";
            }else{
                for(PotionEffect potion : player.getActivePotionEffects()) {
                    if(potion.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                        return "§d";
                    }
                }
                return "§b";
            }
        }
        if (identifier.equals("score")) {
            return "" + Math.round(fFaPlayer.getStats().getScore().get(2));
        }

        if (identifier.equals("rank")) {
            return fFaPlayer.getStats().getRank().getName();
        }
        if (identifier.equals("color")) {
            return fFaPlayer.getStats().getRank().getColor();
        }
        if (identifier.equals("highscore")) {
            return "" + Math.round(fFaPlayer.getStats().getScore().get(3));
        }
        if (identifier.equals("killstreak")) {
            return "" + fFaPlayer.getKillStreak();
        }
        if (identifier.equals("highestkillstreak")) {
            return "" + fFaPlayer.getStats().getHighestKillStreak();
        }
        if (identifier.equals("ratio")) {
            if (fFaPlayer.getStats().getScore().get(1) > 0) {
                return "" + df.format(fFaPlayer.getStats().getScore().get(0) / fFaPlayer.getStats().getScore().get(1));
            } else {
                return "" + fFaPlayer.getStats().getScore().get(0);
            }
        }
        if (identifier.equals("vanish")) {
            if (!fFaPlayer.isVisible())
                return "§7[V] ";
            return "";
        }

        return null;
    }


}

