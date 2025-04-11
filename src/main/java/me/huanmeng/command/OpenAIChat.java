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

// 方案二

//package me.huanmeng.command;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import org.bukkit.ChatColor;
//import org.bukkit.command.*;
//        import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.jetbrains.annotations.NotNull;
//
//import java.io.*;
//        import java.lang.reflect.Type;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//        import static me.huanmeng.util.Abbreviations.M;
//import static me.huanmeng.MengServerX.getMessage;
//
//public class OpenAIChat implements CommandExecutor {
//    private final FileConfiguration config;
//    private final Gson gson = new Gson();
//    private final Map<UUID, List<Message>> conversationHistory = new HashMap<>();
//    private final File historyDir;
//
//    public OpenAIChat(FileConfiguration config) {
//        this.config = config;
//        this.historyDir = new File("plugins/MengServerX/conversations/");
//        if (!historyDir.exists()) {
//            historyDir.mkdirs();
//        }
//    }
//
//    static class Message {
//        String role;
//        String content;
//
//        Message(String role, String content) {
//            this.role = role;
//            this.content = content;
//        }
//    }
//
//    static class OpenAIRequest {
//        String model;
//        List<Message> messages;
//
//        OpenAIRequest(String model, List<Message> messages) {
//            this.model = model;
//            this.messages = messages;
//        }
//    }
//
//    static class OpenAIResponse {
//        List<Choice> choices;
//
//        static class Choice {
//            Message message;
//        }
//    }
//
//    private File getHistoryFile(Player player) {
//        return new File(historyDir, player.getName() + "_history.json");
//    }
//
//    private List<Message> loadHistory(Player player) {
//        File file = getHistoryFile(player);
//        if (file.exists()) {
//            try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
//                Type listType = new TypeToken<List<Message>>() {}.getType();
//                return gson.fromJson(reader, listType);
//            } catch (Exception ignored) {
//            }
//        }
//        return new ArrayList<>();
//    }
//
//    private void saveHistory(Player player, List<Message> history) {
//        try (Writer writer = new OutputStreamWriter(new FileOutputStream(getHistoryFile(player)), StandardCharsets.UTF_8)) {
//            gson.toJson(history, writer);
//        } catch (IOException e) {
//            player.sendMessage(ChatColor.RED + "聊天记录保存失败：" + e.getMessage());
//        }
//    }
//
//    private void deleteHistory(Player player) {
//        File file = getHistoryFile(player);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
//
//    @Override
//    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("player-command"));
//            return true;
//        }
//
//        Player player = (Player) sender;
//        UUID uuid = player.getUniqueId();
//
//        if (command.getName().equalsIgnoreCase("msx_ai_reset")) {
//            conversationHistory.remove(uuid);
//            deleteHistory(player);
//            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reset-history"));
//            return true;
//        }
//
//        if (args.length == 0) {
//            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("no-message"));
//            return true;
//        }
//
//        String userMessage = String.join(" ", args);
//
//        String openaiApiKey = config.getString("openai.api_key");
//        String apiUrl = config.getString("openai.api_url", "https://api.openai.com/v1/chat/completions");
//        String openaiModel = config.getString("openai.model");
//        String systemPrompt = config.getString("openai.system_prompt", "");
//
//        // 获取或加载历史记录
//        List<Message> history = conversationHistory.computeIfAbsent(uuid, k -> loadHistory(player));
//        history.add(new Message("user", userMessage));
//
//        final List<Message> historyRef = history;
//
//        new Thread(() -> {
//            try {
//                URL url = new URL(apiUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                connection.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
//                connection.setDoOutput(true);
//
//                List<Message> messages = new ArrayList<>();
//                if (!systemPrompt.isEmpty()) {
//                    messages.add(new Message("system", systemPrompt));
//                }
//                messages.addAll(historyRef);
//
//                OpenAIRequest requestBody = new OpenAIRequest(openaiModel, messages);
//                String json = gson.toJson(requestBody);
//
//                try (OutputStream os = connection.getOutputStream()) {
//                    os.write(json.getBytes(StandardCharsets.UTF_8));
//                }
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == 200) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
//                    StringBuilder responseStr = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        responseStr.append(line);
//                    }
//
//                    OpenAIResponse response = gson.fromJson(responseStr.toString(), OpenAIResponse.class);
//                    if (response.choices != null && !response.choices.isEmpty()) {
//                        Message reply = response.choices.get(0).message;
//                        historyRef.add(reply);
//                        saveHistory(player, historyRef);
//                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reply") + reply.content);
//                    } else {
//                        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("reply-empty"));
//                    }
//                } else {
//                    sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("failure") + responseCode);
//                }
//
//                connection.disconnect();
//            } catch (Exception e) {
//                sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("error") + e.getMessage());
//            }
//        }).start();
//
//        return true;
//    }
//}
