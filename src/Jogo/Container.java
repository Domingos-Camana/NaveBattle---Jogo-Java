package Jogo;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import Jogo.conteudo.Fases;
import Jogo.conteudo.MainMenu;
import Jogo.conteudo.Fase01;
import Jogo.conteudo.Fase02;
import Jogo.conteudo.Fase03;
import Jogo.conteudo.Fase1;
import Jogo.conteudo.Fase2;
import Jogo.conteudo.Fase3;
import Jogo.conteudo.ListaFases;

public class Container extends JFrame {
    
    private List<Supplier<Fases>> faseSuppliers;
    private ImageIcon ref = new ImageIcon(getClass().getResource("/cont/nave.png"));
    private Image img = ref.getImage();
    private int indiceFaseAtual = 0;
    
    public Container() {
       
        faseSuppliers = new ArrayList<>();
        faseSuppliers.add(() -> new MainMenu());
        faseSuppliers.add(() -> new Fase01());
        faseSuppliers.add(() -> new Fase1());
        faseSuppliers.add(() -> new Fase02());
        faseSuppliers.add(() -> new Fase2());
        faseSuppliers.add(() -> new Fase03());
        faseSuppliers.add(() -> new Fase3());
        
        iniciarFase();
        
        setTitle("Nave Battle");
        setSize(1024, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setIconImage(img);
    }
    
    private void iniciarFase() {
        if (indiceFaseAtual >= faseSuppliers.size()) {
            System.out.println("Todas as fases foram concluídas!");
            return;
        }
        
        final Fases faseAtual = faseSuppliers.get(indiceFaseAtual).get();
        
        faseAtual.setListaFases(new ListaFases() {
            @Override
            public void faseFinalizada() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        faseAtual.encerrarFase();
                        getContentPane().removeAll();
                        indiceFaseAtual++;
                        iniciarFase();
                        getContentPane().revalidate();
                        getContentPane().repaint();
                    }
                });
            }

			@Override
			public void faseRepitida() {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        faseAtual.encerrarFase();
                        getContentPane().removeAll();
                        indiceFaseAtual--;
                        iniciarFase();
                        getContentPane().revalidate();
                        getContentPane().repaint();
                    }
                });
			}

			@Override
			public void faseAbandonada() {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        faseAtual.encerrarFase();
                        getContentPane().removeAll();
                        indiceFaseAtual=0;
                        iniciarFase();
                        getContentPane().revalidate();
                        getContentPane().repaint();
                    }
                });
			}
        });
         
        getContentPane().removeAll();
        getContentPane().add(faseAtual);
        getContentPane().revalidate();
        getContentPane().repaint();
        faseAtual.requestFocusInWindow();
    }
    
    public static void main(String[] args) {
        
    	
    	new Container();
    }
}