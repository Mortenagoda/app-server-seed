package dk.vandborgandersen.life.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;

/**
 * A shopping list.
 *
 * @author mortena@gmail.com
 */

public class ShoppingList {

    @Id
    private String tId;
    private String name;
    private String listNameSimplified;
    private List<ShoppingListItem> items;
    private String userTId;
    private String listIdentifier;

    public ShoppingList() {
    }

    public List<ShoppingListItem> findItemsForName(String name) {
        if (items != null) {
            return items.stream().filter(
                shoppingListItem -> !shoppingListItem.isDeleted() && shoppingListItem.getName().equalsIgnoreCase(name)).collect(
                Collectors.toList());
        }
        return Collections.emptyList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShoppingListItem> getItems() {
        return items == null ? new ArrayList<>() : Collections.unmodifiableList(items);
    }

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
    }

    public List<ShoppingListItem> getItemsNotDeleted() {
        return getItems().stream().filter(shoppingListItem -> !shoppingListItem.isDeleted()).collect(Collectors.toList());
    }

    public void addItem(ShoppingListItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items.add(item);
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getUserTId() {
        return userTId;
    }

    public void setUserTId(String userTId) {
        this.userTId = userTId;
    }

    public String getListNameSimplified() {
        return listNameSimplified;
    }

    public void setListNameSimplified(String listNameSimplified) {
        this.listNameSimplified = listNameSimplified;
    }

    public String getListIdentifier() {
        return listIdentifier;
    }

    public void setListIdentifier(String listIdentifier) {
        this.listIdentifier = listIdentifier;
    }

    public Optional<ShoppingListItem> findItemForIdentifier(String itemIdentifier) {

        Optional<ShoppingListItem> found = null;

        if (items != null) {
            for (ShoppingListItem item : items) {
                if (!item.isDeleted() && item.getIdentifier().equals(itemIdentifier)) {
                    found = Optional.of(item);
                }
            }
        }

        if (found == null) {
            found = Optional.empty();
        }

        return found;
    }
}
