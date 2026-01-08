package it.sarabanda.app.ui.controller;

import it.sarabanda.app.arduino.ArduinoService;
import it.sarabanda.app.arduino.ConnectionResult;
import it.sarabanda.app.arduino.ConnectionState;

public class ConnectionController {
    private final ArduinoService arduinoService;

    public ConnectionController(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    public ConnectionResult connect(String portName) {

        if (arduinoService.getState() == ConnectionState.CONNECTED) {
            return ConnectionResult.ALREADY_CONNECTED;
        }

        boolean success = arduinoService.connect(portName);

        return success
                ? ConnectionResult.CONNECTED
                : ConnectionResult.ERROR;
    }

    public ConnectionResult disconnect() {

        if (arduinoService.getState() == ConnectionState.DISCONNECTED) {
            return ConnectionResult.ALREADY_DISCONNECTED;
        }

        arduinoService.disconnect();
        return ConnectionResult.DISCONNECTED;
    }

    public void disconnectIfNeeded() {
        if (arduinoService.getState() == ConnectionState.CONNECTED) {
            arduinoService.disconnect();
        }
    }

    public void startListening() {
        arduinoService.startListening();
    }
}
