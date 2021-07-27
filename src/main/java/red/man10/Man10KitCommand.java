package red.man10;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

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
            showErrorMessage(sender,permissionError);
            return true;
        }

        return false;
    }
    void showErrorMessage(CommandSender sender,String message){
        sender.sendMessage("§4"+message);
        Bukkit.getLogger().log(Level.WARNING,message);
    }
    void showMessage(CommandSender sender,String message){
        sender.sendMessage("§5"+message);
    }

    public Man10KitCommand(Man10Kit plugin) {
        this.plugin = plugin;
        this.plugin.command = this;
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
                showErrorMessage(sender,"/mkit delete [キット名]");
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
                showErrorMessage(sender,"/mkit set [ユーザー名] [キット名]");
                return false;
            }

            Player t = Bukkit.getPlayer(args[1]);
            if(t.isOnline() == false){
                showErrorMessage(sender,t.getName() +"はオンラインではありません");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.load(t,args[2]);
            });
            return true;
        }

        // Pop
        if (args[0].equalsIgnoreCase("pop")) {
            if(checkPermission(sender,popPermission))
                return false;

            //    引数がある場合
            if (args.length == 2) {
                String name = args[1];
                Player target = Bukkit.getPlayer(name);
                if(target == null){
                    showErrorMessage(sender,name+"はオフラインです");
                    sender.sendMessage(name+"はオフラインです");
                    return false;
                }
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                    plugin.pop(target);
                });
                return true;
            }

            if (sender instanceof Player){
                Player p = (Player) sender;
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                    plugin.pop(p);
                });
                return true;
            }

            return true;
        }


        //  Pushユーザーデータを保存
        if (args[0].equalsIgnoreCase("push")) {
            if(checkPermission(sender,pushPermission))
                return false;

            //    引数がある場合(サーバor外部コマンド)
            if (args.length == 2) {
                String name = args[1];
                Player p = Bukkit.getPlayer(name);
                if(p == null){
                    showErrorMessage(sender,name+"はオフラインです");
                    return false;
                }

                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                    plugin.push(p);
                });
                return true;
            }

            //      ユーザーコマンド
            if (sender instanceof Player){
                Player p = (Player) sender;
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                    plugin.push(p);
                });

                return true;
            }
            return true;
        }





        //      以下はプレイヤー専用コマンド


        if (sender instanceof Player == false){
            showErrorMessage(sender,"プレイヤーのみのコマンドになります");
            return false;
        }
        Player player = (Player)sender;

        /////////////////////////////////////////
        //  Saveコマンド
        if (args[0].equalsIgnoreCase("save")) {
            if(checkPermission(sender,savePermission))
                return false;

            if (args.length != 2) {
                showErrorMessage(sender,"/mkit save [キット名]");
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
                showErrorMessage(sender,"/mkit load [キット名]");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                plugin.load(player,args[1]);
            });
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
                "§e/mkit list §f登録済みのキットを表示\n" +
                "§e/mkit load [キット名] §fキットをロードする(いまのインベントリは消えます)\n" +
                "§e/mkit save [キット名] §f現在のインベントリを保存する\n" +
                "§e/mkit set [ユーザー名] [キット名] §fプレーヤーにキットを設定する\n" +
                "§e/mkit delete [キット名] §f登録済みのキットを削除する\n" +
                "§e/mkit push §f現在のインベントリを瞬間保存する\n" +
                "§e/mkit pop §f瞬間保存したキットを復元する\n"+
                "§e§lhttp://man10.red by takatronix\n"
        );
    }

}


