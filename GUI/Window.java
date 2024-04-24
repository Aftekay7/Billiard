package GUI;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1100;

    private JPanel gamePanel;

    public Window() {

        setTitle("Billiard");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
        add(gamePanel);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

}
