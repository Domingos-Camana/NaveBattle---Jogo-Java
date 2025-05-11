package Jogo.conteudo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class MainMenu extends Fases implements ActionListener {

	private tecladoAdapter meuKeyListener = new tecladoAdapter();

	private Image fundo;
	private Timer timer;
	private int flag1 = 1;
	private int flag3 = 1;
	private int flag2 = 1;
	private int flag4 = 2;
	private boolean flagIniciar = false;
	private boolean flagMenus = false;
	private boolean flagbold = true;
	private boolean jogando;
	private Timer boldIncreaseTimer;

	// Painel
	private JLabel boldLabel;
	private JLabel iniciarLabel;
	private JLabel opcoesLabel;
	private JLabel sairLabel;
	private JLabel dificuldadeLabel;
	private JLabel volumeLabel;
	private JLabel seta1;
	private JLabel seta2;
	private JLabel seta3;
	private JLabel seta4;
	private JTextArea dificulLabel;
	private JTextArea voluLabel;
	private JLabel voltarLabel;
	private JLabel imagemNave3Label;
	private JLabel imagemNave4Label;
	private JLabel imagemNave5Label;
	private JLabel imageNaveBattleLabel;
	private JLabel imagemNaveLabel;
	private JLabel imagemNave1Label;
	private JLabel imagemNave2Label;
	private int som = 10;
	private Font font = new Font("Arial", 0, 38);
	private int bold = 0; // valor inicial do fade (opacidade)

	// Som
	private Clip clipfase;
	private boolean clipstop;
	private float volumeFase = -20.0f;

	// Controle para notificar apenas uma vez o término da fase
	private boolean faseFinalizadaNotificada = true;

	public MainMenu() {
		setFocusable(true);
		setDoubleBuffered(true);
		setLayout(null);
		setFocusTraversalKeysEnabled(false);

		// Carrega os diálogos do arquivo

		iniciarSomFase("/cont/somfase2.wav");
		clipstop = false;

		ImageIcon ref = new ImageIcon(getClass().getResource("/cont/Background00.png"));
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
		if (boldIncreaseTimer != null && boldIncreaseTimer.isRunning()) {
			boldIncreaseTimer.stop();
		}

		if (clipfase != null) {
			clipfase.stop();
		}
	}

	public void iniciarPainel() {

		imagemNaveLabel = new JLabel();
		imagemNave1Label = new JLabel();
		imagemNave2Label = new JLabel();
		imagemNave3Label = new JLabel();
		imagemNave4Label = new JLabel();
		imagemNave5Label = new JLabel();
		imageNaveBattleLabel = new JLabel();
		seta1 = new JLabel();
		seta2 = new JLabel();
		seta3 = new JLabel();
		seta4 = new JLabel();

		iniciarLabel = new JLabel();
		opcoesLabel = new JLabel();
		sairLabel = new JLabel();
		dificuldadeLabel = new JLabel();
		dificulLabel = new JTextArea();
		volumeLabel = new JLabel();
		voluLabel = new JTextArea();
		voltarLabel = new JLabel();

		ImageIcon iconNave = new ImageIcon(getClass().getResource("/cont/nave.png"));
		Image imgNave = iconNave.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

		ImageIcon iconFase01 = new ImageIcon(getClass().getResource("/cont/navebattle.png"));
		Image imgFase01 = iconFase01.getImage().getScaledInstance(1024, 249, Image.SCALE_SMOOTH);

		iniciarLabel.setText("INICIAR");
		iniciarLabel.setBounds(430, 390, 140, 40);
		iniciarLabel.setFont(font);
		iniciarLabel.setForeground(Color.WHITE);
		iniciarLabel.setBackground(new Color(0, 0, 0, 150));

		imagemNaveLabel.setIcon(new ImageIcon(imgNave));
		imagemNaveLabel.setBounds(390, 390, 30, 30);

		opcoesLabel.setText("OPÇÕES");
		opcoesLabel.setBounds(417, 440, 165, 45);
		opcoesLabel.setFont(font);
		opcoesLabel.setForeground(Color.WHITE);
		opcoesLabel.setBackground(new Color(0, 0, 0, 150));

		imagemNave1Label.setIcon(new ImageIcon(imgNave));
		imagemNave1Label.setBounds(377, 440, 30, 30);

		sairLabel.setText("SAIR");
		sairLabel.setBounds(455, 490, 89, 40);
		sairLabel.setFont(font);
		sairLabel.setForeground(Color.WHITE);
		sairLabel.setBackground(new Color(0, 0, 0, 150));

		imagemNave2Label.setIcon(new ImageIcon(imgNave));
		imagemNave2Label.setBounds(415, 490, 30, 30);

		dificuldadeLabel.setText("DIFICULDADE");
		dificuldadeLabel.setBounds(140, 130, 270, 40);
		dificuldadeLabel.setFont(font);
		dificuldadeLabel.setForeground(Color.WHITE);
		dificuldadeLabel.setBackground(new Color(0, 0, 0, 150));
		dificuldadeLabel.setVisible(false);

		imagemNave3Label.setIcon(new ImageIcon(imgNave));
		imagemNave3Label.setBounds(100, 130, 30, 30);
		imagemNave3Label.setVisible(false);

		seta1.setText("<");
		seta1.setBounds(590, 130, 30, 40);
		seta1.setFont(font);
		seta1.setForeground(Color.WHITE);
		seta1.setBackground(new Color(0, 0, 0, 150));
		seta1.setVisible(false);

		dificulLabel.setText("NORMAL");
		dificulLabel.setBounds(630, 130, 200, 40);
		dificulLabel.setFont(font);
		dificulLabel.setForeground(Color.WHITE);
		dificulLabel.setBackground(new Color(0, 0, 0, 150));
		dificulLabel.setVisible(false);
		dificulLabel.setEditable(false);
		dificulLabel.setMargin(new Insets(0, 20, 0, 0));

		seta2.setText(">");
		seta2.setBounds(840, 130, 30, 40);
		seta2.setFont(font);
		seta2.setForeground(Color.WHITE);
		seta2.setBackground(new Color(0, 0, 0, 150));
		seta2.setVisible(false);

		volumeLabel.setText("VOLUME");
		volumeLabel.setBounds(140, 230, 180, 40);
		volumeLabel.setFont(font);
		volumeLabel.setForeground(Color.WHITE);
		volumeLabel.setBackground(new Color(0, 0, 0, 150));
		volumeLabel.setVisible(false);

		imagemNave4Label.setIcon(new ImageIcon(imgNave));
		imagemNave4Label.setBounds(100, 230, 30, 30);
		imagemNave4Label.setVisible(false);

		seta3.setText("<");
		seta3.setBounds(590, 230, 30, 40);
		seta3.setFont(font);
		seta3.setForeground(Color.WHITE);
		seta3.setBackground(new Color(0, 0, 0, 150));
		seta3.setVisible(false);

		voluLabel.setText("" + som);
		voluLabel.setBounds(630, 230, 200, 40);
		voluLabel.setFont(font);
		voluLabel.setForeground(Color.WHITE);
		voluLabel.setBackground(new Color(0, 0, 0, 150));
		voluLabel.setVisible(false);
		voluLabel.setEditable(false);
		voluLabel.setMargin(new Insets(0, 80, 0, 0));

		seta4.setText(">");
		seta4.setBounds(840, 230, 30, 40);
		seta4.setFont(font);
		seta4.setForeground(Color.WHITE);
		seta4.setBackground(new Color(0, 0, 0, 150));
		seta4.setVisible(false);

		voltarLabel.setText("VOLTAR");
		voltarLabel.setBounds(425, 490, 155, 40);
		voltarLabel.setFont(font);
		voltarLabel.setForeground(Color.WHITE);
		voltarLabel.setBackground(new Color(0, 0, 0, 150));
		voltarLabel.setVisible(false);

		imagemNave5Label.setIcon(new ImageIcon(imgNave));
		imagemNave5Label.setBounds(385, 490, 30, 30);
		imagemNave5Label.setVisible(false);

		imageNaveBattleLabel.setIcon(new ImageIcon(imgFase01));
		imageNaveBattleLabel.setBounds(5, 100, 1024, 249);

		boldLabel = new JLabel("");
		boldLabel.setBounds(0, 0, 1024, 720);
		boldLabel.setBackground(new Color(0, 0, 0, bold));
		boldLabel.setOpaque(true);

		setLayout(null);
		add(imageNaveBattleLabel);
		add(iniciarLabel);
		add(opcoesLabel);
		add(sairLabel);
		add(boldLabel);
		add(dificuldadeLabel);
		add(dificulLabel);
		add(volumeLabel);
		add(voluLabel);
		add(voltarLabel);
		add(imagemNaveLabel);
		add(imagemNave1Label);
		add(imagemNave2Label);
		add(imagemNave3Label);
		add(imagemNave4Label);
		add(imagemNave5Label);
		add(seta1);
		add(seta2);
		add(seta3);
		add(seta4);

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

		if (flagbold) {

			if (flagMenus) {

				switch (flag2) {
				case 1: {

					imagemNave3Label.setVisible(true);
					imagemNave4Label.setVisible(false);
					imagemNave5Label.setVisible(false);
					seta1.setVisible(true);
					seta2.setVisible(true);
					seta3.setVisible(false);
					seta4.setVisible(false);

					dificuldadeLabel.setOpaque(true);
					dificulLabel.setOpaque(true);
					volumeLabel.setOpaque(false);
					voluLabel.setOpaque(false);
					seta1.setOpaque(false);
					seta2.setOpaque(false);
					seta3.setOpaque(false);
					seta4.setOpaque(false);

					if (flag3 == 1) {
						seta1.setOpaque(true);
					} else {
						seta2.setOpaque(true);
					}

					voltarLabel.setOpaque(false);

					break;
				}
				case 2: {

					imagemNave3Label.setVisible(false);
					imagemNave4Label.setVisible(true);
					imagemNave5Label.setVisible(false);
					seta4.setVisible(true);
					seta3.setVisible(true);
					seta2.setVisible(false);
					seta1.setVisible(false);

					dificuldadeLabel.setOpaque(false);
					dificulLabel.setOpaque(false);
					volumeLabel.setOpaque(true);
					voluLabel.setOpaque(true);
					voltarLabel.setOpaque(false);
					seta1.setOpaque(false);
					seta2.setOpaque(false);
					seta3.setOpaque(false);
					seta4.setOpaque(false);

					if (flag3 == 1) {
						seta3.setOpaque(true);
					} else {
						seta4.setOpaque(true);
					}

					break;
				}

				case 3: {

					imagemNave3Label.setVisible(false);
					imagemNave4Label.setVisible(false);
					imagemNave5Label.setVisible(true);
					seta4.setVisible(false);
					seta3.setVisible(false);
					seta2.setVisible(false);
					seta1.setVisible(false);

					dificuldadeLabel.setOpaque(false);
					dificulLabel.setOpaque(false);
					volumeLabel.setOpaque(false);
					voluLabel.setOpaque(false);
					voltarLabel.setOpaque(true);
					seta1.setOpaque(false);
					seta2.setOpaque(false);
					seta3.setOpaque(false);
					seta4.setOpaque(false);

					break;
				}

				default: {

					break;
				}
				}

			} else {

				switch (flag1) {
				case 1: {

					imagemNaveLabel.setVisible(true);
					imagemNave1Label.setVisible(false);
					imagemNave2Label.setVisible(false);

					iniciarLabel.setOpaque(true);
					opcoesLabel.setOpaque(false);
					sairLabel.setOpaque(false);
					break;
				}
				case 2: {

					imagemNaveLabel.setVisible(false);
					imagemNave1Label.setVisible(true);
					imagemNave2Label.setVisible(false);

					iniciarLabel.setOpaque(false);
					opcoesLabel.setOpaque(true);
					sairLabel.setOpaque(false);
					break;
				}

				case 3: {

					imagemNaveLabel.setVisible(false);
					imagemNave1Label.setVisible(false);
					imagemNave2Label.setVisible(true);

					iniciarLabel.setOpaque(false);
					opcoesLabel.setOpaque(false);
					sairLabel.setOpaque(true);
					break;
				}

				default: {

					break;
				}
				}
			}
		}

		if ((boldIncreaseTimer == null || !boldIncreaseTimer.isRunning()) && !flagbold) {
			boldIncreaseTimer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					if (bold < 250) {
						bold = Math.min(bold + 50, 250);
						boldLabel.setBackground(new Color(0, 0, 0, bold));
					} else {
						flagIniciar = true;// Fade-in completo: para o timer e inicia a sequência de exibição da imagem
					}
				}
			});
			boldIncreaseTimer.start();
		}

		if (!clipstop && flagIniciar) {
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

		repaint();
	}

	private class tecladoAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (flagMenus) {

				if (key == KeyEvent.VK_SPACE) {

					switch (flag2) {
					case 1: {

						if (flag3 == 1 && flag4 == 2) {

							dificulLabel.setText("FÁCIL");
							dificulLabel.setMargin(new Insets(0, 50, 0, 0));
							flag4--;
						}
						
						if (flag3 == 2 && flag4 == 2) {

							dificulLabel.setText("DÍFICIL");
							dificulLabel.setMargin(new Insets(0, 40, 0, 0));
							flag4++;
						}

						if (flag3 == 2 && flag4 == 1) {

							dificulLabel.setText("NORMAL");
							dificulLabel.setMargin(new Insets(0, 20, 0, 0));
							flag4++;
						}

						if (flag3 == 1 && flag4 == 3) {

							dificulLabel.setText("NORMAL");
							dificulLabel.setMargin(new Insets(0, 20, 0, 0));
							flag4--;
						}

						break;
					}
					case 2: {

						if (flag3 == 1 && som != 10) {

							setVolumeJogo(getVolumeJogo()-0.5f);
							som -= 10;
							voluLabel.setText("" + som);
							volumeFase -= getVolumeJogo();
							alterarVolume(clipfase, volumeFase);
						}
						
						if (flag3 == 2 && som != 90) {

							setVolumeJogo(getVolumeJogo()+0.5f);
							som += 10;
							voluLabel.setText("" + som);
							volumeFase += getVolumeJogo();
							alterarVolume(clipfase, volumeFase);
						}

						break;
					}

					case 3: {

						imageNaveBattleLabel.setVisible(true);
						iniciarLabel.setVisible(true);
						opcoesLabel.setVisible(true);
						sairLabel.setVisible(true);
						imagemNave3Label.setVisible(false);
						imagemNave4Label.setVisible(false);
						imagemNave5Label.setVisible(false);
						imagemNave5Label.setVisible(false);
						dificuldadeLabel.setVisible(false);
						dificulLabel.setVisible(false);
						voltarLabel.setVisible(false);
						volumeLabel.setVisible(false);
						voluLabel.setVisible(false);
						voltarLabel.setOpaque(false);
						flag2 = 1;
						flag3 = 1;
						flagMenus = !flagMenus;

						break;

					}

					default: {

						break;
					}
					}

				}

				if (key == KeyEvent.VK_DOWN) {

					if (flag2 != 3) {

						flag2++;
					}

				}

				if (key == KeyEvent.VK_UP) {

					if (flag2 != 1) {

						flag2--;
					}

				}

				if (key == KeyEvent.VK_RIGHT) {

					if (flag3 != 2) {

						flag3++;
					}

				}

				if (key == KeyEvent.VK_LEFT) {

					if (flag3 != 1) {

						flag3--;
					}

				}

			} else {

				if (key == KeyEvent.VK_SPACE) {

					switch (flag1) {
					case 1: {

						iniciarLabel.setVisible(false);
						opcoesLabel.setVisible(false);
						sairLabel.setVisible(false);
						imageNaveBattleLabel.setVisible(false);
						imagemNaveLabel.setVisible(false);
						imagemNave1Label.setVisible(false);
						imagemNave2Label.setVisible(false);
						flagbold = false;
						break;
					}
					case 2: {

						imageNaveBattleLabel.setVisible(false);
						iniciarLabel.setVisible(false);
						opcoesLabel.setVisible(false);
						sairLabel.setVisible(false);
						imagemNaveLabel.setVisible(false);
						imagemNave1Label.setVisible(false);
						imagemNave2Label.setVisible(false);
						sairLabel.setOpaque(false);
						iniciarLabel.setOpaque(false);
						opcoesLabel.setOpaque(false);
						dificuldadeLabel.setVisible(true);
						dificulLabel.setVisible(true);
						imagemNave3Label.setVisible(true);
						volumeLabel.setVisible(true);
						voluLabel.setVisible(true);
						imagemNave4Label.setVisible(true);
						voltarLabel.setVisible(true);
						imagemNave5Label.setVisible(true);
						flagMenus = !flagMenus;
						break;
					}

					case 3: {

						System.exit(0);
						break;
					}

					default: {

						break;
					}
					}

				}

				if (key == KeyEvent.VK_DOWN) {

					if (flag1 != 3) {

						flag1++;
					}

				}

				if (key == KeyEvent.VK_UP) {

					if (flag1 != 1) {

						flag1--;
					}

				}

			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Não implementado
		}
	}
}
