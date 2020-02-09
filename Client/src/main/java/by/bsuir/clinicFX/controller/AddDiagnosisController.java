package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Doctor;
import by.bsuir.clinicFX.model.Visit;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class AddDiagnosisController {
    private Visit visit;

    private Doctor currentDoctor;

    public void setData(Visit visit, Doctor doctor) {
        this.visit = visit;
        this.currentDoctor = doctor;
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextField diagnoz;

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    private FontAwesomeIcon backButton;

    public void closeWindow(){
        Parent root = null;
        try {
            DoctorMenuController.setCurrentDoctor(currentDoctor);
            Parent newRoot;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/doctor-user-menu/main-menu/doctorUserMenu.fxml"));
            newRoot = fxmlLoader.load();
            Main.setNewScene(newRoot,1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void createDialog(String message)
    {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOnDialogClosed(event -> {
            if(message.equals("Диагноз успешно добавлен!")) {
                closeWindow();
            }
        });
        button.setOnAction(event1 -> dialog.close());
        dialogLayout.setBody(new Text(message));
        dialogLayout.setActions(button);
        dialog.setTranslateX(0);
        dialog.setMaxWidth(365);
        dialog.show();
    }

    @FXML
    void initialize() {
        exit.setOnMouseClicked(event -> System.exit(-1));
        backButton.setOnMouseClicked(event -> closeWindow());
        saveButton.setOnAction(event ->  {
            if(diagnoz.getText().length()!=0) {
                visit.setDiagnosis(diagnoz.getText());
                Packet packet = new Packet();
                packet.setCommand("updateVisit");
                Gson gson = new Gson();
                packet.setData(gson.toJson(visit));
                NetClientThread.sendPacket(gson.toJson(packet));
                createDialog("Диагноз успешно добавлен!");
            }

        });
    }

}
