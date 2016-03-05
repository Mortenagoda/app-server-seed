package dk.vandborgandersen.life.exposure.model;

import dk.vandborgandersen.life.model.ShoppingList;

/**
 * Representation of a shopping list.
 *
 * @author mortena@gmail.com
 */

public class ShoppingListRepresentation {

    private String name;

    public ShoppingListRepresentation() {
    }

    public ShoppingListRepresentation(ShoppingList list) {
        this.name = list.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
