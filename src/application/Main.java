/*
 * Tim Russell, 2017
 */
package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("GPRO Bloodsheet Updater");
			primaryStage.getIcons().addAll(
			// JavaFX is bad at auto choosing icons, so, many are given, ending with 48x48 as a compromise:
					new Image(getClass().getResourceAsStream("logo16.png")), 
					new Image(getClass().getResourceAsStream("logo32.png")),
					new Image(getClass().getResourceAsStream("logo64.png")),
					new Image(getClass().getResourceAsStream("logo256.png")),
					new Image(getClass().getResourceAsStream("logo48.png"))
					); // Getting IllegalArgumentException when images in /res/, so in /src/ for now
			primaryStage.getIcons().add(new Image("file:/logo48.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();	
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}