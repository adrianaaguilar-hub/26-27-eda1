package entregas.aguilarAdriana;

class ListaEnlazada {
    private Nodo cabeza;

    public ListaEnlazada() {
        this.cabeza = null;
    }

    public void insertarAlPrincipio(int dato) {
        Nodo nuevoNodo = new Nodo(dato);
        nuevoNodo.siguiente = cabeza;
        cabeza = nuevoNodo;
    }

    public void eliminarAlPrincipio() {
        if (cabeza != null) {
            cabeza = cabeza.siguiente;
        }
    }

    public void imprimirLista() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.print(actual.dato + " -> ");
            actual = actual.siguiente;
        }
        System.out.println("null");
    }

    public void eliminarRepetidos() {
    Nodo nodoDummy = new Nodo(0);
    nodoDummy.siguiente = cabeza;

    Nodo nodoAnterior = nodoDummy;

    while (nodoAnterior.siguiente != null) {
        Nodo nodoActual = nodoAnterior.siguiente;
        boolean esRepetido = false;

        while (nodoActual.siguiente != null && nodoActual.dato == nodoActual.siguiente.dato) {

            esRepetido = true;
            nodoActual = nodoActual.siguiente;
        }

        if (esRepetido) {
            nodoAnterior.siguiente = nodoActual.siguiente;
        } else {
            nodoAnterior = nodoActual;
        }
    }

    cabeza = nodoDummy.siguiente;
}

    public void eliminarRepetidosSinDummy() {

    }
}

