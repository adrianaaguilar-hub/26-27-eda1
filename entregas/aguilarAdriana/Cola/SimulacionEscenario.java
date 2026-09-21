package entregas.aguilarAdriana.Cola;

public class SimulacionEscenario {

    private static final int MINUTOS_TOTALES = 240;
    private static final double PROBABILIDAD_LLEGADA = 0.6;
    private static final double PROBABILIDAD_ATENCION = 0.4;
    private static final double PROBABILIDAD_PRIORIDAD = 0.2;

    private Cola colaEspera;
    private int cantidadPersonasAtendidas;
    private Console console;

    public SimulacionEscenario() {
        this.colaEspera = new Cola();
        this.cantidadPersonasAtendidas = 0;
        this.console = new Console();
    }

    public void ejecutar() {
        for (int minutoActual = 1; minutoActual <= MINUTOS_TOTALES; minutoActual++) {
            this.evaluarLlegadaCliente();
            this.evaluarAperturaCaja();
        }
        this.mostrarResultados();
    }

    private void evaluarLlegadaCliente() {
        if (Math.random() <= PROBABILIDAD_LLEGADA) {
            boolean esPrioritario = Math.random() <= PROBABILIDAD_PRIORIDAD;
            Cliente nuevoCliente = new Cliente(esPrioritario);
            this.colaEspera.encolar(nuevoCliente);
        }
    }

    private void evaluarAperturaCaja() {
        if (Math.random() <= PROBABILIDAD_ATENCION) {
            if (!this.colaEspera.estaVacia()) {
                this.colaEspera.desencolar();
                this.cantidadPersonasAtendidas++;
            }
        }
    }

    private void mostrarResultados() {
        this.console.writeln("Resultados tras 4 horas de simulacion:");
        this.console.writeln("Personas atendidas: " + this.cantidadPersonasAtendidas);
        this.console.writeln("Personas que quedaron en fila: " + this.colaEspera.obtenerCantidadPersonasEnCola());
    }
}
