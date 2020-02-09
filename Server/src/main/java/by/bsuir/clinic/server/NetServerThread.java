package by.bsuir.clinic.server;

import by.bsuir.clinic.adapter.HibernateProxyTypeAdapter;
import by.bsuir.clinic.controller.StartMenuController;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class NetServerThread extends Application {

    private static double xOffset;
    private static double yOffset;
    private static final String PORT_REGEX = "\\d{4}";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/start-menu/startMenu.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        scene.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        if(this.getParameters().getRaw().size()>0) {
            if(this.getParameters().getRaw().get(0).matches(PORT_REGEX)){
                StartMenuController.getInstance().setPort(Integer.valueOf(this.getParameters().getRaw().get(0)));
            }
        }
    }
}
