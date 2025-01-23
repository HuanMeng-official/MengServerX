package me.huanmeng.command;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static me.huanmeng.util.Abbreviations.M;
import static me.huanmeng.MengServerX.getMessage;

public class OllamaChat implements CommandExecutor {
    private final FileConfiguration config;

    public OllamaChat(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("msx_ollama")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("no-message"));
                return true;
            }
            String message = args[0];

            String ollamaUrl = config.getString("ollama.url");
            String model = config.getString("ollama.model");
            String promots = config.getString("ollama.prompts");

            new Thread(() -> {
                try {
                    URL url = new URL(ollamaUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setDoOutput(true);
                    String jsonInputString = "{ \"model\": \"" + model + "\", \"prompt\": \"" + promots + message + "\", \"stream\": false }";
                    byte[] inputBytes = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    connection.getOutputStream().write(inputBytes);
                    connection.getOutputStream().flush();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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
                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reply") + playerMessage);
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("failure") + responseCode);
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("error") + e.getMessage());
                }
            }).start();
        }
        return true;
    }
}
