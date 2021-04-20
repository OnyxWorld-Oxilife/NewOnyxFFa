package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class LocationBuilder {

    private final OnyxFFaMain main;

    public LocationBuilder(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    public Location getLocation(String parameter, FileConfiguration spawnConfiguration) {
        double x;
        try {
            x = spawnConfiguration.getDouble(parameter + ".x");
        } catch (NullPointerException e) {
            e.printStackTrace();
            x = 0;
        }
        double y;
        try {
            y = spawnConfiguration.getDouble(parameter + ".y");
        } catch (NullPointerException e) {
            e.printStackTrace();
            y = 0;
        }
        double z;
        try {
            z = spawnConfiguration.getDouble(parameter + ".z");
        } catch (NullPointerException e) {
            e.printStackTrace();
            z = 0;
        }
        float yaw;
        try {
            yaw = (float) spawnConfiguration.getDouble(parameter + ".yaw");
        } catch (NullPointerException e) {
            e.printStackTrace();
            yaw = 0f;
        }
        float pitch;
        try {
            pitch = (float) spawnConfiguration.getDouble(parameter + ".pitch");
        } catch (NullPointerException e) {
            e.printStackTrace();
            pitch = 0f;
        }
        return new Location(Bukkit.getWorld(main.getSpawnsConfiguration().getString("NewOnyxFFa.Spawns.Lobby.WorldName")), x, y, z, yaw, pitch);
    }
}
