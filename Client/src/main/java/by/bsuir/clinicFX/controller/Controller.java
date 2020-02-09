package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Customer;
import by.bsuir.clinicFX.model.Doctor;
import com.google.gson.Gson;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;


public class Controller {
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField loginInput;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXTextField loginRegistr;

    @FXML
    private JFXPasswordField passwordRegistr;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField surname;

    @FXML
    private JFXTextField lastname;

    @FXML
    private JFXDatePicker birthday;

    @FXML
    private JFXComboBox<Label> socialStatus;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXButton registrationButton;

    @FXML
    private FontAwesomeIcon exit;


    @FXML
    private JFXCheckBox isDoctor;

    @FXML
    void createDialog(String message)
    {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
        button.setOnAction(event1 -> dialog.close());
        dialogLayout.setBody(new Text(message));
        dialogLayout.setActions(button);
        dialog.setTranslateX(-10);
        dialog.setMaxWidth(365);
        dialog.show();
    }

    @FXML
    void registrationEvent()
    {
        String LOGIN_VALIDATION_REGEX = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        String NAME_VALIDATION_REGEX ="[а-яёА-ЯЁ]{2,30}";
        String PHONE_REGEX = "(^\\+)([0-9]{12})";
        if(!loginRegistr.getText().matches(LOGIN_VALIDATION_REGEX) ||
                !passwordRegistr.getText().matches(LOGIN_VALIDATION_REGEX)) {
            createDialog("Некорректный логин или пароль! \nДоппустимые символы A-z 0-9\n" +
                    "Минимальная длина 8 символов");
            return;
        }
        if(!name.getText().matches(NAME_VALIDATION_REGEX) ||
                    !surname.getText().matches(NAME_VALIDATION_REGEX) ||
                    !lastname.getText().matches(NAME_VALIDATION_REGEX)) {
            createDialog("Личные данные заполнены некорректно! \n Допустимые символы А-Я");
            return;
        }
        if(socialStatus.getSelectionModel().getSelectedItem()==null) {
            createDialog("Выберите социальный статус!");
            return;
         }
        if(phoneNumber.getText().length()!=0) {
            if(!phoneNumber.getText().matches("(^\\+)([0-9]{12})")) {
                createDialog("Телефонный номер не соответствует шаблону:\n\n" +
                        "+(###)##_##_##_##");
                return;
            }
        }
        Customer customer = new Customer();
        customer.setUsername(loginRegistr.getText());
        customer.setPassword(passwordRegistr.getText());
        customer.setFirstName(name.getText());
        customer.setSecondName(surname.getText());
        customer.setLastName(lastname.getText());
        customer.setBirthday(birthday.getValue());
        customer.setSocialStatus(socialStatus.getSelectionModel().getSelectedItem().getText());
        customer.setPhoneNumber(phoneNumber.getText());
        loginRegistr.clear();
        passwordRegistr.clear();
        name.clear();
        surname.clear();
        lastname.clear();
        phoneNumber.clear();
        Packet packet = new Packet();
        packet.setCommand("registration");
        packet.setData(new Gson().toJson(customer));
        if(new Gson().fromJson(NetClientThread.sendPacket(new Gson().toJson(packet)),Packet.class).getData().equals("true"))  {
            createDialog("Регистрация прошла успещно!");
        }
        else {
            createDialog("Такой логин уже существует!");
        }
    }

    @FXML
    void initialize() {
        socialStatus.getItems().add(new Label("Студент"));
        socialStatus.getItems().add(new Label("Рабочий"));
        socialStatus.getItems().add(new Label("Пенсионер"));
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Обязательное поле");
        loginInput.getValidators().add(validator);
        passwordInput.getValidators().add(validator);
        loginRegistr.getValidators().add(validator);
        passwordRegistr.getValidators().add(validator);

        loginRegistr.textProperty().addListener((o,oldVal,newVal)->{
            if(newVal.length()==0) {
                loginRegistr.validate();
            }
        });
        passwordRegistr.textProperty().addListener((o,oldVal,newVal)->{
            if(newVal.length()==0) {
                passwordRegistr.validate();
            }
        });

        loginInput.focusedProperty().addListener(e -> {
            if(loginInput.getText().length()==0){
                 loginInput.validate();
            } else {
                loginInput.resetValidation();
            }
        });
        passwordInput.textProperty().addListener((o,oldVal,newVal)-> {
            if (newVal.length()==0) passwordInput.validate();
        });
        exit.setOnMouseClicked(event -> System.exit(-1));

        registrationButton.setOnAction(event -> registrationEvent());

        loginButton.setOnAction(event -> {
            if(loginInput.getText().length()==0 || passwordInput.getText().length()==0) {
                loginInput.validate();
                passwordInput.validate();
                return;
            }
            if(isDoctor.isSelected()){
                Doctor doctor = new Doctor();
                doctor.setUsername(loginInput.getText());
                doctor.setPassword(passwordInput.getText());
                Packet packet = new Packet();
                packet.setCommand("loginDoctor");
                packet.setData(new Gson().toJson(doctor));
                String response = NetClientThread.sendPacket(new Gson().toJson(packet));
                packet = new Gson().fromJson(response, Packet.class);
                if(packet.getCommand().equals("Admin")) {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/admin-menu/main-menu/adminMenu.fxml"));
                        Main.setNewScene(root,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                if (packet.getCommand().equals("User not found")) {
                    createDialog("Неправильный логин или пароль!");
                } else {
                    try {
                        DoctorMenuController.setCurrentDoctor(new Gson().fromJson(packet.getData(),Doctor.class));
                        Parent root;
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/doctor-user-menu/main-menu/doctorUserMenu.fxml"));
                        root = fxmlLoader.load();
                        Main.setNewScene(root,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            else {
                Customer customer = new Customer();
                customer.setUsername(loginInput.getText());
                customer.setPassword(passwordInput.getText());
                Packet packet = new Packet();
                packet.setCommand("login");
                packet.setData(new Gson().toJson(customer));
                String response = NetClientThread.sendPacket(new Gson().toJson(packet));
                packet = new Gson().fromJson(response, Packet.class);
                if (packet.getCommand().equals("User not found")) {
                    createDialog("Неправильный логин или пароль!");
                } else {
                    try {
                        CustomerMenuController.setCurrentCustomer(new Gson().fromJson(packet.getData(),Customer.class));
                        Parent root;
                        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/customer-user-menu/main-menu/customerUserMenu.fxml"));
                        root = fxmlLoader.load();
                        Main.setNewScene(root,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}

