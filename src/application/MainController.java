package application;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController extends Thread {
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button locationButton;
	@FXML
	private Label statusLabel;
	
	private ArrayList<Integer> tempsArray = new ArrayList<Integer>();
	private ArrayList<Integer> driverArray = new ArrayList<Integer>();
	private ArrayList<Integer> carArray = new ArrayList<Integer>();
	private File chosenFile;
	
	public void update() {
		MainController mainController = new MainController();
		mainController.start();
	}
	
	public void run() {
		// runs in a Thread...
		// TODO: implement threads properly so UI does not hang during operation
		startSpreadsheetBuilder();
	}
	
	public void startWebScraper() {
		// Get username and password from fields:
		String user = usernameField.getText();
		String pass = passwordField.getText();
		// Create new WebScraper client object,
		// initialise username and password and starts headless browser:
		WebScraper client = new WebScraper(user, pass);

		client.login(); // Login to gpro.net
		
		try { // Use WebScraper to populate arrays for the spreadsheet builder to use:
			tempsArray = client.getTempsAndHumidity(client);
			driverArray = client.getDriverValues(client);
			carArray = client.getCarValues(client);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public void startSpreadsheetBuilder() {
		boolean success = true;
		updateLabel("Fetching data...");
		
		try { // Populate arrays with the web scraper:
		    startWebScraper();
		} catch (IndexOutOfBoundsException e) {
			success = false;
		}
		try { // Create a new Spreadsheet Builder:
		    updateLabel("Updating spreadsheet...");
		    SpreadsheetBuilder builder = new SpreadsheetBuilder(this.chosenFile,
		            this.tempsArray, this.driverArray, this.carArray);
		
			try { // And update!
			    builder.updateSpreadsheet();
			    if (success) {
				    updateLabel("Done!");
			    }
			} catch (NullPointerException e) {
				updateLabel("Updating spreadsheet failed - "
						  + "make sure your bloodsheet is not already open");
			} catch (IndexOutOfBoundsException i) {
				updateLabel("Web scraper failed - "
						  + "check your username, password or internet connection");
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateLabel("Updating spreadsheet failed - "
					  + "make sure you have selected your bloodsheet");
			success = false;
		}
	}
	
	public void updateLabel(String labelText) {
        statusLabel.setText(labelText);
	}
	
	@FXML protected void locateFile(ActionEvent event) {
	    FileChooser chooser = new FileChooser();
	    chooser.setTitle("Open Bloodsheet");
	    File file = chooser.showOpenDialog(new Stage());
	    this.chosenFile = file;
	    locationButton.setText("Selected");
	}
}