package de.kid2407.bteplugin.command;

import de.kid2407.bteplugin.BTEPlugin;
import de.kid2407.bteplugin.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Tobias Franz
 * Date: 02.07.2020
 * Time: 19:38
 */
public class SpeedCommand implements CommandExecutor, TabCompleter, Listener {

    public static final float PLAYER_DEFAULT_WALK_SPEED = 0.2F;
    public static final float PLAYER_DEFAULT_FLY_SPEED = 0.1F;

    private final HashMap<String, Float> speeds = new HashMap<String, Float>() {{
        put("langsam", 0.1F);
        put("normal", PLAYER_DEFAULT_WALK_SPEED);
        put("schnell", 0.5F);
        put("lichtgeschwindigkeit", 1F);
    }};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Permissions.SPEED_USE.getPermission())) {
                if (args.length > 0) {
                    String newSpeedname = args[0];

                    if (speeds.containsKey(newSpeedname)) {
                        float newSpeed = speeds.get(newSpeedname);

                        player.setWalkSpeed(newSpeed);
                        player.setFlySpeed(newSpeedname.equals("normal") ? PLAYER_DEFAULT_FLY_SPEED : newSpeed);
                        player.sendMessage(BTEPlugin.PREFIX + "Deine neue Geschwindigkeit ist §" + ChatColor.AQUA.getChar() + newSpeedname);
                    } else {
                        player.sendMessage(BTEPlugin.PREFIX + "Unbekannter Speed.");
                    }
                } else {
                    player.sendMessage(BTEPlugin.PREFIX + "Du must einen Speed angeben!");
                    return false;
                }
            }

            return true;
        }

        sender.sendMessage("Kann nur von einem Spieler benutzt werden!");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 0) {
            return speeds.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        return new ArrayList<>(speeds.keySet());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.getPlayer().setWalkSpeed(PLAYER_DEFAULT_WALK_SPEED);
        event.getPlayer().setFlySpeed(PLAYER_DEFAULT_FLY_SPEED);
    }
}
