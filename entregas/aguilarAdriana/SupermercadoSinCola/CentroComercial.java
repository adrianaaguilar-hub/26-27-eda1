package entregas.aguilarAdriana.Cola;

public class CentroComercial {

    private Cliente primerCliente;
    private Caja[] cajas;
    private Tiempo tiempo;
    private int totalClientesHistorico; 
    private int minutosSinClientes;
    private boolean llegaClienteEsteMinuto;
    final private double PROBABILIDAD_LLEGADA = 0.4;
    private Console console;

    public CentroComercial() {
        console = new Console();
        cajas = new Caja[5];
        for (int i = 0; i < cajas.length; i++) {
            cajas[i] = new Caja(i + 1);
        }
        cajas[0] = new CajaExpress(1);
        cajas[4] = new CajaExpress(5);
        tiempo = new Tiempo();
        primerCliente = null;
        totalClientesHistorico = 0;
        minutosSinClientes = 0;
    }

    public void simular() {
        do {
            tiempo.avanzar();
            this.procesarLlegadaCliente();
            this.registrarEstadoVirtual();
            this.asignarClientesACajas();
            this.procesarAtencionCajas();
            this.mostrarEstado();
            this.pausar();
        } while (!tiempo.haFinalizado());
        this.mostrarResumen();
    }

    private void procesarLlegadaCliente() {
        llegaClienteEsteMinuto = Math.random() <= PROBABILIDAD_LLEGADA;
        if (llegaClienteEsteMinuto) {
            totalClientesHistorico++;
            boolean esPrioritario = Math.random() <= 0.2; 
            Cliente nuevoCliente = new Cliente(totalClientesHistorico, esPrioritario);

            if (primerCliente == null) {
                primerCliente = nuevoCliente;
            } else {
                if (nuevoCliente.tienePrioridad() && !primerCliente.tienePrioridad()) {
                    nuevoCliente.enlazarSiguiente(primerCliente);
                    primerCliente = nuevoCliente;
                } else {
                    primerCliente.formarse(nuevoCliente);
                }
            }
        }
    }

    private void asignarClientesACajas() {
        for (int i = 0; i < cajas.length; i++) {
            if (cajas[i].estaLibre() && primerCliente != null && cajas[i].puedeAtender(primerCliente)) {
                cajas[i].asignar(primerCliente);
                primerCliente = primerCliente.obtenerSiguiente(); 
            }
        }
    }

    private void registrarEstadoVirtual() {
        if (primerCliente == null) {
            minutosSinClientes++;
        }
    }

    private void mostrarEstado() {
        console.cleanScreen();
        tiempo.mostrar(llegaClienteEsteMinuto);
        
        Cliente actual = primerCliente;
        while (actual != null) {
            actual.mostrar();
            actual = actual.obtenerSiguiente();
        }
        console.writeln("\n");
        
        for (int i = 0; i < cajas.length; i++) {
            cajas[i].mostrar();
        }
    }

    private void mostrarResumen() {
        int personasAtendidas = 0;
        int itemsVendidos = 0;
        for (int i = 0; i < cajas.length; i++) {
            personasAtendidas += cajas[i].obtenerPersonasAtendidas();
            itemsVendidos += cajas[i].obtenerItemsVendidos();
        }
        
        int personasEnEspera = 0;
        Cliente actual = primerCliente;
        while (actual != null) {
            personasEnEspera++;
            actual = actual.obtenerSiguiente();
        }

        console.writeln("Personas atendidas: " + personasAtendidas);
        console.writeln("Personas sin atender al cierre: " + personasEnEspera);
        console.writeln("Items vendidos: " + itemsVendidos);
        console.writeln("Minutos sin clientes esperando: " + minutosSinClientes);
    }

    private void pausar() {
        console.pause(1);
    }
}