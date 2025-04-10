import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

public class StockMarketGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField stockCode;
	private JLabel lblQuantintyYouWant;
	private JTextField quantity;
	private JLabel lblNewLabel_1;
	private JButton btnNewButton_1;
	
	
	public StockMarketGUI(Portfolio portfolio) {
		
		//WINDOW MAKER
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Mario\\eclipse-workspace\\StockMarket\\app-logo.jpg"));
		setTitle("Stock Market");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1059, 625);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(234, 234, 234));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter Stock Symbol");
		lblNewLabel.setBackground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(405, 99, 280, 40);
		contentPane.add(lblNewLabel);
		
		stockCode = new JTextField();
		stockCode.setBounds(309, 156, 409, 46);
		contentPane.add(stockCode);
		stockCode.setColumns(10);
		
		lblQuantintyYouWant = new JLabel("Quantity");
		lblQuantintyYouWant.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblQuantintyYouWant.setBounds(458, 230, 280, 40);
		contentPane.add(lblQuantintyYouWant);
		
		quantity = new JTextField();
		quantity.setColumns(10);
		quantity.setBounds(309, 281, 409, 46);
		contentPane.add(quantity);
	
		
		
		
		//BUY SECTION
		JButton btnNewButton = new JButton("Buy");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String symbol = stockCode.getText();
				int stockQuantity = Integer.parseInt(quantity.getText());
				double pricePerShare = 0.0; //Placeholder
				try {
					pricePerShare = getStockPrice(symbol);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(pricePerShare != -1) {
				Stock stock = new Stock(symbol,pricePerShare,stockQuantity);
				stock.buy(stockQuantity);
				
				portfolio.buyStock(stock, stockQuantity, pricePerShare);
				}

			}
		});
		
		
		btnNewButton.setBackground(new Color(0, 128, 0));
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20));
		btnNewButton.setBounds(309, 438, 134, 40);
		contentPane.add(btnNewButton);

		
		
		//SELL SECTION
		JButton btnSell = new JButton("Sell");
		btnSell.setForeground(Color.WHITE);
		btnSell.setFont(new Font("Arial", Font.PLAIN, 20));
		btnSell.setBackground(Color.RED);
		btnSell.setBounds(584, 438, 134, 40);
		
		
		contentPane.add(btnSell);
		
		
		btnSell.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String stockSymbol = stockCode.getText();
		        int stockQuantity;
		        
		        // Έλεγχος για έγκυρη αριθμητική είσοδο
		        try {
		            stockQuantity = Integer.parseInt(quantity.getText());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        // API CONNECTION (Λήψη τιμής μετοχής)
		        double price = 0.0; // Placeholder για API τιμή

		        // Πώληση μετοχής
		        boolean success = portfolio.searchAndSellStock(stockSymbol, stockQuantity, price);

		        // Προβολή αποτελέσματος στον χρήστη
		        if (success) {
		            JOptionPane.showMessageDialog(null, "Stock sold successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		        } else {
		            JOptionPane.showMessageDialog(null, "Stock sale failed! Please check if you have enough shares.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		lblNewLabel_1 = new JLabel("Welcome To Stock Market");
		lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 26));
		lblNewLabel_1.setBounds(355, 0, 409, 88);
		contentPane.add(lblNewLabel_1);

		//PORTFOLIO SECTION
		btnNewButton_1 = new JButton("Portfolio");
		btnNewButton_1.setForeground(SystemColor.activeCaption);
		btnNewButton_1.setBackground(SystemColor.desktop);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    try {
		            File file = new File("portfolio.txt");
		            if (file.exists()) {
		                Desktop.getDesktop().open(file);
		            } else {
		                JOptionPane.showMessageDialog(null, "portfolio.txt not found.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (IOException ex) {
		            JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		btnNewButton_1.setBounds(10, 536, 152, 39);
		contentPane.add(btnNewButton_1);
	}



public static double getStockPrice(String symbol) throws Exception {
	
    String apiKey = "FCLLB1H0VO49DDCG";
    String urlString = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
            + symbol + "&apikey=" + apiKey;
    try {
    URI uri = URI.create(urlString);
    URL url = uri.toURL();

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();
 
    JSONObject obj = new JSONObject(response.toString());
   
    
    JSONObject globalQuote = obj.getJSONObject("Global Quote");
    String priceStr = globalQuote.getString("05. price");
    
    double price = Double.parseDouble(priceStr);
    return price;
    
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error, please enter a valid symbol ", "Invalid Symbol", JOptionPane.ERROR_MESSAGE);
        return -1; 
    }
  }

}

