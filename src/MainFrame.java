import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    public MainFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ConnectFourPanel panel = new ConnectFourPanel();
        panel.setPreferredSize(new Dimension(700, 600));
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }
}
