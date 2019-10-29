package com.ocran;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/com/ocran/layouts/login.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/com/ocran/styles/login.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				DatabaseController.getDatabaseInstance();
			}		
		}).start();
	}
	
	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
