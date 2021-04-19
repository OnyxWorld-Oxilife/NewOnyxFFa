package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.events.OnyxEvent;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Location;

import java.util.List;

public class SumoEvent extends OnyxEvent {

    private final List<Location> spawns = main.getSumoSpawnList();

    @Override
    protected void startEvent() {

    }

    @Override
    protected void stopEvent() {

    }

    @Override
    protected void playerJoinOnyxEvent(FFaPlayer fFaPlayer) {

    }

    @Override
    protected void playerQuitOnyxEvent(FFaPlayer fFaPlayer) {

    }
}
