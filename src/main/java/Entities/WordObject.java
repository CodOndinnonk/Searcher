package Entities;

import java.util.Comparator;


public class WordObject {
    private String word;
    private int quantity;

    public WordObject() {
    }

    public WordObject(String word, int qty) {
        this.word = word;
        this.quantity = qty;
    }

    /**
     * Comparator, allows to sort collection in descending order by {@link WordObject#quantity}
     */
    public static Comparator<WordObject> qtyComparatorDescending = new Comparator<WordObject>() {
        @Override
        public int compare(WordObject o1, WordObject o2) {
            int qty1 = o1.getQuantity();
            int qty2 = o2.getQuantity();
            return qty2 - qty1;
        }
    };


    public void incrementQty() {
        this.quantity++;
    }

    public void decrementQty() {
        this.quantity--;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
