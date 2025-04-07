import java.util.ArrayList;
import java.util.List;

public class Stock {

	private String name;
	private double price;
	private int quantity;
	private List<Stock> stocks;
	
	Stock(String name, double price, int quantity){
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.stocks = new ArrayList<>();
	}
	
	 // Getters
    public String getSymbol() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Buy Stock
    public void buy(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    // Sell Stock
    public boolean sell(int amount) {
        if (amount > 0 && amount <= quantity) {
            this.quantity -= amount;
            return true; 
        }
        return false; 
    }
	
	
}
