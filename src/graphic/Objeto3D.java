package graphic;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import swing.Imagen3D;

public class Objeto3D {

	private static final String ELEMENT_GROUPS = "*ELEMENT GROUPS";
	private static final String INCIDENCE = "*INCIDENCE";
	private static final String COORDINATES = "*COORDINATES";

	private Vector<ElementGroup> groups = new Vector<ElementGroup>();
//	private Vector<Punto3D> puntos = new Vector<Punto3D>();
	private HashMap<Integer, Punto3D> puntos = new HashMap<Integer, Punto3D>();
	
	
	public HashMap<Integer, Punto3D> getPuntos() {
		return puntos;
	}

	private FileReader fr;
	private BufferedReader br;
	
	public void loadSUR(File archivo){
		try {
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			while((linea = br.readLine()) != null){
				if (linea.indexOf(ELEMENT_GROUPS) != -1){
					int cantElementGroups = Integer.parseInt(readLine());
					System.out.println("Found " + cantElementGroups + " ELEMENT GROUPS. ");
					for(int i = 1; i <= cantElementGroups; i++){
						String elemGroup = readLine();
//						System.out.println("Grupo " + i + ":" + elemGroup);
						String[] tokensElemGroup = elemGroup.trim().split(" ");
						if(tokensElemGroup.length == 3){
							int groupCant = Integer.parseInt(tokensElemGroup[1]);
//							System.out.println("Grupo correcto de " + groupCant + " elementos de tipo " + tokensElemGroup[2]);
							groups.add(new ElementGroup(tokensElemGroup[2], groupCant));
						}else{
							System.out.println("ERROR al parsear grupo de elementos");
						}
					}
				}else if (linea.indexOf(INCIDENCE) != -1){
					// Recorrer grupo de elementos
					for (ElementGroup group : groups){
//						System.out.println("Procesando " + group.getTipo());
						
						int cant = group.getCantidad();
						int incidencia = group.getGradoIncidencia();
						
						// Leer elementos del grupo
						for (int e = 1; e <= cant; e++){
							String elemento = readLine();
							
							String[] tokensElemento = elemento.trim().split(" ");
							if(tokensElemento.length == incidencia){
								int puntos[] = new int[incidencia];
								
								// Leer puntos del elemento
								for(int p = 0; p < incidencia; p++){
									puntos[p] = Integer.parseInt(tokensElemento[p]);
								}
								
								group.agregarElemento(new Incidence(puntos));
							}else{
								System.out.println("ERROR al parsear elementos");
							}
						}
						
//						System.out.println("ESTRUCTURA: Grupo " + group.getTipo() + " con " + group.getElemCant() + " elementos.");
					}
					
				}else if (linea.indexOf(COORDINATES) != -1){
					int cantCoordenadas = Integer.parseInt(readLine());
					
					for(int i = 0; i < cantCoordenadas; i++){
						String coord = readLine();
						String[] tokensCoordenadas = coord.trim().split(" ");
						if(tokensCoordenadas.length == 4){
							double x = Double.parseDouble(tokensCoordenadas[1]);
							double y = Double.parseDouble(tokensCoordenadas[2]);
							double z = Double.parseDouble(tokensCoordenadas[3]);
							
							puntos.put(Integer.parseInt(tokensCoordenadas[0]), new Punto3D(x, y, z));
						}else{
							System.out.println("ERROR: Coordenadas 2D no soportadas");
						}
					}

//					System.out.println("ESTRUCTURA: Encontrados " + puntos.size() + " points.");
				}else if (linea.equals("")){
					
				}else{
					System.out.println("ERROR: Linea no parseada");
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void aplicarTransformacion(double[][] M){
		HashMap<Integer, Punto3D> puntosNuevos = new HashMap<Integer, Punto3D>();
		
		Set<Entry<Integer, Punto3D>> pares = puntos.entrySet();
		for (Entry<Integer, Punto3D> par : pares){
			puntosNuevos.put(par.getKey(), par.getValue().aplicarTransformacion(M));
		}
		
		puntos = puntosNuevos;
		
	}
	
	public void trasladar(double tx, double ty, double tz){
		aplicarTransformacion(new double[][]{{1, 0, 0, tx},
											 {0, 1, 0, ty},
											 {0, 0, 1, tz},
											 {0, 0, 0, 0 }});
	}
	
	public void escala(double sx, double sy, double sz){
		aplicarTransformacion(new double[][]{{sx, 0, 0, 0},
											 {0, sy, 0, 0},
											 {0, 0, sz, 0},
											 {0, 0, 0, 1 }});
	}
	
	// s = escala
	public void escalaIsotropica(double s){
		escala(s, s, s);
	}
	
	// o = Angulo en radianes (0, 2pi)
	public void rotacionZ(double o){
		aplicarTransformacion(new double[][]{{Math.cos(o), - Math.sin(o), 0, 0},
											 {Math.sin(o),   Math.cos(o), 0, 0},
											 {     0     ,       0      , 1, 0},
											 {     0     ,       0      , 0, 1}});
	}
	
	public void rotacionX(double o){
//		1 0    0
//		0 cos -sin
//		0 sin cos
		
		aplicarTransformacion(new double[][]{{     1     ,      0     ,       0      , 0},
											 {     0     , Math.cos(o), - Math.sin(o), 0},
											 {     0     , Math.sin(o),   Math.cos(o), 0},
											 {     0     ,      0     ,       0      , 1}});
	}
	
	public void rotacionY(double o){		
		aplicarTransformacion(new double[][]{{  Math.cos(o),  0   , Math.sin(o)  , 0},
											 {     0       ,  1   ,       0      , 0},											 
											 {- Math.sin(o),  0   ,   Math.cos(o), 0},
											 {       0     ,  0   ,       0      , 1}});
	}
	
	private String readLine(){
		String line = null;
		try {
			line = br.readLine().trim();
			
			while (line != null && line.equals("")){
				System.out.println("Salteando linea en blanco");
				line = br.readLine().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
	}

	public void dibujar(Imagen3D imagen, Graphics g) {
		Vector<Incidence> elementos = new Vector<Incidence>();
		
		// Obtener elementos
		for (ElementGroup group : groups){
			elementos.addAll(group.getElementos());
//			group.dibujar(g, puntos);
		}

//		System.out.println("No Ordenados");
		
		// Ordenar
		Collections.sort(elementos, new Comparator<Incidence>() {

			public int compare(Incidence elem1, Incidence elem2) {
				Double z1 = elem1.getZPromedio(puntos), z2 = elem2.getZPromedio(puntos);
				int comp = z2.compareTo(z1);

//				System.out.println("Comparando " + z1 + " - " + z2 + " (" + comp + ")");
				
				return comp;
			}
			
		});
		
//		System.out.println("Ordenados");
		
		// Dibujar
		for (Incidence elemento : elementos){
//			System.out.println("Dibujando " + elemento.getZPromedio(puntos));
			elemento.dibujar(imagen, g, puntos);
		}
		
	}


}
