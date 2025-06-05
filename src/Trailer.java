public class Trailer extends Vehiculo {
    private int numRemolques;

    public Trailer(String matricula, double largo, double peso, String modelo, int numRemolques) {
        super(matricula, largo, peso, modelo);
        this.numRemolques = numRemolques;
    }

    public int getNumRemolques() {
        return numRemolques;
    }

    public void setNumRemolques(int numRemolques) {
        this.numRemolques = numRemolques;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                ", numRemolques=" + numRemolques +
                '}';
    }
}
