package it.sarabanda.app.ui.controller;

import it.sarabanda.app.arduino.ArduinoService;
import it.sarabanda.app.arduino.ConnectionResult;
import it.sarabanda.app.arduino.ConnectionState;

public class ConnectionController {
    private final ArduinoService arduinoService;
    private final Runnable onDisconnectUi;

    public ConnectionController(ArduinoService arduinoService, Runnable onDisconnectUi) {
        this.arduinoService = arduinoService;
        this.onDisconnectUi = onDisconnectUi;
    }

    public ConnectionResult connect() {
        ConnectionResult result;
        if (arduinoService.getState() == ConnectionState.CONNECTED) {
            result = ConnectionResult.ALREADY_CONNECTED;
        } else {
            result = arduinoService.findArduinoPort()
                    .map(port -> arduinoService.connect(port)
                            ? ConnectionResult.CONNECTED
                            : ConnectionResult.ERROR)
                    .orElse(ConnectionResult.PORT_NOT_FOUND);
        }

        return result;
    }


    public ConnectionResult disconnect() {

        if (arduinoService.getState() == ConnectionState.DISCONNECTED) {
            return ConnectionResult.ALREADY_DISCONNECTED;
        }

        arduinoService.disconnect();
        onDisconnectUi.run();
        return ConnectionResult.DISCONNECTED;
    }

    public void disconnectIfNeeded() {
        if (arduinoService.getState() == ConnectionState.CONNECTED) {
            arduinoService.disconnect();
            onDisconnectUi.run();
        }
    }

    public void startListening() {
        arduinoService.startListening();
    }
}
