import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SpringLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.applet.*;
public class PacMan {

	public int speed = 4, pacX = 220, pacY = 190, score = 0;
	private JFrame frame;
	public Element pacman = new Element(pacX, pacY, 30, 30, Color.yellow);
	JLabel lblNewLabel_ContadorScore = new JLabel(String.valueOf(score));

	ArrayList<Element> paredes = new ArrayList<Element>();
	ArrayList<Element> monedas = new ArrayList<Element>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PacMan window = new PacMan();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PacMan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		crearParedes();
		crearMonedas();
		JPanel tablero = new JPanel();
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				// IF�S PARA MOVER EL PACMAN (IF�S INTERNOS SON PARA TELETRANSPORTARLO AL CRUZAR
				// LOS LADOS DE LA VENTANA)
				if (e.getKeyCode() == 87) {
					pacman.y -= speed;
				}
				if (e.getKeyCode() == 83) {
					pacman.y += speed;
				}
				if (e.getKeyCode() == 65) {
					pacman.x -= speed;
					if (pacman.x <= -30) {
						pacman.x = 450;
					}
				}
				if (e.getKeyCode() == 68) {
					pacman.x += speed;
					if (pacman.x >= 480) {
						pacman.x = -20;
					}
				}

				// IF�S PARA VERIFICAR SI QUIERE IR DE ABAJO HACIA ARRIBA O VICEVERSA
				if (pacman.y == 0) {
					pacman.y += speed;
				}
				if (pacman.y == 390) {
					pacman.y -= speed;
				}

				// IF�S PARA VERIFICAR SI HAY UNA COLISION CON UNA PARED
				// FOR EACH PARA VERIFICAR SI EL PACMAN ESTA COLISIONANDO CON CADA PARED QUE
				// HAYA EN EL TABLERO
				for (Element element : paredes) {
					if (pacman.tocando(element)) {
						if (e.getKeyCode() == 87) {
							pacman.y += speed;
						}
						if (e.getKeyCode() == 83) {
							pacman.y -= speed;
						}
						if (e.getKeyCode() == 65) {
							pacman.x += speed;
						}
						if (e.getKeyCode() == 68) {
							pacman.x -= speed;
						}
					}
				}

