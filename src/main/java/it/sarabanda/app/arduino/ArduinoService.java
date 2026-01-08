package it.sarabanda.app.arduino;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoService {

    private SerialPort port;
    private ConnectionState state = ConnectionState.DISCONNECTED;

    public boolean connect(String portName) {
        /*
        port = SerialPort.getCommPort(portName);
        //TODO: rendere la velocità di comunicazione configurabile
        port.setBaudRate(9600);

        if (port.openPort()) {
            state = ConnectionState.CONNECTED;
            return true;
        }

        return false;

         */
        return true;
    }

    public void disconnect() {
        if (port != null && port.isOpen()) {
            port.closePort();
        }
        state = ConnectionState.DISCONNECTED;
    }

    public ConnectionState getState() {
        return state;
    }
}
