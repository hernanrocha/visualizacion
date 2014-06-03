package swing;

import filter.FiltroArchivoSUR;
import graphic.Objeto3D;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.ButtonGroup;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JLabel;

import com.sun.org.apache.bcel.internal.generic.LALOAD;

public class Visualizador3D {

	private JFrame frmVisualizadord;
	protected Objeto3D g = new Objeto3D();
	private Imagen3D panelImagen;
	private JPanel panelOpciones;
	private JPanel panel_1;
	private JButton btnRotar;
	private MouseHandler mouseHandler;
	private JMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenuItem mntmCargarArchivo;
	private JRadioButton rdbtnMover;
	private JRadioButton rdbtnRotar;
	private JPanel panelAcciones;
	private final ButtonGroup groupAcciones = new ButtonGroup();
	private JLabel labelPosX;
	private JLabel lblPosY;
	private JRadioButton rdbtnAmpliar;
	private JRadioButton rdbtnReducir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Visualizador3D window = new Visualizador3D();
					window.frmVisualizadord.setVisible(true);
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
		
		/* Funcionalidad
		 *
		 * 1) Perspectiva, iluminacion (ambiente, origen), borrar los vectores saliente
		 * 2) Transformaciones Mouse - Deformaciones
		 * 3) Vista (triangulos, puntos, solido)
		 * 4) Centrar automaticamente
		 * 5) Detectar profundidad al rotar
		 * 6) Titulo, tamaño, logo, ayuda, about
		 * 
		 */

		initialize();
		
		// Opciones por defecto
		rdbtnMover.setSelected(true);
		
