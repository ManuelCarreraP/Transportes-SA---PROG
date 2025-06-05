public class Furgoneta extends Vehiculo {
    private int nPlazas;

    public Furgoneta(String matricula, double largo, double peso, String modelo, int nPlazas) {
        super(matricula, largo, peso, modelo);
        this.nPlazas = nPlazas;
    }

    public int getNPlazas() {
        return nPlazas;
    }

    public void setNPlazas(int nPlazas) {
        this.nPlazas = nPlazas;
    }

    @Override
    public String toString() {
        return "Furgoneta{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                ", nPlazas=" + nPlazas +
                '}';
    }
}
