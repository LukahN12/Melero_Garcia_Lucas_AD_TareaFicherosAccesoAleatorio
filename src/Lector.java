import java.io.*;


public class Lector {
    static File fiche = new File("C:\\Users\\usuario\\Documents\\proyectoAleatorios\\AleatorioDepart.dat");
    static RandomAccessFile aleatorio;
    public void main(String[] args) throws IOException {
        try {
            aleatorio = new RandomAccessFile(fiche,"rw");
        }catch(IOException e) {
            System.out.println("IOException");
        }

        insertaDepartamento((short) 7,"Manue","Madry",(short) 3);
        // listadoDepartamentos();
    }

    public void insertaDepartamento(short identificador,String nombre,String localidad,short empleados) throws IOException {
        aleatorio.seek(identificador);
        if (aleatorio.read() == -1) {
            aleatorio.writeInt(identificador);
            StringBuffer nombres = new StringBuffer(nombre);
            nombres.setLength(15);
            aleatorio.writeChars(nombres.toString());
            StringBuffer localidades = new StringBuffer(localidad);
            localidades.setLength(15);
            aleatorio.writeChars(localidades.toString());
            aleatorio.writeShort(empleados);

            if (identificador >= aleatorio.length()){
                String NOMBRE = "---------------";
                String LOCALIDAD = "---------------";
                short EMPLEADOS = 0;
                long n = aleatorio.getFilePointer();

                for (long i = 0; i < n; i++) {
                    aleatorio.seek(i);

                    aleatorio.writeInt((int) i);
                    StringBuffer Nombrebuffer = new StringBuffer(NOMBRE);
                    Nombrebuffer.setLength(15);
                    aleatorio.writeChars(Nombrebuffer.toString());
                    StringBuffer Localidadbuffer = new StringBuffer(LOCALIDAD);
                    Localidadbuffer.setLength(15);
                    aleatorio.writeChars(Localidadbuffer.toString());//
                    aleatorio.writeShort(EMPLEADOS);

                }
            }
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
            } catch (IOException e){
                System.out.println(e.getMessage());
            }

        }
    }
}
