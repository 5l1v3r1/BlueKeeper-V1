package br.com.gilbertopapa.bluekeeper.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Pane root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
		
		Scene scene = new Scene(root, 850, 400);
		
		primaryStage.setMinWidth(850);
		primaryStage.setMinHeight(400);
		primaryStage.setMaxHeight(800);
		primaryStage.setTitle("BlueKeeper");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
