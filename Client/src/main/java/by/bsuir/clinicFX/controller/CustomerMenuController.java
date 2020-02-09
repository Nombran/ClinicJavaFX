package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Branch;
import by.bsuir.clinicFX.model.Customer;
import by.bsuir.clinicFX.model.Visit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerMenuController {
    private static Customer currentCustomer;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text mainLabel;

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    private FontAwesomeIcon backButton;

    @FXML
    private JFXTreeTableView<Visit> activeVisitTable;

    @FXML
    private TreeTableColumn<Visit,String> visitDoctor;

    @FXML
    private TreeTableColumn<Visit,String> visitCustomer;

    @FXML
    private TreeTableColumn<Visit,String> visitData;

    @FXML
    private JFXTreeTableView<Visit> pastVisitTable;

    @FXML
    private TreeTableColumn<Visit,String> pastVisitDoctor;

    @FXML
    private TreeTableColumn<Visit,String> pastVisitCustomer;

    @FXML
    private TreeTableColumn<Visit,String> pastVisitData;

    @FXML
    private TreeTableColumn<Visit,String> diagnoz;

    @FXML
    private JFXButton cancelVisit;

    @FXML
    private JFXTreeTableView<Visit> newVisitTable;

    @FXML
    private TreeTableColumn<Visit,String> freeVisitDoctor;

    @FXML
    private TreeTableColumn<Visit,String> freeVisitData;

    @FXML
    private JFXButton saveVisit;

    @FXML
    private JFXComboBox<String> branchesComboBox;

    @FXML
    private JFXComboBox<String> specComboBox;

    private ObservableList<Visit> currentVisits = FXCollections.observableArrayList();

    private ObservableList<Visit> pastVisis = FXCollections.observableArrayList();

    private ObservableList<Visit> freeVisits = FXCollections.observableArrayList();

    private ObservableList<Branch> branches = FXCollections.observableArrayList();

    private ObservableList<String> specializations = FXCollections.observableArrayList();

    public static void setCurrentCustomer(Customer currentCustomer) {
        CustomerMenuController.currentCustomer = currentCustomer;
    }

    public void updateSpecComboBox() {
        specComboBox.getItems().clear();
        if(branches.size()==0) {
            specComboBox.setDisable(true);
        }
        else {
            List<Visit> visits;
            Packet packet = new Packet();
            Gson gson = new Gson();
            packet.setCommand("getVisits");
            String response = NetClientThread.sendPacket(gson.toJson(packet));
            packet = gson.fromJson(response,Packet.class);
            Type itemsListType = new TypeToken<List<Visit>>() {}.getType();
            visits = gson.fromJson(packet.getData(),itemsListType);
            Set<String> specList = new HashSet<>();
            for(Visit visitElem : visits) {
                if(visitElem.getDoctor().getBranch()!=null) {
                    if (visitElem.getDoctor().getBranch().getName().equals(branchesComboBox.getValue())
                            && visitElem.getCustomer() == null) {
                        specList.add(visitElem.getDoctor().getSpecialization());
                    }
                }
            }
            if(specList.size()==0) {
                specComboBox.setDisable(true);
            }
            else{
                specComboBox.setDisable(false);
                 specComboBox.getItems().addAll(specList);

            }

        }
    }

    @FXML
    public void updateNewVisitTable() {
        freeVisits.clear();
        List<Visit> visits;
        Packet packet = new Packet();
        Gson gson = new Gson();
        packet.setCommand("getVisits");
        String response = NetClientThread.sendPacket(gson.toJson(packet));
        packet = gson.fromJson(response,Packet.class);
        Type itemsListType = new TypeToken<List<Visit>>() {}.getType();
        visits = gson.fromJson(packet.getData(),itemsListType);
        for (Visit visit: visits) {
            if(visit.getCustomer() == null && visit.getDoctor().getSpecialization()
                    .equals(specComboBox.getValue())) {
                freeVisits.add(visit);
            }
        }
    }

    @FXML
    public void updateBranches() {
        freeVisits.clear();
        branches.clear();
        Packet packet = new Packet();
        packet.setCommand("getBranchList");
        packet = new Gson().fromJson(NetClientThread.sendPacket(new Gson().toJson(packet)),Packet.class);
        Type itemsListType = new TypeToken<List<Branch>>() {}.getType();
        List<Branch> list = new Gson().fromJson(packet.getData(),itemsListType);
        branches.addAll(list);
        branchesComboBox.getItems().clear();
        if(list.size()==0) {
            branchesComboBox.setDisable(true);
        }
        else {
            branchesComboBox.setDisable(false);
            branchesComboBox.setValue(list.get(0).getName());
            updateSpecComboBox();
        }
        for(Branch branch : list) {
            branchesComboBox.getItems().add(branch.getName());
        }
    }

    public void updateData() {
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
            if(visit.getCustomer()!=null) {
                if(visit.getDate().after(currentDate) && visit.getCustomer().getId() == currentCustomer.getId()) {
                    currentVisits.add(visit);
                }
                else if(visit.getDate().before(currentDate) && visit.getCustomer().getId() == currentCustomer.getId()) {
                    pastVisis.add(visit);
                }
            }
        }
    }

    @FXML
    void initialize() {
        updateData();
        updateBranches();
        mainLabel.setText(currentCustomer.getSecondName() + "  " +currentCustomer.getFirstName());
        exit.setOnMouseClicked(event -> System.exit(-1));
        backButton.setOnMouseClicked(event -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/authorization/authorisation.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Main.setNewScene(root,1);
        });

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm");

        visitDoctor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDoctor().getFirstName()+"  "+param.getValue().getValue().getDoctor().getSecondName()));
        visitData.setCellValueFactory(param -> new SimpleStringProperty(formatForDateNow.format(param.getValue().getValue().getDate())));
        visitCustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomer().getSecondName()+ "  "+param.getValue().getValue().getCustomer().getFirstName()));
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
                ==null ? "Не установлен" : param.getValue().getValue().getDiagnosis()));
        TreeItem<Visit> root3 = new RecursiveTreeItem<>(pastVisis, RecursiveTreeObject::getChildren);
        pastVisitTable.setRoot(root3);
        pastVisitTable.setShowRoot(false);
        pastVisitTable.setEditable(true);

        freeVisitDoctor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDoctor().getSecondName() + "   " +
                param.getValue().getValue().getDoctor().getFirstName()));
        freeVisitData.setCellValueFactory(param -> new SimpleStringProperty(formatForDateNow.format(param.getValue().getValue().getDate())));
        TreeItem<Visit> root4 = new RecursiveTreeItem<>(freeVisits, RecursiveTreeObject::getChildren);
        newVisitTable.setRoot(root4);
        newVisitTable.setShowRoot(false);
        newVisitTable.setEditable(false);

        cancelVisit.setOnAction(event -> {
            if(activeVisitTable.getSelectionModel().getSelectedItem()==null) {
                return;
            }
            Visit visit = activeVisitTable.getSelectionModel().getSelectedItem().getValue();
            visit.setCustomer(null);
            Packet packet = new Packet();
            packet.setCommand("updateVisit");
            packet.setData(new Gson().toJson(visit));
            NetClientThread.sendPacket(new Gson().toJson(packet));
            updateData();
            updateNewVisitTable();
        });

        branchesComboBox.setOnAction(event-> {
            updateSpecComboBox();
        });

        specComboBox.setOnAction(event -> {
            updateNewVisitTable();
        });

        saveVisit.setOnAction(event -> {
            if(newVisitTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Visit visit = newVisitTable.getSelectionModel().getSelectedItem().getValue();
            visit.setCustomer(currentCustomer);
            Packet packet = new Packet();
            Gson gson = new Gson();
            packet.setCommand("updateVisit");
            packet.setData(gson.toJson(visit));
            NetClientThread.sendPacket(gson.toJson(packet));
            updateNewVisitTable();
            updateData();
        });
    }
}
