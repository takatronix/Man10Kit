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
    private Man10Kit plugin;

    public Man10KitCommand(Man10Kit plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        //      引数がない場合
        if (args.length < 1) {
            //showHelp(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            // showHelp(p);
            return true;
        }

        ////////////////////////////////////
        //          保存
        ////////////////////////////////////
        if (args[0].equalsIgnoreCase("save")) {

            if (args.length < 1) {
                p.sendMessage("/mkit save [KitName]");
                return false;
            }

            plugin.save(p,args[1]);
            return true;
        }

        ////////////////////////////////////
        //          Load
        ////////////////////////////////////
        if (args[0].equalsIgnoreCase("load")) {

            if (args.length < 1) {
                p.sendMessage("/mkit load [KitName]");
                return false;
            }

            plugin.load(p,args[1]);
            return true;
        }
        return false;
    }

}


