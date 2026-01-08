package it.sarabanda.app.arduino;

public interface ArduinoService {
    boolean connect(String portName);

    void disconnect();

    ConnectionState getState();

    /**
     * Permette di registrare un listener per i numeri ricevuti
     */
    void setNumberListener(NumberListener listener);

    void startListening();
}
