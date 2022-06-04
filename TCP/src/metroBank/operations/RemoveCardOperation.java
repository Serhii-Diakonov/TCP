package metroBank.operations;

import metroBank.operations.CardOperation;

public class RemoveCardOperation extends CardOperation {
    private String id="-1";

    public RemoveCardOperation(String id){
        super();
        if(id!=null&&!id.isEmpty())this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id!=null&&!id.isEmpty())this.id = id;
    }
}
