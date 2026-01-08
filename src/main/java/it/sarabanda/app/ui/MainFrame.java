package it.sarabanda.app.ui;

import it.sarabanda.app.arduino.ArduinoService;
import it.sarabanda.app.arduino.ArduinoServiceMock;
import it.sarabanda.app.arduino.ArduinoServiceReal;
import it.sarabanda.app.arduino.ConnectionResult;
import it.sarabanda.app.ui.controller.ConnectionController;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private NumberPanel numberPanel;
    private ConnectionController connectionController;

    public MainFrame() {
        initFrame();
        initMenu();

        numberPanel = new NumberPanel();
        add(numberPanel, BorderLayout.CENTER);
        ArduinoService arduinoService = new ArduinoServiceMock();
        arduinoService.setNumberListener(number -> numberPanel.showNumber(number));
        connectionController = new ConnectionController(arduinoService);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                connectionController.disconnectIfNeeded();
            }
        });

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Music Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); // centra la finestra
        setLayout(new BorderLayout());
    }

    private void initMenu() {

        JMenuBar menuBar = new JMenuBar();

        JMenu commandMenu = new JMenu("Command");

        JMenuItem connectItem = new JMenuItem("Connect");
        JMenuItem disconnectItem = new JMenuItem("Disconnect");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Azione Exit
        exitItem.addActionListener(e -> {
            connectionController.disconnectIfNeeded();
            System.exit(0);
        });

        connectItem.addActionListener(this::onConnect);
        disconnectItem.addActionListener(this::onDisconnect);

        commandMenu.add(connectItem);
        commandMenu.add(disconnectItem);
        commandMenu.addSeparator();
        commandMenu.add(exitItem);

        menuBar.add(commandMenu);
        setJMenuBar(menuBar);
    }

    private void onConnect(ActionEvent e) {
        // TODO: in futuro selezione porta
        //String portName = "COM3"; // macOS: /dev/tty.usbmodemXXXX
        String portName = "/dev/tty.usbmodemXXXX";
        ConnectionResult result = connectionController.connect(portName);
        switch (result) {
            case CONNECTED ->
                    showInfo("Connessione ad Arduino riuscita.\nSistema pronto.");
            case ALREADY_CONNECTED ->
                    showInfo("Arduino è già connesso");
            case ERROR ->
                    showError("Impossibile connettersi ad Arduino");
        }
    }

    private void onDisconnect(ActionEvent e) {
        ConnectionResult result =
                connectionController.disconnect();

        switch (result) {
            case DISCONNECTED ->
                    showInfo("Arduino disconnesso");
            case ALREADY_DISCONNECTED ->
                    showInfo("Arduino non è connesso");
        }
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(
                this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
        connectionController.startListening();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                this, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

}
