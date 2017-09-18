package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
			
		if(carpetaDeTrabajo.canWrite()) {
			//que no exista -> lanzará una excepción
			if(!file.exists()) {
				file.mkdir();
			} else {
				throw new GestionFicherosException("Ya existe esa carpeta");
			}
		} else {
			throw new GestionFicherosException("Este usuario no tiene permisos de escritura");
		}
		
		//crear la carpeta -> lanzará una excepción
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		if(carpetaDeTrabajo.canWrite()) {
			//que no exista -> lanzará una excepción
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new GestionFicherosException("Ocurrió un error creando el fichero");
				}
			} else {
				throw new GestionFicherosException("Ya existe esa carpeta");
			}
		} else {
			throw new GestionFicherosException("Este usuario no tiene permisos de escritura");
		}
		
		//crear el fichero -> lanzará una excepción
		actualiza();
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		if(file.canWrite()) {
			//que no exista -> lanzará una excepción
			if(file.exists()) {
				file.delete();
			} else {
				throw new GestionFicherosException("El fichero no existe");
			}
		} else {
			throw new GestionFicherosException("Este usuario no tiene permisos de escritura");
		}
		
		//crear la carpeta -> lanzará una excepción
		actualiza();

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción
		if(file.exists()) {
			//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
			if(file.canRead()) {
				//Título
				strBuilder.append("INFORMACIÓN DEL SISTEMA");
				strBuilder.append("\n\n");
				
				//Nombre
				strBuilder.append("Nombre: \t");
				strBuilder.append(arg0);
				strBuilder.append("\n\n");
				
				//Tipo: fichero o directorio
				strBuilder.append("Tipo: \t" + (file.isDirectory() ? "Directorio" : "Fichero"));
				strBuilder.append("\n\n");
				
				//Ubicación
				strBuilder.append("Ubicación: \t" + file.getPath());
				strBuilder.append("\n\n");
				
				//Fecha de última modificación
				strBuilder.append("Fecha última modificación: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(file.lastModified())));
				strBuilder.append("\n\n");
				
				//Si es un fichero oculto o no
				strBuilder.append("Oculto: \t" + (file.isHidden() ? "Sí" : "No"));
				strBuilder.append("\n\n");
				//Si es directorio: Espacio libre, espacio disponible, espacio total
				if(file.isDirectory()) {
					strBuilder.append("Nº Elementos: ");
					strBuilder.append(file.list().length);
					strBuilder.append("\n\n");
					strBuilder.append("Espacio libre: ");
					strBuilder.append(file.getFreeSpace());
					strBuilder.append("\n\n");
					strBuilder.append("Espacio disponible: ");
					strBuilder.append(file.getUsableSpace());
					strBuilder.append("\n\n");
					strBuilder.append("Espacio total: ");
					strBuilder.append(file.getTotalSpace());
					strBuilder.append("\n\n");
				} else {
					//Si es un fichero: tamaño en bytes
					strBuilder.append("Tamaño: ");
					strBuilder.append(file.list().length + " bytes");
				}
				strBuilder.append("\n");
				
				return strBuilder.toString();
			} else {
				throw new GestionFicherosException("No tienes permisos de lectura sobre el archivo: "+file.getAbsolutePath());
			}
		} else {
			throw new GestionFicherosException("No existe el archivo: "+file.getAbsolutePath());
		}
		
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		if(carpetaDeTrabajo.canWrite()) {
			//que no exista -> lanzará una excepción
			if(new File(carpetaDeTrabajo, arg1).exists()) {
				file.renameTo(new File(carpetaDeTrabajo, arg1));
			} else {
				throw new GestionFicherosException("No existe el fichero");
			}
		} else {
			throw new GestionFicherosException("Este usuario no tiene permisos de escritura");
		}
		
		//renombra el archivo/directorio -> lanzará una excepción
		actualiza();

	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
