package metroBank.stores;

import metroBank.stores.MetroCard;

import java.util.ArrayList;

public class MetroCardBank {
    private ArrayList<MetroCard> store = new ArrayList<>();

    public ArrayList<MetroCard> getStore() {
        return store;
    }

    public void setStore(ArrayList<MetroCard> store) {
        if (store != null) this.store = store;
    }

    public int searchID(String id) {
        if (store != null && store.size() > 0) {
            for (MetroCard c : store)
                if (c.getId().equals(id))
                    return store.indexOf(c);
        }
        return -1;
    }

    public int numCards() {
        return store == null ? 0 : store.size();
    }

    public void addCard(MetroCard card) {
        if(!hasUniqueId(card))card.setAutoId();
        store.add(card);
    }

    private boolean hasUniqueId(MetroCard card){
        if(store!=null&&store.size()>0&&card!=null){
            for(MetroCard c:store){
                if(c.getId().equals(card.getId()))
                    return false;
            }
        }
        return true;
    }

    public boolean removeCard(String id) {
        if (store != null && store.size() > 0) {
            for (MetroCard c : store)
                if (c.getId().equals(id))
                    return store.remove(c);
        }
        return false;
    }

    public boolean addMoney(String id, double money) {
        if (store != null && store.size() > 0) {
            for (MetroCard c : store)
                if (c.getId().equals(id)) {
                    c.setBalance(c.getBalance() + money);
                    return true;
                }
        }
        return false;
    }

    public boolean getMoney(String id, double money) {
        if (store != null && store.size() > 0) {
            for (MetroCard c : store)
                if (c.getId().equals(id))
                    if (c.getBalance() - money >= 0) {
                        c.setBalance(c.getBalance() - money);
                        return true;
                    }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Список карток:\n");
        if(store!=null&&store.size()>0)
            for (MetroCard c : store)
            buf.append(c.toString()).append("\n\n");
        return buf.toString();
    }
}
