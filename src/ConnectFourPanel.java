import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ConnectFourPanel extends JPanel implements ActionListener, MouseListener {
    private final int fps = 60;
    private Timer frameTimer;

    private final int numRows = 6;
    private final int numCols = 7;

    private final Tile[][] tiles = new Tile[numRows][numCols];

    private boolean playersTurn = true;
    private boolean gameOver = false;

    private Tile.OWNER winner;

    public ConnectFourPanel() {
        addMouseListener(this);

        setBackground(Color.blue.darker());

        generateTiles();

        frameTimer = new Timer(1000 / fps, this);
        frameTimer.setActionCommand("frame");

        frameTimer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);

        if (gameOver)
            drawGameOver(g2);
    }

    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.black);
        g2.setFont(Tools.getCustomFont(40));

        String winnerText = "Yellow";
        if (winner == Tile.OWNER.OPPONENT)
            winnerText = "Red";
        if (winner == Tile.OWNER.NEITHER)
            winnerText = "No one";
        Tools.centerString(g2, getBounds(), winnerText + " wins!");
    }

    private void drawGrid(Graphics2D g2) {
        double circleScale = 0.8;
        int cricleWidth = getWidth() / numCols;
        int circleHeight = getHeight() / numRows;

        for (int i = 0; i < tiles[0].length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                g2.setColor(getTile(i, j).getColor());

                int newCircleWidth = (int) (cricleWidth * circleScale);
                int newCircleHeight = (int) (circleHeight * circleScale);

                int x = i * cricleWidth + (cricleWidth - newCircleWidth) / 2;
                int y = j * circleHeight + (circleHeight - newCircleHeight) / 2;

                g2.fillOval(x, y, newCircleWidth, newCircleHeight);
            }
        }
    }

    private void generateTiles() {
        for (int i = 0; i < tiles[0].length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                tiles[j][i] = new Tile(i, j);
            }
        }
    }

    private Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    private boolean addTile(int column, Tile.OWNER owner) {
        for (int i = tiles.length - 1; i > -1; i--) {
            if (tiles[i][column].getOwner() == Tile.OWNER.NEITHER) {
                tiles[i][column].setOwner(owner);
                return true;
            }
        }
        return false;
    }

    private boolean checkForWin(Tile.OWNER owner) {
        for (int row = 0; row < numRows; row++) {
            int count = 0;
            for (int col = 0; col < numCols; col++) {
                if (tiles[row][col].getOwner() == owner) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        for (int col = 0; col < numCols; col++) {
            int count = 0;
            for (int row = 0; row < numRows; row++) {
                if (tiles[row][col].getOwner() == owner) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        for (int row = 0; row < numRows - 3; row++) {
            for (int col = 0; col < numCols - 3; col++) {
                if (tiles[row][col].getOwner() == owner &&
                        tiles[row + 1][col + 1].getOwner() == owner &&
                        tiles[row + 2][col + 2].getOwner() == owner &&
                        tiles[row + 3][col + 3].getOwner() == owner) {
                    return true;
                }
            }
        }

        for (int row = 3; row < numRows; row++) {
            for (int col = 0; col < numCols - 3; col++) {
                if (tiles[row][col].getOwner() == owner &&
                        tiles[row - 1][col + 1].getOwner() == owner &&
                        tiles[row - 2][col + 2].getOwner() == owner &&
                        tiles[row - 3][col + 3].getOwner() == owner) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean tilesFull() {
        int count = 0;
        for (int i = 0; i < tiles[0].length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[j][i].getOwner() != Tile.OWNER.NEITHER)
                    count++;
            }
        }
        return count == numRows * numCols;
    }

    private void setWinner(Tile.OWNER owner) {
        gameOver = true;
        winner = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("frame")) {
            if (checkForWin(Tile.OWNER.PLAYER))
                setWinner(Tile.OWNER.PLAYER);
            if (checkForWin(Tile.OWNER.OPPONENT))
                setWinner(Tile.OWNER.OPPONENT);
            if (tilesFull()) {
                setWinner(Tile.OWNER.NEITHER);
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) {
            generateTiles();
            winner = null;
            gameOver = false;
            return;
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            int column = e.getX() / 100;
            boolean success;
            if (playersTurn) {
                success = addTile(column, Tile.OWNER.PLAYER);
            } else {
                success = addTile(column, Tile.OWNER.OPPONENT);
            }
            if (success) {
                Tools.playSound("chip");
                playersTurn = !playersTurn;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}