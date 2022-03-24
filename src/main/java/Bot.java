import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import ticketBot.ConfigFile;
import ticketBot.commands.*;
import ticketBot.events.ReactionEvent;
import ticketBot.events.ShutdownListener;
import ticketBot.util.DataUtility;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Bot extends ListenerAdapter {

    private static ConfigFile config;
    private static JDA jda;

    public static void main(String[] args) throws LoginException, IOException, InterruptedException {
        InputStream inputStream = Bot.class.getResourceAsStream("config.yml");

        Yaml yaml = new Yaml();
        Map<String, Object> obj = yaml.load(inputStream);
        config = new ConfigFile(obj);

        inputStream.close();

        DataUtility data = new DataUtility("data.yml", Bot.class);

        CommandClientBuilder builder = new CommandClientBuilder();

        EventWaiter waiter = new EventWaiter();

        // Set your bot's prefix
        builder.setPrefix(config.getPrefix());
        builder.setOwnerId(config.getOwner());

        // Add commands
        //builder.addCommand(new CoolCommand());
        builder.addCommands(new AddTicketCommand(), new DeleteTicketCommand(), new RemoveTicketCommand(), new TicketEmbed());

        CommandClient client = builder.build();

        jda = JDABuilder.create("Token",
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS)
                .setMemberCachePolicy(MemberCachePolicy.PENDING)
                .addEventListeners(new ReactionEvent(), new ShutdownListener(Bot.class))
                .setActivity(Activity.playing("Being Pog As Always :)"))
                .addEventListeners(waiter, client)
                .build();

        jda.awaitReady();

        ConfigFile.setJda(jda);

        TextChannel textchannel = jda.getTextChannelById("928798990005313627");

        textchannel.retrieveMessageById(ConfigFile.getEmbedId()).queue();

        int MINUTES = 1;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Pog Fucking Champ");
                System.out.println(DataUtility.getIds());
                DataUtility.write("data.yml", Bot.class);
            }
        }, 0, 1000 * 60 * MINUTES);
    }



    public static JDA getJda() {
        return jda;
    }
}
