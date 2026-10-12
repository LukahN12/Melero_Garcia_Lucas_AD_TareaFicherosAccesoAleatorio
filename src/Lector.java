import org.iesalandalus.programacion.utilidades.Entrada;

import java.io.*;


public class Lector {

    static RandomAccessFile aleatorio;
    static final int LONGITUD_REGISTRO = 64;
    // Clase :
    // File fiche = new File("C:\\Users\\usuario\\Documents\\proyectoAleatorios\\AleatorioDepart.dat");
    // Casa:
    File fiche = new File("C:\\Users\\neon2\\Documents\\FicherosAleatorios\\AleatorioDepart.dat");


    public void main(String[] args) throws IOException {
        if (fiche.exists()) {
            System.out.println("Se va a proceder a borrar el fichero AleatorioDepart.dat ya que existe");
            fiche.delete();
        }
        try {
            aleatorio = new RandomAccessFile(fiche, "rw");
        } catch (IOException e) {
            System.out.println("IOException");
        }
        insertaDepartamento((short) 150, "Manue", "Madry", (short) 3);
        System.out.println("---");
        consultaDepartamento(150);
        System.out.println(" ");
        System.out.println("---");
        insertaDepartamento((short) 75, "Pepe", "Alicante", (short) 3);
        System.out.println(" ");
        System.out.println("---");
        consultaDepartamento(75);
        consultaDepartamento(73);

//        System.out.println("---");
//        inspeccionFisica();

        // listadoDepartamentos();
    }

    public void insertaDepartamento(short identificador, String nombre, String localidad, short empleados) throws IOException {
        boolean valido = true;
        if (existeDepartamento(identificador)) {
            do {
                System.out.print("¿Estas seguro que quieres sobreescribir (Y / N)? ");
            }while (!Entrada.cadena().equalsIgnoreCase("y")&&!Entrada.cadena().equalsIgnoreCase("n"));
        }

        if (valido) {
            long posicion = (identificador - 1) * LONGITUD_REGISTRO;
            aleatorio.seek(posicion);
            aleatorio.writeShort(identificador);
            StringBuffer nombres = new StringBuffer(nombre);
            nombres.setLength(15);
            aleatorio.writeChars(nombres.toString());
            StringBuffer localidades = new StringBuffer(localidad);
            localidades.setLength(15);
            aleatorio.writeChars(localidades.toString());
            aleatorio.writeShort(empleados);
            short i = 1;
            String NOMBRE = "---------------";
            String LOCALIDAD = "---------------";
            short EMPLEADOS = 0;
            do {
                posicion = (identificador - (i + 1)) * LONGITUD_REGISTRO;
                aleatorio.seek(posicion);
                aleatorio.writeShort(0);
                StringBuffer Nombrebuffer = new StringBuffer(NOMBRE);
                Nombrebuffer.setLength(15);
                aleatorio.writeChars(Nombrebuffer.toString());
                StringBuffer Localidadbuffer = new StringBuffer(LOCALIDAD);
                Localidadbuffer.setLength(15);
                aleatorio.writeChars(Localidadbuffer.toString());
                aleatorio.writeShort(EMPLEADOS);
                i++;
            } while (!existeDepartamento(identificador - i));
        }
    }

