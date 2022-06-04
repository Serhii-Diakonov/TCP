package metroBank.stores;

import java.io.Serializable;
public class MetroCard implements Serializable {

    private String id;      //must be unique
    private User user;
    private String college;
    private double balance;

    public MetroCard() {
        setAutoId();
        user = new User();
        college = "Невідомо";
        balance = 0;
    }

    public MetroCard(String id, User us, String col, double blnc) {
        super();
        if (id != null && !id.isEmpty()) this.id = id;
        if (us != null) user = us;
        if (col != null && !col.isEmpty()) college = col;
        balance = blnc;
    }

    public double getBalance() {
        return balance;
    }

    public String getCollege() {
        return college;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setBalance(double balance) {
        Double buf= balance;
        if(buf!=null)this.balance = balance;
    }

    public void setCollege(String college) {
        if (college != null && !college.isEmpty())
            this.college = college;
    }

    public void setId(String id){
        if(id!=null&&!id.isEmpty())this.id=id;
    }

    public void setAutoId() {
        Character A=(char)(Math.random() * 25 + 65);
        Character b=(char)(Math.random() * 25 + 97);
        id= A.toString()+ b.toString()+ this.hashCode();
    }

    public void setUser(User user) {
        if (user != null) this.user = user;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Власник: " + user.toString() + "\n" +
                "Заклад: " + college + "\n" +
                "Баланс: " + balance;
    }
}
