public class Main {
    public static void main(String[] args) {
    	
    	Portfolio portfolio = new Portfolio();
		StockMarketGUI frame = new StockMarketGUI(portfolio);
		frame.setVisible(true);
    }
}
