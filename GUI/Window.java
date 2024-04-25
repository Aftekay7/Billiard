package GUI;

import javax.swing.*;

public class Window extends JFrame {
    private final int WIDTH = 1500;
    private final int HEIGHT = 1100;

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
}
