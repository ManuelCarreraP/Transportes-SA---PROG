import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase para manejar el CRUD de vehículos en archivos CSV.
 * Operaciones: Añadir, Buscar, Eliminar, Listar y Actualizar.
 * @author TuNombre
 */
public class VehiculoFileManager {
    // Se ha añadido el peso a la cabecera CSV para que quede claro que se guarda.
    // La cabecera define el orden y nombre de los campos.
    private static final String CABECERA_CSV = "TIPO,MATRICULA,LARGO,PESO,MODELO,ATRIBUTO_EXTRA\n";

    /**
     * Añade un vehículo al archivo CSV.
     * Si el archivo no existe o está vacío, escribe primero la cabecera.
     * @param vehiculo Vehículo a añadir.
     * @param rutaArchivo Ruta del archivo CSV.
     * @throws IOException Si hay error al escribir.
     */
    public static void añadirVehiculo(Vehiculo vehiculo, String rutaArchivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            File file = new File(rutaArchivo);
            if (!file.exists() || file.length() == 0) { // Verifica si el archivo no existe o está vacío
                writer.write(CABECERA_CSV);
            }
            writer.write(vehiculoToCSV(vehiculo) + "\n");
        }
    }

    /**
     * Lista todos los vehículos del archivo CSV.
     * @param rutaArchivo Ruta del archivo CSV.
     * @return Lista de vehículos.
     * @throws IOException Si hay error al leer.
     */
    public static List<Vehiculo> listarVehiculos(String rutaArchivo) throws IOException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        File file = new File(rutaArchivo);
        if (!file.exists()) {
            return vehiculos; // Retorna lista vacía si el archivo no existe
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            reader.readLine(); // Saltar la cabecera
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Ignorar líneas vacías
                    try {
                        Vehiculo v = parsearVehiculoDesdeCSV(line);
                        vehiculos.add(v);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Advertencia: No se pudo parsear la línea CSV: '" + line + "'. " + e.getMessage());
                    }
                }
            }
        }
        return vehiculos;
    }

    /**
     * Busca un vehículo por matrícula en el archivo CSV.
     * @param matricula Matrícula del vehículo a buscar.
     * @param rutaArchivo Ruta del archivo CSV.
     * @return El vehículo encontrado o null si no existe.
     * @throws IOException Si hay error al leer.
     */
    public static Vehiculo buscarVehiculo(String matricula, String rutaArchivo) throws IOException {
        return listarVehiculos(rutaArchivo).stream()
                .filter(v -> v.getMatricula().equalsIgnoreCase(matricula))
                .findFirst()
                .orElse(null);
    }

    /**
     * Actualiza un vehículo en el archivo CSV.
     * @param matricula Matrícula del vehículo a actualizar.
     * @param nuevoVehiculo Objeto Vehiculo con los nuevos datos.
     * @param rutaArchivo Ruta del archivo CSV.
     * @return true si se actualizó, false si no se encontró.
     * @throws IOException Si hay error al leer o escribir.
     */
    public static boolean actualizarVehiculo(String matricula, Vehiculo nuevoVehiculo, String rutaArchivo) throws IOException {
        List<Vehiculo> vehiculos = listarVehiculos(rutaArchivo);
        boolean actualizado = false;
        for (int i = 0; i < vehiculos.size(); i++) {
            if (vehiculos.get(i).getMatricula().equalsIgnoreCase(matricula)) {
                vehiculos.set(i, nuevoVehiculo);
                actualizado = true;
                break;
            }
        }
        if (actualizado) {
            sobrescribirArchivo(vehiculos, rutaArchivo);
        }
        return actualizado;
    }

    /**
     * Elimina un vehículo del archivo CSV por matrícula.
     * @param matricula Matrícula del vehículo a eliminar.
     * @param rutaArchivo Ruta del archivo CSV.
     * @return true si se eliminó, false si no se encontró.
     * @throws IOException Si hay error al leer o escribir.
     */
    public static boolean eliminarVehiculo(String matricula, String rutaArchivo) throws IOException {
        List<Vehiculo> vehiculos = listarVehiculos(rutaArchivo);
        boolean eliminado = vehiculos.removeIf(v -> v.getMatricula().equalsIgnoreCase(matricula));
        if (eliminado) {
            sobrescribirArchivo(vehiculos, rutaArchivo);
        }
        return eliminado;
    }

    /**
     * Convierte un objeto Vehiculo a una cadena CSV.
     * @param vehiculo El vehículo a convertir.
     * @return La cadena CSV.
     */
    private static String vehiculoToCSV(Vehiculo vehiculo) {
        String tipo = "";
        String atributoExtra = "";

        if (vehiculo instanceof Camion camion) {
            tipo = "CAMION";
            atributoExtra = String.valueOf(camion.getCapacidadCarga());
        } else if (vehiculo instanceof Furgoneta furgoneta) {
            tipo = "FURGONETA";
            atributoExtra = String.valueOf(furgoneta.getNPlazas());
        } else if (vehiculo instanceof Trailer trailer) {
            tipo = "TRAILER";
            atributoExtra = String.valueOf(trailer.getNumRemolques());
        }

        return String.join(",",
                tipo,
                vehiculo.getMatricula(),
                String.valueOf(vehiculo.getLargo()),
                String.valueOf(vehiculo.getPeso()),
                vehiculo.getModelo(),
                atributoExtra
        );
    }

    /**
     * Parsea una línea CSV para crear un objeto Vehiculo.
     * @param linea La línea CSV.
     * @return El objeto Vehiculo.
     * @throws IllegalArgumentException Si la línea no tiene el formato esperado o el tipo es desconocido.
     */
    private static Vehiculo parsearVehiculoDesdeCSV(String linea) {
        String[] datos = linea.split(",");
        if (datos.length != 6) { // TIPO,MATRICULA,LARGO,PESO,MODELO,ATRIBUTO_EXTRA
            throw new IllegalArgumentException("Formato de línea CSV incorrecto. Se esperaban 6 campos, se encontraron " + datos.length + ": " + linea);
        }

        String tipoVehiculo = datos[0].trim().toUpperCase();
        String matricula = datos[1].trim();
        try {
            double largo = Double.parseDouble(datos[2].trim());
            double peso = Double.parseDouble(datos[3].trim());
            String modelo = datos[4].trim();

            return switch (tipoVehiculo) {
                case "CAMION" -> new Camion(matricula, largo, peso, modelo, Double.parseDouble(datos[5].trim())); // CORREGIDO: Double.parseDouble
                case "FURGONETA" -> new Furgoneta(matricula, largo, peso, modelo, Integer.parseInt(datos[5].trim()));
                case "TRAILER" -> new Trailer(matricula, largo, peso, modelo, Integer.parseInt(datos[5].trim()));
                default -> throw new IllegalArgumentException("Tipo de vehículo desconocido en CSV: " + tipoVehiculo);
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error de formato numérico en la línea CSV: '" + linea + "'. Detalle: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error general al parsear la línea CSV: '" + linea + "'. Detalle: " + e.getMessage(), e);
        }
    }

    /**
     * Sobrescribe el archivo CSV con la lista actual de vehículos.
     * Se usa después de eliminar o actualizar vehículos.
     * @param vehiculos Lista de vehículos a escribir.
     * @param rutaArchivo Ruta del archivo CSV.
     * @throws IOException Si hay error al escribir.
     */
    private static void sobrescribirArchivo(List<Vehiculo> vehiculos, String rutaArchivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(CABECERA_CSV);
            for (Vehiculo v : vehiculos) {
                writer.write(vehiculoToCSV(v) + "\n");
            }
        }
    }
}