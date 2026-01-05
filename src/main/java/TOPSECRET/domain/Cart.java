package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Item> itemsList;  // Mudamos para Item
    private User buyer;
    private Transaction transaction;

    public Cart(User buyer) {
        this.itemsList = new ArrayList<>();
        this.buyer = buyer;
        this.transaction = null;
    }

    // Adiciona um Item ao carrinho
    public void addItem(Item item) {
        itemsList.add(item);
    }

    // Remove um Item do carrinho
    public void removeItem(Item item) {
        itemsList.remove(item);
    }

    // Retorna a lista de itens no carrinho
    public List<Item> getItemsList() {
        return itemsList;
    }

    // Retorna o comprador do carrinho
    public User getBuyer() {
        return buyer;
    }

    // Retorna a transação associada ao carrinho
    public Transaction getTransaction() {
        return transaction;
    }
}
