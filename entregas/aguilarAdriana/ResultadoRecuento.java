package entregas.aguilarAdriana;

public class ResultadoRecuento {
    public ResultadoRecuento(Candidato[] candidatos) {
    }

    public void sumarVotoDe(String nombre);

    public boolean corregirVotoDe(String nombre);

    public int votosDe(String nombre);

    public String[] nombres();

    public int[] votos();

    public boolean hayEmpate();

    public String proclamar();
}
