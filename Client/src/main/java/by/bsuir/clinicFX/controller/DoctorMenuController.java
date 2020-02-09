package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Doctor;
import by.bsuir.clinicFX.model.Visit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DoctorMenuController {

    private static Doctor currentDoctor;

    public static void setCurrentDoctor(Doctor currentDoctor) {
        DoctorMenuController.currentDoctor = currentDoctor;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text mainLabel;

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    private FontAwesomeIcon backButton;

    @FXML
    private JFXTreeTableView<?> branchTable;

    @FXML
    private TreeTableColumn<Visit, String> freeVisitDoctor;

    @FXML
    private TreeTableColumn<Visit, String> freeVisitData;

    @FXML
    private JFXButton addVisit;

    @FXML
    private JFXButton editVisit;

    @FXML
    private JFXButton deleteVisit;

    @FXML
    private JFXTreeTableView<Visit> freeVisitTable;

    @FXML
    private TreeTableColumn<Visit, String> visitDoctor;

    @FXML
    private TreeTableColumn<Visit, String> visitCustomer;

    @FXML
    private TreeTableColumn<Visit, String> visitData;

    @FXML
    private JFXTreeTableView<Visit> activeVisitTable;

    @FXML
    private TreeTableColumn<Visit, String> pastVisitDoctor;

    @FXML
    private TreeTableColumn<Visit, String> pastVisitCustomer;

    @FXML
    private TreeTableColumn<Visit, String> pastVisitData;

    @FXML
    private TreeTableColumn<Visit, String> diagnoz;

    @FXML
    private JFXButton addDiagnoz;

    @FXML
    private JFXButton deletePastVisit;

    @FXML
    private JFXTreeTableView<Visit> pastVisitTable;

    private ObservableList<Visit> freeVisits = FXCollections.observableArrayList();

    private ObservableList<Visit> currentVisits = FXCollections.observableArrayList();

    private ObservableList<Visit> pastVisis = FXCollections.observableArrayList();

    @FXML
    private JFXButton saveVisitButton;

    @FXML
    public void updateData() {
        freeVisits.clear();
        currentVisits.clear();
        pastVisis.clear();
        List<Visit> visits;
        Packet packet = new Packet();
        Gson gson = new Gson();
        packet.setCommand("getVisits");
        String response = NetClientThread.sendPacket(gson.toJson(packet));
        packet = gson.fromJson(response,Packet.class);
        Type itemsListType = new TypeToken<List<Visit>>() {}.getType();
        visits = gson.fromJson(packet.getData(),itemsListType);
        for(Visit visit:visits) {
            Date currentDate = new Date();
            if(visit.getDate().after(currentDate) && visit.getCustomer()==null && visit.getDoctor().getId()==currentDoctor.getId()) {
                freeVisits.add(visit);
            }
            else
                if(visit.getDate().after(currentDate) && visit.getCustomer()!=null && visit.getDoctor().getId()==currentDoctor.getId()){
                    currentVisits.add(visit);
                }
                else
            if(visit.getDoctor().getId() == currentDoctor.getId() && visit.getCustomer()!=null && visit.getDate().before(currentDate)){
                     pastVisis.add(visit);
                }
            }
        }



    @FXML
    void initialize() {
        mainLabel.setText(currentDoctor.getSecondName() + "  " + currentDoctor.getFirstName());
        exit.setOnMouseClicked(event -> System.exit(-1));
        backButton.setOnMouseClicked(event -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/authorization/authorisation.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Main.setNewScene(root, 1);
        });
        updateData();
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm");


        freeVisitDoctor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDoctor().getFirstName() + "  " + param.getValue().getValue().getDoctor().getSecondName()));
        freeVisitData.setCellValueFactory(param -> new SimpleStringProperty(formatForDateNow.format(param.getValue().getValue().getDate())));
        TreeItem<Visit> root = new RecursiveTreeItem<>(freeVisits, RecursiveTreeObject::getChildren);
        freeVisitTable.setRoot(root);
        freeVisitTable.setShowRoot(false);
        freeVisitTable.setEditable(false);

        visitDoctor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDoctor().getFirstName() + "  " + param.getValue().getValue().getDoctor().getSecondName()));
        visitData.setCellValueFactory(param -> new SimpleStringProperty(formatForDateNow.format(param.getValue().getValue().getDate())));
        visitCustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomer().getSecondName() + "  " + param.getValue().getValue().getCustomer().getFirstName()));
        TreeItem<Visit> root2 = new RecursiveTreeItem<>(currentVisits, RecursiveTreeObject::getChildren);
        activeVisitTable.setRoot(root2);
        activeVisitTable.setShowRoot(false);
        activeVisitTable.setEditable(false);

        pastVisitDoctor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDoctor().getSecondName() + "  " + param.getValue().getValue().getDoctor().getFirstName()));
        pastVisitCustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()
                .getValue().getCustomer().getSecondName() + "  " + param.getValue()
                .getValue().getCustomer().getFirstName()));
        pastVisitData.setCellValueFactory(param -> new SimpleStringProperty(formatForDateNow.format(param.getValue().getValue().getDate())));
        diagnoz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDiagnosis()
                == null ? "Не установлен" : param.getValue().getValue().getDiagnosis()));
        TreeItem<Visit> root3 = new RecursiveTreeItem<>(pastVisis, RecursiveTreeObject::getChildren);
        pastVisitTable.setRoot(root3);
        pastVisitTable.setShowRoot(false);
        pastVisitTable.setEditable(true);

        addVisit.setOnAction(event -> {
            try {
                AddVisitController.setCurrentDoctor(currentDoctor);
                Parent newRoot;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/doctor-user-menu/add-visit-menu/addVisit.fxml"));
                newRoot = fxmlLoader.load();
                Main.setNewScene(newRoot, 1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        deleteVisit.setOnAction(event -> {
            if (freeVisitTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Visit visit = freeVisitTable.getSelectionModel().getSelectedItem().getValue();
            Packet packet = new Packet();
            packet.setCommand("deleteVisit");
            packet.setData(new Gson().toJson(visit));
            NetClientThread.sendPacket(new Gson().toJson(packet));
            updateData();
        });


        addDiagnoz.setOnAction(event -> {
            if (pastVisitTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Visit visit = pastVisitTable.getSelectionModel().getSelectedItem().getValue();
            try {
                Parent newRoot;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/doctor-user-menu/add-diagnoz-menu/addDiagnoz.fxml"));
                newRoot = fxmlLoader.load();
                Main.setNewScene(newRoot, 1);
                AddDiagnosisController controller =
                        fxmlLoader.getController();
                controller.setData(visit, currentDoctor);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        deletePastVisit.setOnAction(event -> {
            if (pastVisitTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Visit visit = pastVisitTable.getSelectionModel().getSelectedItem().getValue();
            Packet packet = new Packet();
            packet.setCommand("deleteVisit");
            packet.setData(new Gson().toJson(visit));
            NetClientThread.sendPacket(new Gson().toJson(packet));
            updateData();
        });
        saveVisitButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File directoryToSave = directoryChooser.showDialog(Main.getSecondStage());
            if(directoryToSave==null) {
                return;
            }
            try(FileWriter writer = new FileWriter(directoryToSave.getAbsolutePath() + "/DoctorSchedule.txt", false))
            {
                StringBuilder resultText = new StringBuilder();
                resultText.append("\t\t\t\t\t\t\tСписок занятых талонов!\r\n\r\n");
                resultText.append("   ФИО врача   ");
                resultText.append("        ФИО пациента  ");
                resultText.append("      Дата и время  ");
                resultText.append("\r\n--------------------------------------------------------------------------------------------------\r\n");
                for(Visit freeVisit : currentVisits) {
                    resultText.append(currentDoctor.getSecondName()).append(" ").append(currentDoctor.getFirstName());
                    resultText.append("    ");
                    resultText.append(freeVisit.getCustomer().getSecondName()).append(" ").append(freeVisit.getCustomer().getFirstName());
                    resultText.append("    ");
                    resultText.append(formatForDateNow.format(freeVisit.getDate()));
                    resultText.append("\r\n\r\n");
                }
                resultText.append("\r\n");
                resultText.append("Дата формирования отчета : ");
                resultText.append(formatForDateNow.format(new Date()));
                writer.write(resultText.toString());
                writer.flush();
            }
            catch(IOException ex){
                System.out.println("Ошибка открытия файла");
            }
        });

    }

}
