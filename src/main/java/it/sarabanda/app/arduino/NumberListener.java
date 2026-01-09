package it.sarabanda.app.arduino;

@FunctionalInterface
public interface NumberListener {
    void onNumberReceived(int number);
}
