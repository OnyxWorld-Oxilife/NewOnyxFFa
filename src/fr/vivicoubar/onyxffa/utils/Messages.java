package fr.vivicoubar.onyxffa.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Messages {

    public void actionBarMessage(Player p, String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public void broadcastActionBarMessage(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            actionBarMessage(p, msg);
        }
    }

    public void chatMessage(Player p, String msg) {
        p.sendMessage(msg);
    }

    public void broadcastChatMessage(String msg) {
        Bukkit.broadcastMessage(msg);
    }

    public void multipleChatMessages(Player p, List<String > messages) {
        for (String msg : messages) {
            chatMessage(p, msg);
        }
    }

    public void multipleBroadcastMessages(List<String > messages) {
        for (String msg : messages) {
            broadcastChatMessage(msg);
        }
    }

}
