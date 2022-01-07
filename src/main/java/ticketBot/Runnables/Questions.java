package ticketBot.Runnables;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class Questions {


    private int number;
    private List<String> answers = new ArrayList<>();
    private List<String> list;
    private TextChannel channel;
    private boolean doneQuestions = false;
    private String toDeleteEmbed;

    public Questions(List<String> list, TextChannel channel) {
        this.channel = channel;
        this.list = list;
        this.number = 0;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getToDeleteEmbed() {
        return toDeleteEmbed;
    }

    public void setToDeleteEmbed(String toDeleteEmbed) {
        this.toDeleteEmbed = toDeleteEmbed;
    }

    public void addAnswer(String toAdd) {
        answers.add(toAdd);
    }

    public void setDoneQuestions(boolean doneQuestions) {
        this.doneQuestions = doneQuestions;
    }

    public boolean getDoneQuestions() {
        return doneQuestions;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public List<String> getList() {
        return list;
    }

    public TextChannel getChannel() {
        return channel;
    }
}
