package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Doctor;
import by.bsuir.clinicFX.model.Visit;
import com.google.gson.Gson;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class AddVisitController {
    private static Doctor currentDoctor;

    public static void setCurrentDoctor(Doctor currentDoctor) {
        AddVisitController.currentDoctor = currentDoctor;
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextField doctorField;

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    private FontAwesomeIcon backButton;

    @FXML
    private JFXDatePicker visitDate;

    @FXML
    private JFXTimePicker visitTime;

    @FXML
    void createDialog(String message)
    {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOnDialogClosed(event -> {
            if(message.equals("Визит успешно добавлен!")) {
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

    public void closeWindow(){
        Parent root = null;
        try {
            Parent newRoot;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/doctor-user-menu/main-menu/doctorUserMenu.fxml"));
            newRoot = fxmlLoader.load();
            Main.setNewScene(newRoot,1);
            DoctorMenuController controller =
                    fxmlLoader.<DoctorMenuController>getController();
            controller.setCurrentDoctor(currentDoctor);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        doctorField.setText(currentDoctor.getSecondName() + "  " +currentDoctor.getFirstName());
        doctorField.setDisable(true);
        exit.setOnMouseClicked(event -> System.exit(-1));
        backButton.setOnMouseClicked(event -> closeWindow());
        saveButton.setOnAction(event -> {
            if(visitDate.getValue()!=null && visitTime.getValue()!=null) {
                LocalDate date = visitDate.getValue();
                LocalTime time = visitTime.getValue();
                Date currentDate = new Date();
                Date visitDate = new Date((date.getYear()-1900), date.getMonthValue()==1 ? 12 : date.getMonthValue()-1, date.getDayOfMonth(), time.getHour(), time.getMinute());
                if (visitDate.after(currentDate)) {
                    Visit visit = new Visit();
                    visit.setDoctor(currentDoctor);
                    visit.setCustomer(null);
                    visit.setDate(visitDate);
                    Packet packet = new Packet();
                    Gson gson = new Gson();
                    packet.setCommand("addVisit");
                    packet.setData(gson.toJson(visit));
                    String response = NetClientThread.sendPacket(gson.toJson(packet));
                    packet = gson.fromJson(response,Packet.class);
                    if(gson.fromJson(packet.getData(),Boolean.class)) {
                        createDialog("Визит успешно добавлен!");
                    }
                    else {
                        createDialog("Визит на данное время уже существует!");
                    }
                }
            }
        });
    }
}
