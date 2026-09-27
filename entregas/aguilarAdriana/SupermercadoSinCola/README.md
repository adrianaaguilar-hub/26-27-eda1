# SupermercadoSinCola

Este README reúne el código actual de las clases Java de esta carpeta. La implementación principal usa referencias enlazadas en Cliente. (falta implementar la funcion de array lleno = array nuevo mas grande)

Trabajarlo como estructura de datos que apuntan a otras, implementar a tu codigo el que se cuele alguien

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
```

## Cliente.java

```java
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
```

## Console.java

```java
package entregas.aguilarAdriana.Cola;

public class Console {
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
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
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
        console.writeln((llegaClienteEsteMinuto ? "" : "no") + " llegÃ³ un cliente");
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

