package by.bsuir.clinicFX.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    private static double xOffset;
    private static double yOffset;
    private static Stage primaryStage;
    private static Stage secondStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/authorization/authorisation.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        secondStage.initStyle(StageStyle.TRANSPARENT);
        secondStage.setResizable(false);
//        Scene scene = new Scene(root);
//        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();
            setNewScene(root,1);
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    };



    public static void setNewScene(Parent root,int stageType) {
        Scene scene = new Scene(root);
        Stage stage;
        if(stageType == 1) {
            stage = primaryStage;
        }
        else{
            stage = secondStage;
        }
        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
            stage.setScene(scene);
            stage.centerOnScreen();
            if(stageType==2) {
                secondStage.show();
            }
    }

    public static void resetSecondStage(){
        secondStage.close();

    }

    public static void main(String[] args) {
        if(args.length>0) {
            NetClientThread.setServerAddress(args[0]);
        }
        if(args.length>1) {
            NetClientThread.setPort(Integer.valueOf(args[1]));
        }
        launch(args);
    }

    public static Stage getSecondStage() {
        return secondStage;
    }
}
