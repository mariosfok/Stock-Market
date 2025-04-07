import java.time.LocalDateTime;

public class Transaction {
    private Stock stock;
    private String type; // "BUY" ή "SELL"
    private int quantity;
    private double pricePerShare; // Τιμή ανά μετοχή τη στιγμή της συναλλαγής
    private LocalDateTime timestamp; // Ημερομηνία & ώρα συναλλαγής

    public Transaction(Stock stock, int quantity, double pricePerShare) {
        this.stock = stock;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.timestamp = LocalDateTime.now(); 
    }

    public Stock getStock() {
        return stock;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return stock.getSymbol() + "," + quantity + "," + pricePerShare;
    }

}
