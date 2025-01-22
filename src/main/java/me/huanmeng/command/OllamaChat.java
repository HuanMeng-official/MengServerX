package me.huanmeng.command;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import static me.huanmeng.util.Abbreviations.M;

public class OllamaChat implements CommandExecutor {
    private JavaPlugin plugin = null;

    public OllamaChat() {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("msx_ollama")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "请提供要发送给 Ollama 的消息");
                return true;
            }

            String message = args[0];
            new Thread(() -> {
                try {
                    URL url = new URL(Objects.requireNonNull(plugin.getConfig().getString("OllamaUrl")));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    String model = plugin.getConfig().getString("OllamaModel");
                    String jsonInputString = "{ \"model\": \"" + model + "\", \"prompt\": \"" + message + "\", \"stream\": false }";
                    byte[] inputBytes = jsonInputString.getBytes(Objects.requireNonNull(plugin.getConfig().getString("OllamaEncoding")));
                    connection.getOutputStream().write(inputBytes);
                    connection.getOutputStream().flush();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        connection.disconnect();

                        String responseJsonString = response.toString();
                        Gson gson = new Gson();
                        HashMap<String, Object> responseJson = gson.fromJson(responseJsonString, HashMap.class);
                        String playerMessage = (String) responseJson.get("response");

                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "回应: " + playerMessage);
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "请求失败，状态码: " + responseCode);
                    }

                } catch (Exception e) {
                    sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "发生错误: " + e.getMessage());
                }
            }).start();
        }
        return true;
    }
}

