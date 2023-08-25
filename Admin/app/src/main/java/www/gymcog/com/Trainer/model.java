package www.gymcog.com.Trainer;

public class model
{
  String name,phone,email,pass,upi_id,salary,lastPlan,plan_valid_till;
    public model()
    {

    }

    public model(String name, String phone, String email, String pass, String upi_id, String salary, String lastPlan, String valid_till) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.upi_id=upi_id;
        this.salary=salary;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }

    public String getlastPlan() {
        return lastPlan;
    }

    public void setlastPlan(String lastPlan) {
        this.lastPlan = lastPlan;
    }

    public String getPlan_valid_till() {
        return plan_valid_till;
    }

    public void setPlan_valid_till(String plan_valid_till) {
        this.plan_valid_till = plan_valid_till;
    }
}