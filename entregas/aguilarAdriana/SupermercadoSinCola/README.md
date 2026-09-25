# SupermercadoSinCola

## Caja.java

```java
package entregas.aguilarAdriana.Cola;

public class Caja {

    protected int numero;
    private Cliente cliente;
    private int itemsVendidos;
    private int personasAtendidas;
    protected int itemsRestantes;
    private Console console;

    public Caja(int numero) {
        this.numero = numero;
        itemsVendidos = 0;
        personasAtendidas = 0;
        itemsRestantes = 0;
        console = new Console();
    }

    public boolean estaLibre() {
        return cliente == null;
    }

    public void asignar(Cliente cliente) {
        this.cliente = cliente;
        itemsRestantes = cliente.obtenerItems();
    }

    public void avanzarAtencion() {
        if (!this.estaLibre()) {
            itemsRestantes = itemsRestantes - 1;
            if (itemsRestantes == 0) {
                personasAtendidas = personasAtendidas + 1;
                itemsVendidos = itemsVendidos + cliente.obtenerItems();
                cliente = null;
            }
        }
    }

    public void mostrar() {
        console.write("Caja ["+numero+"] ");
        console.writeln("[:]".repeat(itemsRestantes));
    }

    public int obtenerPersonasAtendidas() {
        return personasAtendidas;
    }

    public int obtenerItemsVendidos() {
        return itemsVendidos;
    }

    public boolean puedeAtender(Cliente cliente){
        return true;
    }

}
```

## CajaExpress.java

```java
package entregas.aguilarAdriana.Cola;

public class CajaExpress extends Caja {

    Console console;

    public CajaExpress(int numero){
        super(numero);
        console = new Console();
    }
    
    @Override
    public void mostrar() {
        console.write("CajaE["+numero+"] ");
        console.writeln("[:]".repeat(itemsRestantes));
    }

    @Override
    public boolean puedeAtender(Cliente cliente){
        return cliente.obtenerItems()<=10;
    }

}
```

## CCCF.java

```java
package entregas.aguilarAdriana.Cola;

class CCCF {   
    public static void main(String[] args) {

        CentroComercial centro = new CentroComercial();
        centro.simular();
    }
}
```

## CentroComercial.java

```java
package entregas.aguilarAdriana.Cola;

public class CentroComercial {

    private Cola cola;
    private Caja[] cajas;
    private Tiempo tiempo;
    private boolean llegaClienteEsteMinuto;
    final private double PROBABILIDAD_LLEGADA = 0.4;
    private Console console;

    public CentroComercial() {
        console = new Console();
        cola = new Cola();
        cajas = new Caja[5];
        for (int i = 0; i < cajas.length; i++) {
            cajas[i] = new Caja(i + 1);
        }
        cajas[0] = new CajaExpress(1);
        cajas[4] = new CajaExpress(5);
        tiempo = new Tiempo();
    }

    public void simular() {
        do {
            tiempo.avanzar();
            this.procesarLlegadaCliente();
            cola.registrarEstado();
            this.asignarClientesACajas();
            this.procesarAtencionCajas();
            this.mostrarEstado();
            this.pausar();
        } while (!tiempo.haFinalizado());
        this.mostrarResumen();
    }

    private void mostrarResumen() {
        int minutosSinClientes = cola.obtenerMinutosSinClientes();
        int personasEnCola = cola.obtenerCantidadPersonasEnCola();
        int personasAtendidas = this.obtenerPersonasAtendidas();
        int itemsVendidos = this.obtenerItemsVendidos();

        console.writeln("Personas atendidas: " + personasAtendidas);
        console.writeln("Personas en cola al cierre: " + personasEnCola);
        console.writeln("Items vendidos: " + itemsVendidos);
        console.writeln("Minutos sin clientes en cola: " + minutosSinClientes);
    }

    private int obtenerPersonasAtendidas(){
        int totalItems=0;
        for(int numeroCaja=0; numeroCaja<cajas.length; numeroCaja++){
            totalItems= totalItems + cajas[numeroCaja].obtenerPersonasAtendidas();
        }
        return totalItems;
    }

    private int obtenerItemsVendidos(){
        int totalItems=0;
        for(int numeroCaja=0; numeroCaja<cajas.length; numeroCaja++){
            totalItems = totalItems + cajas[numeroCaja].obtenerItemsVendidos();
        }
        return totalItems;
    }

    private void pausar() {
        console.pause(1);
    }

    private void mostrarEstado() {
        console.cleanScreen();
        tiempo.mostrar(llegaClienteEsteMinuto);
        cola.mostrar();
        this.mostrarCajas();
    }

    private void mostrarCajas(){
        for(int numeroCaja=0; numeroCaja<cajas.length; numeroCaja++){
            cajas[numeroCaja].mostrar();
        }
    }

    private void procesarAtencionCajas() {
        for(int numeroCaja=0; numeroCaja<cajas.length; numeroCaja++){
            cajas[numeroCaja].avanzarAtencion();
        }
    }

    private void asignarClientesACajas() {
        for(int numeroCaja=0; numeroCaja<cajas.length; numeroCaja++){
            if (cajas[numeroCaja].estaLibre() 
                && cola.hayClientes()
                && cajas[numeroCaja].puedeAtender(cola.primero())){
                Cliente cliente = cola.quitarCliente();
                cajas[numeroCaja].asignar(cliente);
            }
        }
    }

    private void procesarLlegadaCliente() {
        llegaClienteEsteMinuto = Math.random() <= PROBABILIDAD_LLEGADA;
        if (llegaClienteEsteMinuto) {
            Cliente cliente = new Cliente();
            cola.añadirCliente(cliente);
        }
    }
}
```

