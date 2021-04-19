package fr.vivicoubar.onyxffa.events;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;

import java.util.ArrayList;

public abstract class OnyxEvent {
    protected OnyxFFaMain main = OnyxFFaMain.getInstance();
    protected EventState state = EventState.WAITING;
    protected ArrayList<FFaPlayer> eventPlayers = new ArrayList<>();

    protected EventState getState() {
        return state;
    }

    protected ArrayList<FFaPlayer> getEventPlayers() {
        return eventPlayers;
    }

    protected abstract void startEvent();
    protected abstract void stopEvent();

    protected abstract void playerJoinOnyxEvent(FFaPlayer fFaPlayer);
    protected abstract void playerQuitOnyxEvent(FFaPlayer fFaPlayer);

    public String timeParser(int seconds) {
        double d = Math.floor(seconds/(3600*24));
        double h = Math.floor(seconds % (3600*24) / 3600);
        double m = Math.floor(seconds % 3600 / 60);
        double s = Math.floor(seconds % 60);

        String dDisplay = d > 0 ? d + "d " : "";
        String hDisplay = h > 0 ? h + "h " : "";
        String mDisplay = m > 0 ? m + "m " : "";
        String sDisplay = s > 0 ? s + "s" : "";

        return dDisplay + hDisplay + mDisplay + sDisplay;
    }


}
