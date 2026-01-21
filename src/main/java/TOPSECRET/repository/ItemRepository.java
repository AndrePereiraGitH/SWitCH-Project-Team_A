package TOPSECRET.repository;

import TOPSECRET.domain.Dimensions;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publication;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
private List<Item> _items = new ArrayList<>();

public ItemRepository() {
    _items = new ArrayList<>();
}

public Item createAndAddItem(Condition condition, double weight, Dimensions dimensions, Publication publication) throws InstanceAlreadyExistsException {
    Item item = new Item(condition, weight, dimensions, publication);

    // verify if item already exists
    if(verifyIfExists(item)) {
        throw new InstanceAlreadyExistsException("Item already exists!");
    }

    // add item to items list
    _items.add(item);

    return item;
}

private boolean verifyIfExists(Item item) {
    if (item == null) {
        throw new IllegalArgumentException("Item is null");
    }

    return _items.contains(item);
}
git
    public List<Item> get_Items() {
        return _items;
    }

}
