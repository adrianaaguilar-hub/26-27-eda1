package entregas.aguilarAdriana.Cola;

public class Cola {

    private Cliente[] clientes;
    private final int CAPACIDAD_MAXIMA = 100;
    private int minutosSinClientes;
    private int tamaño;
    private Console console;

    public Cola() {
        clientes = new Cliente[CAPACIDAD_MAXIMA];
        minutosSinClientes = 0;
        tamaño = 0;
        console = new Console();
    }

    public void registrarEstado() {
        if (tamaño == 0) {
            minutosSinClientes = minutosSinClientes + 1;
        }
    }

    public boolean encolar(Cliente nuevoCliente) {
        if (this.estaLlena() || nuevoCliente == null) {
            return false;
        }

        if (nuevoCliente.tienePrioridad()) {
            this.insertarAlFrente(nuevoCliente);
        } else {
            this.insertarAlFinal(nuevoCliente);
        }
        
        this.tamaño++;
        return true;
    }

    private boolean estaLlena() {
        return this.tamaño >= this.CAPACIDAD_MAXIMA;
    }

    private void insertarAlFinal(Cliente clienteNormal) {
        this.clientes[this.tamaño] = clienteNormal;
    }

    private void insertarAlFrente(Cliente clientePrioritario) {
        for (int i = this.tamaño; i > 0; i--) {
            this.clientes[i] = this.clientes[i - 1];
        }
        
        this.clientes[0] = clientePrioritario;
    }

    public boolean hayClientes() {
        return tamaño > 0;
    }

    public Cliente quitarCliente() {
        Cliente cliente = clientes[0];
        for (int i = 0; i < tamaño - 1; i++) {
            clientes[i] = clientes[i + 1];
        }
        clientes[tamaño - 1] = null;
        tamaño = tamaño - 1;
        return cliente;
    }

    public void mostrar() {
        for(int i=0;i<tamaño;i++){
            clientes[i].mostrar();
        }
        console.writeln();
    }

    public int obtenerMinutosSinClientes() {
        return minutosSinClientes;
    }

    public int obtenerCantidadPersonasEnCola() {
        return tamaño;
    }

    public Cliente primero() {
        return clientes[0];
    }

    public boolean estaVacia() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estaVacia'");
    }

    public void desencolar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desencolar'");
    }

}