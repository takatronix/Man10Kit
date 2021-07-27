package red.man10;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.security.auth.login.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Man10Kit extends JavaPlugin {

    @Override
    public void onEnable() {

        // Plugin startup logic
      //  getServer().getPluginManager().registerEvents (this,this);
        //
        getCommand("mkit").setExecutor(new Man10KitCommand(this));
        getCommand("kit").setExecutor(new Man10KitCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    //      キットを削除
    public boolean delete(Player p, String kitName){
        String fileName = kitName;
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Kits");
        File f = new File(userdata, File.separator + fileName + ".yml");

        if(f.delete()){
            serverMessage(p.getDisplayName()+"はkit: "+kitName+" を削除した");
            return false;
        }else{
            serverMessage(p.getDisplayName()+"はkit: "+kitName+" の削除に失敗した");

            return false;
        }
    }
    //      キットを保存する
    public boolean push(Player p){

        PlayerInventory inv= p.getInventory();
        //String kitString = toBase64(inv);

        String fileName = p.getUniqueId().toString();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);

        f.delete();

        if (!f.exists()) {
            try {
                data.set("creator", p.getName());
                data.set("inventory",p.getInventory().getContents());
                data.set("armor",p.getInventory().getArmorContents());
                data.save(f);
            } catch (IOException exception) {
                p.sendMessage("キットの保存に失敗した");
                exception.printStackTrace();
                return false;
            }
        }

        //serverMessage(""+p.getName()+ ":ユーザーデータを保存した:"+fileName);

        return true;
    }
    //      キットを保存する
    public boolean save(Player p, String kitName){

        PlayerInventory inv= p.getInventory();
      //  String kitString = toBase64(inv);

        String fileName = kitName;
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Kits");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);


        if (!f.exists()) {
            try {

                data.set("creator", p.getName());
                data.set("inventory",p.getInventory().getContents());
                data.set("armor",p.getInventory().getArmorContents());
                data.save(f);
            } catch (IOException exception) {
                p.sendMessage("キットの保存に失敗した");
                exception.printStackTrace();
                return false;
            }
        }

        //serverMessage(""+p.getName()+ ":キットを保存した:"+kitName);

        return true;
    }

    //      キット
    public boolean list(CommandSender p) {

        File folder = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Kits");

        p.sendMessage("§e§l========== 登録済みのキット =========");
        int n = 1;
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isFile()){
                String filename = f.getName();
                //      隠しファイルは無視
                if(filename.substring(0,1).equalsIgnoreCase(".")){
                    continue;
                }
                int point = filename.lastIndexOf(".");
                if (point != -1) {
                    filename =  filename.substring(0, point);
                }
                p.sendMessage( "§e§l"+n +": §f§l" + filename);
                n++;
            }
        }

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


        Object a = data.get("inventory");
        Object b = data.get("armor");

        if(a == null || b == null){
            p.sendMessage("No saved inventory to load");
            return true;
        }
        ItemStack[] inventory = null;
        ItemStack[] armor = null;
        if (a instanceof ItemStack[]){
            inventory = (ItemStack[]) a;
        } else if (a instanceof List){
            List lista = (List) a;
            inventory = (ItemStack[]) lista.toArray(new ItemStack[0]);
        }
        if (b instanceof ItemStack[]){
            armor = (ItemStack[]) b;
        } else if (b instanceof List){
            List listb = (List) b;
            armor = (ItemStack[]) listb.toArray(new ItemStack[0]);
        }
        p.getInventory().clear();
        p.getInventory().setContents(inventory);
        p.getInventory().setArmorContents(armor);



        return true;
    }
    //      キットを読み込む
    public boolean pop(Player p){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Man10Kit").getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            p.sendMessage("ユーザーデーターは存在しない:"+p.getName());
            return false;
        }


        Object a = data.get("inventory");
        Object b = data.get("armor");

        if(a == null || b == null){
            p.sendMessage("No saved inventory to load");
            return true;
        }
        ItemStack[] inventory = null;
        ItemStack[] armor = null;
        if (a instanceof ItemStack[]){
            inventory = (ItemStack[]) a;
        } else if (a instanceof List){
            List lista = (List) a;
            inventory = (ItemStack[]) lista.toArray(new ItemStack[0]);
        }
        if (b instanceof ItemStack[]){
            armor = (ItemStack[]) b;
        } else if (b instanceof List){
            List listb = (List) b;
            armor = (ItemStack[]) listb.toArray(new ItemStack[0]);
        }
        p.getInventory().clear();
        p.getInventory().setContents(inventory);
        p.getInventory().setArmorContents(armor);

        return true;
    }

    boolean saveUserKit(Player p, String kitName){

        String kitString = toBase64(p.getInventory());


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
    /**
     * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
     *
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    /**
     *
     * A method to serialize an {@link ItemStack} array to Base64 String.
     *
     * <p />
     *
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to serialize an inventory to Base64 string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     *
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException
     */
    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     *
     * <p />
     *
     * Base off of {@link #fromBase64(String)}.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }


}
