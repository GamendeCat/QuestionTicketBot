package ticketBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import ticketBot.util.DiscordHtmlTranscripts;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFile {

    private static String prefix;
    private static String owner;
    private static String botPrefix;

    private static String embedColor;
    private static List<String> supportRoles;

    private static String ticketChannelId;

    public static String getTicketChannelId() {
        return ticketChannelId;
    }

    private static String logChannelId;
    private static TextChannel logChannel;

    public static TextChannel getLogChannel() {
        return logChannel;
    }

    private static Map<String, String> categories = new HashMap<>();

    private static int ticketCloseCountdown;

    private static String embedId;

    private static EmbedBuilder typeOfTicketEmbed;
    private static List<QuestionCategory> questionCategories = new ArrayList<>();

    public static String getEmbedId() {
        return embedId;
    }

    private static Map<String, Emote> emojiList = new HashMap<>();

    public static Map<String, Emote> getEmojiList() {
        return emojiList;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getOwner() {
        return owner;
    }

    public static String getBotPrefix() {
        return botPrefix;
    }

    public static String getEmbedColor() {
        return embedColor;
    }

    public static List<String> getSupportRoles() {
        return supportRoles;
    }

    public static Map<String, String> getCategories() {
        return categories;
    }

    public static int getTicketCloseCountdown() {
        return ticketCloseCountdown;
    }

    public static List<QuestionCategory> getQuestions() {
        return questionCategories;
    }

    public boolean iscUOrRn() {
        return cUOrRn;
    }

    public static EmbedBuilder getTypeOfTicketEmbed() {
        return typeOfTicketEmbed;
    }

    public static List<QuestionCategory> getQuestionCategories() {
        return questionCategories;
    }

    //private boolean countUp;
    //private boolean randomNumber;

    private boolean cUOrRn; // countUp = false || random number = true

    private static DiscordHtmlTranscripts transcript;

    public static DiscordHtmlTranscripts getTranscript() {
        return transcript;
    }

    public ConfigFile(Map<String, Object> map) {
        transcript = DiscordHtmlTranscripts.getInstance();
        prefix = (String) map.get("prefix");
        owner = (String) map.get("owner");
        botPrefix = (String) map.get("botPrefix");
        embedColor = (String) map.get("embedColor");
        supportRoles = (List<String>) map.get("supportRoles");
        String category1 = (String) map.get("category");
        ticketCloseCountdown = (int) map.get("ticketCloseCountdown");
        cUOrRn = (boolean) map.get("cUOrRn");
        embedId = (String) map.get("ticketEmbedId");
        ticketChannelId = (String) map.get("ticketChannelId");
        logChannelId = (String) map.get("logChannelId");

        for(String value : (List<String>) map.get("catagories")) {
             String[] values = value.split(":");
             String catagoryName = values[0];
             long id = Long.parseLong(values[1]);
             String emojiName = values[2];
             boolean animated = Boolean.parseBoolean(values[3]);
             String uniCode = values[4];
             if(values[5] != null) {
                 categories.put(catagoryName, values[5]);
             }else {
                 categories.put(catagoryName, category1);
             }
             Activity.Emoji emoji = new Activity.Emoji(emojiName, id, animated);

             emojiList.put(catagoryName, new Emote(emoji, uniCode));
        }

        Map<String, Map<String, List<String>>> map1 = (Map<String, Map<String, List<String>>>) map.get("questions");
        System.out.println(map1);

        List<String> list = new ArrayList<>();

        for(String key : map1.keySet()) {
            Map<String, List<String>> object = map1.get(key);

            System.out.println("" +  object.values().stream().findFirst().get());
            System.out.println(object.get("Questions"));
            System.out.println(key);
            QuestionCategory category = new QuestionCategory("" + object.values().stream().findFirst().get(), object.get("Questions"), key);
            questionCategories.add(category);
            list.add((object.values().stream().findFirst().get() + " for **" + key + "**"));
        }

        setupTypeOfTicket(list);
    }

    public void setupTypeOfTicket(List<String> list) {
        typeOfTicketEmbed = new EmbedBuilder();
        typeOfTicketEmbed.setColor(Color.decode("#de3e33"));
        typeOfTicketEmbed.setTitle("How can we help you today?");
        StringBuilder builder = new StringBuilder();
        builder.append("Please choose one of the following categories!\n\n");
        for(String i : list) {
            builder.append(i + "\n\n");
        }
        typeOfTicketEmbed.setDescription(builder.toString());
    }

    private static JDA jda;

    public static void setJda(JDA jda) {
        ConfigFile.jda = jda;

        logChannel = jda.getTextChannelById(logChannelId);
    }

    public static JDA getJda() {
        return jda;
    }
}
