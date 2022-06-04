package metroBank.operations;

public class ShowBalanceOperation extends CardOperation{
    private String id="-1";

    public ShowBalanceOperation(String id){
        if(id!=null&&!id.isEmpty())
            this.id=id;
    }

    public String getId() {
        return id;
    }
}
