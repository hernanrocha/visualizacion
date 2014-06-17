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
import java.util.StringTokenizer;
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
	private Vector<Incidence> elementos = new Vector<Incidence>();
	private HashMap<Integer, Punto3D> puntos = new HashMap<Integer, Punto3D>(); // Puntos originales
	
	private HashMap<Integer, Punto3D> puntosNuevos = new HashMap<Integer, Punto3D>(); // Puntos con transformacion
	private MatrizTransformacion matrizAcumulada = new MatrizIdentidad(); // Matriz de transformaciones acumuladas
	
	private FileReader fr;
	private BufferedReader br;
	private Punto3D puntoMinimo;
	private Punto3D puntoMaximo;
	private Punto3D puntoCentro;
	
	public Objeto3D(File f){
		loadSUR(f);
	}
	
//	public void aplicarTransformacion(double[][] M){
//		HashMap<Integer, Punto3D> puntosNuevos = new HashMap<Integer, Punto3D>();
//
//		Set<Entry<Integer, Punto3D>> pares = puntos.entrySet();
//		for (Entry<Integer, Punto3D> par : pares){
//			puntosNuevos.put(par.getKey(), par.getValue().aplicarTransformacion(M));
//		}
//
//		puntos = puntosNuevos;
//
//	}

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
	
	public void aplicarTransformacion(Imagen3D imagen){
		puntosNuevos = new HashMap<Integer, Punto3D>();
		
		Set<Entry<Integer, Punto3D>> pares = puntos.entrySet();
		for (Entry<Integer, Punto3D> par : pares){
			
			Punto3D puntoT = par.getValue().aplicarTransformacion(matrizAcumulada.getMatriz());
			
			if (imagen.isPerspectiva()){
				MatrizTransformacion matriz = new MatrizPerspectiva(puntoT.getZ());
//				matriz.imprimir();
//				System.out.println(puntoT);
				puntoT = puntoT.aplicarTransformacion(matriz.getMatriz());
			}
			
			puntosNuevos.put(par.getKey(), puntoT);
		}		
	}
	
	public void agregarTransformacion(MatrizTransformacion matriz){
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
		aplicarTransformacion(imagen);
		
		// 2) Ordenar elementos segun valor medio de Z
		Collections.sort(elementos, new Comparator<Incidence>() {
			public int compare(Incidence elem1, Incidence elem2) {
				Double z1 = elem1.getZPromedio(puntosNuevos);
				Double z2 = elem2.getZPromedio(puntosNuevos);
				
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
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			
			double xMin;
			double xMax;
			double yMin;
			double yMax;
			double zMin;
			double zMax;

			// Lectura del fichero
			String linea;
			while((linea = br.readLine()) != null){
				if (linea.indexOf(ELEMENT_GROUPS) != -1){
					int cantElementGroups = Integer.parseInt(readLine());
					
					System.out.println("Found " + cantElementGroups + " ELEMENT GROUPS. ");
					
					for(int i = 1; i <= cantElementGroups; i++){
						String elemGroup = readLine();
						
						String[] tokensElemGroup = elemGroup.trim().split(" ");
						if(tokensElemGroup.length == 3){
							int groupCant = Integer.parseInt(tokensElemGroup[1]);
							// Agregar Element Group
							grupos.add(new ElementGroup(tokensElemGroup[2], groupCant));
						}else{
							System.out.println("ERROR al parsear grupo de elementos");
						}
					}
				}else if (linea.indexOf(INCIDENCE) != -1){
					// Recorrer grupo de elementos
					for (ElementGroup group : grupos){
						System.out.println("Procesando " + group.getTipo());
						
						int cant = group.getCantidad();
						int incidencia = group.getGradoIncidencia();
						
						// Leer elementos del grupo
						for (int elem = 1; elem <= cant; elem++){
							String elemento = readLine();
							StringTokenizer st = new StringTokenizer(elemento);
							
							// Leer puntos del elemento
							int puntos[] = new int[incidencia];
							for(int p = 0; p < incidencia; p++){
								puntos[p] = Integer.valueOf(st.nextToken());
							}
							
							// Agregar elemento al grupo
							group.agregarElemento(new Incidence(puntos));
						}
						
						System.out.println("ESTRUCTURA: Grupo " + group.getTipo() + " con " + group.getElemCant() + " elementos.");
						
						for (ElementGroup grupo : grupos){
							elementos.addAll(grupo.getElementos());
						}
					}
					
				}else if (linea.indexOf(COORDINATES) != -1){
					// Recorrer coordenadas de puntos
		            xMin = Double.MAX_VALUE;
		            xMax = Double.MIN_VALUE;
		            yMin = Double.MAX_VALUE;
		            yMax = Double.MIN_VALUE;
		            zMin = Double.MAX_VALUE;
		            zMax = Double.MIN_VALUE;
					
					int cantCoordenadas = Integer.parseInt(readLine());					
					
					for(int i = 0; i < cantCoordenadas; i++){
						// Leer coordenada
						String coord = readLine();
						
						// Parsear valores id, x, y, z
						StringTokenizer st = new StringTokenizer(coord);
						int id = Integer.valueOf(st.nextToken());
						double x = Double.valueOf(st.nextToken());
						double y = Double.valueOf(st.nextToken());
						double z = Double.valueOf(st.nextToken());
						
						// Actualizar puntos minimos y maximos
				        xMin = Math.min(xMin, x);
				        xMax = Math.max(xMax, x);
				        yMin = Math.min(yMin, y);
				        yMax = Math.max(yMax, y);
				        zMin = Math.min(zMin, z);
				        zMax = Math.max(zMax, z);
						
						puntos.put(id, new Punto3D(x, y, z));
					}

					System.out.println("ESTRUCTURA: Encontrados " + puntos.size() + " points.");
					
					puntoMinimo = new Punto3D(xMin, yMin, zMin);
					puntoMaximo = new Punto3D(xMax, yMax, zMax);
					puntoCentro = new Punto3D((xMin + xMax) / 2, (yMin + yMax) / 2, (zMin + zMax) / 2);
					
					System.out.println("Centro: " + puntoCentro);
					
				}/*else if (linea.equals("")){
					
				}else{
					System.out.println("ERROR: Linea no parseada");
				}*/
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
				line = br.readLine().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
	}
	
}
