package ticketBot;

import net.dv8tion.jda.api.entities.Activity;

public class Emote {

    public Emote(Activity.Emoji emoji, String unicode) {
        this.emoji = emoji;
        this.unicode = unicode;
    }

    private Activity.Emoji emoji;
    private String unicode;

    public Activity.Emoji getEmoji() {
        return emoji;
    }

    public String getUnicode() {
        return unicode;
    }
}
