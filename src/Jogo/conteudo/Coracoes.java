package Jogo.conteudo;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Coracoes {

	private Image image;
	private int x, y;
	private boolean eVisivel;

	public Coracoes(int x, int y) {
		this.x = x;
		this.y = y;
		eVisivel = true;
	}

	public void load() {
		ImageIcon ref = new ImageIcon(getClass().getResource("/cont/coracoes.png"));
		image = ref.getImage();
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

	public void setVisivel(boolean eVisivel) {
		this.eVisivel = eVisivel;
	}

}
