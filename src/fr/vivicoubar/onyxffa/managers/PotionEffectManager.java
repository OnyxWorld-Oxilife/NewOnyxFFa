package fr.vivicoubar.onyxffa.managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectManager {

    public void addPotionEffect(Player player, PotionEffect potionEffect){
        for(PotionEffect activepotion : player.getActivePotionEffects()){
            if(potionEffect.getType() == activepotion.getType()){
                int time = activepotion.getDuration() + potionEffect.getDuration();
                player.removePotionEffect(activepotion.getType());
                player.addPotionEffect(new PotionEffect(potionEffect.getType(), time, potionEffect.getAmplifier()));
                return;
            }
        }
        player.addPotionEffect(potionEffect);
    }
    public void clearAllPotionEffect(Player player){
        for(PotionEffect activepotion : player.getActivePotionEffects()){
            player.removePotionEffect(activepotion.getType());
        }
    }
    public void clearPotionEffectType(Player player, PotionEffectType potionEffectType){
        for(PotionEffect activepotion : player.getActivePotionEffects()){
            if(potionEffectType == activepotion.getType()) {
                player.removePotionEffect(activepotion.getType());
            }
        }
    }
}
