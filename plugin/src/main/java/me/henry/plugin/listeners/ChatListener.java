package me.henry.plugin.listeners;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.henry.plugin.utils.Rank;

public class ChatListener implements org.bukkit.event.Listener {
    private final me.henry.plugin.Plugin plugin = me.henry.plugin.Plugin.getInstance();
    private Thread floodThread; // store the flood thread
    private final Map<String, String> emojis = Map.of(
            ":sob:", "\uD83D\uDE2D",
            ":smile:", "\uD83D\uDE04",
            ":heart:", "\u2764\uFE0F",
            ":thumbup:", "\uD83D\uDC4D",
            ":skull:", "☠");

    private String parseEmojis(String message) {
        for (Map.Entry<String, String> emoji : emojis.entrySet()) {
            message = message.replace(emoji.getKey(), emoji.getValue());
        }
        return message;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Rank rank = plugin.rankManager.getRank(p);
        String msg = e.getMessage();
        ConsoleCommandSender sender = plugin.sender;

        // START FLOOD
        if (msg.equalsIgnoreCase("!flood")) {
            if (floodThread == null || !floodThread.isAlive()) {
                if (rank == null || !rank.isAtLeast(Rank.MOD) || !p.isOp()) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    e.setCancelled(true);
                    return;
                }

                floodThread = new Thread(() -> {
                    java.util.Random random = new java.util.Random();
                    while (!Thread.currentThread().isInterrupted()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 100; i++) {
                            char c = (char) (random.nextInt(26) + 'a');
                            if (random.nextBoolean())
                                c = Character.toUpperCase(c);
                            sb.append(c);
                        }
                        sender.sendMessage(sb.toString());
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                });
                floodThread.start();
            } else {
                sender.sendMessage(ChatColor.RED + "Flood is already running!");
            }
            e.setCancelled(true);
            return;
        }

        // STOP FLOOD
        if (msg.equalsIgnoreCase("!stop")) {
            if (floodThread != null && floodThread.isAlive()) {
                floodThread.interrupt();
                sender.sendMessage(ChatColor.GREEN + ":3 stopped!");
            } else {
                sender.sendMessage(ChatColor.RED + "No flood to stop!");
            }
            e.setCancelled(true);
            return;
        }

        // Normal chat formatting
        String formattedMessage = parseEmojis(msg);
        e.setFormat(rank.getPrefix() + ChatColor.ITALIC + p.getName() + ChatColor.GRAY + " : " + ChatColor.RESET
                + formattedMessage);
    }
}
