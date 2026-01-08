package it.sarabanda.app.arduino;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoServiceReal implements ArduinoService {

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

    @Override
    public void setNumberListener(NumberListener listener) {
        //TODO: implemet me!!!
    }

    @Override
    public void startListening() {
        //todo: implement me!!!
    }
}
