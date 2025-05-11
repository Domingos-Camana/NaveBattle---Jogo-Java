package Jogo.conteudo;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class TiroBs1 {

	private Image image;
	private int x, y;
	private int altura, largura;
	private boolean Visivel;

	private static final int LARGURA = -10;
	private static int VELOCIDADE = 4;

	public TiroBs1(int x, int y) {

		this.x = x;
		this.y = y;

		Visivel = true;
	}

	public void load() {

		ImageIcon ref = new ImageIcon(getClass().getResource("/cont/bulletsmb1.png"));
		image = ref.getImage();

		altura = ref.getIconHeight();
		largura = ref.getIconWidth();
	}

	public void update() {

		x -= VELOCIDADE;
		if (x <= LARGURA) {

			Visivel = false;
		}
	}
	
	public Rectangle getBounds() {

		return new Rectangle(x, y, largura, altura);
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
		return Visivel;
	}

	public static int getVELOCIDADE() {
		return VELOCIDADE;
	}

	public void setVisivel(boolean Visivel) {
		this.Visivel = Visivel;
	}

	public static void setVELOCIDADE(int vELOCIDADE) {
		VELOCIDADE = vELOCIDADE;
	}

}

