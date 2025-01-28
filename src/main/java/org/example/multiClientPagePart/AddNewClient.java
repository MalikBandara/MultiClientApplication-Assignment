package org.example.multiClientPagePart;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewClient extends Application {

    @FXML
    private TextField clientUserName;


    public static void main(String[] args) {
        launch(args);
    }


    @FXML
    void btnAddNewClient(ActionEvent event) throws IOException {

        String text = clientUserName.getText();
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/multiclient_form.fxml"))));
        stage.setTitle(clientUserName.getText() + "'s Chat");
        stage.show();
        clientUserName.clear();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/client_add.fxml"))));
        stage.show();


        new Thread(()->{
            try {
                ServerFormContoller serverFormContoller = new ServerFormContoller();
                serverFormContoller.Server();
            }catch (Exception e ){
                e.printStackTrace();
            }

        }).start();


    }
}
