package red.man10;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by takatronix on 2017/03/06.
 */
public class Man10KitCommand implements CommandExecutor {
    JavaPlugin plugin;
    public FightClubMan10KitCommandCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;




        return false;
    }

}


