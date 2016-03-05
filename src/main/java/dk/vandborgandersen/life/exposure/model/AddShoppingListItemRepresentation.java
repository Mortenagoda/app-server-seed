package dk.vandborgandersen.life.exposure.model;

/**
 * Representation used for adding a new item.
 *
 * @author mortena@gmail.com
 */

public class AddShoppingListItemRepresentation {
    private String name;
    private int amount;
    private boolean purchased;

    public AddShoppingListItemRepresentation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}
