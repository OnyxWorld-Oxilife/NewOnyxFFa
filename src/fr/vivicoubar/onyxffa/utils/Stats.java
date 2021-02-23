package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stats {
    private double kills;
    private double points;
    private double deaths;
    private Rank rank;
    private final FFaPlayer player;
    private final OnyxFFaMain main;

    public Stats(FFaPlayer fFaPlayer, OnyxFFaMain onyxFFaMain) {
        this.player = fFaPlayer;
        this.main = onyxFFaMain;
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        this.kills = statsConfiguration.getDouble("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Kills");
        this.deaths = statsConfiguration.getDouble("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Deaths");
        this.points = statsConfiguration.getDouble("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Points");
        for(Rank setRank : main.getRanksManager().getRanks()){
            if(points >= setRank.lowerBound && points < setRank.upperBound){
                this.rank = setRank;
            }
        }
        }

    public Rank getRank() {
        return this.rank;
    }
    public List<Double> getScore(){
        List<Double> score = new ArrayList<>();
        score.add(kills);
        score.add(deaths);
        score.add(points);
        return score;
    }
    public void iterateKills(){
        this.kills ++;
        main.getStatsConfiguration().set("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Kills", this.kills);
    }
    public void iterateDeaths(){
        this.deaths ++;
        main.getStatsConfiguration().set("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Deaths", this.deaths);
    }
    public void setPoints(double points) throws IOException {
        this.points = points;
        main.getStatsConfiguration().set("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Points", this.points);
        main.getStatsConfiguration().save(main.getStatsFile());
    }
    public void reset(){
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        statsConfiguration.set("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Kills", 0);
        statsConfiguration.set("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Deaths", 0);
        statsConfiguration.set("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Points", 0);

        this.kills = statsConfiguration.getDouble("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Kills");
        this.deaths = statsConfiguration.getDouble("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Deaths");
        this.points = statsConfiguration.getDouble("NewOnyxFFa." + this.player.getPlayer().getUniqueId() + ".Points");
        for(Rank setRank : main.getRanksManager().getRanks()) {
            if (points >= setRank.lowerBound && points < setRank.upperBound) {
                this.rank = setRank;
            }
        }
    }
}

