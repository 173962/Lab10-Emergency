package it.polito.tdp.emergency;

import it.polito.tdp.emergency.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Emergency.fxml"));
			Parent view = (Parent) loader.load();

			EmergencyController controller = loader.getController();
			Model model = new Model();
			controller.setModel(model);

			Scene main = new Scene(view);
			primaryStage.setScene(main);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
