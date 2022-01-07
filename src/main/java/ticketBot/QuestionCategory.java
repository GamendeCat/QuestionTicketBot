package ticketBot;

import java.util.List;
import java.util.Set;

public class QuestionCategory {

    private String uniCode;
    private List<String> questions;
    private String name;

    public QuestionCategory(String uniCode, List<String> questions, String name) {
        this.uniCode = uniCode;
        this.questions = questions;
        this.name = name;
    }
    public List<String> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }

    public String getUniCode() {
        return uniCode;
    }
}
