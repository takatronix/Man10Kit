package red.man10;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.security.auth.login.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public final class Man10Kit extends JavaPlugin {

    @Override
    public void onEnable() {

        // Plugin startup logic
      //  getServer().getPluginManager().registerEvents (this,this);
        //
        getCommand("mkit").setExecutor(new Man10KitCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    //      キットを保存する
    public boolean save(Player p, String kitName){

        String kitString = InventoryToBase64.toBase64(p.getInventory());

        String fileName = kitName;
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Kits");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);

        //When the player file is created for the first time...
        if (!f.exists()) {
            try {

                data.set("creator", p.getName());
                data.set("inventory",kitString);

                data.save(f);
            } catch (IOException exception) {
                p.sendMessage("キットの保存に失敗した");
                exception.printStackTrace();
                return false;
            }
        }

        p.sendMessage("キットを保存した:"+kitName);

        return true;
    }
    //      キットを読み込む
    public boolean load(Player p, String kitName){
        String fileName = kitName;
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Kits");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            p.sendMessage("キットは存在しない:"+kitName);
            return false;
        }

        String s = data.getString("inventory");
    //    p.sendMessage(s);
        try{
             Inventory inv = InventoryToBase64.fromBase64(s);

        }
        catch (IOException e) {
            p.sendMessage(e.getLocalizedMessage());
            return false;
        }
        p.sendMessage("キットを読み込んだ:"+kitName);

        return true;
    }
    boolean saveUserKit(Player p, String kitName){

        String kitString = InventoryToBase64.toBase64(p.getInventory());


        String fileName = kitName;
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "PlayerData");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);

        //When the player file is created for the first time...
        if (!f.exists()) {
            try {

                data.set("creator", p.getName());
                data.set("inventory",kitString);

                data.save(f);
            } catch (IOException exception) {
                serverMessage("キットの保存に失敗した");
                exception.printStackTrace();
                return false;
            }
        }

        serverMessage("キットの保存に成功した");
        return true;
    }


    //     サーバーメッセージ
    void serverMessage(String text){
        //command("say "+text);
        Bukkit.getServer().broadcastMessage(text);
    }




}
