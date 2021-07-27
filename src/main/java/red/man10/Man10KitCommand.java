package red.man10;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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

        ////////////////////////////////////
        //          jail
        ////////////////////////////////////
        if (args[0].equalsIgnoreCase("jail")) {

            if (args.length != 2) {
                sender.sendMessage("/mkit jail [username]");
                return false;
            }
            Player t = Bukkit.getPlayer(args[1]);
            Bukkit.getLogger().info(args[1]);

            if(t.isOnline() == false){
                Bukkit.getLogger().info("player is not online");
                sender.sendMessage(t.getName() +"はオンラインではありません");
                return false;
            }
            plugin.push(t);
            Bukkit.getLogger().info("_jail");
            plugin.load(t,"_jail");
            return true;
        }


        if (args[0].equalsIgnoreCase("push")) {


            //    引数がある場合
            if (args.length == 2) {
                String name = args[1];
                Player p = Bukkit.getPlayer(name);
                if(p == null){
                    sender.sendMessage(name+"はオフラインです");
                    return false;
                }
                plugin.push(p);
                sender.sendMessage(name+"のユーザーデータをバックアップしました");
                return true;
            }

            //      ユーザーコマンド
            if (sender instanceof Player){
                Player p = (Player) sender;
                plugin.push(p);
                return true;
            }



            return true;
        }
        ////////////////////////////////////
        //          set
        ////////////////////////////////////
        if (args[0].equalsIgnoreCase("set")) {

            Bukkit.getLogger().info("set");
            if (args.length != 3) {
                sender.sendMessage("/mkit set [username] [KitName]");
                return false;
            }

            Player t = Bukkit.getPlayer(args[1]);
            if(t.isOnline() == false){
                Bukkit.getLogger().info("player is not online");
                sender.sendMessage(t.getName() +"はオンラインではありません");
                return false;
            }


            plugin.load(t,args[2]);
            return true;
        }


        if ((args[0].equalsIgnoreCase("pop")) || (args[0].equalsIgnoreCase("unjail"))) {
            //    引数がある場合

            if (args.length == 2) {
                String name = args[1];
                Player p = Bukkit.getPlayer(name);
                p.sendMessage(args[0]);
                if(p == null){
                    sender.sendMessage(name+"はオフラインです");
                    return false;
                }
                if(plugin.pop(p)){
                    sender.sendMessage(name+"のユーザーデータを復元しました");
                }else{
                    sender.sendMessage(name+"のユーザーデータは存在しない");
                }
                return true;
            }

            //      ユーザーコマンド
            if (sender instanceof Player){
                Player p = (Player) sender;
                plugin.pop(p);
                return true;
            }

            return true;
        }





        if(sender instanceof Player)
        {
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

                if (args.length <= 1) {
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

                if (args.length <= 1) {
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
            /////////////////////////////////////
            //          list
            ////////////////////////////////////
            if (args[0].equalsIgnoreCase("list")) {

                plugin.list(p);
                return true;
            }





            showHelp(p);
            return true;
            // The command was executed by a player.
        }
        /*
        else if(sender instanceof ConsoleCommandSender)
        {
            ////////////////////////////////////
            //          set
            ////////////////////////////////////
            if (args[0].equalsIgnoreCase("set")) {

                Bukkit.getLogger().info("set");
                if (args.length <= 2) {
                    sender.sendMessage("/mkit set [username] [KitName]");
                    return false;
                }

                Player t = Bukkit.getPlayer(args[1]);
                if(t.isOnline() == false){
                    Bukkit.getLogger().info("player is not online");
                    sender.sendMessage(t.getName() +"はオンラインではありません");
                    return false;
                }


                plugin.load(t,args[2]);
                return true;
            }

            //The command was executed from console.
        }


        //The command was executed from console.

        showHelp(sender);


*/
        return true;
    }
    void showHelp(CommandSender p){
        p.sendMessage("§e==============§d●§f●§a●§e Man10 KitPlugin §d●§f●§a●§e===============");
        p.sendMessage("" +
                "/mkit list 登録済みのキットを表示\n" +
                "/mkit load [キット名] キットをロードする(いまのインベントリは消えます)\n" +
                "/mkit save [キット名] 現在のインベントリを保存する\n" +
                "/mkit delete [キット名] 登録済みのキットを削除する\n" +
                "/mkit push 現在のインベントリを瞬間保存する\n" +
                "/mkit pop 瞬間保存したキットを復元する\n"+
                "§e§lhttp://man10.red by takatronix\n"
        );
    }

}


