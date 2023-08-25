package www.gymcog.trainer_n_client.gymcog.Trainer.Feedback;

public class Feedback_Model {

    public Feedback_Model(){

    }

    String name,feedback,phone;

    public Feedback_Model(String name, String feedback, String phone) {
        this.name = name;
        this.feedback = feedback;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