		rdbtnAmpliar = new JRadioButton("Ampliar");
		rdbtnAmpliar.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mouseHandler.setZoomIn(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		groupAcciones.add(rdbtnAmpliar);
		GridBagConstraints gbc_rdbtnAmpliar = new GridBagConstraints();
		gbc_rdbtnAmpliar.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnAmpliar.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAmpliar.insets = new Insets(0, 15, 0, 5);
		gbc_rdbtnAmpliar.gridx = 0;
		gbc_rdbtnAmpliar.gridy = 2;
		panelAcciones.add(rdbtnAmpliar, gbc_rdbtnAmpliar);
		
		rdbtnReducir = new JRadioButton("Reducir");
		rdbtnReducir.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mouseHandler.setZoomOut(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		groupAcciones.add(rdbtnReducir);
		GridBagConstraints gbc_rdbtnReducir = new GridBagConstraints();
		gbc_rdbtnReducir.insets = new Insets(0, 15, 0, 5);
		gbc_rdbtnReducir.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnReducir.anchor = GridBagConstraints.WEST;
		gbc_rdbtnReducir.gridx = 0;
		gbc_rdbtnReducir.gridy = 3;
		panelAcciones.add(rdbtnReducir, gbc_rdbtnReducir);
		
		labelPosX = new JLabel("New label");
		GridBagConstraints gbc_labelPosX = new GridBagConstraints();
		gbc_labelPosX.insets = new Insets(0, 0, 5, 0);
		gbc_labelPosX.gridx = 0;
		gbc_labelPosX.gridy = 2;
		panelOpciones.add(labelPosX, gbc_labelPosX);
		
		lblPosY = new JLabel("New label");
		GridBagConstraints gbc_lblPosY = new GridBagConstraints();
		gbc_lblPosY.gridx = 0;
		gbc_lblPosY.gridy = 3;
		panelOpciones.add(lblPosY, gbc_lblPosY);
		
		// Cargar SUR
		cargar(new File("sur/747.sur"));
//		cargarMisil();
//		cargarFork();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Ventana principal
		frmVisualizadord = new JFrame();
		frmVisualizadord.setTitle("Visualizador 3D");
		frmVisualizadord.setBounds(50, 50, 800, 700);
		frmVisualizadord.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 172, 0};
		gridBagLayout.rowHeights = new int[]{261, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frmVisualizadord.getContentPane().setLayout(gridBagLayout);
		
		// Panel Imagen
		panelImagen = new Imagen3D();
		
		GridBagConstraints gbc_panelImagen = new GridBagConstraints();
		gbc_panelImagen.fill = GridBagConstraints.BOTH;
		gbc_panelImagen.insets = new Insets(0, 0, 0, 5);
		gbc_panelImagen.gridx = 0;
		gbc_panelImagen.gridy = 0;
		frmVisualizadord.getContentPane().add(panelImagen, gbc_panelImagen);

		// Cargar Handler
		mouseHandler = new MouseHandler(this, panelImagen);
		
		// Panel Opciones
		panelOpciones = new JPanel();
		
		GridBagConstraints gbc_panelOpciones = new GridBagConstraints();
		gbc_panelOpciones.insets = new Insets(15, 15, 15, 15);
		gbc_panelOpciones.fill = GridBagConstraints.BOTH;
		gbc_panelOpciones.gridx = 1;
		gbc_panelOpciones.gridy = 0;
		frmVisualizadord.getContentPane().add(panelOpciones, gbc_panelOpciones);
		
		GridBagLayout gbl_panelOpciones = new GridBagLayout();
		gbl_panelOpciones.columnWidths = new int[]{48, 0};
		gbl_panelOpciones.rowHeights = new int[]{29, 38, 0, 0, 0};
		gbl_panelOpciones.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelOpciones.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelOpciones.setLayout(gbl_panelOpciones);
		
		initAcciones();
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		panelOpciones.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{48, 0};
		gbl_panel_1.rowHeights = new int[]{59, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		initMenu();
		

	}

	// Menu Bar
	private void initMenu() {
		
		menuBar = new JMenuBar();
		frmVisualizadord.setJMenuBar(menuBar);
		
		mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		mntmCargarArchivo = new JMenuItem("Cargar Archivo");
		mntmCargarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirArchivo();
			}
		});
		mnArchivo.add(mntmCargarArchivo);
		
	}

	// Panel Opciones - Menu Acciones
	private void initAcciones() {
		
		panelAcciones = new JPanel();
		panelAcciones.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		GridBagConstraints gbc_panelAcciones = new GridBagConstraints();
		gbc_panelAcciones.insets = new Insets(0, 0, 5, 0);
		gbc_panelAcciones.fill = GridBagConstraints.BOTH;
		gbc_panelAcciones.gridx = 0;
		gbc_panelAcciones.gridy = 0;
		panelOpciones.add(panelAcciones, gbc_panelAcciones);
		
		GridBagLayout gbl_panelAcciones = new GridBagLayout();
		gbl_panelAcciones.columnWidths = new int[]{53, 0};
		gbl_panelAcciones.rowHeights = new int[]{23, 0, 0, 0, 0};
		gbl_panelAcciones.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelAcciones.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelAcciones.setLayout(gbl_panelAcciones);
		
		rdbtnMover = new JRadioButton("Mover");
		rdbtnMover.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
		    	mouseHandler.setMover(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		groupAcciones.add(rdbtnMover);
		
		GridBagConstraints gbc_rdbtnMover = new GridBagConstraints();
		gbc_rdbtnMover.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnMover.insets = new Insets(5, 15, 0, 5);
		gbc_rdbtnMover.gridx = 0;
		gbc_rdbtnMover.gridy = 0;
		panelAcciones.add(rdbtnMover, gbc_rdbtnMover);
		
		rdbtnRotar = new JRadioButton("Rotar");
		rdbtnRotar.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
		    	mouseHandler.setRotar(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		groupAcciones.add(rdbtnRotar);
		
		GridBagConstraints gbc_rdbtnRotar = new GridBagConstraints();
		gbc_rdbtnRotar.insets = new Insets(0, 15, 0, 5);
		gbc_rdbtnRotar.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnRotar.gridx = 0;
		gbc_rdbtnRotar.gridy = 1;
		panelAcciones.add(rdbtnRotar, gbc_rdbtnRotar);
		
	}

	protected void cargar(File f) {
		// 1) Parsear archivo *.sur para cargar estructura
		g.loadSUR(f);
		
		// 2) Centrar objeto 3D
		g.centrar(1200, 680);
		
//		g.trasladar(20, 20, 20);
//		g.escalaIsotropica(10);
//		g.rotacionZ(Math.PI);
//		g.trasladar(600, 200, 20);
		
		
		panelImagen.objeto = g;
		panelImagen.cargado = true;
		panelImagen.updateUI();
		
//		handler.setMover(true);
		
		panelImagen.addMouseListener(mouseHandler);
		panelImagen.addMouseMotionListener(mouseHandler);
		panelImagen.addMouseWheelListener(mouseHandler);
		
	}
	
	protected void cargarMisil(){
		cargar(new File("sur/misil-ori.sur"));

		g.escalaIsotropica(10);
		
	}	

	private void cargarFork() {
		cargar(new File("sur/fork.sur"));				
	}
	
	protected void abrirArchivo() {
		JFileChooser fc = new JFileChooser("sur");

        // Mostrar la ventana para abrir archivo y recoger la respuesta
        fc.setFileFilter(new FiltroArchivoSUR());
        int respuesta = fc.showOpenDialog(null);

        // Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION){
            g.loadSUR(fc.getSelectedFile());
        }
	}

	public void setPosicionActual(int f, int c) {
		labelPosX.setText("" + c);
		lblPosY.setText("" + f);
	}

}
