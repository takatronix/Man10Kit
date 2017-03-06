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
            showHelp(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
             showHelp(p);
            return true;
        }

        ////////////////////////////////////
        //          save
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
        /////////////////////////////////////
        //          delete
        ////////////////////////////////////
        if (args[0].equalsIgnoreCase("delete")) {

            if (args.length < 1) {
                p.sendMessage("/mkit delete [KitName]");
                return false;
            }

            plugin.delete(p,args[1]);
            return true;
        }

        return true;
    }

    void showHelp(Player p){
        p.sendMessage("§e==============§d●§f●§a●§e Man10 KitPlugin §d●§f●§a●§e===============");
        p.sendMessage("" +
                "http://man10.red by takatronix\n" +
                "/mkit list - List all kits\n" +
                "/mkit load - Load a kit\n" +
                "/mkit save - Save your inventory to kit\n" +
                "/mkit delete - Delete a saved kit\n" +
                "/mkit push - Push user's inventory\n" +
                "/mkit pop - Pop user's inventory"

        );
    }

}


