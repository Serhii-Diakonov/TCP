package metroBank.operations;

import metroBank.stores.MetroCard;

public class AddMetroCardOperation extends CardOperation{
    private MetroCard card=null;

    public AddMetroCardOperation(){card=new MetroCard();}

    public MetroCard getCard() {
        return card;
    }
}
