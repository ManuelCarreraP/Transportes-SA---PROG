public class Camion extends Vehiculo {
    private double capacidadCarga; // en toneladas

    public Camion(String matricula, double largo, double peso, String modelo, double capacidadCarga) {
        super(matricula, largo, peso, modelo);
        this.capacidadCarga = capacidadCarga;
    }

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    @Override
    public String toString() {
        return "Camion{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                ", capacidadCarga=" + capacidadCarga + " toneladas" +
                '}';
    }
}


