import java.io.*;


public class Lector {

    static RandomAccessFile aleatorio;
    static final int LONGITUD_REGISTRO = 64;


    public void main(String[] args) throws IOException {
        // Clase :
        // static File fiche = new File("C:\\Users\\usuario\\Documents\\proyectoAleatorios\\AleatorioDepart.dat");
        // Casa:
        File fiche = new File("C:\\Users\\neon2\\Documents\\FicherosAleatorios\\AleatorioDepart.dat");
        if (fiche.exists()) {
            System.out.println("Se va a proceder a borrar el fichero AleatorioEmpleados.dat ya que existe");
            fiche.delete();
        }
        try {
            aleatorio = new RandomAccessFile(fiche, "rw");
        } catch (IOException e) {
            System.out.println("IOException");
        }
        insertaDepartamento((short) 90, "Manue", "Madry", (short) 3);
        System.out.println("---");

        consultaDepartamento(90);
        System.out.println(" ");
        System.out.println("---");
        consultaDepartamento(15);

        // listadoDepartamentos();
    }

    public void insertaDepartamento(int identificador, String nombre, String localidad, short empleados) throws IOException {
        if (!existeDepartamento(identificador)) {
            long posicion = (identificador - 1) * LONGITUD_REGISTRO;
            aleatorio.seek(posicion);
            aleatorio.writeInt(identificador);
            StringBuffer nombres = new StringBuffer(nombre);
            nombres.setLength(15);
            aleatorio.writeChars(nombres.toString());
            StringBuffer localidades = new StringBuffer(localidad);
            localidades.setLength(15);
            aleatorio.writeChars(localidades.toString());
            aleatorio.writeShort(empleados);
            int i = 1;
            String NOMBRE = "---------------";
            String LOCALIDAD = "---------------";
            short EMPLEADOS = 0;
            do {
                posicion = identificador-1 * LONGITUD_REGISTRO;
                aleatorio.seek(posicion);
                aleatorio.writeInt(identificador-i);
                StringBuffer Nombrebuffer = new StringBuffer(NOMBRE);
                Nombrebuffer.setLength(15);
                aleatorio.writeChars(Nombrebuffer.toString());
                StringBuffer Localidadbuffer = new StringBuffer(LOCALIDAD);
                Localidadbuffer.setLength(15);
                aleatorio.writeChars(Localidadbuffer.toString());
                aleatorio.writeShort(EMPLEADOS);
                i++;
            }while (!existeDepartamento(identificador-i));
        }
    }

    public void listadoDepartamentos() throws IOException {
        for (; ; ) {
            try {
                short identificador = aleatorio.readShort();
                System.out.println(identificador);

                // Si he recorrido todos los bytes salgo del for
                if (aleatorio.getFilePointer() == 9) {
                    break;

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void consultaDepartamento(int id) {
//        if (existeDepartamento(id)){
            try {
                long posicion = (id - 1) * LONGITUD_REGISTRO;
                aleatorio.seek(posicion); // nos posicionamos
                int idBuscado = aleatorio.readInt(); // obtengo id de empleado

                if (idBuscado == id || idBuscado == 0) {
                    char apellido[] = new char[15], ape;
                    for (int i = 0; i < apellido.length; i++) {
                        ape = aleatorio.readChar();//
                        apellido[i] = ape;
                    }
                    String apellidoS = new String(apellido).trim();// convierto a String el array
                    char localidad[] = new char[15], loc;
                    for (int i = 0; i < localidad.length; i++) {
                        loc = aleatorio.readChar();//
                        localidad[i] = loc;
                    }
                    String localidadS = new String(localidad).trim();// convierto a String el array

                    short empleados = aleatorio.readShort();

                    System.out.printf("ID: %d, Nombre: %s, Localidad: %s, Empleados: %d", id, apellidoS, localidadS, empleados);
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
            System.out.println(resultado);
            if (!resultado) {
                System.out.printf("ID: %d, NO EXISTE.%n", id);
            } else {
                aleatorio.seek(posicion);
                int identificador = aleatorio.readInt();
                resultado = identificador > 0;
                switch (identificador){
                    case 0 -> System.out.printf("ID: %d, Posición inicializada pero no usada%n",id);
                    case -1 -> System.out.printf("ID: %d, Registro Eliminado%n",id);
                    default -> System.out.printf("ID: %d, Registro Valido%n",id);
                }
            }
        }catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero o la posición indicada no es la correcta (Existencia).");
        }

        return resultado;
    }
}
