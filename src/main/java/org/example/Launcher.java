package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        Parent parent =  FXMLLoader.load(getClass().getResource("/server_form.fxml"));
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        stage1.setTitle("server ");
        stage1.show();

        Parent parent1 =  FXMLLoader.load(getClass().getResource("/client_form.fxml"));
        Scene scene1 = new Scene(parent1);
        Stage stage2 = new Stage();
        stage2.setScene(scene1);
        stage2.setTitle("client ");
        stage2.show();
    }
}
