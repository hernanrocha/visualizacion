package graphic;

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
import java.util.StringTokenizer;
import java.util.Vector;

import matriz.MatrizEscala;
import matriz.MatrizTransformacion;
import matriz.MatrizTraslacion;

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

	private FileReader fr;
	private BufferedReader br;
	private Punto3D centro;
	
	public Objeto3D(File f){
		loadSUR(f);
	}
	
	// Cargar SUR (Verificar espacios entre coordenadas)
	@SuppressWarnings("unchecked")
	public void loadSUR(File archivo){
		try {
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			
			double xMin = Double.MAX_VALUE;
			double xMax = Double.MIN_VALUE;
			double yMin = Double.MAX_VALUE;
			double yMax = Double.MIN_VALUE;
			double zMin = Double.MAX_VALUE;
			double zMax = Double.MIN_VALUE;

			// Lectura del fichero
			String linea;
			while((linea = br.readLine()) != null){
				if (linea.indexOf(ELEMENT_GROUPS) != -1){
					int cantElementGroups = Integer.parseInt(readLine());
					
//					System.out.println("Found " + cantElementGroups + " ELEMENT GROUPS. ");
					
					for(int i = 1; i <= cantElementGroups; i++){
						String elemGroup = readLine();
						
						String[] tokensElemGroup = elemGroup.trim().split(" ");
						if(tokensElemGroup.length == 3){
							int groupCant = Integer.parseInt(tokensElemGroup[1]);
							// Agregar Element Group
							grupos.add(new ElementGroup(tokensElemGroup[2], groupCant));
						}else{
//							System.out.println("ERROR al parsear grupo de elementos");
						}
					}
				}else if (linea.indexOf(INCIDENCE) != -1){
					// Recorrer grupo de elementos
					for (ElementGroup group : grupos){
//						System.out.println("Procesando " + group.getTipo());
						
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
						
//						System.out.println("ESTRUCTURA: Grupo " + group.getTipo() + " con " + group.getElemCant() + " elementos.");
						
						for (ElementGroup grupo : grupos){
							elementos.addAll(grupo.getElementos());
						}
					}
					
				}else if (linea.indexOf(COORDINATES) != -1){					
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

//					System.out.println("ESTRUCTURA: Encontrados " + puntos.size() + " points.");
					
					centro = new Punto3D((xMin + xMax) / 2, (yMin + yMax) / 2, (zMin + zMax) / 2);
					
//					System.out.println("Centro: " + centro);
					
				}/*else if (linea.equals("")){
					
				}else{
					System.out.println("ERROR: Linea no parseada");
				}*/
			}
			
			puntosNuevos = (HashMap<Integer, Punto3D>) puntos.clone();
			
			// Centrar
			aplicarTransformacion(new MatrizTraslacion(- centro.getX(), - centro.getY(), - centro.getZ()));
			
			// Ajustar tamaño a ventana
			double ratio = 300 / Math.max((xMax - xMin) / 2, (yMax - yMin) / 2);
			aplicarTransformacion(new MatrizEscala(ratio));
			
//			System.out.println("Zs: " + zMin + " - " + zMax);
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
	
	public void aplicarTransformacion(MatrizTransformacion matriz){
		
		HashMap<Integer, Punto3D> puntosNuevos = new HashMap<Integer, Punto3D>();

		Set<Entry<Integer, Punto3D>> pares = this.puntosNuevos.entrySet();
		for (Entry<Integer, Punto3D> par : pares){
			puntosNuevos.put(par.getKey(), par.getValue().aplicarTransformacion(matriz.getMatriz()));
		}

		this.puntosNuevos = puntosNuevos;

		centro = centro.aplicarTransformacion(matriz.getMatriz());		
	}

	public Vector<Incidence> getElementos() {
		
		// Ordenar elementos segun valor medio de Z
		Collections.sort(elementos, new Comparator<Incidence>() {
			public int compare(Incidence elem1, Incidence elem2) {
				Double z1 = elem1.getZPromedio(puntosNuevos);
				Double z2 = elem2.getZPromedio(puntosNuevos);
				
				return z2.compareTo(z1);
			}
		});
		
		return elementos;		
	}
	
	public HashMap<Integer, Punto3D> getPuntos(){
		return puntosNuevos;
	}
	
	public Punto3D getCentro(){
		return centro;
	}
}
