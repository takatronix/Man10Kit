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

    final String permissionError = "§cコマンド権限がありません";
    // permissions
    final String helpPermission = "man10.red.man10kit.help";
    final String loadPermission = "man10.red.man10kit.load";
    final String savePermission = "man10.red.man10kit.save";
    final String setPermission = "man10.red.man10kit.set";
    final String listPermission = "man10.red.man10kit.list";
    final String deletePermission = "man10.red.man10kit.delete";
    final String pushPermission = "man10.red.man10kit.push";
    final String popPermission = "man10.red.man10kit.pop";

    boolean checkPermission(CommandSender sender,String permission){
        if(!sender.hasPermission(permission)){
            sender.sendMessage(permissionError);
            return true;
        }
        return false;
    }


    public Man10KitCommand(Man10Kit plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // 引数なし -> help
        if(args.length == 0){
            showHelp(sender);
            return false;
        }

        /////////////////////////////////////////
        //  Listコマンド
        if (args[0].equalsIgnoreCase("list")) {
            if(checkPermission(sender,listPermission))
                return false;

            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.list(sender);
            });
            return true;
        }

        /////////////////////////////////////////
        //  Deleteコマンド
        if (args[0].equalsIgnoreCase("delete")) {
            if(checkPermission(sender,deletePermission))
                return false;

            if (args.length != 2) {
                sender.sendMessage("§5§l/mkit delete [KitName]");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.delete(sender,args[1]);
            });
            return true;
        }


        /////////////////////////////////////////
        //  Setコマンド
        if (args[0].equalsIgnoreCase("set")) {
            if(checkPermission(sender,setPermission))
                return false;

            Bukkit.getLogger().info("set");
            if (args.length != 3) {
                sender.sendMessage("§5/mkit set [ユーザー名] [キット名]");
                return false;
            }

            Player t = Bukkit.getPlayer(args[1]);
            if(t.isOnline() == false){
                sender.sendMessage(t.getName() +"はオンラインではありません");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.load(t,args[2]);
            });
            return true;
        }

        if (sender instanceof Player == false){
            sender.sendMessage("§5プレイヤーのみのコマンドになります");
            return false;
        }


        Player player = (Player)sender;

        /////////////////////////////////////////
        //  Saveコマンド
        if (args[0].equalsIgnoreCase("save")) {
            if(checkPermission(sender,savePermission))
                return false;

            if (args.length != 2) {
                player.sendMessage("§a/mkit save [キット名]");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.save(player,args[1]);
            });
            return true;
        }
        /////////////////////////////////////////
        //  Loadコマンド
        if (args[0].equalsIgnoreCase("load")) {
            if(checkPermission(sender,loadPermission))
                return false;

            if (args.length != 2) {
                player.sendMessage("§a/mkit load [キット名]");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.load(player,args[1]);
            });
            return true;
        }

        /////////////////////////////////////////
        //  setコマンド
        if (args[0].equalsIgnoreCase("set")) {
            if(checkPermission(sender,setPermission))
                return false;

            if (args.length != 2) {
                player.sendMessage("§a/mkit load [キット名]");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.load(player,args[1]);
            });
            return true;
        }



        if (args[0].equalsIgnoreCase("push")) {
            if(checkPermission(sender,pushPermission))
                return false;

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



        if (args[0].equalsIgnoreCase("pop")) {
            if(checkPermission(sender,popPermission))
                return false;

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

            //      ユーザーコマンドかチェック
            if (sender instanceof Player){
                Player p = (Player) sender;
                plugin.pop(p);
                return true;
            }

            return true;
        }


            showHelp(sender);

        return true;
    }


    //
    void showHelp(CommandSender p){
        if(checkPermission(p,helpPermission))
            return ;

        p.sendMessage("§e==============§d●§f●§a●§e Man10 Kit Plugin §d●§f●§a●§e===============");
        p.sendMessage("" +
                "/mkit list 登録済みのキットを表示\n" +
                "/mkit load [キット名] キットをロードする(いまのインベントリは消えます)\n" +
                "/mkit save [キット名] 現在のインベントリを保存する\n" +
                "/mkit set [キット名] 現在のインベントリを保存する\n" +
                "/mkit delete [キット名] 登録済みのキットを削除する\n" +
                "/mkit push 現在のインベントリを瞬間保存する\n" +
                "/mkit pop 瞬間保存したキットを復元する\n"+
                "§e§lhttp://man10.red by takatronix\n"
        );
    }

}


