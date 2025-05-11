package Jogo.conteudo;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Tiros {

    private Image image;
    private int x, y;
    private int altura, largura;
    private boolean eVisivel;
    private static final int LARGURA = 980;
    private static int VELOCIDADE = 2;

    public Tiros(int x, int y) {
        this.x = x;
        this.y = y;
        eVisivel = true;
    }

    public void load() {
        ImageIcon ref = new ImageIcon(getClass().getResource("/cont/bullets1.png"));
        image = ref.getImage();
        altura = ref.getIconHeight();
        largura = ref.getIconWidth();
    }

    public void update() {
        x += VELOCIDADE;
        if (x > LARGURA) {
            eVisivel = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura / 2, altura);
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean iseVisivel() {
        return eVisivel;
    }

    public static int getVELOCIDADE() {
        return VELOCIDADE;
    }

    public void seteVisivel(boolean eVisivel) {
        this.eVisivel = eVisivel;
    }

    public static void setVELOCIDADE(int vELOCIDADE) {
        VELOCIDADE = vELOCIDADE;
    }
}
