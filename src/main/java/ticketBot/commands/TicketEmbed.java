package ticketBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import ticketBot.ConfigFile;
import ticketBot.Emote;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketEmbed extends Command {

    private Map<String, Emote> emojiMap = ConfigFile.getEmojiList();

    public TicketEmbed() {
        super.name = "ticketembed";
        super.hidden = true;
        super.requiredRole = "Staff";
    }



    @Override
    protected void execute(CommandEvent e) {
        TextChannel channel = e.getTextChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        String titleEmoji = "<:ticket:923905867869454367>";
        embedBuilder.setTitle(titleEmoji + " GamendeCat's Ticket System");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("If you need help from the staff team please open a ticket by reacting to the topic\n your issue falls under below! \n\n ");
        List<String> list = new ArrayList<>();
        for(String key : emojiMap.keySet()) {
            list.add(key);
        }

        System.out.println(list);
        for(String key : list) {
            Activity.Emoji emoji = emojiMap.get(key).getEmoji();
            stringBuilder.append(emoji.getAsMention() + " for **" + key + "** \n\n");
        }
        stringBuilder.append("\n");
        embedBuilder.setDescription(stringBuilder.toString());
        embedBuilder.setFooter("Upon reacting, a channel will be created where you will be assisted by the staff team\n after completing a short form.");
        embedBuilder.setColor(Color.decode(ConfigFile.getEmbedColor()));

        channel.sendMessageEmbeds(embedBuilder.build()).queue(message -> {
            for(String key : list) {
                String emoji = emojiMap.get(key).getUnicode();
                message.addReaction(emoji).queue();
            }
        });
    }
}
