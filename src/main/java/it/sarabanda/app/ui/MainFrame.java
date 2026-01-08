package it.sarabanda.app.ui;

import it.sarabanda.app.arduino.ArduinoService;
import it.sarabanda.app.arduino.ConnectionState;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private NumberPanel numberPanel;
    private ArduinoService arduinoService;

    public MainFrame() {
        initFrame();
        initMenu();

        numberPanel = new NumberPanel();
        add(numberPanel, BorderLayout.CENTER);
        arduinoService = new ArduinoService();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                disconnectIfNeeded();
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
            disconnectIfNeeded();
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
        if (arduinoService.getState() == ConnectionState.CONNECTED) {
            JOptionPane.showMessageDialog(
                    this,
                    "Arduino è già connesso",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // TODO: in futuro selezione porta
        //String portName = "COM3"; // macOS: /dev/tty.usbmodemXXXX
        String portName = "/dev/tty.usbmodemXXXX";
        boolean connected = arduinoService.connect(portName);

        if (connected) {
            JOptionPane.showMessageDialog(
                    this,
                    "Connessione ad Arduino riuscita.\nSistema pronto.",
                    "Connesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Impossibile connettersi ad Arduino",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onDisconnect(ActionEvent e) {

        if (arduinoService.getState() == ConnectionState.DISCONNECTED) {
            JOptionPane.showMessageDialog(
                    this,
                    "Arduino non è connesso",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        arduinoService.disconnect();

        JOptionPane.showMessageDialog(
                this,
                "Arduino disconnesso",
                "Disconnesso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void disconnectIfNeeded() {
        if (arduinoService != null &&
                arduinoService.getState() == ConnectionState.CONNECTED) {

            arduinoService.disconnect();
        }
    }
}
