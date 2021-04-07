package fr.vivicoubar.onyxffa.managers;


import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RanksManager {
    private final List<Rank> ranks = new ArrayList<>();
    private final OnyxFFaMain main;

    public RanksManager(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
        ConfigurationSection rankSection = main.getRanksConfiguration().getConfigurationSection("NewOnyxFFa.Ranks");
        for (String rank : rankSection.getKeys(false)) {
            FileConfiguration rankconfig = main.getRanksConfiguration();
            ranks.add(new Rank(main,
                    rankconfig.getString("NewOnyxFFa.Ranks." + rank + ".Name"),
                    rankconfig.getString("NewOnyxFFa.Ranks." + rank + ".Color"),
                    rankconfig.getDouble("NewOnyxFFa.Ranks." + rank + ".UpperBound"),
                    rankconfig.getDouble("NewOnyxFFa.Ranks." + rank + ".LowerBound"),
                    rankconfig.getDouble("NewOnyxFFa.Ranks." + rank + ".RankNumber"),
                    rankconfig.getStringList("NewOnyxFFa.Ranks." + rank + ".CommandsOnGoToRank"),
                    rankconfig.getStringList("NewOnyxFFa.Ranks." + rank + ".CommandsOnLeaveRank")
            ));
        }
    }

    public List<Rank> getRanks() {
        return this.ranks;
    }
}
