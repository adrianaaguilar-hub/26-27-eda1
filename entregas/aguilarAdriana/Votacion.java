package entregas.aguilarAdriana;

public class Votacion {
    public Votacion(Profesor presidente) {
    }

    public void registrarLlegada(int idAlumno);

    public void registrarSalida(int idAlumno);

    public boolean proponerCandidato(int idAlumno);

    public boolean retirarCandidato(int idAlumno);

    public void cerrarCandidaturas();

    public void elegirSecretario(int idAlumno);

    public void abrirVotacion();

    public boolean votar(int idAlumno, int idPapeleta);

    public void contarVotos();

    public void corregirAnotacion(String opcion);

    public void cerrarVotacion();

    public void proclamarResultado();
}
