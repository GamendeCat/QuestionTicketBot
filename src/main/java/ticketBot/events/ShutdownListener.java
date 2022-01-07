package ticketBot.events;

import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ticketBot.util.DataUtility;

public class ShutdownListener extends ListenerAdapter {

    private Class clazz;

    public ShutdownListener(Class clazz){
        this.clazz = clazz;
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println("POG FUCKING CHAMP" + event.getTimeShutdown());
        super.onShutdown(event);
        event.getTimeShutdown();

        //DataUtility.write("data.yml", clazz);
    }
}
