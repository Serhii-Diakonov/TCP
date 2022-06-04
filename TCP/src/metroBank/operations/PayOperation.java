package metroBank.operations;

public class PayOperation extends CardOperation{
    private String id="-1";
    private double money=0;

    public PayOperation(String id, double mn){
        if(id!=null&&!id.isEmpty()){
            this.id=id;
            money=mn;
        }
    }

    public String getId() {
        return id;
    }

    public double getMoney() {
        return money;
    }
}

