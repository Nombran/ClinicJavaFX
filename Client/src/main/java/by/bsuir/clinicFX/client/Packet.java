package by.bsuir.clinicFX.client;

public class Packet {

    private String command;
    private String data;

    public Packet() {
    }

    public Packet(String command,String data) {
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
