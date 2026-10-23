package entregas.aguilarAdriana;

public class Profesor {
    public Profesor(String nombre) {
    }

    public String obtenerNombre();

    public Secretario elegirSecretario(int idAlumno, ListaPresentes presentes);

    public void abrirVotacion();

    public void cerrarVotacion();

    public boolean corregirAnotacion(
            String opcion,
            ResultadoRecuento recuento);

    public void proclamar(ResultadoRecuento recuento);
}
