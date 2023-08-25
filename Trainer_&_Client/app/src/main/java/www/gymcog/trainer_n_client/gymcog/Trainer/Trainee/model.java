package www.gymcog.trainer_n_client.gymcog.Trainer.Trainee;

public class model
{
  String name,phone,email,pass,lastPlan,plan_valid_till;
    public model()
    {

    }

    public model(String name, String phone, String email, String pass, String lastPlan, String valid_till) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.lastPlan=lastPlan;
        this.plan_valid_till=valid_till;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLastPlan() {
        return lastPlan;
    }

    public void setLastPlan(String lastPlan) {
        this.lastPlan = lastPlan;
    }

    public String getPlan_valid_till() {
        return plan_valid_till;
    }

    public void setPlan_valid_till(String plan_valid_till) {
        this.plan_valid_till = plan_valid_till;
    }

}