package Jogo.conteudo;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Jogador implements ActionListener {

    private int x, y, dx, dy;
    private Image image;
    private int altura, largura;
    private List<Tiros> tiros;
    private boolean Visivel, Turbo, pass;
    private Timer timer;
    private int VIDAS = 5;

    // Atributos adicionados para controle do som do tiro
    private Clip clipTiro;
    private float volumeTiro = -30.0f; // valor inicial do volume

    public Jogador(int x, int y) {
        
    	this.x = x;
        this.y = y;
        Visivel = true;
        Turbo = false;
        pass = false;
        tiros = new ArrayList<Tiros>();
        timer = new Timer(30000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Turbo == true) {
            turbo();
            Turbo = false;
        }
        if (Turbo == false) {
            load();
        }
    }

    public void load() {
        ImageIcon ref = new ImageIcon(getClass().getResource("/cont/nave.png"));
        image = ref.getImage();
        altura = ref.getIconHeight();
        largura = ref.getIconWidth();
    }

    public void update() {
        if (x <= 2) {
            dx = 0;
            x++;
        }
        if (x >= 950 && !pass) {
            dx = 0;
            x--;
        }
        
        if (y <= 80) {
            dy = 0;
            y++;
        }
        if (y >= 620) {
            dy = 0;
            y--;
        }
        x += dx;
        y += dy;
    }

    
    // Método para tocar o som do tiro utilizando o atributo volumeTiro
    private void tocarSom(String caminhoDoSom) {
        try {
            // O caminho deve ser relativo ao classpath (por exemplo, /cont/shot.wav)
            URL url = getClass().getResource(caminhoDoSom);
            if (url == null) {
                System.err.println("Recurso não encontrado: " + caminhoDoSom);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clipTiro = AudioSystem.getClip();
            clipTiro.open(audioInputStream);

            // Ajusta o volume do tiro conforme o atributo volumeTiro
            alterarVolume(clipTiro, volumeTiro);
            
            clipTiro.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alterarVolume(Clip clip, float novoVolume) {
    	if (clipTiro.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clipTiro.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(novoVolume);
        }
    }
    
    // Método para criar o tiro e tocar o som correspondente
    public void bullets1() {
        this.tiros.add(new Tiros(x + largura, y + (altura / 2)));
        tocarSom("/cont/shot1.wav");
    }

    public void turbo() {
        Turbo = true;
        ImageIcon ref = new ImageIcon(getClass().getResource("/cont/naveturbo.png"));
        image = ref.getImage();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura - 7, (altura-8));
    }

    public void keypressed(KeyEvent tecla) {
        int codigo = tecla.getKeyCode();

        if (codigo == KeyEvent.VK_D) {
            turbo();
        }

        if (codigo == KeyEvent.VK_A) {
            if (!Turbo) {
                bullets1();
            }
        }

        if (codigo == KeyEvent.VK_UP) {
            dy = -3;
        }

        if (codigo == KeyEvent.VK_DOWN) {
            dy = 3;
        }

        if (codigo == KeyEvent.VK_LEFT) {
            dx = -3;
        }

        if (codigo == KeyEvent.VK_RIGHT) {
            dx = 3;
        }
    }

    public void keyReleased(KeyEvent tecla) {
        int codigo = tecla.getKeyCode();

        if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_DOWN) {
            dy = 0;
        }

        if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Image getImage() {
        return image;
    }

    public boolean isVisivel() {
        return Visivel;
    }

    public void setVisivel(boolean visivel) {
        Visivel = visivel;
    }

    public List<Tiros> getTiros() {
        return tiros;
    }

    public boolean isTurbo() {
        return Turbo;
    }

    public void setTurbo(boolean turbo) {
        Turbo = turbo;
    }

    public int getVIDAS() {
        return VIDAS;
    }

    public void setVIDAS(int vIDAS) {
        VIDAS = vIDAS;
    }

    // Métodos para acesso e alteração do Clip e do volume do tiro
    public Clip getClip() {
        return clipTiro;
    }

    public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public void setVolumeTiro(float volume) {
        this.volumeTiro = volume;
        alterarVolume(clipTiro, volumeTiro);
    }
    
    // Novo método getVolumeTiro para recuperar o volume atual do tiro
    public float getVolumeTiro() {
        return this.volumeTiro;
    }
}
