import org.iesalandalus.programacion.utilidades.Entrada;

import java.io.*;
import java.util.Locale;


public class Lector {

    static RandomAccessFile aleatorio;
    static final int LONGITUD_REGISTRO = 64;
    // Clase :
    // File fiche = new File("C:\\Users\\usuario\\Documents\\proyectoAleatorios\\AleatorioDepart.dat");
    // Casa:
    File fiche = new File("C:\\Users\\neon2\\Documents\\FicherosAleatorios\\AleatorioDepart.dat");


    public void main(String[] args) throws IOException {
        if (integrador()){
            try {
                aleatorio = new RandomAccessFile(fiche, "rw");
            } catch (IOException e) {
                System.out.println("IOException");
            }
            insertaDepartamento((short) 2, "Manue", "Madry", (short) 3);
            System.out.println("---");
            consultaDepartamento(2);
            System.out.println(" ");
            System.out.println("---");
            insertaDepartamento((short) 3, "Pepe", "Alicante", (short) 8);
            System.out.println(" ");
            System.out.println("---");
            consultaDepartamento(3);
            consultaDepartamento(1);

            System.out.println("---");
            actualizaNumeroEmpleados((short) 2, (short) 1);
            System.out.println("---");
            consultaDepartamento(2);

            System.out.println("---");
            eliminaDepartamento(3);
            System.out.println("---");
            consultaDepartamento(3);
            System.out.println("---");
            estadisticas();
            // inspeccionFisica();
        }
    }

    public void menu(){

    }

