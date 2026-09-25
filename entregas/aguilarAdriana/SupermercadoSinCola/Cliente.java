package entregas.aguilarAdriana.Cola;

public class Cliente {
    private int id;
    private int items;
    private boolean tienePrioridad;
    private Cliente siguiente; 
    private Console console;

    public Cliente(int id, boolean tienePrioridad) {
        this.id = id;
        this.tienePrioridad = tienePrioridad;
        this.items = this.generarItems();
        this.siguiente = null;
        this.console = new Console();
    }

    private int generarItems() {
        return (int) (Math.random() * (15 - 5) + 5);
    }

   
    public void formarse(Cliente nuevo) {
        if (this.siguiente == null) {
            this.siguiente = nuevo;
        } else if (nuevo.tienePrioridad() && !this.siguiente.tienePrioridad()) {
            nuevo.enlazarSiguiente(this.siguiente);
            this.siguiente = nuevo;
        } else {
            this.siguiente.formarse(nuevo);
        }
    }

    public void enlazarSiguiente(Cliente cliente) {
        this.siguiente = cliente;
    }

    public Cliente obtenerSiguiente() {
        return this.siguiente;
    }

    public boolean tienePrioridad() {
        return tienePrioridad;
    }

    public int obtenerItems() {
        return items;
    }

    public void mostrar() {
        String indicadorPrioridad = tienePrioridad ? "P" : "";
        console.write("[C" + id + "-" + items + indicadorPrioridad + "]_O/ ");
    }
}