package it.sarabanda.app.arduino;

import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is a mock implementation just for testing the result
 */
public class ArduinoServiceMock implements ArduinoService {

    private ConnectionState state = ConnectionState.DISCONNECTED;
    private NumberListener listener;
    private Timer timer;
    private final Random random = new Random();
    private boolean listening = false;

    @Override
    public boolean connect(String portName) {

        if (state == ConnectionState.CONNECTED) {
            return false;
        }

        state = ConnectionState.CONNECTED;

        return true;
    }

    @Override
    public void disconnect() {
        state = ConnectionState.DISCONNECTED;
        listening = false;

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public ConnectionState getState() {
        return state;
    }

    @Override
    public void setNumberListener(NumberListener listener) {
        this.listener = listener;
    }

    @Override
    public void startListening() {
        if (state != ConnectionState.CONNECTED || listening) {
            return;
        }

        listening = true;

        timer = new Timer("MockArduinoTimer");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (listener != null && state == ConnectionState.CONNECTED) {
                    int number = random.nextInt(5) + 1;
                    listener.onNumberReceived(number);
                }
            }
        }, 0, 2000);
    }

    @Override
    public Optional<String> findArduinoPort() {
        return Optional.of("MOCK_PORT");
    }
}
