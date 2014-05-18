package swing;

import graphic.Objeto3D;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizador3D {

	private JFrame frame;
	protected Objeto3D g = new Objeto3D();
	private Imagen3D panelImagen;
	private JPanel panel;
	private JPanel panel_1;
	private JButton btnRotar;
	private MouseHandler handler;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Visualizador3D window = new Visualizador3D();
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
	public Visualizador3D() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 48, 0};
		gridBagLayout.rowHeights = new int[]{261, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		panelImagen = new Imagen3D();
		GridBagConstraints gbc_canvasImagen = new GridBagConstraints();
		gbc_canvasImagen.insets = new Insets(0, 0, 0, 5);
		gbc_canvasImagen.gridx = 0;
		gbc_canvasImagen.gridy = 0;
		frame.getContentPane().add(panelImagen, gbc_canvasImagen);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{48, 0};
		gbl_panel.rowHeights = new int[]{261, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{48, 0};
		gbl_panel_1.rowHeights = new int[]{59, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnCargar = new JButton("Cargar");
		GridBagConstraints gbc_btnCargar = new GridBagConstraints();
		gbc_btnCargar.insets = new Insets(0, 0, 5, 0);
		gbc_btnCargar.anchor = GridBagConstraints.NORTH;
		gbc_btnCargar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCargar.gridx = 0;
		gbc_btnCargar.gridy = 0;
		panel_1.add(btnCargar, gbc_btnCargar);
		
		btnRotar = new JButton("Rotar");
		btnRotar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handler.setRotar(!handler.isRotar());
			}
		});
		GridBagConstraints gbc_btnRotar = new GridBagConstraints();
		gbc_btnRotar.gridx = 0;
		gbc_btnRotar.gridy = 1;
		panel_1.add(btnRotar, gbc_btnRotar);
		btnCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cargar();
			}
		});
	}

	protected void cargar() {
		g.loadSUR(new File("sur/747.sur"));
//		g.trasladar(20, 20, 20);
//		g.escalaIsotropica(10);
//		g.rotacionZ(Math.PI);
//		g.trasladar(600, 200, 20);
		
		
		panelImagen.objeto = g;
		panelImagen.cargado = true;

		panelImagen.updateUI();
		
		handler = new MouseHandler(panelImagen);
		handler.setMover(true);
		panelImagen.addMouseListener(handler);
		panelImagen.addMouseMotionListener(handler);
		
	}

}
