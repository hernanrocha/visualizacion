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

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import matriz.MatrizTransformacion;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Visualizador3D {

	private JFrame frmVisualizadord;
	protected Objeto3D g = new Objeto3D();
	private Imagen3D panelImagen;
	private JPanel panelOpciones;
	private JButton btnRotar;
	private MouseHandler mouseHandler;
	private JMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenuItem mntmCargarArchivo;
	private final ButtonGroup groupAcciones = new ButtonGroup();
	private JLabel labelPosX;
	private JLabel lblPosY;
	private JComboBox comboVista;
	private JMenu mnAyuda;
	private JMenuItem mntmAcercaDe;
	private JCheckBox chckbxPerspectiva;
	private JTable table;
	private JButton btnAplicar;
	private JComboBox comboBox_1;
	private JPanel panel;
	private JSpinner spinPerspectiva;
	private JLabel lblDistancia;
	private JPanel panel_2;
	private JPanel panel_3;
	private JCheckBox chckbxBackfaceCulling;
	private JSpinner ambienteR;
	private JSpinner ambienteG;
	private JSpinner spinner;
	private JLabel lblRojo;
	private JLabel lblVerde;
	private JLabel lblAzul;
	private JPanel panel_1;

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
		 * 1) Perspectiva, iluminacion (ambiente, origen)
		 * 2) Transformaciones Deformaciones
		 * 5) Detectar profundidad al rotar
		 * 6) tamaño, logo, ayuda, about.
		 * 7) Atajos, imagenes de botones
		 * 
		 */

		initialize();
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Luz Ambiente", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		panelOpciones.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{48, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		lblRojo = new JLabel("Rojo");
		GridBagConstraints gbc_lblRojo = new GridBagConstraints();
		gbc_lblRojo.anchor = GridBagConstraints.EAST;
		gbc_lblRojo.insets = new Insets(0, 0, 5, 5);
		gbc_lblRojo.gridx = 0;
		gbc_lblRojo.gridy = 0;
		panel_1.add(lblRojo, gbc_lblRojo);
		
		lblVerde = new JLabel("Verde");
		GridBagConstraints gbc_lblVerde = new GridBagConstraints();
		gbc_lblVerde.anchor = GridBagConstraints.EAST;
		gbc_lblVerde.insets = new Insets(0, 0, 5, 5);
		gbc_lblVerde.gridx = 0;
		gbc_lblVerde.gridy = 1;
		panel_1.add(lblVerde, gbc_lblVerde);
		
		ambienteG = new JSpinner();
		GridBagConstraints gbc_ambienteG = new GridBagConstraints();
		gbc_ambienteG.anchor = GridBagConstraints.WEST;
		gbc_ambienteG.insets = new Insets(0, 0, 5, 0);
		gbc_ambienteG.gridx = 1;
		gbc_ambienteG.gridy = 1;
		panel_1.add(ambienteG, gbc_ambienteG);
		ambienteG.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		
		lblAzul = new JLabel("Azul");
		GridBagConstraints gbc_lblAzul = new GridBagConstraints();
		gbc_lblAzul.anchor = GridBagConstraints.EAST;
		gbc_lblAzul.insets = new Insets(0, 0, 0, 5);
		gbc_lblAzul.gridx = 0;
		gbc_lblAzul.gridy = 2;
		panel_1.add(lblAzul, gbc_lblAzul);
		
		ambienteR = new JSpinner();
		GridBagConstraints gbc_ambienteR = new GridBagConstraints();
		gbc_ambienteR.anchor = GridBagConstraints.WEST;
		gbc_ambienteR.insets = new Insets(0, 0, 5, 0);
		gbc_ambienteR.gridx = 1;
		gbc_ambienteR.gridy = 0;
		panel_1.add(ambienteR, gbc_ambienteR);
		ambienteR.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		
		spinner = new JSpinner();
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.WEST;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 2;
		panel_1.add(spinner, gbc_spinner);
		spinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		
		labelPosX = new JLabel("New label");
		GridBagConstraints gbc_labelPosX = new GridBagConstraints();
		gbc_labelPosX.insets = new Insets(0, 0, 5, 0);
		gbc_labelPosX.gridx = 0;
		gbc_labelPosX.gridy = 2;
		panelOpciones.add(labelPosX, gbc_labelPosX);
		
		lblPosY = new JLabel("New label");
		GridBagConstraints gbc_lblPosY = new GridBagConstraints();
		gbc_lblPosY.insets = new Insets(0, 0, 5, 0);
		gbc_lblPosY.gridx = 0;
		gbc_lblPosY.gridy = 3;
		panelOpciones.add(lblPosY, gbc_lblPosY);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Transformaciones", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		panelOpciones.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{48, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		comboBox_1 = new JComboBox();
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent item) {
				if (item.getStateChange() == ItemEvent.SELECTED){
					actualizarTransformacionTabla();
				}
			}
		});
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_1.gridx = 0;
		gbc_comboBox_1.gridy = 0;
		panel.add(comboBox_1, gbc_comboBox_1);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Shear", "Twisting", "Tappering", "Bending", "Personalizada"}));
		comboBox_1.setSelectedIndex(0);
		
		table = new JTable();
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		panel.add(table, gbc_table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"1", null, null, null},
				{null, "1", null, null},
				{null, null, "1", null},
				{null, null, null, "1"},
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
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setMinWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(30);
		table.getColumnModel().getColumn(3).setPreferredWidth(30);
		
		btnAplicar = new JButton("Aplicar");
		btnAplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				agregarTransformacionTabla();
			}
		});
		GridBagConstraints gbc_btnAplicar = new GridBagConstraints();
		gbc_btnAplicar.gridx = 0;
		gbc_btnAplicar.gridy = 2;
		panel.add(btnAplicar, gbc_btnAplicar);
		
		// Cargar SUR
		cargar(new File("sur/747.sur"));
