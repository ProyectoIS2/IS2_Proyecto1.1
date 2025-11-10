package iterator;

import java.util.List;

public class ExtendedIteratorImplementation implements ExtendedIterator<String>{
	private List<String> ciudades;
	private int posicion;
	
	public ExtendedIteratorImplementation(List<String> ciudades) {
		this.ciudades = ciudades;
		this.posicion = 0;
	}

	//comprueba si quedan elementos por recorrer
	@Override
	public boolean hasNext() { 
		return (posicion < ciudades.size());
	}

	//devuelve el elemento en la posicion actual y avanza su posicion
	@Override
	public String next() { 
		String ciudad = ciudades.get(posicion);
		posicion +=1;
		return ciudad;
	}

	//Devuelve el elemento en la posición actual y retrocede la posición
	@Override
	public String previous() { 
		String ciudad = ciudades.get(posicion);
		posicion -=1;
		return ciudad;
	}

	//Comprueba si es la primera posición o no. Si fuera position > 0, el bucle se detendría cuando el valor de 
	//posicion fuera 0, y nunca mostraría el primer elemento de la lista.
	@Override
	public boolean hasPrevious() { 
		return posicion >= 0;
	}

	//inicializa el valor de posicion a 0
	@Override
	public void goFirst() { 
		posicion = 0;
		
	}

	//inicializa el valor posicion al ultimo elemento de la lista ciudades, es decir, a la ultima posicion
	@Override
	public void goLast() { 
		posicion = ciudades.size()-1;
	}

}