    public boolean integrador(){
        boolean resultado = true;

        try {
            if (aleatorio.length() % 64 != 0){
                resultado = false;
                System.out.println("La longitud no es multiplo de 64");
            }

            long id = 1;
            short numeroDepartamento;
            int departamentosActivos = 0;
            int departamentosInicializados = 0;
            int departamentosEliminados = 0;
            int totalEmpleados = 0;

            do {
                try {
                    long posicion = (id - 1) * LONGITUD_REGISTRO;
                    aleatorio.seek(posicion);
                    numeroDepartamento = aleatorio.readShort();
                    System.out.printf("%d | %d\n", numeroDepartamento,id);
                    if (numeroDepartamento > 0) {
                        departamentosActivos++;
                        aleatorio.seek(posicion);
                        short idResultado = aleatorio.readShort();
                        if (id != idResultado){
                            resultado = false;
                            System.out.println("Valores incongruentes de identificador positivo");
                        }

                        char[] nombre = new char[15];
                        for (int i = 0; i < nombre.length; i++) {
                            nombre[i] = aleatorio.readChar();
                        }
                        String nombreS = new String(nombre).trim();// Para esquipear bites

                        if (nombreS.isBlank() || nombreS.isEmpty()){
                            resultado = false;
                            System.out.println("Nombre vacio en un registro activo");
                        }

                        char[] localidad = new char[15];
                        for (int i = 0; i < localidad.length; i++) {
                            localidad[i] = aleatorio.readChar();
                        }
                        String localidadS = new String(localidad).trim();// Para esquipear bites

                        if (localidadS.isBlank() || localidadS.isEmpty()){
                            resultado = false;
                            System.out.println("Localidad vacia en un registro activo");
                        }

                        short empleados = aleatorio.readShort();

                        if (empleados < 0){
                            resultado = false;
                            System.out.println("Empleados negativos en un registro activo");
                        }


                    }

                    if (numeroDepartamento > -1){
                        resultado = false;
                        System.out.println("Valores de negativo inferiores a -1");

                    }

                    if (numeroDepartamento == -1){
                        aleatorio.seek(posicion);
                        short idResultado = aleatorio.readShort();

                        char[] nombre = new char[15];
                        for (int i = 0; i < nombre.length; i++) {
                            nombre[i] = aleatorio.readChar();
                        }
                        String nombreS = new String(nombre).trim();// Para esquipear bites

                        if (!nombreS.equals("---------------")){
                            resultado = false;
                            System.out.println("Formato incorrecto en nombre vacio");
                        }

                        char[] localidad = new char[15];
                        for (int i = 0; i < localidad.length; i++) {
                            localidad[i] = aleatorio.readChar();
                        }
                        String localidadS = new String(localidad).trim();// Para esquipear bites

                        if (!localidadS.equals("---------------")){
                            resultado = false;
                            System.out.println("Formato incorrecto en localidad vacia");
                        }

                        short empleados = aleatorio.readShort();
                    }

                    if (numeroDepartamento == 0){
                        departamentosInicializados++;

                        aleatorio.seek(posicion);
                        short idResultado = aleatorio.readShort();

                        char[] nombre = new char[15];
                        for (int i = 0; i < nombre.length; i++) {
                            nombre[i] = aleatorio.readChar();
                        }
                        String nombreS = new String(nombre).trim();// Para esquipear bites

                        if (!nombreS.equals("***************")){
                            resultado = false;
                            System.out.println("Formato incorrecto en nombre borrada");
                        }

                        char[] localidad = new char[15];
                        for (int i = 0; i < localidad.length; i++) {
                            localidad[i] = aleatorio.readChar();
                        }
                        String localidadS = new String(localidad).trim();// Para esquipear bites

                        if (!localidadS.equals("***************")){
                            resultado = false;
                            System.out.println("Formato incorrecto en localidad borrada");
                        }

                        short empleados = aleatorio.readShort();

                    }
                } catch (FileNotFoundException e) {
                    System.out.println("El fichero no ha sido encontrado.");
                } catch (IllegalArgumentException e) {
                    System.out.println("El modo de lectura escritura del fichero no es correcto.");
                } catch (IOException e) {
                    System.out.println("Se ha producido un error de E/S en el fichero o la posición indicada no es la correcta. (Estadistico) ");
                }
                id++;
            } while (existeDepartamento(id) && resultado);
            System.out.printf("Posiciones activas: %d \nPosiciones Inicializadas: %d \nPosiciones Eliminadas: %d \n",departamentosActivos,departamentosInicializados, departamentosEliminados);
            double porcentaje = ((double) departamentosActivos / (departamentosEliminados + departamentosInicializados)) * 100 ;
            System.out.println("Ratio de ocupación: " + porcentaje + "%");
            System.out.println("El total de empleados activos son: " + totalEmpleados);
        }catch (IOException e){
            System.out.println("Error de E/S en el integrador");
        }


        return resultado;
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

    public void inspeccionFisica() {
        long id = 1;
        do {
            consultaDepartamento(id);
            id++;
        } while (existeDepartamento((int) id));
    }

    public void consultaDepartamento(long id) {
            try {
                long posicion = (id - 1) * LONGITUD_REGISTRO;
                aleatorio.seek(posicion);

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

            } catch (FileNotFoundException e) {
                System.out.println("El fichero no ha sido encontrado.");
            } catch (IllegalArgumentException e) {
                System.out.println("El modo de lectura escritura del fichero no es correcto.");
            } catch (IOException e) {
                System.out.println("Se ha producido un error de E/S en el fichero o la posición indicada no es la correcta.");
            }
    }

    public boolean existeDepartamento(long id){
        boolean resultado = true;
        long posicion = (id - 1) * LONGITUD_REGISTRO;
        try {
            resultado = posicion < aleatorio.length();
            if (resultado) {
                aleatorio.seek(posicion);
                int identificador = aleatorio.readInt();
                resultado = identificador != 0;
            }
        }catch (IOException e) {
            System.out.println("Se ha alcanzado el extremo del fichero.");
        }

        return resultado;
    }

    public void eliminaDepartamento(int id) {
        if(existeDepartamento(id)){
            long posicion = (id - 1) * LONGITUD_REGISTRO;
            try {
                String respuesta;
                aleatorio.seek(posicion);
                do {
                    System.out.print("CONFIRMA LA OPERACIÓN DE BORRADO ( Y / N ) ");
                    respuesta =  Entrada.cadena().toLowerCase(Locale.ROOT);
                }while (!respuesta.equalsIgnoreCase("y")&&!respuesta.equalsIgnoreCase("n"));

                if (respuesta.equalsIgnoreCase("y")){
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

    public void actualizaNumeroEmpleados(short numDepartamento, short nuevoEmpleado){
        if(existeDepartamento(numDepartamento)){
            long posicion = (numDepartamento - 1) * LONGITUD_REGISTRO;
            try {
                String respuesta;
                aleatorio.seek(posicion);
                do {
                    System.out.print("CONFIRMA LA OPERACIÓN DE EDICIÓN ( Y / N ) ");
                    respuesta =  Entrada.cadena().toLowerCase(Locale.ROOT);
                }while (!respuesta.equalsIgnoreCase("y")&&!respuesta.equalsIgnoreCase("n"));

                if (respuesta.equalsIgnoreCase("y")){
                    aleatorio.writeShort(numDepartamento);
                    aleatorio.skipBytes(60);
                    aleatorio.writeShort(nuevoEmpleado);
                } else {
                    System.out.println("Operación cancelada");
                }
            }catch (IOException e) {
                System.out.println("Se ha alcanzado el extremo del fichero.");
            }
        } else {
            System.out.println("No se puede editar un registro inexistente");
        }
    }

    public void actualizaNombreLocalidad(int numDepartamento, String nombre, String localidad){
        if(existeDepartamento(numDepartamento)){
            long posicion = (numDepartamento - 1) * LONGITUD_REGISTRO;
            try {
                aleatorio.seek(posicion);
                String Respuesta;
                do {
                    System.out.print("CONFIRMA LA OPERACIÓN DE EDICIÓN ( Y / N ) ");
                    Respuesta =  Entrada.cadena().toLowerCase(Locale.ROOT);
                }while (!Respuesta.equalsIgnoreCase("y")&&!Respuesta.equalsIgnoreCase("n"));

                if (Respuesta.equalsIgnoreCase("y")){
                    aleatorio.writeShort(numDepartamento);
                    StringBuffer Nombrebuffer = new StringBuffer(nombre);
                    Nombrebuffer.setLength(15);
                    aleatorio.writeChars(Nombrebuffer.toString());
                    StringBuffer Localidadbuffer = new StringBuffer(localidad);
                    Localidadbuffer.setLength(15);
                    aleatorio.writeChars(Localidadbuffer.toString());
                } else {
                    System.out.println("Operación cancelada");
                }
            }catch (IOException e) {
                System.out.println("Se ha alcanzado el extremo del fichero.");
            }
        } else {
            System.out.println("No se puede editar un registro inexistente");
        }
    }

    public void estadisticas() {
        try {
            System.out.println("La longitud del fichero es: " + aleatorio.length());
        }catch (IOException e){
            System.out.println("ERROR ESTADISTICO");
        }

        long id = 1;
        short numeroDepartamento;
        int departamentosActivos = 0;
        int departamentosInicializados = 0;
        int departamentosEliminados = 0;
        int totalEmpleados = 0;

        do {
            try {
                long posicion = (id - 1) * LONGITUD_REGISTRO;
                aleatorio.seek(posicion);
                numeroDepartamento = aleatorio.readShort();
                System.out.printf("%d | %d\n", numeroDepartamento,id);
                if (numeroDepartamento > 0) {
                    departamentosActivos++;
                        aleatorio.seek(posicion);

                        short idResultado = aleatorio.readShort();

                        char[] nombre = new char[15];
                        for (int i = 0; i < nombre.length; i++) {
                            nombre[i] = aleatorio.readChar();
                        }
                        String nombreS = new String(nombre).trim();// Para esquipear bites

                        char[] localidad = new char[15];
                        for (int i = 0; i < localidad.length; i++) {
                            localidad[i] = aleatorio.readChar();
                        }
                        String localidadS = new String(localidad).trim();// Para esquipear bites

                        short empleados = aleatorio.readShort();
                        System.out.println(empleados);
                        totalEmpleados += empleados;


                }

                if (numeroDepartamento == -1){
                    departamentosEliminados++;
                }

                if (numeroDepartamento == 0){
                    departamentosInicializados++;
                }
            } catch (FileNotFoundException e) {
                System.out.println("El fichero no ha sido encontrado.");
            } catch (IllegalArgumentException e) {
                System.out.println("El modo de lectura escritura del fichero no es correcto.");
            } catch (IOException e) {
                System.out.println("Se ha producido un error de E/S en el fichero o la posición indicada no es la correcta. (Estadistico) ");
            }
            id++;
        } while (existeDepartamento(id));
        System.out.printf("Posiciones activas: %d \nPosiciones Inicializadas: %d \nPosiciones Eliminadas: %d \n",departamentosActivos,departamentosInicializados, departamentosEliminados);
        double porcentaje = ((double) departamentosActivos / (departamentosEliminados + departamentosInicializados)) * 100 ;
        System.out.println("Ratio de ocupación: " + porcentaje + "%");
        System.out.println("El total de empleados activos son: " + totalEmpleados);
    }
}


