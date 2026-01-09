package it.sarabanda.app.ui;

import it.sarabanda.app.arduino.ArduinoService;
import it.sarabanda.app.arduino.ArduinoServiceMock;
import it.sarabanda.app.arduino.ArduinoServiceReal;
import it.sarabanda.app.arduino.ConnectionResult;
import it.sarabanda.app.ui.controller.ConnectionController;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private JMenuBar mainMenuBar;
    private boolean fullScreenMode = false;
    private final NumberPanel numberPanel;
    private final ConnectionController connectionController;

    public MainFrame() {
        initFrame();
        initMenu();

        numberPanel = new NumberPanel();
        add(numberPanel, BorderLayout.CENTER);
        ArduinoService arduinoService = new ArduinoServiceMock();
        //ArduinoService arduinoService = new ArduinoServiceReal();
        arduinoService.setNumberListener(number ->
                SwingUtilities.invokeLater(() ->
                        numberPanel.showNumber(number)
                )
        );
        connectionController = new ConnectionController(arduinoService,
                () -> SwingUtilities.invokeLater(numberPanel::clear));

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
        initKeyBindings();
    }

    private void initMenu() {

        mainMenuBar = new JMenuBar();

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

        mainMenuBar.add(commandMenu);
        setJMenuBar(mainMenuBar);
    }

    private void onConnect(ActionEvent e) {
        ConnectionResult result = connectionController.connect();
        switch (result) {
            case CONNECTED ->
                    {
                        showInfo("Connessione ad Arduino riuscita.\nSistema pronto.");
                        connectionController.startListening();
                        applyFullScreen(true);
                    }
            case ALREADY_CONNECTED ->
                    showInfo("Arduino è già connesso");
            case PORT_NOT_FOUND ->
                    showError("Arduino non trovato.\nCollega il dispositivo USB.");
            case ERROR ->
                    showError("Impossibile connettersi ad Arduino");
        }
    }

    private void onDisconnect(ActionEvent e) {
        ConnectionResult result =
                connectionController.disconnect();

        switch (result) {
            case DISCONNECTED -> {
                numberPanel.clear();
                showInfo("Arduino disconnesso");

            }
            case ALREADY_DISCONNECTED ->
                    showInfo("Arduino non è connesso");
        }
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(
                this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                this, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void applyFullScreen(boolean enable) {

        if (fullScreenMode == enable) {
            return;
        }

        fullScreenMode = enable;

        dispose(); // chiude temporaneamente la finestra

        setUndecorated(enable);
        setJMenuBar(enable ? null : mainMenuBar);

        setVisible(true);
    }

    private void initKeyBindings() {

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "exitFullScreen");

        getRootPane().getActionMap()
                .put("exitFullScreen", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applyFullScreen(false);
                    }
                });
    }
}
