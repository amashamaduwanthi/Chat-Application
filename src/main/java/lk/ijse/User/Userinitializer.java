package lk.ijse.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.controller.ClientFormController;

public class Userinitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ClientForm.fxml"));
        ClientFormController controller = new ClientFormController();
        fxmlLoader.setController(controller);
        stage.setScene(new Scene(fxmlLoader.load()));

        Stage pstage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(stage.getScene().getWindow());
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
        stage.setTitle("EChat");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
