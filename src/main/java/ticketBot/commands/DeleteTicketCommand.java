package ticketBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import ticketBot.ConfigFile;
import ticketBot.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DeleteTicketCommand extends Command {

    private int closeCooldown = ConfigFile.getTicketCloseCountdown();

    public DeleteTicketCommand() {
        super.name = "close";
        super.help = "Close an open ticket.";
    }

    @Override
    protected void execute(CommandEvent e) {
        // Ignore the command if the channel in which this command was execute is not a ticket.
        InputStream inputStream = null;
        try {
            inputStream = ConfigFile.getTranscript().generateFromMessages(e.getTextChannel().getIterableHistory().stream().collect(Collectors.toList()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Utilities.closingTicketProcess(e.getTextChannel(), e.getAuthor(), inputStream);
    }
}
