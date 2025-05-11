package Jogo.conteudo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Fase02 extends Fases implements ActionListener {

	private tecladoAdapter meuKeyListener = new tecladoAdapter();

	private Image fundo;
	private Timer timer;
	private int flag = 0; // índice atual do diálogo (inicia em 0)
	private boolean flagTitle = true;
	private boolean flagbold = true;
	private boolean jogando;
	private Timer boldIncreaseTimer;
	private Timer boldDecreaseTimer;
	private Timer hideTimer;
	private Timer finishTimer;

	// Painel
	private JLabel boldLabel;
	private JLabel labelPular;
	private JLabel imageFase01Label;
	private String dial[]; // Diálogos carregados do arquivo
	private JTextArea dialogoTextArea;
	private JLabel imagemHeroLabel;
	private JLabel imagemNpcLabel;
	private Font font = new Font("Arial", 20, 24);
	private int bold = 250; // valor inicial do fade (opacidade)

	// Som
	private Clip clipfase;
	private boolean clipstop;
	private float volumeFase = -20.0f + getVolumeJogo();

	// Controle para notificar apenas uma vez o término da fase
	private boolean faseFinalizadaNotificada = true;

	public Fase02() {
		setFocusable(true);
		setDoubleBuffered(true);
		setLayout(null);
		setFocusTraversalKeysEnabled(false);

		// Carrega os diálogos do arquivo
		carregarDialogos("/cont/dial_F02_PT-pt.txt");

		iniciarSomFase("/cont/somfase2.wav");
		clipstop = false;

		ImageIcon ref = new ImageIcon(getClass().getResource("/cont/Background01.png"));
		fundo = ref.getImage();

		iniciarPainel();

		addKeyListener(meuKeyListener);

		timer = new Timer(5, this);
		timer.start();

		jogando = true;
	}

	@Override
	public void iniciarFase() {
		// Implementar se necessário
	}

	@Override
	public void encerrarFase() {
		removeKeyListener(meuKeyListener);
		setFocusable(false);
		setEnabled(false);
		timer.stop();
		if (hideTimer != null && hideTimer.isRunning()) {
			hideTimer.stop();
		}
		if (finishTimer != null && finishTimer.isRunning()) {
			finishTimer.stop();
		}
		if (boldIncreaseTimer != null && boldIncreaseTimer.isRunning()) {
			boldIncreaseTimer.stop();
		}
		if (boldDecreaseTimer != null && boldDecreaseTimer.isRunning()) {
			boldDecreaseTimer.stop();
		}
		if (clipfase != null) {
			clipfase.stop();
		}

	}

	public void iniciarPainel() {

		labelPular = new JLabel("Esc para pular");

		imagemHeroLabel = new JLabel();
		imagemNpcLabel = new JLabel();
		imageFase01Label = new JLabel();

		ImageIcon iconHero = new ImageIcon(getClass().getResource("/cont/hero.png"));
		Image imgHero = iconHero.getImage().getScaledInstance(45 * 3, 43 * 3, Image.SCALE_SMOOTH);

		ImageIcon iconNpc = new ImageIcon(getClass().getResource("/cont/npc.png"));
		Image imgNpc = iconNpc.getImage().getScaledInstance(45 * 3, 43 * 3, Image.SCALE_SMOOTH);

		ImageIcon iconFase01 = new ImageIcon(getClass().getResource("/cont/fase02.png"));
		Image imgFase01 = iconFase01.getImage().getScaledInstance(1024 / 2, 131 / 2, Image.SCALE_SMOOTH);

		imagemHeroLabel.setIcon(new ImageIcon(imgHero));
		imagemHeroLabel.setBounds(50, 420, 45 * 3, 43 * 3);

		imagemNpcLabel.setIcon(new ImageIcon(imgNpc));
		imagemNpcLabel.setBounds(1024 - 200, 420, 45 * 3, 43 * 3);

		imageFase01Label.setIcon(new ImageIcon(imgFase01));
		imageFase01Label.setBounds(0, 290, 600, 131);
		imageFase01Label.setVisible(false);

		labelPular.setBounds(850, 20, 200, 30);
		labelPular.setForeground(Color.WHITE);
		
		// Inicializa o diálogo com o primeiro elemento do array dial
		dialogoTextArea = new JTextArea(dial[0]);
		dialogoTextArea.setFont(font);
		dialogoTextArea.setForeground(Color.WHITE);
		dialogoTextArea.setBounds(24, 550, 964, 120);
		dialogoTextArea.setBackground(new Color(0, 0, 0, 150));
		dialogoTextArea.setEditable(false); // Para que o jogador não edite o texto
		dialogoTextArea.setLineWrap(true);
		dialogoTextArea.setWrapStyleWord(true);
		dialogoTextArea.setMargin(new Insets(10, 10, 10, 10));

		boldLabel = new JLabel("");
		boldLabel.setBounds(0, 0, 1024, 720);
		boldLabel.setBackground(new Color(0, 0, 0, bold));
		boldLabel.setOpaque(true);

		setLayout(null);
		add(imageFase01Label);
		add(boldLabel);
		add(labelPular);
		add(dialogoTextArea);
		add(imagemNpcLabel);
		add(imagemHeroLabel);

		revalidate();
		repaint();
	}

	private void carregarDialogos(String caminhoArquivo) {
		List<String> dialogos = new ArrayList<>();
		try (InputStream is = getClass().getResourceAsStream(caminhoArquivo);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

			if (is == null) {
				System.err.println("Arquivo de diálogo não encontrado: " + caminhoArquivo);
				return;
			}

			String linha;
			while ((linha = reader.readLine()) != null) {
				dialogos.add(linha);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		dial = dialogos.toArray(new String[0]);
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
		if (jogando) {
			graficos.drawImage(fundo, 0, 0, this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (flag == 5) {
			ImageIcon iconNpc = new ImageIcon(getClass().getResource("/cont/npc.png"));
			Image imgNpc = iconNpc.getImage().getScaledInstance(45 * 3, 43 * 3, Image.SCALE_SMOOTH);
			imagemNpcLabel.setIcon(new ImageIcon(imgNpc));
			imagemNpcLabel.setBounds(1024 - 200, 420, 45 * 3, 43 * 3);
		}

		if (flag == 5) {
			ImageIcon iconHero = new ImageIcon(getClass().getResource("/cont/serioushero.png"));
			Image imgHero = iconHero.getImage().getScaledInstance(45 * 3, 43 * 3, Image.SCALE_SMOOTH);
			imagemHeroLabel.setIcon(new ImageIcon(imgHero));
			imagemHeroLabel.setBounds(50, 420, 45 * 3, 43 * 3);
		}

		if (flag == 11) {
			ImageIcon iconHero = new ImageIcon(getClass().getResource("/cont/hero.png"));
			Image imgHero = iconHero.getImage().getScaledInstance(45 * 3, 43 * 3, Image.SCALE_SMOOTH);
			imagemHeroLabel.setIcon(new ImageIcon(imgHero));
			imagemHeroLabel.setBounds(50, 420, 45 * 3, 43 * 3);
		}

		if ((boldIncreaseTimer == null || !boldIncreaseTimer.isRunning()) && !flagbold && flag >= dial.length
				&& flagTitle) {
			boldIncreaseTimer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					if (bold < 250) {
						bold = Math.min(bold + 50, 250);
						boldLabel.setBackground(new Color(0, 0, 0, bold));
					} else {
						// Fade-in completo: para o timer e inicia a sequência de exibição da imagem
						boldIncreaseTimer.stop();
						flagTitle = false;
						// Exibe a imagem
						imageFase01Label.setVisible(true);
						// Timer para manter a imagem por 2000 ms
						hideTimer = new Timer(2000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								imageFase01Label.setVisible(false);
								// Timer para encerrar a fase após 500 ms
								finishTimer = new Timer(500, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if (!clipstop) {
											clipstop = true;
											encerrarFase();
											faseFinalizadaNotificada = false;
										}

										if (jogando && !faseFinalizadaNotificada) {
											if (listener != null) {
												listener.faseFinalizada();
											}
											faseFinalizadaNotificada = true;
										}
									}
								});
								finishTimer.setRepeats(false);
								finishTimer.start();
							}
						});
						hideTimer.setRepeats(false);
						hideTimer.start();
					}
				}
			});
			boldIncreaseTimer.start();
		}

		if ((boldDecreaseTimer == null || !boldDecreaseTimer.isRunning()) && flag == 0 && flagbold) {

			boldDecreaseTimer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					if (bold >= 0) {
						bold = Math.max(bold - 50, 0);
					}
					boldLabel.setBackground(new Color(0, 0, 0, bold));
				}
			});
			boldDecreaseTimer.start();
		}

		repaint();
	}

	private class tecladoAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_SPACE) {
				// Se o diálogo atual for o último, encerra a exibição
				if (flag >= dial.length - 1) {
					if (boldDecreaseTimer != null && boldDecreaseTimer.isRunning()) {
						boldDecreaseTimer.stop();
					}
					flagbold = false;
				} else {
					// Avança para o próximo diálogo e atualiza a label

					dialogoTextArea.setText(dial[flag]);
				}
				flag++;

			}

			if (key == KeyEvent.VK_ESCAPE) {

				boldDecreaseTimer.stop();

				flagbold = false;

				flag = 15;

			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Não implementado
		}
	}
}
