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

import matriz.MatrizEscala;
import matriz.MatrizIdentidad;
import matriz.MatrizPerspectiva;
import matriz.MatrizRotacionX;
import matriz.MatrizRotacionY;
import matriz.MatrizRotacionZ;
import matriz.MatrizTransformacion;
import matriz.MatrizTraslacion;
import swing.Imagen3D;

public class Objeto3D {

	private static final String ELEMENT_GROUPS = "*ELEMENT GROUPS";
	private static final String INCIDENCE = "*INCIDENCE";
	private static final String COORDINATES = "*COORDINATES";
	public static final int VISTA_TRIANGULOS = 1;
	public static final int VISTA_SOLIDO = 2;
	public static final int VISTA_PUNTOS = 3;

	private Vector<ElementGroup> grupos = new Vector<ElementGroup>();
	private HashMap<Integer, Punto3D> puntos = new HashMap<Integer, Punto3D>();
	private HashMap<Integer, Punto3D> puntosNuevos = new HashMap<Integer, Punto3D>();
	private MatrizTransformacion matrizAcumulada = new MatrizIdentidad();
	

	private FileReader fr;
	private BufferedReader br;

	public void centrar(int width, int heigth){
		double xMin = 10000, xMax = -10000, yMin = 10000, yMax = -10000;
		
		// Obtener minimos y maximos
		for (Punto3D punto : puntos.values()){
			double x = punto.getX();
			double y = punto.getY();
			
			if (xMin > x)
				xMin = x;
			
			if (xMax < x)
				xMax = x;
			
			if (yMin > y)
				yMin = y;
			
			if (yMax < y)
				yMax = y;
		}
		
		// Realizar traslacion al centro
		double tx = (double) width / 2 - (xMax - xMin) / 2;
		double ty = (double) heigth / 2 - (yMax - yMin) / 2;
		
		trasladar(tx, ty, 0);
		
	}
	
	public void aplicarTransformacion(){
		puntosNuevos = new HashMap<Integer, Punto3D>();
		
		Set<Entry<Integer, Punto3D>> pares = puntos.entrySet();
		for (Entry<Integer, Punto3D> par : pares){
//			MatrizTransformacion matriz = new MatrizPerspectiva(distancia);
//			matriz = matriz.producto(matrizAcumulada);
			
			puntosNuevos.put(par.getKey(), par.getValue().aplicarTransformacion(matrizAcumulada.getMatriz()));
		}		
	}
	
	public void agregarTransformacion(MatrizTransformacion matriz){
		matriz.imprimir();
		matrizAcumulada = matriz.producto(matrizAcumulada);
	}
	
	// (Tx, Ty, Tz) Vector de traslacion
	public void trasladar(double tx, double ty, double tz){
		agregarTransformacion(new MatrizTraslacion(tx, ty, tz));
	}
	
	// (Sx, Sy, Sz) Vector de escala
	public void escala(double sx, double sy, double sz){		
		agregarTransformacion(new MatrizEscala(sx, sy, sz));
	}
	
	// s = escala
	public void escalaIsotropica(double s){
		agregarTransformacion(new MatrizEscala(s));
	}
	
	// Rotacion en eje X
	// o = Angulo en radianes [0, 2pi]
	public void rotacionX(double o){
		agregarTransformacion(new MatrizRotacionX(o));
	}
	
	// Rotacion en eje Y
	public void rotacionY(double o){		
		agregarTransformacion(new MatrizRotacionY(o));
	}
	
	// Rotacion en eje Z.
	public void rotacionZ(double o){
		agregarTransformacion(new MatrizRotacionZ(o));
	}

	public void dibujar(Imagen3D imagen, Graphics g) {
		// 1) Aplicar transformacion a puntos originales
		aplicarTransformacion();
		
		
		// Obtener elementos
		Vector<Incidence> elementos = new Vector<Incidence>();
		for (ElementGroup grupo : grupos){
			elementos.addAll(grupo.getElementos());
		}
		
		// 2) Ordenar elementos segun valor medio de Z
		Collections.sort(elementos, new Comparator<Incidence>() {
			public int compare(Incidence elem1, Incidence elem2) {
				Double z1 = elem1.getZPromedio(puntosNuevos), z2 = elem2.getZPromedio(puntosNuevos);
				
				return z2.compareTo(z1);
			}			
		});
		
		// 3) Dibujar los elementos en pantalla
		for (Incidence elemento : elementos){
			elemento.dibujar(imagen, g, puntosNuevos);
		}
		
	}	
	
	// Cargar SUR (Verificar espacios entre coordenadas)
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
							grupos.add(new ElementGroup(tokensElemGroup[2], groupCant));
						}else{
							System.out.println("ERROR al parsear grupo de elementos");
						}
					}
				}else if (linea.indexOf(INCIDENCE) != -1){
					// Recorrer grupo de elementos
					for (ElementGroup group : grupos){
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
	
//	public HashMap<Integer, Punto3D> getPuntos() {
//		return puntos;
//	}
	
}
