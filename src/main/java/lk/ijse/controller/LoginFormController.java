package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public Button loginPanel;
    public AnchorPane loginpanel;
    public TextField txtName;

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        if (!txtName.getText().isEmpty()){
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/view/ClientForm.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(txtName.getText());
            primaryStage.setResizable(false);
            ClientFormController controller = new ClientFormController();
            controller.setClientName(txtName.getText()); // Set the parameter
            primaryStage.show();
            txtName.clear();


            primaryStage.show();
            txtName.clear();
        }
    }

    }