//		cargarMisil();
//		cargarFork();
	}

	protected void actualizarTransformacionTabla() {
		System.out.println("Actualizando");
		int transformacion = 0;
		
		switch ((String) comboBox_1.getSelectedItem()) {
		case "Shear":
			transformacion = Tabla.SHEAR;
			break;
		case "Twisting":
			transformacion = Tabla.TWISTING;
			break;
		case "Tappering":
			transformacion = Tabla.TAPPERING;
			break;
		case "Bending":
			transformacion = Tabla.BENDING;
			break;
		case "Personalizada":
			transformacion = Tabla.PERSONALIZADA;
			break;
		default:
			break;
		}
		
		Tabla.cargar(table, transformacion);
	}

	protected void agregarTransformacionTabla() {
		double[][] matriz = new double[4][4];
		
		for(int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				Object valor = table.getValueAt(i, j);
				System.out.println(valor);
				
				try{
					if (valor != null){
						matriz[i][j] = Double.parseDouble((String) valor);
					}
				} catch (Exception e){
				}
			}
		}
		
		g.agregarTransformacion(new MatrizTransformacion(matriz));
		panelImagen.updateUI();
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
		gbl_panelOpciones.rowHeights = new int[]{29, 0, 0, 0, 0, 0};
		gbl_panelOpciones.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelOpciones.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelOpciones.setLayout(gbl_panelOpciones);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Visualizacion", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panelOpciones.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{48, 0};
		gbl_panel_2.rowHeights = new int[]{29, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		comboVista = new JComboBox();
		GridBagConstraints gbc_comboVista = new GridBagConstraints();
		gbc_comboVista.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboVista.insets = new Insets(0, 0, 5, 0);
		gbc_comboVista.gridx = 0;
		gbc_comboVista.gridy = 0;
		panel_2.add(comboVista, gbc_comboVista);
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
		gbc_chckbxBackfaceCulling.insets = new Insets(0, 10, 5, 5);
		gbc_chckbxBackfaceCulling.gridx = 0;
		gbc_chckbxBackfaceCulling.gridy = 1;
		panel_2.add(chckbxBackfaceCulling, gbc_chckbxBackfaceCulling);
		
		chckbxPerspectiva = new JCheckBox("Perspectiva");
		GridBagConstraints gbc_chckbxPerspectiva = new GridBagConstraints();
		gbc_chckbxPerspectiva.anchor = GridBagConstraints.WEST;
		gbc_chckbxPerspectiva.insets = new Insets(0, 10, 5, 0);
		gbc_chckbxPerspectiva.gridx = 0;
		gbc_chckbxPerspectiva.gridy = 2;
		panel_2.add(chckbxPerspectiva, gbc_chckbxPerspectiva);
		chckbxPerspectiva.setToolTipText("Ver en Perspectiva");
		
		panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		panel_2.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{48, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		lblDistancia = new JLabel("Distancia");
		GridBagConstraints gbc_lblDistancia = new GridBagConstraints();
		gbc_lblDistancia.insets = new Insets(0, 10, 0, 5);
		gbc_lblDistancia.gridx = 0;
		gbc_lblDistancia.gridy = 0;
		panel_3.add(lblDistancia, gbc_lblDistancia);
		
		spinPerspectiva = new JSpinner();
		GridBagConstraints gbc_spinPerspectiva = new GridBagConstraints();
		gbc_spinPerspectiva.insets = new Insets(0, 0, 0, 5);
		gbc_spinPerspectiva.anchor = GridBagConstraints.WEST;
		gbc_spinPerspectiva.gridx = 1;
		gbc_spinPerspectiva.gridy = 0;
		panel_3.add(spinPerspectiva, gbc_spinPerspectiva);
		spinPerspectiva.setModel(new SpinnerNumberModel(0.0, 0.0, 3.0, 0.1));
		
		initMenu();
		

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
		
		mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		mntmAcercaDe = new JMenuItem("Acerca De");
		mnAyuda.add(mntmAcercaDe);
		
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
