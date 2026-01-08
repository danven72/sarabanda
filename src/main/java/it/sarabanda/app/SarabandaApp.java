package it.sarabanda.app;

import it.sarabanda.app.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SarabandaApp {
    public static void main(String[] args) {

        // Imposta Look & Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Avvia la UI sul thread grafico
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
