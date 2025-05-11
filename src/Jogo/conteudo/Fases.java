package Jogo.conteudo;

import javax.swing.JPanel;

public abstract class Fases extends JPanel {
    
    protected ListaFases listener;
    
    private float volumeJogo;
    private int  inimigosJogo;
    
    public void setListaFases(ListaFases listener) {
        this.listener = listener;
    }
    
    // Método para inicializar a fase (pode ser chamado no construtor da fase)
    public abstract void iniciarFase();
    
    // Método para encerrar a fase e realizar as limpezas necessárias
    public abstract void encerrarFase();

	public float getVolumeJogo() {
		return volumeJogo;
	}

	public void setVolumeJogo(float volumeJogo) {
		this.volumeJogo = volumeJogo;
	}

	public int getInimigosJogo() {
		return inimigosJogo;
	}

	public void setInimigosJogo(int inimigosJogo) {
		this.inimigosJogo = inimigosJogo;
	}
}