				// IF�S PARA VERIFICAR SI HAY UNA COLISION CON UNA MONEDA
				// FOR EACH PARA VERIFICAR SI EL PACMAN ESTA COLISIONANDO CON CADA MONEDA QUE
				// HAYA EN EL TABLERO
				for (Element element : monedas) {
					if (pacman.x + 10 >= element.x && pacman.x + 10 <= element.x + element.w
							&& pacman.y + 10 >= element.y && pacman.y + 10 <= element.y + element.h) {
						score++;
						lblNewLabel_ContadorScore.setText(String.valueOf(score));
						monedas.remove(element);
						AudioClip musicaFondo = java.applet.Applet.newAudioClip(getClass().getResource("/eat-coin.wav"));
						musicaFondo.play();
					}
				}
				tablero.repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		frame.setFocusable(true);
		frame.requestFocus();

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 3, 0, 0));

		JButton btnNewButton = new JButton("Reiniciar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pacman.x = 220;
				pacman.y = 190;
				score = 0;
				lblNewLabel_ContadorScore.setText(String.valueOf(score));
				crearMonedas();
				tablero.repaint();
				frame.setFocusable(true);
				frame.requestFocus();
			}
		});
		panel.add(btnNewButton);

		JLabel label = new JLabel("");
		panel.add(label);

		JLabel lblNewLabel = new JLabel("SCORE: ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);

		lblNewLabel_ContadorScore.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel_ContadorScore);

		tablero.setBackground(new Color(51, 255, 153));
		frame.getContentPane().add(tablero, BorderLayout.CENTER);
		tablero.add(new MyGraphics());
	}

	public class MyGraphics extends JComponent {

		private static final long serialVersionUID = 1L;

		MyGraphics() {
			setPreferredSize(new Dimension(480, 420));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// fondo
			g.fillRect(0, 0, 480, 420);

			// pacman
			g.setColor(pacman.c);
			g.fillArc(pacman.x, pacman.y, pacman.w, pacman.h, 0, 360);

			// pared
			// FOR EACH PARA BUSCAR CADA PARED DENTRO DEL ARRAYLIST PAREDES Y PINTARLAS
			for (Element element : paredes) {
				g.setColor(element.c);
				g.fillRect(element.x, element.y, element.w, element.h);
			}

			// FOR EACH PARA BUSCAR CADA MONEDA DENTRO DEL ARRAYLIST MONEDAS Y PINTARLAS
			for (Element element : monedas) {
				g.setColor(element.c);
				g.fillOval(element.x, element.y, element.w, element.h);
			}

		}
	}

	public class Element {
		int x, y, h, w;
		Color c;

		public Element(int x, int y, int h, int w, Color c) {
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.c = c;
		}

		public boolean tocando(Element e) {
			if (this.x < e.x + e.w && this.x + this.w > e.x && this.y < e.y + e.h && this.y + this.h > e.y) {
				return true;
			} else {
				return false;
			}
		}
	}

	// METODO PARA CREAR LAS PAREDES DEL JUEGO
	public void crearParedes() {
		paredes.add(new Element(0, 20, 140, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(470, 20, 140, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(10, 150, 10, 40, Color.decode("#BAD6F0")));
		paredes.add(new Element(430, 150, 10, 40, Color.decode("#BAD6F0")));
		paredes.add(new Element(0, 0, 20, 500, Color.decode("#BAD6F0")));
		paredes.add(new Element(0, 400, 20, 500, Color.decode("#BAD6F0")));
		paredes.add(new Element(0, 300, 140, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(470, 300, 140, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(10, 300, 10, 40, Color.decode("#BAD6F0")));
		paredes.add(new Element(430, 300, 10, 40, Color.decode("#BAD6F0")));
		paredes.add(new Element(50, 150, 60, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(420, 150, 60, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(0, 200, 10, 60, Color.decode("#BAD6F0")));
		paredes.add(new Element(420, 200, 10, 60, Color.decode("#BAD6F0")));
		paredes.add(new Element(50, 250, 60, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(420, 250, 60, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(0, 250, 10, 60, Color.decode("#BAD6F0")));
		paredes.add(new Element(420, 250, 10, 60, Color.decode("#BAD6F0")));
		paredes.add(new Element(230, 0, 80, 10, Color.decode("#BAD6F0")));

		paredes.add(new Element(50, 53, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(100, 53, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(50, 53, 10, 50, Color.decode("#BAD6F0")));
		paredes.add(new Element(50, 103, 10, 60, Color.decode("#BAD6F0")));

		paredes.add(new Element(150, 130, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(200, 130, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(150, 130, 10, 50, Color.decode("#BAD6F0")));
		paredes.add(new Element(150, 180, 10, 60, Color.decode("#BAD6F0")));

		paredes.add(new Element(90, 180, 10, 30, Color.decode("#BAD6F0")));

		paredes.add(new Element(260, 130, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(310, 130, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(260, 130, 10, 50, Color.decode("#BAD6F0")));
		paredes.add(new Element(260, 180, 10, 60, Color.decode("#BAD6F0")));

		paredes.add(new Element(350, 180, 10, 30, Color.decode("#BAD6F0")));

		paredes.add(new Element(360, 53, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(410, 53, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(360, 53, 10, 50, Color.decode("#BAD6F0")));
		paredes.add(new Element(360, 103, 10, 60, Color.decode("#BAD6F0")));

		paredes.add(new Element(150, 260, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(200, 260, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(150, 260, 10, 50, Color.decode("#BAD6F0")));
		paredes.add(new Element(150, 310, 10, 60, Color.decode("#BAD6F0")));

		paredes.add(new Element(260, 260, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(310, 260, 50, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(260, 260, 10, 50, Color.decode("#BAD6F0")));
		paredes.add(new Element(260, 310, 10, 60, Color.decode("#BAD6F0")));

		paredes.add(new Element(50, 340, 30, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(80, 340, 30, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(50, 340, 10, 30, Color.decode("#BAD6F0")));
		paredes.add(new Element(50, 360, 10, 40, Color.decode("#BAD6F0")));

		paredes.add(new Element(360, 340, 30, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(390, 340, 30, 10, Color.decode("#BAD6F0")));
		paredes.add(new Element(360, 340, 10, 30, Color.decode("#BAD6F0")));
		paredes.add(new Element(360, 360, 10, 40, Color.decode("#BAD6F0")));

		paredes.add(new Element(230, 370, 40, 10, Color.decode("#BAD6F0")));
	}

	public void crearMonedas() {
		for (int i = 30; i < 400; i += 40) {
			for (int j = 25; j < 450; j += 20) {
				monedas.add(new Element(j, i, 12, 12, Color.yellow));
			}
		}
	}
}
