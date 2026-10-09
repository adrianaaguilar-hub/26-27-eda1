package entregas.retoEstructuradeDatos;

public class Main {
    public static void main(String[] args) {
        ArraySimulado array = new ArraySimulado(3);

        array.guardar(0, "10");
        array.guardar(1, "20");
        array.guardar(2, "30");
        array.mostrar();

        System.out.println(array.obtener(1));

        array.guardar(1, "99");
        array.mostrar();

        array.guardar(5, "50");
    }
}
