package it.sarabanda.app.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class NumberPanel extends JPanel {

    private final JLabel numberLabel;

    public NumberPanel() {
        setLayout(new BorderLayout());

        numberLabel = new JLabel("", SwingConstants.CENTER);
        numberLabel.setFont(createBigFont());
        numberLabel.setForeground(Color.BLUE);

        add(numberLabel, BorderLayout.CENTER);
    }

    /**
     * Metodo pubblico per visualizzare il numero
     */
    public void showNumber(int number) {
        numberLabel.setText(String.valueOf(number));
        resizeFont();
    }

    /**
     * Font iniziale grande
     */
    private Font createBigFont() {
        return new Font("SansSerif", Font.BOLD, 200);
    }

    /**
     * Ridimensiona il font per occupare lo spazio disponibile
     */
    private void resizeFont() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) {
            return;
        }

        int fontSize = Math.min(panelWidth, panelHeight);
        Font font = new Font("SansSerif", Font.BOLD, fontSize);
        numberLabel.setFont(font);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        resizeFont();
    }

    public void clear() {
        numberLabel.setText("");
    }
}
