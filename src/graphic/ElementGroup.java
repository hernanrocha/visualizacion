package graphic;

import java.util.Vector;

public class ElementGroup {
	
	private String tipo;
	private int cantidad;
	private int gradoIncidencia;
	private Vector<Incidence> elementos = new Vector<Incidence>();

	public ElementGroup(String tipo, int cantidad) {
		super();
		this.tipo = tipo;
		this.cantidad = cantidad;
		
		gradoIncidencia = 3;
		
	}

	public String getTipo() {
		return tipo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public int getGradoIncidencia() {
		return gradoIncidencia;
	}	
	
	public Vector<Incidence> getElementos() {
		return elementos;
	}
	
	public void agregarElemento(Incidence elemento){
		elementos.add(elemento);
	}
	
	public int getElemCant(){
		return elementos.size();
	}

}
