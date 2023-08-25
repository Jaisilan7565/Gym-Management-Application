package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Payments;

public class payment_model {

    public payment_model(){

    }

    String payment_id, payment_date, membership_type, amount;

    public payment_model(String payment_id, String payment_date, String membership_type, String amount) {
        this.payment_id = payment_id;
        this.payment_date = payment_date;
        this.membership_type = membership_type;
        this.amount = amount;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getMembership_type() {
        return membership_type;
    }

    public void setMembership_type(String membership_type) {
        this.membership_type = membership_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
