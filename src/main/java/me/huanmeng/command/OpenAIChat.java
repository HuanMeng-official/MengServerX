package me.huanmeng.command;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static me.huanmeng.util.Abbreviations.M;
import static me.huanmeng.MengServerX.getMessage;

public class OpenAIChat implements CommandExecutor {
    private final FileConfiguration config;
    private final Gson gson = new Gson();
    private final Map<UUID, List<Message>> conversationHistory = new HashMap<>();

    public OpenAIChat(FileConfiguration config) {
        this.config = config;
    }

    static class Message {
        String role;
        String content;

        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    static class OpenAIRequest {
        String model;
        List<Message> messages;

        OpenAIRequest(String model, List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    static class OpenAIResponse {
        List<Choice> choices;

        static class Choice {
            Message message;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("player-command"));
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (command.getName().equalsIgnoreCase("msx_ai_reset")) {
            conversationHistory.remove(uuid);
            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reset-history"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("no-message"));
            return true;
        }

        String userMessage = String.join(" ", args);

        String openaiApiKey = config.getString("openai.api_key");
        String apiUrl = config.getString("openai.api_url", "https://api.openai.com/v1/chat/completions");
        String openaiModel = config.getString("openai.model");
        String systemPrompt = config.getString("openai.system_prompt", "");

        List<Message> history = conversationHistory.getOrDefault(uuid, new ArrayList<>());
        history.add(new Message("user", userMessage));

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
                connection.setDoOutput(true);

                List<Message> messages = new ArrayList<>();
                if (!systemPrompt.isEmpty()) {
                    messages.add(new Message("system", systemPrompt));
                }
                messages.addAll(history);

                OpenAIRequest requestBody = new OpenAIRequest(openaiModel, messages);
                String json = gson.toJson(requestBody);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder responseStr = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseStr.append(line);
                    }
                    reader.close();

                    OpenAIResponse response = gson.fromJson(responseStr.toString(), OpenAIResponse.class);
                    if (response.choices != null && !response.choices.isEmpty()) {
                        Message reply = response.choices.get(0).message;
                        history.add(reply);
                        conversationHistory.put(uuid, history);
                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reply") + reply.content);
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reply-empty"));
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("failure") + responseCode);
                }

                connection.disconnect();
            } catch (Exception e) {
                sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("error") + e.getMessage());
            }
        }).start();

        return true;
    }
}
