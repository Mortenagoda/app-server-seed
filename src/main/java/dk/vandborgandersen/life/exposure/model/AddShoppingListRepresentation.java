package dk.vandborgandersen.life.exposure.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation used for adding a new shopping list.
 *
 * @author mortena@gmail.com
 */
@XmlRootElement
public class AddShoppingListRepresentation {
    private String name;

    public AddShoppingListRepresentation() {
    }

    public AddShoppingListRepresentation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
