package dev.rhtj.pokesal;

public record ItemRecord(
    String id,
    String name,
    String description,
    int price) {
 
        public int getSellingPrice() {
        return (int) (price * 0.75);
    }
}
