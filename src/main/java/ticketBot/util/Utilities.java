package ticketBot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import ticketBot.ConfigFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class Utilities {
    /**
     * The characters from which a suffix is to be generated.
     *
     * {@link Utilities#getSuffix(int)}
     */
    private static final String TICKET_SUFFIX_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Returns the template of the embed to be used in the messages sent by the bot.
     *
     * @param title The title of the embed sent in discord.
     * @param description The description of the embed sent in discord.
     * @return EmbedBuilder containing the title and description, with the colour.
     */
    public static EmbedBuilder getDefaultEmbed(String title, String description) {
        return new EmbedBuilder().setTitle(title).setDescription(description).setColor(Color.decode("#de3e33"));
    }

    public static String getSuffix(int length) {
        StringBuilder builder = new StringBuilder();
        while (length > 0) {
            builder.append(TICKET_SUFFIX_CHARS.charAt((int)(Math.random() * TICKET_SUFFIX_CHARS.length())));
            length--;
        }

        return builder.toString().trim().isEmpty() ? null : builder.toString();
    }

    public static void sendMessageToLogChannel(User ticketUser, User ticketCloser, InputStream inputStream, TextChannel textChannel) {
        String name = "Undefined";
        String id = "Undefined";
        String tag = "@Undefined";
        if(ticketUser != null) {
            name = ticketUser.getName();
            id = ticketUser.getId();
            tag = ticketUser.getAsTag();
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(new Color(65, 60, 233));
        embedBuilder.setTitle(textChannel.getName() + "'s Log");
        embedBuilder.addField("User: ", name + " (" + tag + ")", false);
        embedBuilder.addField("User Id: ", id, false);
        embedBuilder.addField("Closer: ", ticketCloser.getName() + " (" + ticketCloser.getAsTag() + ")", false);
        embedBuilder.addField("Closer Id", ticketCloser.getId(), false);
        embedBuilder.setThumbnail(ticketUser.getAvatarUrl());

        ConfigFile.getLogChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        ConfigFile.getLogChannel().sendFile(inputStream, textChannel.getName() + " (" + ticketUser.getId() + ")" + ".html").queue();

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closingTicketProcess(TextChannel textChannel, User otherUser, InputStream inputStream) {
        String s = textChannel.getTopic();
        String requiredString = s.substring(s.indexOf("(") + 1, s.indexOf(")"));

        User user = ConfigFile.getJda().getUserById(requiredString);
        sendMessageToLogChannel(user, otherUser, inputStream, textChannel);

        textChannel.sendMessageEmbeds(Utilities.getDefaultEmbed("Closing Ticket", "Closing this ticket in " + ConfigFile.getTicketCloseCountdown() + " second(s).").build()).queue();
        textChannel.delete().queueAfter(ConfigFile.getTicketCloseCountdown(), TimeUnit.SECONDS);

        // Making sure they can't run this event again!
        textChannel.getManager().setTopic(" ");

        DataUtility.removeId(textChannel.getId());
    }
}
