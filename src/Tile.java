import java.awt.*;

public class Tile {
    public enum OWNER {
        PLAYER,
        OPPONENT,
        NEITHER
    }

    private int x;
    private  int y;
    private OWNER owner;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = OWNER.NEITHER;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public OWNER getOwner() {
        return owner;
    }

    public void setOwner(OWNER owner) {
        this.owner = owner;
    }

    public Color getColor() {
        if(owner == OWNER.PLAYER)
            return Color.yellow;
        if(owner == OWNER.OPPONENT)
            return Color.red;
        return Color.white;
    }
}
