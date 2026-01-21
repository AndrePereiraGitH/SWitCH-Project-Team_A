package TOPSECRET.repository;

import TOPSECRET.domain.Dimensions;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
private List<Item> _items = new ArrayList<>();

public ItemRepository() {
    _items = new ArrayList<>();
}

public void createAndAddItem(Condition condition, double weight, Dimensions dimensions, Publication publication) {
    Item item = new Item(condition, weight, dimensions, publication);

    // ensure that item exists
    if (item == null) {
        throw new IllegalArgumentException("Item cannot be null");
    }
    // add item to items list
    _items.add(item);
}

public List<Item> get_Items() {
    return _items;
}
/*
public boolean verifyIfItemExists(Item item) {

    if (item == null) {
        throw new IllegalArgumentException("You tried to find a null Item");
    }


}

 */

}