    public void inspeccionFisica() throws IOException {
        long id = 1;
        do {
            try {
                long posicion = (id - 1) * LONGITUD_REGISTRO;
                aleatorio.seek(posicion);
                long idBuscado = ( aleatorio.getFilePointer() / 64 ) + 1;
                if (idBuscado == id || idBuscado == 0) {
                    short idResultado = aleatorio.readShort();
                    char[] nombre = new char[15];
                    for (int i = 0; i < nombre.length; i++) {
                        nombre[i] = aleatorio.readChar();
                    }
                    String nombreS = new String(nombre).trim();// convierto a String el array
                    char[] localidad = new char[15];
                    for (int i = 0; i < localidad.length; i++) {
                        localidad[i] = aleatorio.readChar();
                    }
                    String localidadS = new String(localidad).trim();// convierto a String el array

                    short empleados = aleatorio.readShort();

                    System.out.printf("ID: %d, Nombre: %s, Localidad: %s, Empleados: %d \n", idResultado, nombreS, localidadS, empleados);
                } else {
                    System.out.printf("ID: %d,DA ERROR.%n", id);
                }
            } catch (FileNotFoundException e) {
                System.out.println("El fichero no ha sido encontrado.");
            } catch (IllegalArgumentException e) {
                System.out.println("El modo de lectura escritura del fichero no es correcto.");
            } catch (IOException e) {
                System.out.println("Se ha producido un error de E/S en el fichero o la posición indicada no es la correcta.");
            }
            id++;
        } while (existeDepartamento((int) id));
    }

    public void consultaDepartamento(int id) {
            try {
                long posicion = (long) (id - 1) * LONGITUD_REGISTRO;
                aleatorio.seek(posicion);
                long idBuscado = ( aleatorio.getFilePointer() / 64 ) + 1;
                if (idBuscado == id || idBuscado == 0) {
                    short idResultado = aleatorio.readShort();
                    char[] nombre = new char[15];
                    for (int i = 0; i < nombre.length; i++) {
                        nombre[i] = aleatorio.readChar();
                    }
                    String nombreS = new String(nombre).trim();// convierto a String el array
                    char[] localidad = new char[15];
                    for (int i = 0; i < localidad.length; i++) {
                        localidad[i] = aleatorio.readChar();
                    }
                    String localidadS = new String(localidad).trim();// convierto a String el array

                    short empleados = aleatorio.readShort();

                    System.out.printf("ID: %d, Nombre: %s, Localidad: %s, Empleados: %d \n", idResultado, nombreS, localidadS, empleados);

                } else {
                    System.out.printf("ID: %d,DA ERROR.%n", id);
                }
            } catch (FileNotFoundException e) {
                System.out.println("El fichero no ha sido encontrado.");
            } catch (IllegalArgumentException e) {
                System.out.println("El modo de lectura escritura del fichero no es correcto.");
            } catch (IOException e) {
                System.out.println("Se ha producido un error de E/S en el fichero o la posición indicada no es la correcta.");
            }
    }

    public boolean existeDepartamento(int id){
        boolean resultado = false;
        long posicion = (id - 1) * LONGITUD_REGISTRO;
        try {
            resultado = posicion < aleatorio.length();
            if (resultado) {
                aleatorio.seek(posicion);
                int identificador = aleatorio.readInt();
                resultado = identificador > 0;
            }
        }catch (IOException e) {
            System.out.println("Se ha alcanzado el extremo del fichero.");
        }

        return resultado;
    }

    public void eliminaDepartamento(int id) throws FileNotFoundException {
        if(existeDepartamento(id)){
            long posicion = (id - 1) * LONGITUD_REGISTRO;
            try {
                aleatorio.seek(posicion);
                do {
                    System.out.print("CONFIRMA LA OPERACIÓN DE BORRADO ( Y / N ) ");
                }while (!Entrada.cadena().equalsIgnoreCase("y")&&!Entrada.cadena().equalsIgnoreCase("n"));

                char Respuesta = Entrada.caracter();
                if (Respuesta == 'Y'){
                    aleatorio.writeShort(-1);
                    StringBuffer nombres = new StringBuffer("***************");
                    nombres.setLength(15);
                    aleatorio.writeChars(nombres.toString());
                    StringBuffer localidades = new StringBuffer("***************");
                    localidades.setLength(15);
                    aleatorio.writeChars(localidades.toString());
                    aleatorio.writeShort(-1);
                } else {
                    System.out.println("Operación cancelada");
                }
            }catch (IOException e) {
                System.out.println("Se ha alcanzado el extremo del fichero.");
            }
        }
    }
}
