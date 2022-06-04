package metroBank.operations;

public class AddMoneyOperation extends CardOperation {
    private String id = "-1";
    private double money = 0;

    public AddMoneyOperation(String id, double mn) {
        if (id != null && !id.isEmpty()) {
            this.id = id;
            money = mn;
        }
    }

    public double getMoney() {
        return money;
    }

    public String getId() {
        return id;
    }

    public void setMoney(double mn) {
        money = mn;
    }

    public void setId(String id) {
        if (id != null && !id.isEmpty())
            this.id = id;
    }
}
