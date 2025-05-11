package Jogo.conteudo;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Inimigos {

	 private Image image;
	    private int x, y;
	    private int altura, largura;
	    private boolean eVisivel, retorna = false;
	    private static final int LARGURA = -10;
	    private static int VELOCIDADE = 3;

	    public Inimigos(int x, int y, boolean retorn) {
	        this.x = x;
	        this.y = y;
	        eVisivel = true;
	        retorna = retorn;
	    }

	    public void load() {
	        ImageIcon ref = new ImageIcon(getClass().getResource("/cont/enemy1.png"));
	        image = ref.getImage();
	        altura = ref.getIconHeight();
	        largura = ref.getIconWidth();
	    }

	    public void update() {
	        x -= VELOCIDADE;
	        if (x <= LARGURA && !retorna) {
	            eVisivel = false;
	        }
	        
	        if (x <= LARGURA && retorna) {
	        	x = (int) (Math.random() * 2058 + 2048);
				y = 90 + (int) (Math.random() * (600 - 90 + 1));
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

	    public boolean isVisivel() {
	        return eVisivel;
	    }

	    public static int getVELOCIDADE() {
	        return VELOCIDADE;
	    }

	    public void setVisivel(boolean eVisivel) {
	        this.eVisivel = eVisivel;
	    }

	    public static void setVELOCIDADE(int vELOCIDADE) {
	        VELOCIDADE = vELOCIDADE;
	    }

		public void setRetorna(boolean retorna) {
			this.retorna = retorna;
		}
}
