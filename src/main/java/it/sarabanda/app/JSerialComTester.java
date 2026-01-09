package it.sarabanda.app;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class JSerialComTester {
    public static void main(String[] args) throws Exception {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port: ports){
            System.out.println("Port: " + port.getSystemPortName() + " - " + port.getDescriptivePortName());
            port.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return 0;
                }

                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    System.out.println("**** Event type: " +serialPortEvent.getEventType() +
                            " Source: " + serialPortEvent.getSource() +
                           " SerialPort: " +serialPortEvent.getSerialPort() +
                            " Received Data: " + serialPortEvent.getReceivedData()
                    )                    ;

                }
            });
        }
        System.out.println("Press ESC to stop...");
        while (true) {
            if (System.in.available() > 0) {
                int key = System.in.read();
                //System.out.println("Key pressed: " + key);
                if (key == 27) { // ESC key
                    System.out.

                            println("ESC pressed. Exiting...");
                    break;
                }
            }
            Thread.sleep(1000);
        }
    }
}
