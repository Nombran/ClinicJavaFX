package by.bsuir.clinic.controller;

import by.bsuir.clinic.server.ServerThread;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartMenuController {
    private static StartMenuController instance;

    public static StartMenuController getInstance() {
        return instance;
    }

    private int port=0;

    public void setPort(int port) {
        this.port = port;
    }

    public StartMenuController() {
        instance = this;
    }

    private boolean isRunning = true;

    private ServerSocket serverSocket;

    @FXML
    private JFXButton startServerButton;

    @FXML
    private Text statusText;

    @FXML
    private JFXButton interruptServerButton;

    @FXML
    private JFXTextArea logArea;

    @FXML
    private FontAwesomeIcon exit;

    @FXML
    public void addLog(String log) {
        this.logArea.setText(logArea.getText() + "\n"+log);
    }

    @FXML
    void initialize() {
        interruptServerButton.setDisable(true);
        startServerButton.setOnAction(event -> {
            startServerButton.setDisable(true);
            new Thread(() -> {
                isRunning=true;
                try {
                    HibernateSessionFactoryUtil.getSessionFactory();
                    Platform.runLater(() -> {
                        statusText.setText("Сервер запущен");
                        statusText.setFill(Color.color(0,1,0));
                        startServerButton.setDisable(true);
                        interruptServerButton.setDisable(false);
                    });
                    serverSocket = new ServerSocket( port == 0 ? 8071 : port); //8071
                    System.out.println("initialized");
                    while (isRunning) {
                        Socket sock = serverSocket.accept();
                        System.out.println(sock.getInetAddress().getHostName() + " connected");
                        ServerThread server = new ServerThread(sock);
                        server.start();
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }
            }).start();

        });
        interruptServerButton.setOnAction(event -> {
            isRunning=false;
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                statusText.setText("Сервер выключен");
                statusText.setFill(Color.color(1,0,0));
                interruptServerButton.setDisable(true);
                startServerButton.setDisable(false);
            });
        });
        exit.setOnMouseClicked(event -> {
            HibernateSessionFactoryUtil.closeFactory();
            System.exit(-1);
        });
    }

}
