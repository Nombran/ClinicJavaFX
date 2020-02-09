package by.bsuir.clinicFX.client;

import java.io.*;
import java.net.*;


public class NetClientThread {
    private static int port = 0;
    private static InetAddress serverAddress;

    public static void setServerAddress(String serverAddress){
        try {
            NetClientThread.serverAddress = InetAddress.getByName(serverAddress);
        } catch (UnknownHostException e) {
            System.out.println("Адрес недоступен!");
            serverAddress = null;
        }
    }

    public static void setPort(int newPort) {
        NetClientThread.port = newPort;
    }

    public static String sendPacket(String command) {
        String response = "error";
        try {
            Socket s = new Socket(serverAddress==null? InetAddress.getLocalHost() : serverAddress, port==0? 8071 : port);
            PrintStream ps = new PrintStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps.println(command);
            response = br.readLine();
            s.close();
        } catch (UnknownHostException e) {
            System.out.println("адрес недоступен");
            //e.printStackTrace();

        } catch (IOException e) {
            System.out.println("ошибка I/О потока");
            //e.printStackTrace();
        }
        return response;
    }
}