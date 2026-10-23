package entregas.aguilarAdriana;

public class Candidaturas {
    public Candidaturas() {
    }

    public boolean proponer(int idAlumno, ListaPresentes presentes);

    public boolean retirar(int idAlumno);

    public void cerrar();

    public boolean estanCerradas();

    public boolean esCandidato(String nombre);

    public int posicionDe(String nombre);

    public Candidato[] vistaDefinitiva();
}
