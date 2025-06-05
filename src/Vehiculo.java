public abstract class Vehiculo {
    public String matricula;
    public double largo;
    public double peso;
    public String modelo;

    public Vehiculo(String matricula, double largo, double peso, String modelo) {
        this.matricula = matricula;
        this.largo = largo;
        this.peso = peso;
        this.modelo = modelo;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public double getLargo() { return largo; }
    public void setLargo(double largo) { this.largo = largo; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                '}';
    }
}
