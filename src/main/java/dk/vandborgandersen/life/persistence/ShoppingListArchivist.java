package dk.vandborgandersen.life.persistence;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.life.model.ShoppingList;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Archivist for shopping lists.
 *
 * @author mortena@gmail.com
 */
@Singleton
public class ShoppingListArchivist {

    private static final String SHOP_COL = "ShoppingLists";
    @Inject
    private MongoOperations mongoOperations;

    public List<ShoppingList> findShoppingLists(User user) {
        return mongoOperations.find(new Query(Criteria.where("userTId").is(user.gettId())), ShoppingList.class, SHOP_COL);
    }

    public String add(User user, String listName) {
        ShoppingList list = new ShoppingList();
        list.setName(listName);
        String listNameSimplified = listName.trim().replace(" ", "_");
        list.setListNameSimplified(listNameSimplified);
        list.setUserTId(user.gettId());

        long count =
            mongoOperations.count(new Query(Criteria.where("listNameSimplified").is(listNameSimplified)), ShoppingList.class, SHOP_COL);
        if (count > 0) {
            list.setListIdentifier(listNameSimplified + "_" + count);
        } else {
            list.setListIdentifier(listNameSimplified);
        }

        mongoOperations.insert(list, SHOP_COL);
        return list.getListIdentifier();
    }

    public void save(ShoppingList shoppingList) {
        mongoOperations.save(shoppingList, SHOP_COL);
    }

    public Optional<ShoppingList> getList(String listIdentifier) {
        ShoppingList shoppingList =
            mongoOperations.findOne(new Query(Criteria.where("listIdentifier").is(listIdentifier)), ShoppingList.class, SHOP_COL);
        if (shoppingList != null) {
            return Optional.of(shoppingList);
        } else {
            return Optional.empty();
        }
    }

    public void delete(ShoppingList list) {
        mongoOperations.remove(list, SHOP_COL);
    }
}
