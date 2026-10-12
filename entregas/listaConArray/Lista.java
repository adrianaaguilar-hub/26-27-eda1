package entregas.listaConArray;

public class Lista {
    
    private String[] elementos;
    private int contador;

    public Lista(int capacidad) {
        this.elementos = new String[capacidad];
        this.contador = 0;
    }

    public int tamaño() {
        return this.contador;
    }

    public boolean estaVacia() {
        return this.contador == 0;
    }

    public void añadir(String elemento) {
        if (this.contador < this.elementos.length) {
            this.elementos[this.contador] = elemento;
            this.contador++;
        }
    }

    public void insertar(int posicion, String elemento) {
        if (posicion >= 0 && posicion <= this.contador && this.contador < this.elementos.length) {
            for (int i = this.contador; i > posicion; i--) {
                this.elementos[i] = this.elementos[i - 1];
            }
            this.elementos[posicion] = elemento;
            this.contador++;
        }
    }

    public String obtener(int posicion) {
        String elementoEncontrado = null;
        if (posicion >= 0 && posicion < this.contador) {
            elementoEncontrado = this.elementos[posicion];
        }
        return elementoEncontrado;
    }

    public String eliminar(int posicion) {
        String elementoEliminado = null;
        if (posicion >= 0 && posicion < this.contador) {
            elementoEliminado = this.elementos[posicion];
            for (int i = posicion; i < this.contador - 1; i++) {
                this.elementos[i] = this.elementos[i + 1];
            }
            this.elementos[this.contador - 1] = null;
            this.contador--;
        }
        return elementoEliminado;
    }

    public void mostrar() {
        for (int i = 0; i < this.contador; i++) {
            System.out.println(this.elementos[i]);
        }
    }
}