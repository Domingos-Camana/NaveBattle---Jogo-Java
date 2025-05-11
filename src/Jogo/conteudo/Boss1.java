package Jogo.conteudo;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Boss1 {

	private int x, y;
	private Image image;
	private int altura, largura;
	private List<TiroBs1> tirosBs;
	private boolean Visivel;
	private int VIDAS = 5;

	private static final int LARGURA = 780;
	private static int VELOCIDADE = 2;

	public Boss1(int x, int y) {

		this.x = x;
		this.y = y;

		Visivel = true;
		tirosBs = new ArrayList<TiroBs1>();
	}

	public void load() {

		ImageIcon ref = new ImageIcon(getClass().getResource("/cont/miniboss1.png"));
		image = ref.getImage();

		altura = ref.getIconHeight();
		largura = ref.getIconWidth();
	}

	public void update() {

		if (x > LARGURA) {

			x -= VELOCIDADE;

		}

		if (x <= LARGURA) {

			int a = 1 + (int) (Math.random() * (2 - 1 + 1));

			if (a == 1) {

				if (y >= 80) {

					y -= VELOCIDADE;

				}

			} else {

				if (y <= 620) {

					y += VELOCIDADE;

				}

			}
		}
	}

	public void bullets2() {

		this.tirosBs.add(new TiroBs1(x - largura, y + (altura / 2)));
	}

	public Rectangle getBounds() {

		return new Rectangle(x, y, largura / 2, altura);
	}

	public boolean isVisivel() {
		return Visivel;
	}

	public void setVisivel(boolean visivel) {
		Visivel = visivel;
	}

	public static int getVELOCIDADE() {
		return VELOCIDADE;
	}

	public static void setVELOCIDADE(int vELOCIDADE) {
		VELOCIDADE = vELOCIDADE;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Image getImage() {
		return image;
	}

	public List<TiroBs1> getTiroBs1() {
		return tirosBs;
	}

	public int getVIDAS() {
		return VIDAS;
	}

	public void setVIDAS(int vIDAS) {
		VIDAS = vIDAS;
	}

}
