package by.bsuir.clinicFX.controller;

import by.bsuir.clinicFX.client.Main;
import by.bsuir.clinicFX.client.NetClientThread;
import by.bsuir.clinicFX.client.Packet;
import by.bsuir.clinicFX.model.Branch;
import by.bsuir.clinicFX.model.Doctor;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminMenuController {
    @FXML
    private JFXButton addBranch;

    @FXML
    private JFXButton editBranch;

    @FXML
    private JFXButton deleteBranch;

    @FXML
    private JFXButton addDoctor;

    @FXML
    private JFXButton editDoctor;

    @FXML
    private JFXButton deleteDoctor;

    @FXML
    private FontAwesomeIcon exit;

    private ObservableList<Branch> branches = FXCollections.observableArrayList();

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<Branch> branchTable;

    @FXML
    private TreeTableColumn<Branch,String> branchNameCol;

    @FXML
    private TreeTableColumn<Branch,String> branchPhoneCol;

    @FXML
    private TreeTableColumn<Branch,String> branchDescriptionCol;

    @FXML
    private JFXComboBox<String> branchesComboBox;

    @FXML
    private TreeTableColumn<Doctor, String> doctorNameCol;

    @FXML
    private TreeTableColumn<Doctor,String> doctorSurnameCol;

    @FXML
    private TreeTableColumn<Doctor,String> doctorLastnameCol;

    @FXML
    private TreeTableColumn<Doctor, String> doctorRankCol;

    @FXML
    private TreeTableColumn<Doctor,String> doctorSpecCol;

    @FXML
    private JFXTreeTableView doctorTable;

    @FXML
    private FontAwesomeIcon backButton;

    @FXML
    private BarChart<String, Number> seasonBartChart;

    @FXML
    private PieChart branchPieChart;

    @FXML
    private JFXButton seasonStatFileButton;

    @FXML
    private JFXButton branchStatFileButton;

    @FXML
    public double[] initializeBarChart() {
        Gson gson = new Gson();
        seasonBartChart.getXAxis().setLabel("Месяц года");
        seasonBartChart.getYAxis().setLabel("Процент от посещений");
        Packet packet = new Packet();
        packet.setCommand("getVisits");
        packet = gson.fromJson(NetClientThread.sendPacket(gson.toJson(packet)),Packet.class);
        Type itemsListType = new TypeToken<List<Visit>>() {}.getType();
        List<Visit> visits = gson.fromJson(packet.getData(),itemsListType);
        double[] numberOfVisits = new double[12];
        for(int i=0;i<12;i++) {
            numberOfVisits[i] = 0.;
        }
        int allPasVisitNumber=0;
        for(Visit visit : visits) {
            if(visit.getDate().before(new Date())) {
                numberOfVisits[visit.getDate().getMonth()]++;
                allPasVisitNumber++;
            }
        }
        for(int i=0;i<12;i++) {
            numberOfVisits[i] /=allPasVisitNumber;
            numberOfVisits[i] *= 100;
        }
        XYChart.Series<String,Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Январь", numberOfVisits[0]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Февраль", numberOfVisits[1]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Март", numberOfVisits[2]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Апрель", numberOfVisits[3]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Май", numberOfVisits[4]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Июнь", numberOfVisits[5]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Июль", numberOfVisits[6]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Август", numberOfVisits[7]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Сентябрь", numberOfVisits[8]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Октябрь", numberOfVisits[9]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Ноябрь", numberOfVisits[10]));
        dataSeries1.getData().add(new XYChart.Data<String, Number>("Декабрь", numberOfVisits[11]));
        dataSeries1.setName("Статистика за все время");
        seasonBartChart.getData().clear();
        seasonBartChart.getData().add(dataSeries1);
        return numberOfVisits;
    }

    @FXML
    private void updateBranches() {
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
            updateDoctorsTable();

        }
        for(Branch branch : list) {
            branchesComboBox.getItems().add(branch.getName());
        }
    }

    private void updateDoctorsTable() {
        doctors.clear();
        String selectedBranch = branchesComboBox.getValue();
        for(Branch branch : branches) {
            if(branch.getName().equals(selectedBranch)) {
                Packet packet = new Packet();
                packet.setCommand("getDoctors");
                packet.setData(new Gson().toJson(branch));
                String response =  NetClientThread.sendPacket(new Gson().toJson(packet));
                packet = new Gson().fromJson(response,Packet.class);
                Type itemsListType = new TypeToken<List<Doctor>>() {}.getType();
                List<Doctor> doctorList = new Gson().fromJson(packet.getData(),itemsListType);
                doctors.addAll(doctorList);
            }
        }
    }

    @FXML
    private Map<String,Integer> initializeBrachPieChart() {
        branchPieChart.getData().clear();
        Gson gson = new Gson();
        Packet packet = new Packet();
        packet.setCommand("getVisits");
        packet = gson.fromJson(NetClientThread.sendPacket(gson.toJson(packet)),Packet.class);
        Type itemsListType = new TypeToken<List<Visit>>() {}.getType();
        List<Visit> visits = gson.fromJson(packet.getData(),itemsListType);
        Map<String,Integer> slices = new HashMap<>();
        for(Branch branch : branches) {
            slices.put(branch.getName(),0);
            for(Visit visit : visits) {
                if(visit.getDate().before(new Date()) && visit
                        .getDoctor()
                        .getBranch()
                        .getName().equals(branch.getName())) {
                    slices.put(branch.getName(),slices.get(branch.getName())+1);
                }
            }
            PieChart.Data slice1 = new PieChart.Data(branch.getName(), slices.get(branch.getName()));
            branchPieChart.getData().add(slice1);
        }
        return slices;
    }
    @FXML
    void initialize() {
        updateBranches();
        branchNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
        branchPhoneCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPhoneNumber())
        );
        branchDescriptionCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDescription())
        );
        TreeItem<Branch> root2 = new RecursiveTreeItem<>(branches, RecursiveTreeObject::getChildren);
        branchTable.getColumns().setAll(branchNameCol,branchPhoneCol,branchDescriptionCol);
        branchTable.setRoot(root2);
        branchTable.setShowRoot(false);
        branchTable.setEditable(false);

        doctorNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getFirstName()));
        doctorSurnameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSecondName())
        );
        doctorLastnameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getLastName())
        );
        doctorRankCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPosition())
        );
        doctorSpecCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSpecialization())
        );
        TreeItem<Doctor> root3 = new RecursiveTreeItem<>(doctors, RecursiveTreeObject::getChildren);
        doctorTable.getColumns().setAll(doctorNameCol,doctorSurnameCol,doctorLastnameCol,doctorRankCol,doctorSpecCol);
        doctorTable.setRoot(root3);
        doctorTable.setShowRoot(false);
        doctorTable.setEditable(false);


        exit.setOnMouseClicked(event -> System.exit(-1));

        backButton.setOnMouseClicked(event -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/authorization/authorisation.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Main.setNewScene(root,1);});
        addBranch.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/admin-menu/add-branch-menu/addBranch.fxml"));
                Main.setNewScene(root,1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        editBranch.setOnAction(event -> {
            if(branchTable.getSelectionModel().getSelectedItem()==null) {
                return;
            }
            Branch branch = branchTable.getSelectionModel().getSelectedItem().getValue();
            try {
                Parent root;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin-menu/edit-branch-menu/editBranch.fxml"));
                root = fxmlLoader.load();
                Main.setNewScene(root,1);
                EditBranchController controller =
                        fxmlLoader.<EditBranchController>getController();
                controller.setFields(branch);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        deleteBranch.setOnAction(event -> {
            if(branchTable.getSelectionModel().getSelectedItem()==null) {
                return;
            }
            Branch branch = branchTable.getSelectionModel().getSelectedItem().getValue();
            Packet packet = new Packet();
            packet.setCommand("deleteBranch");
            packet.setData(new Gson().toJson(branch));
            NetClientThread.sendPacket(new Gson().toJson(packet));
            updateBranches();
        });

        addDoctor.setOnAction(event -> {
            if(branches.size()==0) {
                return;
            }
            List<Branch> branchList = new ArrayList<>(branches);
            try {
                Parent root;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin-menu/add-doctor-menu/addDoctor.fxml"));
                root = fxmlLoader.load();
                Main.setNewScene(root,1);
                AddDoctorController controller =
                        fxmlLoader.getController();
                controller.setBranchList(branchList);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        editDoctor.setOnAction(event -> {
            if(doctorTable.getSelectionModel().getSelectedItem()==null) {
                return;
            }
            List<Branch> branchList = new ArrayList<>(branches);
            int index = doctorTable.getSelectionModel().getFocusedIndex();
            Doctor doctor = null;
            String str = doctorTable.getTreeItem(index).toString();
            for(Doctor doctorElem:doctors) {
                if(str.contains(doctorElem.toString())) {
                    doctor = doctorElem;
                }
            }
            try {
                Parent root;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin-menu/edit-doctor-menu/editDoctor.fxml"));
                root = fxmlLoader.load();
                Main.setNewScene(root,1);
                EditDoctorController controller =
                        fxmlLoader.<EditDoctorController>getController();
                controller.setData(doctor,branchList);
           } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        deleteDoctor.setOnAction(event -> {
            if(doctorTable.getSelectionModel().getSelectedItem()==null) {
                return;
            }
            int index = doctorTable.getSelectionModel().getFocusedIndex();
            Doctor doctor = null;
            String str = doctorTable.getTreeItem(index).toString();
            for(Doctor doctorElem:doctors) {
                if (str.contains(doctorElem.toString())) {
                    doctor = doctorElem;
                }
            }
            Packet packet = new Packet();
            packet.setCommand("deleteDoctor");
            packet.setData(new Gson().toJson(doctor));
            NetClientThread.sendPacket(new Gson().toJson(packet));
            updateDoctorsTable();
        });

        branchesComboBox.setOnAction(event-> {
            updateDoctorsTable();
        });
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm");
        initializeBarChart();
        initializeBrachPieChart();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        seasonStatFileButton.setOnAction(event -> {
            File directoryToSave = directoryChooser.showDialog(Main.getSecondStage());
            if(directoryToSave==null) {
                return;
            }
            try(FileWriter writer = new FileWriter(directoryToSave.getAbsolutePath() + "/SeasonStats.txt", false))
            {
                double[] stats = initializeBarChart();
                StringBuilder resultText = new StringBuilder();
                resultText.append("\t\t\t\t\t\t\tСезонная статистика посещений!");
                resultText.append("\r\n\r\n");
                resultText.append("   Месяц       Процент\r\n");
                resultText.append("----------------------------------------------------------------------");
                for(int i=0;i<12;i++) {
                    resultText.append("\r\n");
                    switch (i) {
                        case 0:
                            resultText.append("Январь      :   ");
                        break;
                        case 1:
                            resultText.append("Февраль     :   ");
                            break;
                        case 2:
                            resultText.append("Март        :   ");
                            break;
                        case 3:
                            resultText.append("Апрель      :   ");
                            break;
                        case 4:
                            resultText.append("Май         :   ");
                            break;
                        case 5:
                            resultText.append("Июнь        :   ");
                            break;
                        case 6:
                            resultText.append("Июль        :   ");
                            break;
                        case 7:
                            resultText.append("Август      :   ");
                            break;
                        case 8:
                            resultText.append("Сентябрь    :   ");
                            break;
                        case 9:
                            resultText.append("Октябрь     :   ");
                            break;
                        case 10:
                            resultText.append("Ноябрь      :   ");
                            break;
                        case 11:
                            resultText.append("Декабрь     :   ");
                            break;
                            default:break;
                    }
                    resultText.append(Double.isNaN(stats[i]) ? "0" : stats[i]);
                    resultText.append('%');
                }
                resultText.append("\r\n\r\n\r\n");
                resultText.append("Дата Формирования отчета : ");
                resultText.append(formatForDateNow.format(new Date()));
                writer.write(resultText.toString());
                writer.flush();
            }
            catch(IOException ex){
                System.out.println("Ошибка открытия файла");
            }
        });

        branchStatFileButton.setOnAction(event -> {
            Map<String,Integer> branchStats = initializeBrachPieChart();
            File directoryToSave = directoryChooser.showDialog(Main.getSecondStage());
            if(directoryToSave==null) {
                return;
            }
            try(FileWriter writer = new FileWriter(directoryToSave.getAbsolutePath() + "/BranchStats.txt", false))
            {
                StringBuilder resultText = new StringBuilder();
                resultText.append("\t\t\t\t\t\t\tСтатистика посещений по отделениям!\r\n");
                for(String key: branchStats.keySet()) {
                    resultText.append(key);
                    resultText.append(" : ");
                    resultText.append(branchStats.get(key));
                    resultText.append("\r\n");
                }
                resultText.append("\r\n\r\n\r\n");
                resultText.append("Дата Формирования отчета : ");
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
