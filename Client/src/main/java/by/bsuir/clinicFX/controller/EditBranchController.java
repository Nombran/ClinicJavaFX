package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Branch;
import com.google.gson.Gson;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class EditBranchController {

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextField brachName;

    @FXML
    private JFXTextArea branchDescription;

    @FXML
    private FontAwesomeIcon backButton;

    private Branch branch;

    @FXML
    private StackPane stackPane;


    @FXML
    void createDialog(String message)
    {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOnDialogClosed(event -> {
            if(message.equals("Отделение успешно добавлено!")) {
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
    public void setFields(Branch branch) {
        phoneNumber.setText(branch.getPhoneNumber());
        brachName.setText(branch.getName());
        branchDescription.setText(branch.getDescription());
        this.branch = branch;
    }

    public void closeWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/admin-menu/main-menu/adminMenu.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Main.setNewScene(root,1);
    }

    @FXML
    void initialize() throws IOException {
        exit.setOnMouseClicked(event -> System.exit(-1));
        saveButton.setOnAction(e -> {
            if(!phoneNumber.getText().matches("(^\\+)([0-9]{12})")){
                createDialog("Телефонный номер не соответствует шаблону:\n\n" +
                        "+(###)##_##_##_##");
                return;
            } else {
                if (!brachName.getText().matches("[а-яёА-ЯЁ]{2,30}")) {
                    createDialog("В назвнии отделения разрешено использовать\n только символы " +
                            "русского языка!");
                    return;
                } else {
                    branch.setName(brachName.getText());
                    branch.setDescription(branchDescription.getText());
                    branch.setPhoneNumber(phoneNumber.getText());
                    Packet packet = new Packet();
                    packet.setCommand("updateBranch");
                    packet.setData(new Gson().toJson(branch));
                    NetClientThread.sendPacket(new Gson().toJson(packet));
                    closeWindow();
                }
            }
        });
        backButton.setOnMouseClicked(event -> {closeWindow();
            });

    }
}

