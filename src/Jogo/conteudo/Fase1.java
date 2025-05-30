package Jogo.conteudo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Fase1 extends Fases implements ActionListener {

	private tecladoAdapter meuKeyListener = new tecladoAdapter();

	private boolean flag = true;
	private boolean flagfim = false;
	private int flagescolha = 1;

	private Image fundo;
	private Jogador jogador;
	private Timer timer;
	private Timer fim;
	private Timer fim1;
	private List<Coracoes> coracoes;
	private List<Inimigos> inimigos;

	// Painel
	private JLabel labelInimigosRemovidos;
	private JLabel imagemInimigoLabel;
	private JLabel labelInimRemov;
	private JLabel imagemInimLabel;
	private JLabel labelavancar;
	private JLabel labelperfeito;
	private JLabel repetirLabel;
	private JLabel menuLabel;
	private JLabel imagemNaveLabel;
	private JLabel imagemNave1Label;
	private int inimigos1Removidos = 0;
	private int inimigosRemovidos_numtotal = 0;

	private boolean jogando;
	private int inimigos_1 = 10;
	private int inimigos_numtotal = inimigos_1;

	// Som
	private Clip clipfase;
	private boolean clipstop;
	private float volumeFase = -35.5f + getVolumeJogo();
	private long startTime;
	private boolean volumeAlterado = false;
	private static final float MAX_VOLUME = 6.0f;
	private static final float MIN_VOLUME = -80.0f;
	private static final float VOLUME_STEP = 1.0f;
	private Timer volumeIncreaseTimer;
	private Timer volumeDecreaseTimer;
	boolean flagfim1 = false;

	// Controle para notificar apenas uma vez o término da fase
	private boolean faseFinalizadaNotificada = true;

	public Fase1() {
		setFocusable(true);
		setDoubleBuffered(true);
		setLayout(null);

		// Carrega o fundo da fase
		ImageIcon ref = new ImageIcon(getClass().getResource("/cont/Background1.png"));
		
		fundo = ref.getImage();
		jogador = new Jogador(-100, 300);
		jogador.load();
		iniciarSomFase("/cont/somfase1.wav");
		
		iniciarPainel();
		iniciarInimigos();

		clipstop = false;
		

		addKeyListener(meuKeyListener);

		timer = new Timer(16, this);
		timer.start();

		jogando = true;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void iniciarFase() {
		// Caso seja necessário fazer alguma inicialização adicional
	}

	@Override
	public void encerrarFase() {
		timer.stop();
		if (clipfase != null || clipfase.isRunning()) {
			clipfase.stop();
		}
		removeKeyListener(meuKeyListener);
		setFocusable(false);
		setEnabled(false);
		removeAll();
	}

	public void iniciarInimigos() {
		int[] coorden = new int[inimigos_1];
		inimigos = new ArrayList<>();

		for (int i = 0; i < coorden.length; i++) {
			int x = (int) (Math.random() * 2058 + 2048);
			int y = 90 + (int) (Math.random() * (600 - 90 + 1));
			inimigos.add(new Inimigos(x, y, false));
		}
	}

	public void iniciarCoracoes() {
		int[] coorden = new int[5];
		coracoes = new ArrayList<>();
		int l = 20;

		for (int i = 0; i < coorden.length; i++) {
			int x, y;
			if (coracoes.isEmpty()) {
				x = 10;
				y = 20;
				coracoes.add(new Coracoes(x, y));
			} else {
				x = 10 + l;
				y = 20;
				coracoes.add(new Coracoes(x, y));
				l += 20;
			}
		}
	}

	public void iniciarPainel() {
		iniciarCoracoes();

		imagemInimigoLabel = new JLabel();
		repetirLabel = new JLabel();
		menuLabel = new JLabel();
		imagemNaveLabel = new JLabel();
		imagemNave1Label = new JLabel();
		labelInimRemov = new JLabel("x 0/" + inimigos_numtotal);
		labelInimigosRemovidos = new JLabel("X0");
		imagemInimLabel = new JLabel();
		labelavancar = new JLabel("ESPAÇO PARA AVANÇAR");
		labelperfeito = new JLabel("PERFEITO!");

		ImageIcon iconInimigo = new ImageIcon(getClass().getResource("/cont/enemy1.png"));

		ImageIcon iconNave = new ImageIcon(getClass().getResource("/cont/nave.png"));
		Image imgNave = iconNave.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

		Image imgInimigo = iconInimigo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

		Image imgInim = iconInimigo.getImage().getScaledInstance(20 * 2, 20 * 2, Image.SCALE_SMOOTH);

		imagemInimigoLabel.setIcon(new ImageIcon(imgInimigo));
		imagemInimigoLabel.setBounds(820, 20, 30, 30);

		labelInimigosRemovidos.setBounds(850, 20, 200, 30);
		labelInimigosRemovidos.setForeground(Color.WHITE);

		imagemInimLabel.setIcon(new ImageIcon(imgInim));
		imagemInimLabel.setBounds(170, 120, 20 * 2, 20 * 2);
		imagemInimLabel.setVisible(false);

		labelInimRemov.setBounds(230, 120, 200, 30);
		labelInimRemov.setForeground(Color.WHITE);
		labelInimRemov.setFont(new Font("Arial", 0, 40));
		labelInimRemov.setVisible(false);

		labelperfeito.setBounds(390, 390, 250, 30);
		labelperfeito.setForeground(Color.WHITE);
		labelperfeito.setFont(new Font("Arial", 0, 40));
		labelperfeito.setVisible(false);

		labelavancar.setBounds(370, 600, 250, 30);
		labelavancar.setForeground(Color.WHITE);
		labelavancar.setFont(new Font("Arial", 0, 20));
		labelavancar.setVisible(false);

		repetirLabel.setText("REPETIR");
		repetirLabel.setBounds(270, 600, 190, 40);
		repetirLabel.setFont(new Font("Arial", 0, 40));
		repetirLabel.setForeground(Color.WHITE);
		repetirLabel.setBackground(new Color(0, 0, 0, 150));
		repetirLabel.setVisible(false);

		imagemNaveLabel.setIcon(new ImageIcon(imgNave));
		imagemNaveLabel.setBounds(230, 600, 30, 30);
		imagemNaveLabel.setVisible(false);

		menuLabel.setText("MENU");
		menuLabel.setBounds(600, 600, 165, 45);
		menuLabel.setFont(new Font("Arial", 0, 40));
		menuLabel.setForeground(Color.WHITE);
		menuLabel.setBackground(new Color(0, 0, 0, 150));
		menuLabel.setVisible(false);

		imagemNave1Label.setIcon(new ImageIcon(imgNave));
		imagemNave1Label.setBounds(560, 600, 30, 30);
		imagemNave1Label.setVisible(false);

		setLayout(null);
		add(labelInimigosRemovidos);
		add(imagemInimigoLabel);
		add(labelInimRemov);
		add(imagemInimLabel);
		add(labelavancar);
		add(labelperfeito);
		add(repetirLabel);
		add(menuLabel);
		add(imagemNaveLabel);
		add(imagemNave1Label);

		revalidate();
		repaint();
	}

	private void iniciarSomFase(String caminhoDoSom) {
		try {
			URL url = getClass().getResource(caminhoDoSom);
			if (url == null) {
				System.err.println("Recurso não encontrado: " + caminhoDoSom);
				return;
			}
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clipfase = AudioSystem.getClip();
			clipfase.open(audioInputStream);
			alterarVolume(clipfase, volumeFase);
			clipfase.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void variarSom(int loop, String caminho) {

		clipfase.stop();
		iniciarSomFase(caminho);
		clipfase.stop();
		clipfase.setFramePosition(0);
		clipfase.loop(loop);
		clipfase.start();
	}

	private void alterarVolume(Clip clip, float novoVolume) {
		if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(novoVolume);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graficos = (Graphics2D) g;

		if (jogando && flag) {
			graficos.drawImage(fundo, 0, 0, this);

			for (Coracoes t : coracoes) {
				t.load();
				graficos.drawImage(t.getImage(), t.getX(), t.getY(), this);
			}

			for (Tiros t : jogador.getTiros()) {
				t.load();
				graficos.drawImage(t.getImage(), t.getX(), t.getY(), this);
			}

			graficos.drawImage(jogador.getImage(), jogador.getX(), jogador.getY(), this);

			for (Inimigos t : inimigos) {
				t.load();
				graficos.drawImage(t.getImage(), t.getX(), t.getY(), this);
			}
		} else {
			ImageIcon gameOver = new ImageIcon(getClass().getResource("/cont/GameOver.png"));
			graficos.drawImage(gameOver.getImage(), 0, 0, this);

		}

		if (!flag) {
			ImageIcon gameOver = new ImageIcon(getClass().getResource("/cont/Background.png"));
			graficos.drawImage(gameOver.getImage(), 0, 0, this);

		}

		if (jogador != null) {

			colisoes();
		}

	}

	public void colisoes() {
		Rectangle formaNave = jogador.getBounds();
		Rectangle formaInNave;
		Rectangle formaTiro;
		int vidaAnterior;

		for (int i = 0; i < inimigos.size(); i++) {
			vidaAnterior = jogador.getVIDAS();
			Inimigos tempInimigo = inimigos.get(i);
			formaInNave = tempInimigo.getBounds();

			if (formaNave.intersects(formaInNave)) {
				if (jogador.isTurbo()) {
					tempInimigo.setVisivel(false);
				} else {
					jogador.setVIDAS(vidaAnterior - 1);
					if (vidaAnterior > jogador.getVIDAS()) {
						coracoes.get(coracoes.size() - 1).setVisivel(false);
						tempInimigo.setVisivel(false);
						if (jogador.getVIDAS() == 0) {
							jogador.setVisivel(false);
							tempInimigo.setVisivel(false);
							jogando = false;
						}
					}
				}
			}
		}

		List<Tiros> tiros = jogador.getTiros();
		for (int i = 0; i < tiros.size(); i++) {
			Tiros tempTiro = tiros.get(i);
			formaTiro = tempTiro.getBounds();

			for (int j = 0; j < inimigos.size(); j++) {
				Inimigos tempInimigo = inimigos.get(j);
				formaInNave = tempInimigo.getBounds();

				if (formaTiro.intersects(formaInNave) && tempInimigo.isVisivel()) {
					tempTiro.seteVisivel(false);
					tempInimigo.setVisivel(false);
					inimigos1Removidos++;
					inimigosRemovidos_numtotal++;
					labelInimigosRemovidos.setText("X" + inimigos1Removidos);
					labelInimRemov.setText("x " + inimigos1Removidos + "/" + inimigos_numtotal);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		jogador.setVolumeTiro(jogador.getVolumeTiro()+getVolumeJogo());
		
		if(jogador != null) {
		
			if (jogador.isVisivel()) {
				jogador.update();
			} else {
				jogador = null;
			}
		}
		
		if (jogador != null && jogando) {

			
			timer.setDelay(jogador.isTurbo() ? 5 : 16);
			timer.restart();

			List<Tiros> tiros = jogador.getTiros();
			for (int i = 0; i < tiros.size(); i++) {
				Tiros t = tiros.get(i);
				if (t.iseVisivel()) {
					t.update();
					if (jogador.isTurbo()) {
						tiros.get(i).setVELOCIDADE(-2);
					} else {
						tiros.get(i).setVELOCIDADE(2);
					}
				} else {
					tiros.remove(i);
				}
			}
			
			if (inimigos.size() <= 0 && jogador.getX() <= 1024 && jogador != null) {

				jogador.setPass(true);
				fim = new Timer(500, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						jogador.setX(jogador.getX() + 2);

					}
				});
				fim.start();
			}

			if (jogador.getX() >= 1024 && !flagfim1) {

				variarSom(0, "/cont/shot.wav");

			}

			if (jogador.getX() >= 2000 && !flagfim1) {

				labelInimigosRemovidos.setVisible(false);
				imagemInimigoLabel.setVisible(false);
				imagemInimLabel.setVisible(true);
				labelInimRemov.setVisible(true);
				labelavancar.setVisible(true);

				if (inimigosRemovidos_numtotal == inimigos_numtotal) {

					labelperfeito.setVisible(true);
				}

				flagfim1 = true;

				flag = false;
				fim.stop();

			}
		}

		if (!coracoes.isEmpty() && !coracoes.get(coracoes.size() - 1).isVisivel()) {
			coracoes.remove(coracoes.size() - 1);
		}

		for (int i = 0; i < inimigos.size(); i++) {
			Inimigos t = inimigos.get(i);
			if (t.isVisivel()) {
				t.update();
			} else {
				inimigos.remove(i);
			}
		}

		if (flag) {
			labelInimigosRemovidos.setVisible(jogando);
			imagemInimigoLabel.setVisible(jogando);
		}

		if (!jogando || jogador == null) {

			menuLabel.setVisible(true);
			repetirLabel.setVisible(true);

			switch (flagescolha) {
			case 1: {

				imagemNaveLabel.setVisible(true);
				imagemNave1Label.setVisible(false);

				break;
			}
			case 2: {

				imagemNaveLabel.setVisible(false);
				imagemNave1Label.setVisible(true);

				break;
			}

			default: {

				break;
			}
			}
		}

		if (!jogando && clipfase != null && clipfase.isRunning()) {
			clipfase.stop();
		}
		/*
		 * if (!jogando && jogador.getClip() != null && jogador.getClip().isRunning()) {
		 * jogador.setVolumeTiro(-80.0f); }
		 */
		if (volumeAlterado && System.currentTimeMillis() - startTime >= 60000) {
			alterarVolume(clipfase, -20.0f);
			volumeAlterado = true;
		}

		

		if (flagfim && !clipstop) {

			clipstop = true;
			encerrarFase();
			faseFinalizadaNotificada = false;
		}

		// Notifica o listener apenas uma vez quando a fase termina
		if (jogando && !faseFinalizadaNotificada) {
			if (listener != null) {
				listener.faseFinalizada();
			}
			faseFinalizadaNotificada = true;
		}

		repaint();
	}

	private class tecladoAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
			if (jogador != null) {
				jogador.keypressed(e);
			}
			int key = e.getKeyCode();

			if (!jogando || jogador == null) {

				if (key == KeyEvent.VK_RIGHT) {

					if (flagescolha != 2) {

						flagescolha++;
					}

				}

				if (key == KeyEvent.VK_LEFT) {

					if (flagescolha != 1) {

						flagescolha--;
					}

				}
			}

			if (key == KeyEvent.VK_SPACE) {

				if (inimigos.size() <= 0 && !flag) {

					flagfim = true;

				}

				if (!jogando) {

					switch (flagescolha) {
					case 1: {

						listener.faseRepitida();
						break;
					}
					case 2: {

						listener.faseAbandonada();
						break;
					}

					default: {

						break;
					}
					}
				}
			}

			if (key == KeyEvent.VK_EQUALS || key == KeyEvent.VK_ADD) {
				if (volumeIncreaseTimer == null || !volumeIncreaseTimer.isRunning()) {
					volumeIncreaseTimer = new Timer(100, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent evt) {
							if (volumeFase < MAX_VOLUME) {
								volumeFase = Math.min(volumeFase + VOLUME_STEP, MAX_VOLUME);
								alterarVolume(clipfase, volumeFase);
							}
							if (jogador.getVolumeTiro() < MAX_VOLUME) {
								jogador.setVolumeTiro(Math.min(jogador.getVolumeTiro() + VOLUME_STEP, MAX_VOLUME));
								alterarVolume(jogador.getClip(), jogador.getVolumeTiro());
							}
						}
					});
					volumeIncreaseTimer.start();
				}
			} else if (key == KeyEvent.VK_SUBTRACT || key == KeyEvent.VK_MINUS) {
				if (volumeDecreaseTimer == null || !volumeDecreaseTimer.isRunning()) {
					volumeDecreaseTimer = new Timer(100, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent evt) {
							if (volumeFase > MIN_VOLUME) {
								volumeFase = Math.max(volumeFase - VOLUME_STEP, MIN_VOLUME);
								alterarVolume(clipfase, volumeFase);
							}
							if (jogador.getVolumeTiro() > MIN_VOLUME) {
								jogador.setVolumeTiro(Math.max(jogador.getVolumeTiro() - VOLUME_STEP, MIN_VOLUME));
								alterarVolume(jogador.getClip(), jogador.getVolumeTiro());
							}
						}
					});
					volumeDecreaseTimer.start();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			if (jogador != null) {
				jogador.keyReleased(e);
			}
			int key = e.getKeyCode();
			if ((key == KeyEvent.VK_EQUALS || key == KeyEvent.VK_ADD) && volumeIncreaseTimer != null) {
				volumeIncreaseTimer.stop();
			} else if ((key == KeyEvent.VK_SUBTRACT || key == KeyEvent.VK_MINUS) && volumeDecreaseTimer != null) {
				volumeDecreaseTimer.stop();
			}
		}
	}
}
