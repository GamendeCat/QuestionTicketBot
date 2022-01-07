package ticketBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import ticketBot.util.Utilities;

public class AddTicketCommand extends Command {

    public AddTicketCommand() {
        super.name = "add";
        super.arguments = "<user-id>";
        super.help = "Adds a player to a ticket.";
    }


    @Override
    protected void execute(CommandEvent e) {
        if(!e.getTextChannel().getTopic().contains("Ticket")) return;

        if (e.getArgs().trim().split(" ").length != 1) {
            e.reply(Utilities.getDefaultEmbed("Ticket => Error", "Please make sure you specify the ID of the user you want to add to this ticket.").build());
            return;
        }

        Member member = e.getGuild().retrieveMemberById(e.getArgs()).complete();
        if (member == null) {
            e.reply(Utilities.getDefaultEmbed("Ticket => Error", "The user with the ID **" + e.getArgs() + "** doesn't exist.").build());
            return;
        }

        e.getTextChannel().getManager().putPermissionOverride(member, 3072L, 8192L).queue();
        e.reply(Utilities.getDefaultEmbed("Ticket => Added", "The user " + member.getAsMention() + " was added to the ticket").build());
    }
}