## Cliente.java

```java
package entregas.aguilarAdriana.Cola;

public class Cliente {

    private int items;
    private boolean tienePrioridad = true;
    private Console console;

    public Cliente(boolean tienePrioridad) {
        this.tienePrioridad = tienePrioridad;
        items = this.generarItems();
        console = new Console();
    }

    private int generarItems() {
        final int MAXIMO_ITEMS = 15;
        final int MINIMO_ITEMS = 5;
        return (int) (Math.random() * (MAXIMO_ITEMS - MINIMO_ITEMS) + MINIMO_ITEMS);
    }

    public boolean tienePrioridad () {
        return tienePrioridad;
    }
    
    public int obtenerItems() {
        return items;
    }

    public void mostrar() {
        console.write("[" + items + "]_O/");
    }
}
```

## Cola.java

```java
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
```

## Console.java

```java
package entregas.aguilarAdriana.Cola;
    import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class Console {

    private static final String INTEGER_regExp = "-?\\d+";
    private static final String DOUBLE_regExp = "-?(\\d+(\\.\\d+)?([eE][+-]?\\d+)?|\\.\\d+([eE][+-]?\\d+)?)";
    private static final String CHAR_regExp = ".";

    private BufferedReader input;

    public Console() {
        this.input = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readString() {
        return this.readString("");
    }

    public String readString(String title) {
        assert title != null;

        this.write(title);
        String string = "";
        try {
            string = this.input.readLine();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return string;
    }

    public char readChar() {
        return this.readChar("");
    }

    public char readChar(String title) {
        assert title != null;

        Pattern charPattern = Pattern.compile(CHAR_regExp);
        char characterInput = ' ';
        boolean ok;
        do {
            String string = this.readString(title);
            ok = charPattern.matcher(string).find();
            if (ok) {
                characterInput = string.charAt(0);
            } else {
                this.writeError(charPattern.toString());
            }
        } while (!ok);
        return characterInput;
    }

    public int readInt() {
        return this.readInt("");
    }

    public int readInt(String title) {
        assert title != null;

        Pattern intPattern = Pattern.compile(INTEGER_regExp);
        int intInput = 0;
        boolean ok;
        do {
            String string = this.readString(title);
            ok = intPattern.matcher(string.trim()).matches();
            if (ok) {
                intInput = Integer.parseInt(string.trim());
            } else {
                this.writeError(intPattern.toString());
            }
        } while (!ok);
        return intInput;
    }

    public double readDouble() {
        return this.readDouble("");
    }

    public double readDouble(String title) {
        assert title != null;

        Pattern doublePattern = Pattern.compile(DOUBLE_regExp);
        double doubleInput = 0;
        boolean ok;
        do {
            String string = this.readString(title);
            ok = doublePattern.matcher(string.trim()).matches();
            if (ok) {
                doubleInput = Double.parseDouble(string.trim());
            } else {
                this.writeError(doublePattern.toString());
            }
        } while (!ok);
        return doubleInput;
    }

    public void write(String string) {
        assert string != null;

        System.out.print(string);
    }

    public void writeln(String string) {
        this.write(string + "\n");
    }

    public void writeln() {
        this.writeln("");
    }

    public void write(char character) {
        System.out.print(character);
    }

    public void writeln(char character) {
        this.write(character + "\n");
    }

    public void write(int value) {
        System.out.print(value);
    }

    public void writeln(int value) {
        this.write(value + "\n");
    }

    public void write(double value) {
        System.out.print(value);
    }

    public void writeln(double value) {
        this.write(value + "\n");
    }

    public void write(Object object) {
        System.out.print(object);
    }

    public void writeln(Object object) {
        this.write(object + "\n");
    }

    private void writeError(String regExp) {
        System.out.println("Error de formato: se esperaba " + regExp);
    }

    public void pause(int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
        }
    }

    public void cleanScreen() {
        System.out.print("\\033[H\\033[2J");
        System.out.flush();
    }
}
```

## Principal.java

```java
package entregas.aguilarAdriana.Cola;

public class Principal {

    public static void main(String[] args) {
        SimulacionEscenario escenario = new SimulacionEscenario();
        escenario.ejecutar();
    }
}
```

## SimulacionEscenario.java

```java
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
```

## Tiempo.java

```java
package entregas.aguilarAdriana.Cola;

public class Tiempo {

    private final double HORA_APERTURA = 9.0;
    private final double HORA_CIERRE = 21.0;
    private final double MINUTO = 0.0167;
    private double horaActual;
    private Console console;

    public Tiempo() {
        horaActual = HORA_APERTURA;
        console = new Console();
    }

    public void avanzar() {
        horaActual = horaActual + MINUTO;
    }

    public boolean haFinalizado() {
        return HORA_CIERRE < horaActual;
    }

    public void mostrar(boolean llegaClienteEsteMinuto) {
        console.write(this.horaHumana());
        console.write(" ");
        console.writeln((llegaClienteEsteMinuto ? "" : "no") + " llegó un cliente");
    }

    private String horaHumana() {
        int hora = (int) horaActual;
        int minutos =(int) ((horaActual - hora)*60);
        return hora + ":" + minutos;
    }

    public static void main(String[] args) {
        Tiempo tiempo = new Tiempo();
        tiempo.mostrar(true);
        for(int i=0;i<60;i++){
            tiempo.avanzar();
        }
        tiempo.mostrar(true);
    }


}
```
