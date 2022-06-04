package metroBank.operations;

public class ShowInfoOperation extends CardOperation {
    private String id = "-1";

    public ShowInfoOperation(String id) {
        if (id != null && !id.isEmpty())
            this.id = id;
    }

    public String getId() {
        return id;
    }
}
