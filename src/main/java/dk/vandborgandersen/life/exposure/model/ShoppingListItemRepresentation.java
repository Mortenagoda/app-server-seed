package dk.vandborgandersen.life.exposure.model;

import javax.xml.bind.annotation.XmlRootElement;

import dk.vandborgandersen.life.model.ShoppingListItem;

/**
 * A list item.
 *
 * @author mortena@gmail.com
 */
@XmlRootElement
public class ShoppingListItemRepresentation {
    private String name;
    private int amount;
    private boolean purchased;

    public ShoppingListItemRepresentation(String name, int amount, boolean purchased) {
        this.name = name;
        this.amount = amount;
        this.purchased = purchased;
    }

    public ShoppingListItemRepresentation(ShoppingListItem item) {
        this.name = item.getName();
        this.amount = item.getAmount();
        this.purchased = item.isPurchased();
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
