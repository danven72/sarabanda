package it.sarabanda.app.arduino;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoServiceReal implements ArduinoService {

    private NumberListener listener;
    private SerialPort port;
    private ConnectionState state = ConnectionState.DISCONNECTED;

    public boolean connect(String portName) {
        port = SerialPort.getCommPort(portName);
        //TODO: rendere la velocità di comunicazione configurabile
        port.setBaudRate(9600);
        boolean connected = false;
        state = ConnectionState.DISCONNECTED;
        if (port.openPort()) {
            state = ConnectionState.CONNECTED;
            connected = true;
        }

        return connected;
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
        this.listener = listener;
    }

    @Override
    public void startListening() {
        if (port == null || !port.isOpen()) {
            throw new IllegalStateException("Serial port not connected");
        }

        if (listener == null) {
            throw new IllegalStateException("NumberListener not set");
        }

        Thread serialThread = new Thread(() -> {
            try (var scanner = new java.util.Scanner(port.getInputStream())) {

                scanner.useDelimiter("\n"); //TODO: verificare cosa arriva veramente

                while (state == ConnectionState.CONNECTED && scanner.hasNext()) {

                    String line = scanner.next().trim();

                    if (!line.isEmpty()) {
                        try {
                            int value = Integer.parseInt(line);
                            listener.onNumberReceived(value);
                        } catch (NumberFormatException e) {
                            // ignora input non valido
                        }
                    }
                }

            } catch (Exception e) {
                state = ConnectionState.DISCONNECTED;
            }
        }, "Arduino-Serial-Listener");

        serialThread.setDaemon(true);
        serialThread.start();
    }
}
