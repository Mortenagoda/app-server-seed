package dk.vandborgandersen.life.model;

/**
 * Item in a shopping list.
 *
 * @author mortena@gmail.com
 */

public class ShoppingListItem {
    private String name;
    private String identifier;
    private boolean purchased;
    private int amount;
    private boolean deleted;
    private int internalCount;

    public ShoppingListItem(String name, boolean purchased, int amount) {
        this.name = name;
        this.purchased = purchased;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getInternalCount() {
        return internalCount;
    }

    public void setInternalCount(int internalCount) {
        this.internalCount = internalCount;
    }

    public void initIdentifier() {
        if (internalCount > 0) {
            identifier = name + "_" + internalCount;
        } else {
            identifier = name;
        }
    }

    public String getIdentifier() {
        return identifier;
    }
}
