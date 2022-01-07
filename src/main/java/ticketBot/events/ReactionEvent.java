package ticketBot.events;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.ChannelManager;
import ticketBot.*;
import ticketBot.Runnables.Questions;
import ticketBot.util.DataUtility;
import ticketBot.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ReactionEvent extends ListenerAdapter {

    private List<Questions> ticketList = new ArrayList<>();

    private Map<String, String> ticketCategory = ConfigFile.getCategories();
    private int suffixLength = 4;
    private List<String> supportRoles = ConfigFile.getSupportRoles();

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        System.out.println("yes");
        TextChannel textChannel = e.getChannel();

        String categoryName = null;
        System.out.println(textChannel.getId().equals(ConfigFile.getTicketChannelId()));
        System.out.println(textChannel.getId());
        System.out.println(ConfigFile.getTicketChannelId());
        if(textChannel.getId().equals(ConfigFile.getTicketChannelId())) {
            Emote emote = null;
            Map<String, Emote> map = ConfigFile.getEmojiList();
            for (String key : map.keySet()) {
                if (map.get(key).getUnicode().equals(e.getReaction().getReactionEmote().getName())) {
                    e.getReaction().removeReaction(e.getUser()).queue();
                    System.out.println("hi");
                    emote = map.get(key);
                    categoryName = key;

                    String ticketName = categoryName + "-" + e.getUser().getName();
                    String ticketTopic = "This Ticket has been made by " + e.getUser().getName() + " (" + e.getUser().getId() + ")";

                    TextChannel ticket = e.getGuild().createTextChannel(ticketName, e.getGuild().getCategoriesByName(ticketCategory.get(categoryName), true).get(0)).complete();

                    ticket.getManager().setTopic(ticketTopic);
                    // Allow only the user to see the ticket and disable viewing the ticket for all other members.
                    ChannelManager ticketManager = ticket.getManager().putPermissionOverride(e.getMember(), 3072L, 8192L)
                            .putPermissionOverride(e.getGuild().getRolesByName("@everyone", true).get(0), 0L, 1024L);

                    // Allow the support roles to view the ticket.
                    for (String supportRole : supportRoles) {
                        if (!e.getGuild().getRolesByName(supportRole, true).isEmpty()) {
                            ticketManager = ticketManager.putPermissionOverride(e.getGuild().getRolesByName(supportRole, true).get(0), 3072L, 8192L);
                        }
                    }
                    ticketManager.queue();

                    ticket.sendMessageEmbeds(ConfigFile.getTypeOfTicketEmbed().build()).queue(message -> {
                        for(QuestionCategory category : ConfigFile.getQuestionCategories()) {
                            message.addReaction(category.getUniCode()).queue();
                        }
                    });

                    e.getChannel().sendMessageEmbeds(Utilities.getDefaultEmbed("Ticket Opened", "Your ticket is " + ticket.getAsMention()).build()).queue(embed -> {
                        embed.delete().queueAfter(4, TimeUnit.SECONDS);
                    });
                    break;
                }
            }

            if (emote == null) return;

            textChannel.sendMessageEmbeds(ConfigFile.getTypeOfTicketEmbed().build());
            return;

        }else if(textChannel.getTopic().contains("Ticket")) {
            System.out.println("hi?");
            List<String> questions;

            if(e.getUser().isBot()) return;

            String emoji = e.getReaction().getReactionEmote().getName();

            for (QuestionCategory key : ConfigFile.getQuestions()) {
                System.out.println(key.getUniCode());
                System.out.println(e.getReaction().getReactionEmote().getName());
                if (key.getUniCode().equalsIgnoreCase(e.getReaction().getReactionEmote().getName())) {
                    e.getChannel().deleteMessageById(e.getMessageIdLong()).queue();
                    questions = key.getQuestions();
                    final Questions questions1 = new Questions( key.getQuestions(), textChannel);
                    ticketList.add(questions1);
                    textChannel.sendMessageEmbeds(Utilities.getDefaultEmbed("Question 1", questions.get(0)).build()).queue(message -> questions1.setToDeleteEmbed(message.getId()));
                    break;
                }else if(emoji.equals("🔒")) {
                    try {
                        InputStream inputStream = ConfigFile.getTranscript().generateFromMessages(textChannel.getIterableHistory().stream().collect(Collectors.toList()));

                        Utilities.closingTicketProcess(textChannel, e.getUser(), inputStream);
                        return;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else if(emoji.equals("👍")) {
                    String i = textChannel.getName().replace("ticket", "done");
                    textChannel.getManager().setName(i);
                }
            }
        }
    }


    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        TextChannel channel = e.getChannel();

        if(e.getAuthor().isBot()) return;

        for(Questions question : ticketList) {
            if(channel.equals(channel)) {
                question.setNumber((question.getNumber() + 1));
                System.out.println(question.getNumber());

                try {
                    String answer = e.getMessage().getContentRaw();

                    e.getChannel().deleteMessageById(e.getMessageIdLong()).queue();

                    e.getChannel().deleteMessageById(question.getToDeleteEmbed()).queue();

                    question.addAnswer(answer);

                    String someQuestion = question.getList().get(question.getNumber());

                    channel.sendMessageEmbeds(Utilities.getDefaultEmbed("Question " + (question.getNumber() + 1), someQuestion).build()).queue(message -> question.setToDeleteEmbed(message.getId()));
                    break;
                }catch(IndexOutOfBoundsException exception) {
                    ticketList.remove(question);
                    StringBuilder stringBuilder = new StringBuilder();
                    List<String> answers = question.getAnswers();
                    List<String> questions = question.getList();
                    for(int i = 0; i < questions.size(); i++) {
                        stringBuilder.append("**Question:** " + questions.get(i) + "\n **Answer:** " + answers.get(i) + "\n\n");
                    }

                    channel.sendMessageEmbeds(Utilities.getDefaultEmbed("Question Answers", stringBuilder.toString()).build()).queue(message -> {
                        message.addReaction("🔒").queue();
                        message.addReaction("👍").queue();
                        DataUtility.addId(channel.getId(), message.getId());
                    });

                    break;
                }
            }
        }
    }
}
