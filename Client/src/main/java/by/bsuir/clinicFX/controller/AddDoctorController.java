package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Branch;
import by.bsuir.clinicFX.model.Doctor;
import com.google.gson.Gson;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class AddDoctorController {
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXPasswordField passwordRegistr;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField surname;

    @FXML
    private JFXTextField lastname;

    @FXML
    private JFXComboBox<String> branchComboBox;

    @FXML
    private JFXTextField position;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextField loginRegistr;

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    private JFXTextField specialization;

    @FXML
    private FontAwesomeIcon backButton;

    private List<Branch> branchList;

    @FXML
    void createDialog(String message)
    {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOnDialogClosed(event -> {
            if(message.equals("Врач успешно зарегистрирован")) {
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

    public void setBranchList(List<Branch> branchList){
        this.branchList = branchList;
        for(Branch branch : branchList) {
            branchComboBox.getItems().add(branch.getName());
        }
    }

    public void closeWindow(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/admin-menu/main-menu/adminMenu.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Main.setNewScene(root,1);
    }

    @FXML
    void initialize() {
        exit.setOnMouseClicked(event -> System.exit(-1));
        backButton.setOnMouseClicked(event -> closeWindow());
        saveButton.setOnAction(event -> {
            String LOGIN_VALIDATION_REGEX = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
            String NAME_VALIDATION_REGEX ="[а-яёА-ЯЁ\\s-]{2,30}";
            if(!loginRegistr.getText().matches(LOGIN_VALIDATION_REGEX) ||
                    !passwordRegistr.getText().matches(LOGIN_VALIDATION_REGEX)) {
                createDialog("Некорректный логин или пароль! \nДоппустимые символы A-z 0-9\n" +
                        "Минимальная длина 8 символов");
                return;
            }
            if(!name.getText().matches(NAME_VALIDATION_REGEX) ||
                    !surname.getText().matches(NAME_VALIDATION_REGEX) ||
                    !lastname.getText().matches(NAME_VALIDATION_REGEX) ||
                    !specialization.getText().matches(NAME_VALIDATION_REGEX) ||
                    !position.getText().matches(NAME_VALIDATION_REGEX)){
                createDialog("Личные данные заполнены некорректно! \n Допустимые символы \"А-Я\"");
                return;
            }
            if(branchComboBox.getSelectionModel().getSelectedItem()==null) {
                createDialog("Выберите отделение!");
                return;
            }
            Doctor doctor = new Doctor();
            doctor.setUsername(loginRegistr.getText());
            doctor.setPassword(passwordRegistr.getText());
            doctor.setFirstName(name.getText());
            doctor.setSecondName(surname.getText());
            doctor.setLastName(lastname.getText());
            Branch docBranch=null;
            for (Branch branchElem:branchList) {
                if(branchElem.getName().equals(branchComboBox.getValue())) {
                    docBranch=branchElem;
                }
            }
            if(docBranch==null) {
                return;
            }
            Gson gson = new Gson();
            doctor.setBranch(docBranch);
            doctor.setSpecialization(specialization.getText());
            doctor.setPosition(position.getText());
            Packet packet = new Packet();
            packet.setCommand("addDoctor");
            packet.setData(gson.toJson(doctor));
            String response = NetClientThread.sendPacket(new Gson().toJson(packet));
            packet = gson.fromJson(response,Packet.class);
            if(!gson.fromJson(packet.getData(),Boolean.class)) {
                createDialog("Такой логин уже существует!");
            }
            else {
                createDialog("Врач успешно зарегистрирован");
            }
        });
    }
}
