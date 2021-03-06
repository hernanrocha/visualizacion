package swing;

import filter.FiltroArchivoPNG;
import filter.FiltroArchivoSUR;
import graphic.Objeto3D;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import matriz.MatrizTransformacion;

import java.awt.Font;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Visualizador3D {

	private JFrame frmVisualizadord;
	private Imagen3D panelImagen;
	private JPanel panelOpciones;
	private JMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenuItem mntmCargarArchivo;
	private JLabel labelPosX;
	private JLabel lblPosY;
	private JComboBox comboVista;
	private JMenu mnAyuda;
	private JMenuItem mntmAcercaDe;
	private JTable tableTransformacion;
	private JButton btnAplicar;
	private JPanel panelTransformacion;
	private JPanel panelVisualizacion;
	private JCheckBox chckbxBackfaceCulling;
	private JPanel panelLuzAmbiente;
	private JPanel panelPosicion;
	private JLabel lblPosicionX;
	private JLabel lblPosicionY;
	private JSeparator separator;
	private JMenuItem mntmSalir;
	private JMenuItem mntmGuardarComo;
	private JComboBox comboLuz;
	private JPanel panelObjColor;
	private JLabel labelObjRed;
	private JSpinner spinObjRed;
	private JLabel lblObjMuestra;
	private JLabel labelObjGreen;
	private JSpinner spinObjGreen;
	private JLabel labelObjBlue;
	private JSpinner spinObjBlue;
	private JPanel panelAmbColor;
	private JLabel labelAmbRojo;
	private JSpinner spinAmbRed;
	private JLabel lblAmbMuestra;
	private JLabel labelAmbGreen;
	private JSpinner spinAmbGreen;
	private JLabel labelAmbBlue;
	private JSpinner spinAmbBlue;
	private JComboBox comboObjeto;
	
	File[] files = new File[]{new File("sur/747.sur"),
			new File("sur/horse.sur"),
			new File("sur/hand.sur"),
			new File("sur/hebe.sur"),
			new File("sur/misil.sur"),
			new File("sur/torus.sur")};
	
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
		initialize();		

		actualizarMuestraObjeto();
		
		actualizarMuestraAmbiente();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Ventana principal
		frmVisualizadord = new JFrame();
		frmVisualizadord.setIconImage(Toolkit.getDefaultToolkit().getImage(Visualizador3D.class.getResource("/icon/icon.png")));
		frmVisualizadord.setTitle("Visualizador 3D");
		frmVisualizadord.setBounds(50, 20, 1200, 700);
		frmVisualizadord.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 200, 0};
		gridBagLayout.rowHeights = new int[]{261, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frmVisualizadord.getContentPane().setLayout(gridBagLayout);
		
		// Panel Imagen
		panelImagen = new Imagen3D();
		
		GridBagConstraints gbc_panelImagen = new GridBagConstraints();
		gbc_panelImagen.fill = GridBagConstraints.BOTH;
		gbc_panelImagen.insets = new Insets(15, 15, 15, 5);
		gbc_panelImagen.gridx = 0;
		gbc_panelImagen.gridy = 0;
		frmVisualizadord.getContentPane().add(panelImagen, gbc_panelImagen);

		// Cargar Handler
		MouseHandler mouseHandler = new MouseHandler(this, panelImagen);
		
		// Panel Opciones
		panelOpciones = new JPanel();
		
		GridBagConstraints gbc_panelOpciones = new GridBagConstraints();
		gbc_panelOpciones.insets = new Insets(15, 10, 15, 15);
		gbc_panelOpciones.fill = GridBagConstraints.BOTH;
		gbc_panelOpciones.gridx = 1;
		gbc_panelOpciones.gridy = 0;
		frmVisualizadord.getContentPane().add(panelOpciones, gbc_panelOpciones);
		
		GridBagLayout gbl_panelOpciones = new GridBagLayout();
		gbl_panelOpciones.columnWidths = new int[]{48, 0};
		gbl_panelOpciones.rowHeights = new int[]{36, 0, 0, 0, 0, 0, 0};
		gbl_panelOpciones.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelOpciones.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelOpciones.setLayout(gbl_panelOpciones);
		
		initMenu();
		
		initPanelObjeto();
		
		initPanelLuzAmbiente();
		
		initPanelAmbienteColor();
		
		initPanelVisualizacion();
		
		initPanelTransformacion();
		
		initPanelPosicion();		
				
	}
	
	private void initMenu() {
		
		menuBar = new JMenuBar();
		frmVisualizadord.setJMenuBar(menuBar);
		
		mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		mntmCargarArchivo = new JMenuItem("Cargar Archivo");
		mntmCargarArchivo.setIcon(new ImageIcon(Visualizador3D.class.getResource("/icon/open.png")));
		mntmCargarArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmCargarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirArchivo();
			}
		});
		mnArchivo.add(mntmCargarArchivo);
		
		mntmGuardarComo = new JMenuItem("Guardar Como");
		mntmGuardarComo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guardarImagenComo();
			}
		});
		mntmGuardarComo.setIcon(new ImageIcon(Visualizador3D.class.getResource("/icon/save.png")));
		mntmGuardarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnArchivo.add(mntmGuardarComo);
		
		separator = new JSeparator();
		mnArchivo.add(separator);
		
		mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmVisualizadord.dispose();
			}
		});
		mntmSalir.setIcon(new ImageIcon(Visualizador3D.class.getResource("/icon/close.png")));
		mntmSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnArchivo.add(mntmSalir);
		
		mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		mntmAcercaDe = new JMenuItem("Acerca De");
		mntmAcercaDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AcercaDe acercaDe = new AcercaDe();
				acercaDe.setVisible(true);
			}
		});
		mntmAcercaDe.setIcon(new ImageIcon(Visualizador3D.class.getResource("/icon/about.png")));
		mnAyuda.add(mntmAcercaDe);
		
	}
	
	private void initPanelObjeto() {
		panelObjColor = new JPanel();
		panelObjColor.setBorder(new TitledBorder(null, "Objeto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelObjColor = new GridBagConstraints();
		gbc_panelObjColor.insets = new Insets(5, 5, 0, 5);
		gbc_panelObjColor.gridx = 0;
		gbc_panelObjColor.gridy = 0;
		panelOpciones.add(panelObjColor, gbc_panelObjColor);
		GridBagLayout gbl_panelObjColor = new GridBagLayout();
		gbl_panelObjColor.columnWidths = new int[]{47, 47, 47, 0};
		gbl_panelObjColor.rowHeights = new int[]{0, 20, 0, 0, 0};
		gbl_panelObjColor.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelObjColor.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		panelObjColor.setLayout(gbl_panelObjColor);
		
		comboObjeto = new JComboBox();
		GridBagConstraints gbc_comboObjeto = new GridBagConstraints();
		gbc_comboObjeto.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboObjeto.gridwidth = 3;
		gbc_comboObjeto.insets = new Insets(5, 5, 5, 5);
		gbc_comboObjeto.gridx = 0;
		gbc_comboObjeto.gridy = 0;
		panelObjColor.add(comboObjeto, gbc_comboObjeto);
		comboObjeto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = comboObjeto.getSelectedIndex();
				
				if(index != 0){
					cargar(files[index - 1]);
				}
			}
		});
		comboObjeto.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar Objeto", "747", "Caballo", "Mano", "Hebe", "Misil", "Toro"}));
		comboObjeto.setSelectedIndex(0);
		
		labelObjRed = new JLabel("Rojo");
		labelObjRed.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(5, 5, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		panelObjColor.add(labelObjRed, gbc_label);
		
		spinObjRed = new JSpinner();
		spinObjRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				actualizarMuestraObjeto();
			}
		});
		spinObjRed.setModel(new SpinnerNumberModel(180, 0, 255, 1));
		spinObjRed.setToolTipText("Rojo");
		GridBagConstraints gbc_spinObjRed = new GridBagConstraints();
		gbc_spinObjRed.anchor = GridBagConstraints.WEST;
		gbc_spinObjRed.insets = new Insets(5, 0, 5, 5);
		gbc_spinObjRed.gridx = 1;
		gbc_spinObjRed.gridy = 1;
		panelObjColor.add(spinObjRed, gbc_spinObjRed);
		
		lblObjMuestra = new JLabel("");
		GridBagConstraints gbc_lblObjMuestra = new GridBagConstraints();
		gbc_lblObjMuestra.insets = new Insets(5, 0, 5, 5);
		gbc_lblObjMuestra.weighty = 40.0;
		gbc_lblObjMuestra.weightx = 40.0;
		gbc_lblObjMuestra.fill = GridBagConstraints.BOTH;
		gbc_lblObjMuestra.gridheight = 3;
		gbc_lblObjMuestra.gridx = 2;
		gbc_lblObjMuestra.gridy = 1;
		panelObjColor.add(lblObjMuestra, gbc_lblObjMuestra);
		
		labelObjGreen = new JLabel("Verde");
		labelObjGreen.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 5, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		panelObjColor.add(labelObjGreen, gbc_label_2);
		
		spinObjGreen = new JSpinner();
		spinObjGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				actualizarMuestraObjeto();
			}
		});
		spinObjGreen.setModel(new SpinnerNumberModel(255, 0, 255, 1));
		spinObjGreen.setToolTipText("Verde");
		GridBagConstraints gbc_spinObjGreen = new GridBagConstraints();
		gbc_spinObjGreen.anchor = GridBagConstraints.WEST;
		gbc_spinObjGreen.insets = new Insets(0, 0, 5, 5);
		gbc_spinObjGreen.gridx = 1;
		gbc_spinObjGreen.gridy = 2;
		panelObjColor.add(spinObjGreen, gbc_spinObjGreen);
		
		labelObjBlue = new JLabel("Azul");
		labelObjBlue.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 5, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 3;
		panelObjColor.add(labelObjBlue, gbc_label_3);
		
		spinObjBlue = new JSpinner();
		spinObjBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				actualizarMuestraObjeto();
			}
		});
		spinObjBlue.setModel(new SpinnerNumberModel(100, 0, 255, 1));
		spinObjBlue.setToolTipText("Azul");
		GridBagConstraints gbc_spinObjBlue = new GridBagConstraints();
		gbc_spinObjBlue.anchor = GridBagConstraints.WEST;
		gbc_spinObjBlue.insets = new Insets(0, 0, 5, 5);
		gbc_spinObjBlue.gridx = 1;
		gbc_spinObjBlue.gridy = 3;
		panelObjColor.add(spinObjBlue, gbc_spinObjBlue);		
	}
	

	private void initPanelLuzAmbiente() {
		panelLuzAmbiente = new JPanel();
		panelLuzAmbiente.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fuente de Luz", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelLuzAmbiente = new GridBagConstraints();
		gbc_panelLuzAmbiente.fill = GridBagConstraints.BOTH;
		gbc_panelLuzAmbiente.insets = new Insets(5, 5, 0, 5);
		gbc_panelLuzAmbiente.gridx = 0;
		gbc_panelLuzAmbiente.gridy = 1;
		panelOpciones.add(panelLuzAmbiente, gbc_panelLuzAmbiente);
		GridBagLayout gbl_panelLuzAmbiente = new GridBagLayout();
		gbl_panelLuzAmbiente.columnWidths = new int[]{48, 0};
		gbl_panelLuzAmbiente.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelLuzAmbiente.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelLuzAmbiente.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		panelLuzAmbiente.setLayout(gbl_panelLuzAmbiente);
		
		comboLuz = new JComboBox();
		comboLuz.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent item) {
				if (item.getStateChange() == ItemEvent.SELECTED){
					actualizarLuz();
				}
			}
		});
		comboLuz.setModel(new DefaultComboBoxModel(new String[] {"Lateral Izquierda", "Lateral Derecha", "Cenital", "Contrapicado", "Frontal", "Contraluz", "Semi-lateral", "Semi-contraluz"}));
		comboLuz.setSelectedIndex(0);
		GridBagConstraints gbc_comboLuz = new GridBagConstraints();
		gbc_comboLuz.insets = new Insets(5, 5, 5, 5);
		gbc_comboLuz.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboLuz.gridx = 0;
		gbc_comboLuz.gridy = 0;
		panelLuzAmbiente.add(comboLuz, gbc_comboLuz);		
	}
	
	
	private void initPanelAmbienteColor() {
		panelAmbColor = new JPanel();
		panelAmbColor.setBorder(new TitledBorder(null, "Luz Ambiente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelAmbColor = new GridBagConstraints();
		gbc_panelAmbColor.insets = new Insets(5, 5, 0, 5);
		gbc_panelAmbColor.gridx = 0;
		gbc_panelAmbColor.gridy = 2;
		panelOpciones.add(panelAmbColor, gbc_panelAmbColor);
		GridBagLayout gbl_panelAmbColor = new GridBagLayout();
		gbl_panelAmbColor.columnWidths = new int[]{47, 47, 47, 0};
		gbl_panelAmbColor.rowHeights = new int[]{20, 0, 0, 0};
		gbl_panelAmbColor.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelAmbColor.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		panelAmbColor.setLayout(gbl_panelAmbColor);
		
		labelAmbRojo = new JLabel("Rojo");
		labelAmbRojo.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_labelAmbRojo = new GridBagConstraints();
		gbc_labelAmbRojo.anchor = GridBagConstraints.EAST;
		gbc_labelAmbRojo.insets = new Insets(5, 5, 5, 5);
		gbc_labelAmbRojo.gridx = 0;
		gbc_labelAmbRojo.gridy = 0;
		panelAmbColor.add(labelAmbRojo, gbc_labelAmbRojo);
		
		spinAmbRed = new JSpinner();
		spinAmbRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				actualizarMuestraAmbiente();
			}
		});
		spinAmbRed.setModel(new SpinnerNumberModel(100, 0, 255, 1));
		spinAmbRed.setToolTipText("Rojo");
		GridBagConstraints gbc_spinAmbRed = new GridBagConstraints();
		gbc_spinAmbRed.anchor = GridBagConstraints.WEST;
		gbc_spinAmbRed.insets = new Insets(5, 0, 5, 5);
		gbc_spinAmbRed.gridx = 1;
		gbc_spinAmbRed.gridy = 0;
		panelAmbColor.add(spinAmbRed, gbc_spinAmbRed);
		
		lblAmbMuestra = new JLabel("");
		GridBagConstraints gbc_lblAmbMuestra = new GridBagConstraints();
		gbc_lblAmbMuestra.insets = new Insets(5, 0, 5, 5);
		gbc_lblAmbMuestra.weighty = 40.0;
		gbc_lblAmbMuestra.weightx = 40.0;
		gbc_lblAmbMuestra.fill = GridBagConstraints.BOTH;
		gbc_lblAmbMuestra.gridheight = 3;
		gbc_lblAmbMuestra.gridx = 2;
		gbc_lblAmbMuestra.gridy = 0;
		panelAmbColor.add(lblAmbMuestra, gbc_lblAmbMuestra);
		
		labelAmbGreen = new JLabel("Verde");
		labelAmbGreen.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_labelAmbGreen = new GridBagConstraints();
		gbc_labelAmbGreen.anchor = GridBagConstraints.EAST;
		gbc_labelAmbGreen.insets = new Insets(0, 5, 5, 5);
		gbc_labelAmbGreen.gridx = 0;
		gbc_labelAmbGreen.gridy = 1;
		panelAmbColor.add(labelAmbGreen, gbc_labelAmbGreen);
		
		spinAmbGreen = new JSpinner();
		spinAmbGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				actualizarMuestraAmbiente();
			}
		});
		spinAmbGreen.setModel(new SpinnerNumberModel(100, 0, 255, 1));
		spinAmbGreen.setToolTipText("Verde");
		GridBagConstraints gbc_spinAmbGreen = new GridBagConstraints();
		gbc_spinAmbGreen.anchor = GridBagConstraints.WEST;
		gbc_spinAmbGreen.insets = new Insets(0, 0, 5, 5);
		gbc_spinAmbGreen.gridx = 1;
		gbc_spinAmbGreen.gridy = 1;
		panelAmbColor.add(spinAmbGreen, gbc_spinAmbGreen);
		
		labelAmbBlue = new JLabel("Azul");
		labelAmbBlue.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_labelAmbBlue = new GridBagConstraints();
		gbc_labelAmbBlue.anchor = GridBagConstraints.EAST;
		gbc_labelAmbBlue.insets = new Insets(0, 5, 5, 5);
		gbc_labelAmbBlue.gridx = 0;
		gbc_labelAmbBlue.gridy = 2;
		panelAmbColor.add(labelAmbBlue, gbc_labelAmbBlue);
		
		spinAmbBlue = new JSpinner();
		spinAmbBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				actualizarMuestraAmbiente();
			}
		});
		spinAmbBlue.setModel(new SpinnerNumberModel(100, 0, 255, 1));
		spinAmbBlue.setToolTipText("Azul");
		GridBagConstraints gbc_spinAmbBlue = new GridBagConstraints();
		gbc_spinAmbBlue.anchor = GridBagConstraints.WEST;
		gbc_spinAmbBlue.insets = new Insets(0, 0, 5, 5);
		gbc_spinAmbBlue.gridx = 1;
		gbc_spinAmbBlue.gridy = 2;
		panelAmbColor.add(spinAmbBlue, gbc_spinAmbBlue);		
	}
	
	
	private void initPanelVisualizacion() {
		panelVisualizacion = new JPanel();
		panelVisualizacion.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Visualizacion", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelVisualizacion = new GridBagConstraints();
		gbc_panelVisualizacion.fill = GridBagConstraints.BOTH;
		gbc_panelVisualizacion.insets = new Insets(5, 5, 0, 5);
		gbc_panelVisualizacion.gridx = 0;
		gbc_panelVisualizacion.gridy = 3;
		panelOpciones.add(panelVisualizacion, gbc_panelVisualizacion);
		GridBagLayout gbl_panelVisualizacion = new GridBagLayout();
		gbl_panelVisualizacion.columnWidths = new int[]{48, 0};
		gbl_panelVisualizacion.rowHeights = new int[]{29, 0, 0};
		gbl_panelVisualizacion.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelVisualizacion.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelVisualizacion.setLayout(gbl_panelVisualizacion);
		
		comboVista = new JComboBox();
		GridBagConstraints gbc_comboVista = new GridBagConstraints();
		gbc_comboVista.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboVista.insets = new Insets(5, 5, 5, 5);
		gbc_comboVista.gridx = 0;
		gbc_comboVista.gridy = 0;
		panelVisualizacion.add(comboVista, gbc_comboVista);
		comboVista.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent item) {
				if (item.getStateChange() == ItemEvent.SELECTED){
					actualizarVista();
				}
			}
		});
		comboVista.setModel(new DefaultComboBoxModel(new String[] {"Puntos", "Triangulacion", "Solido"}));
		comboVista.setSelectedIndex(2);
		comboVista.setToolTipText("Modo de Visualizacion");
		
		chckbxBackfaceCulling = new JCheckBox("Backface Culling");
		chckbxBackfaceCulling.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent item) {
				panelImagen.setBackfaceCulling(item.getStateChange() == ItemEvent.SELECTED);
			}
		});
		GridBagConstraints gbc_chckbxBackfaceCulling = new GridBagConstraints();
		gbc_chckbxBackfaceCulling.anchor = GridBagConstraints.WEST;
		gbc_chckbxBackfaceCulling.insets = new Insets(0, 10, 0, 0);
		gbc_chckbxBackfaceCulling.gridx = 0;
		gbc_chckbxBackfaceCulling.gridy = 1;
		panelVisualizacion.add(chckbxBackfaceCulling, gbc_chckbxBackfaceCulling);		
	}
	
	
	private void initPanelTransformacion() {
		panelTransformacion = new JPanel();
		panelTransformacion.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Transformaciones", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelTransformacion = new GridBagConstraints();
		gbc_panelTransformacion.insets = new Insets(5, 5, 0, 5);
		gbc_panelTransformacion.fill = GridBagConstraints.BOTH;
		gbc_panelTransformacion.gridx = 0;
		gbc_panelTransformacion.gridy = 4;
		panelOpciones.add(panelTransformacion, gbc_panelTransformacion);
		GridBagLayout gbl_panelTransformacion = new GridBagLayout();
		gbl_panelTransformacion.columnWidths = new int[]{48, 0};
		gbl_panelTransformacion.rowHeights = new int[]{0, 0, 0};
		gbl_panelTransformacion.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelTransformacion.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panelTransformacion.setLayout(gbl_panelTransformacion);
		
		tableTransformacion = new JTable();
		GridBagConstraints gbc_tableTransformacion = new GridBagConstraints();
		gbc_tableTransformacion.fill = GridBagConstraints.BOTH;
		gbc_tableTransformacion.insets = new Insets(5, 5, 5, 5);
		gbc_tableTransformacion.gridx = 0;
		gbc_tableTransformacion.gridy = 0;
		panelTransformacion.add(tableTransformacion, gbc_tableTransformacion);
		tableTransformacion.setModel(new DefaultTableModel(
			new Object[][] {
				{"1", "0", "0", "0"},
				{"0", "1", "0", "0"},
				{"0", "0", "1", "0"},
				{"0", "0", "0", "1"},
			},
			new String[] {
				"New column", "New column", "New column", "New column"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableTransformacion.getColumnModel().getColumn(0).setResizable(false);
		tableTransformacion.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableTransformacion.getColumnModel().getColumn(0).setMinWidth(10);
		tableTransformacion.getColumnModel().getColumn(1).setPreferredWidth(30);
		tableTransformacion.getColumnModel().getColumn(2).setPreferredWidth(30);
		tableTransformacion.getColumnModel().getColumn(3).setPreferredWidth(30);
		
		btnAplicar = new JButton("Aplicar");
		btnAplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				agregarTransformacionTabla();
			}
		});
		GridBagConstraints gbc_btnAplicar = new GridBagConstraints();
		gbc_btnAplicar.gridx = 0;
		gbc_btnAplicar.gridy = 1;
		panelTransformacion.add(btnAplicar, gbc_btnAplicar);		
	}
	
	
	private void initPanelPosicion() {
		panelPosicion = new JPanel();
		panelPosicion.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Posicion", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelPosicion = new GridBagConstraints();
		gbc_panelPosicion.insets = new Insets(5, 5, 5, 5);
		gbc_panelPosicion.fill = GridBagConstraints.BOTH;
		gbc_panelPosicion.gridx = 0;
		gbc_panelPosicion.gridy = 5;
		panelOpciones.add(panelPosicion, gbc_panelPosicion);
		GridBagLayout gbl_panelPosicion = new GridBagLayout();
		gbl_panelPosicion.columnWidths = new int[]{48, 0, 0};
		gbl_panelPosicion.rowHeights = new int[]{0, 0, 0};
		gbl_panelPosicion.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelPosicion.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelPosicion.setLayout(gbl_panelPosicion);
		
		lblPosicionX = new JLabel("Posicion X:");
		GridBagConstraints gbc_lblPosicionX = new GridBagConstraints();
		gbc_lblPosicionX.anchor = GridBagConstraints.EAST;
		gbc_lblPosicionX.insets = new Insets(0, 0, 5, 5);
		gbc_lblPosicionX.gridx = 0;
		gbc_lblPosicionX.gridy = 0;
		panelPosicion.add(lblPosicionX, gbc_lblPosicionX);
		
		labelPosX = new JLabel("");
		GridBagConstraints gbc_labelPosX = new GridBagConstraints();
		gbc_labelPosX.anchor = GridBagConstraints.WEST;
		gbc_labelPosX.insets = new Insets(0, 0, 5, 0);
		gbc_labelPosX.gridx = 1;
		gbc_labelPosX.gridy = 0;
		panelPosicion.add(labelPosX, gbc_labelPosX);
		
		lblPosicionY = new JLabel("Posicion Y:");
		GridBagConstraints gbc_lblPosicionY = new GridBagConstraints();
		gbc_lblPosicionY.anchor = GridBagConstraints.EAST;
		gbc_lblPosicionY.insets = new Insets(0, 0, 0, 5);
		gbc_lblPosicionY.gridx = 0;
		gbc_lblPosicionY.gridy = 1;
		panelPosicion.add(lblPosicionY, gbc_lblPosicionY);
		
		lblPosY = new JLabel("");
		GridBagConstraints gbc_lblPosY = new GridBagConstraints();
		gbc_lblPosY.anchor = GridBagConstraints.WEST;
		gbc_lblPosY.gridx = 1;
		gbc_lblPosY.gridy = 1;
		panelPosicion.add(lblPosY, gbc_lblPosY);		
	}
	
	
	protected void actualizarLuz() {
		panelImagen.setLuzPunto(comboLuz.getSelectedIndex());
	}

	protected void actualizarMuestraObjeto() {
		int r = (int) spinObjRed.getValue();
		int g = (int) spinObjGreen.getValue();
		int b = (int) spinObjBlue.getValue();

		Color c = new Color(r,g,b);
		lblObjMuestra.setIcon(new ImageIcon(new ImgMuestra(c, 80, 80)));
		panelImagen.setObjectColor(c);
	}
	
	protected void actualizarMuestraAmbiente() {
		int r = (int) spinAmbRed.getValue();
		int g = (int) spinAmbGreen.getValue();
		int b = (int) spinAmbBlue.getValue();

		Color c = new Color(r,g,b);
		lblAmbMuestra.setIcon(new ImageIcon(new ImgMuestra(c, 80, 80)));
		panelImagen.setAmbientColor(c);
	}
	
	protected void agregarTransformacionTabla() {
		if(!panelImagen.isCargado())
			return;
		
		double[][] matriz = new double[4][4];
		
		for(int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				Object valor = tableTransformacion.getValueAt(i, j);
				
				try{
					if (valor != null){
						matriz[i][j] = Double.parseDouble((String) valor);
					}
				} catch (Exception e){
				}
			}
		}
		
		panelImagen.objeto.aplicarTransformacion((new MatrizTransformacion(matriz)));
		panelImagen.updateUI();
		
	}

	protected void actualizarVista() {
		String vista = (String) comboVista.getSelectedItem();
		
		switch (vista) {
		case "Solido":
			panelImagen.setVista(Objeto3D.VISTA_SOLIDO);
			break;
		case "Triangulacion":
			panelImagen.setVista(Objeto3D.VISTA_TRIANGULOS);
			break;
		case "Puntos":
			panelImagen.setVista(Objeto3D.VISTA_PUNTOS);
			break;
		default:
			break;
		}
	}

	protected void cargar(File f) {
		// Parsear archivo *.sur para cargar estructura
		Objeto3D obj = new Objeto3D(f);
		
		// Mostrar objeto en pantalla
		panelImagen.setObjeto(obj);
		actualizarLuz();
	}
	
	protected void abrirArchivo() {
		JFileChooser fc = new JFileChooser("sur");

        // Mostrar la ventana para abrir archivo y recoger la respuesta
        fc.setFileFilter(new FiltroArchivoSUR());
        int respuesta = fc.showOpenDialog(null);

        // Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION){
            cargar(fc.getSelectedFile());
            comboObjeto.setSelectedIndex(0);
        }
	}
	
	protected void guardarImagenComo() {
		JFileChooser fc = new JFileChooser("");

        // Mostrar la ventana para abrir archivo y recoger la respuesta
        fc.setFileFilter(new FiltroArchivoPNG());
        int respuesta = fc.showSaveDialog(null);

        // Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION){
        	String path = fc.getSelectedFile().getAbsolutePath();
        	
        	if (! path.endsWith(".png")){
        		path += ".png";
        	}
        	
            guardarImagen(new File(path));
        }
		
	}

	private void guardarImagen(File f) {
		try {
			int w = panelImagen.getWidth();
		    int h = panelImagen.getHeight();
		    BufferedImage buffIm = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		    Graphics2D g = buffIm.createGraphics();
		    panelImagen.paint(g);
		    
			ImageIO.write(buffIm, "png", f);
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}		
	}

	public void setPosicionActual(int f, int c) {
		labelPosX.setText("" + c);
		lblPosY.setText("" + f);
	}
	
}
