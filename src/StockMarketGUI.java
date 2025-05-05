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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

/*
This class represents the GUI for the Stock Market application.
It allows the user to perform the following actions:
1. Enter a stock symbol and quantity of shares to buy.
2. Perform a buy or sell operation for a stock.
3. View and manage the portfolio stored in a text file.
4. Display information about the application's functionality.

Key Components:
- Stock Code Input Field: Where the user enters the stock symbol.
- Quantity Input Field: Where the user enters the quantity of shares to buy or sell.
- Buy Button: Executes a buy operation for the entered stock symbol and quantity.
- Sell Button: Executes a sell operation for the entered stock symbol and quantity.
- Portfolio Button: Opens the portfolio file where all stock transactions are stored.
- Info Button: Displays a window with application information.
- API Connection: Fetches live stock prices from Alpha Vantage API.
*/


public class StockMarketGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField stockCode;
	private JLabel lblQuantintyYouWant;
	private JTextField quantity;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JTextField txtSellPrice;
	
	
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
		lblNewLabel.setBounds(398, 31, 280, 40);
		contentPane.add(lblNewLabel);
		
		stockCode = new JTextField();
		stockCode.setBounds(309, 82, 409, 46);
		contentPane.add(stockCode);
		stockCode.setColumns(10);
		
		lblQuantintyYouWant = new JLabel("Quantity");
		lblQuantintyYouWant.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblQuantintyYouWant.setBounds(456, 139, 108, 40);
		contentPane.add(lblQuantintyYouWant);
		
		quantity = new JTextField();
		quantity.setColumns(10);
		quantity.setBounds(309, 190, 409, 46);
		contentPane.add(quantity);
		
		JLabel lblSellPrice = new JLabel("Sell Price");
		lblSellPrice.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblSellPrice.setBounds(456, 263, 108, 40);
		contentPane.add(lblSellPrice);
		
		txtSellPrice = new JTextField();
		txtSellPrice.setColumns(10);
		txtSellPrice.setBounds(309, 314, 409, 46);
		contentPane.add(txtSellPrice);
	
		
		
		
		//BUY SECTION
		JButton btnNewButton = new JButton("Buy");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String symbol = stockCode.getText();
				int stockQuantity;
				
				    String input = quantity.getText();
				    try {
				        stockQuantity = Integer.parseInt(input);
				        if (stockQuantity <= 0) {
				            JOptionPane.showMessageDialog(null, "Quantity must be over 0.", "Error", JOptionPane.ERROR_MESSAGE);
				        }
				    } catch (NumberFormatException e1) {
				        JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
				        stockQuantity = -1; 
				    }
				 
				
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
		btnNewButton.setBounds(310, 398, 134, 40);
		contentPane.add(btnNewButton);

		
		
		//SELL SECTION
		JButton btnSell = new JButton("Sell");
		btnSell.setForeground(Color.WHITE);
		btnSell.setFont(new Font("Arial", Font.PLAIN, 20));
		btnSell.setBackground(Color.RED);
		btnSell.setBounds(584, 398, 134, 40);
		
		
		contentPane.add(btnSell);
		
		
		btnSell.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String stockSymbol = stockCode.getText();
		        int stockQuantity;

		        try {
		            stockQuantity = Integer.parseInt(quantity.getText());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        double sellingPrice;
		        
		        try {
		            sellingPrice = Double.parseDouble(txtSellPrice.getText());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        double profit = portfolio.searchAndSellStock(stockSymbol, stockQuantity, sellingPrice);

		        if (profit != -1) {
		            JOptionPane.showMessageDialog(null,
		                String.format("Stock sold successfully!\nProfit/Loss: %.2f€", profit),
		                "Success",
		                JOptionPane.INFORMATION_MESSAGE);
		        } else {
		            JOptionPane.showMessageDialog(null,
		                "Stock sale failed! Please check if you have enough shares.",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});


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
		btnNewButton_1.setBounds(445, 535, 152, 39);
		contentPane.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Info");
		btnNewButton_2.setForeground(SystemColor.activeCaption);
		btnNewButton_2.setBackground(SystemColor.desktop);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_2.setBounds(641, 533, 152, 42);
		contentPane.add(btnNewButton_2);
		
	
		
		btnNewButton_2.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        JFrame infoFrame = new JFrame("Informations");
		        infoFrame.setSize(400, 300);
		        infoFrame.setLocationRelativeTo(null); // Κέντρο της οθόνης
		        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Μόνο αυτό το παράθυρο κλείνει

		        JTextArea textArea = new JTextArea();
		        textArea.setText("• Adding new stock purchases by specifying the stock code, quantity\r\n"
		        		+"\n"
		        		+"• Selling existing stock purchases \r\n"
		        		+"\n"
		        		+"• All portfolio data is stored and managed through a file named Portfolio.txt.\r\n"
		        		+"\n"
		        		+ "• Each line in the Portfolio.txt follows the structure:\r\n"
		        		
		        		+ "CODE|QUANTITY|PRICE PER UNIT\r\n");
		        textArea.setWrapStyleWord(true);
		        textArea.setLineWrap(true);
		        textArea.setEditable(false);
		        
		        infoFrame.getContentPane().add(new JScrollPane(textArea)); // αν έχεις πολλά κείμενα, scroll αυτόματα
		        infoFrame.setVisible(true);
		    }
		});
		
		
		JButton btnNewButton_3 = new JButton("Check Stock Price");
		btnNewButton_3.setForeground(SystemColor.activeCaption);
		btnNewButton_3.setBackground(SystemColor.desktop);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_3.setBounds(249, 535, 152, 40);
		contentPane.add(btnNewButton_3);
		
	
		

		
		btnNewButton_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String stockSymbol = stockCode.getText();
				
				double pricePerShare = 0.0; //Placeholder

				try {
					pricePerShare = getStockPrice(stockSymbol);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if(pricePerShare != -1) {
					 JOptionPane.showMessageDialog(null, "The price of "+stockSymbol+" is at: "+pricePerShare);
			   }
			}
		});
	

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
