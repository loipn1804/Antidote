package antidote;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table OBJECT_FAQ.
 */
public class ObjectFaq {

    private Long id;
    private Long userID;
    private String question;
    private String answer;

    public ObjectFaq() {
    }

    public ObjectFaq(Long id) {
        this.id = id;
    }

    public ObjectFaq(Long id, Long userID, String question, String answer) {
        this.id = id;
        this.userID = userID;
        this.question = question;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}