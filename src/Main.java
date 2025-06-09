import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String CSV_FILE_PATH = "vehiculos.csv";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        do {
            System.out.println("\n--- Gestión de Vehículos ---");
            System.out.println("1. Operaciones con Archivo CSV");
            System.out.println("2. Operaciones con Base de Datos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            option = getIntInput();

            switch (option) {
                case 1:
                    handleCsvOperations();
                    break;
                case 2:
                    handleDatabaseOperations();
                    break;
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (option != 0);

        scanner.close();
    }

    private static void handleCsvOperations() {
        int option;
        do {
            System.out.println("\n--- Operaciones con Archivo CSV ---");
            System.out.println("1. Añadir Vehículo");
            System.out.println("2. Listar Vehículos");
            System.out.println("3. Buscar Vehículo");
            System.out.println("4. Actualizar Vehículo");
            System.out.println("5. Eliminar Vehículo");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            option = getIntInput();

            try {
                switch (option) {
                    case 1:
                        addVehiculoToCsv();
                        break;
                    case 2:
                        listVehiculosFromCsv();
                        break;
                    case 3:
                        searchVehiculoInCsv();
                        break;
                    case 4:
                        updateVehiculoInCsv();
                        break;
                    case 5:
                        deleteVehiculoFromCsv();
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal.");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (IOException e) {
                System.err.println("Error de E/S al operar con el archivo CSV: " + e.getMessage());
            }
        } while (option != 0);
    }

    private static void addVehiculoToCsv() throws IOException {
        System.out.println("\n--- Añadir Vehículo al CSV ---");
        System.out.print("Tipo de vehículo (Camion, Furgoneta, Trailer): ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Largo: ");
        double largo = getDoubleInput();
        System.out.print("Peso: ");
        double peso = getDoubleInput();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine(); // Consume the newline after double input

        Vehiculo vehiculo = null;
        switch (tipo.toUpperCase()) {
            case "CAMION":
                System.out.print("Capacidad de Carga (toneladas): ");
                double capacidadCarga = getDoubleInput();
                vehiculo = new Camion(matricula, largo, peso, modelo, capacidadCarga);
                break;
            case "FURGONETA":
                System.out.print("Número de Plazas: ");
                int nPlazas = getIntInput();
                vehiculo = new Furgoneta(matricula, largo, peso, modelo, nPlazas);
                break;
            case "TRAILER":
                System.out.print("Número de Remolques: ");
                int numRemolques = getIntInput();
                vehiculo = new Trailer(matricula, largo, peso, modelo, numRemolques);
                break;
            default:
                System.out.println("Tipo de vehículo no reconocido.");
                return;
        }

        if (vehiculo != null) {
            VehiculoFileManager.añadirVehiculo(vehiculo, CSV_FILE_PATH);
            System.out.println("Vehículo añadido exitosamente al CSV.");
        }
    }

    private static void listVehiculosFromCsv() throws IOException {
        System.out.println("\n--- Listado de Vehículos del CSV ---");
        List<Vehiculo> vehiculos = VehiculoFileManager.listarVehiculos(CSV_FILE_PATH);
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos en el archivo CSV.");
        } else {
            vehiculos.forEach(System.out::println);
        }
    }

    private static void searchVehiculoInCsv() throws IOException {
        System.out.println("\n--- Buscar Vehículo en CSV ---");
        System.out.print("Matrícula del vehículo a buscar: ");
        String matricula = scanner.nextLine();
        Vehiculo vehiculo = VehiculoFileManager.buscarVehiculo(matricula, CSV_FILE_PATH);
        if (vehiculo != null) {
            System.out.println("Vehículo encontrado: " + vehiculo);
        } else {
            System.out.println("Vehículo con matrícula '" + matricula + "' no encontrado.");
        }
    }

    private static void updateVehiculoInCsv() throws IOException {
        System.out.println("\n--- Actualizar Vehículo en CSV ---");
        System.out.print("Matrícula del vehículo a actualizar: ");
        String matricula = scanner.nextLine();

        Vehiculo existingVehiculo = VehiculoFileManager.buscarVehiculo(matricula, CSV_FILE_PATH);
        if (existingVehiculo == null) {
            System.out.println("Vehículo con matrícula '" + matricula + "' no encontrado.");
            return;
        }

        System.out.println("Vehículo actual: " + existingVehiculo);

        // For simplicity, let's prompt for all fields to create a new vehicle object
        // In a real application, you might offer to update specific fields
        System.out.print("Nuevo Largo: ");
        double nuevoLargo = getDoubleInput();
        System.out.print("Nuevo Peso: ");
        double nuevoPeso = getDoubleInput();
        System.out.print("Nuevo Modelo: ");
        String nuevoModelo = scanner.nextLine(); // Consume newline

        Vehiculo updatedVehiculo = null;
        if (existingVehiculo instanceof Camion camion) {
            System.out.print("Nueva Capacidad de Carga (toneladas): ");
            double nuevaCapacidadCarga = getDoubleInput();
            updatedVehiculo = new Camion(matricula, nuevoLargo, nuevoPeso, nuevoModelo, nuevaCapacidadCarga);
        } else if (existingVehiculo instanceof Furgoneta furgoneta) {
            System.out.print("Nuevo Número de Plazas: ");
            int nuevoNPlazas = getIntInput();
            updatedVehiculo = new Furgoneta(matricula, nuevoLargo, nuevoPeso, nuevoModelo, nuevoNPlazas);
        } else if (existingVehiculo instanceof Trailer trailer) {
            System.out.print("Nuevo Número de Remolques: ");
            int nuevoNumRemolques = getIntInput();
            updatedVehiculo = new Trailer(matricula, nuevoLargo, nuevoPeso, nuevoModelo, nuevoNumRemolques);
        }

        if (updatedVehiculo != null) {
            boolean updated = VehiculoFileManager.actualizarVehiculo(matricula, updatedVehiculo, CSV_FILE_PATH);
            if (updated) {
                System.out.println("Vehículo actualizado exitosamente en el CSV.");
            } else {
                System.out.println("No se pudo actualizar el vehículo (esto no debería ocurrir si se encontró inicialmente).");
            }
        }
    }

    private static void deleteVehiculoFromCsv() throws IOException {
        System.out.println("\n--- Eliminar Vehículo del CSV ---");
        System.out.print("Matrícula del vehículo a eliminar: ");
        String matricula = scanner.nextLine();
        boolean deleted = VehiculoFileManager.eliminarVehiculo(matricula, CSV_FILE_PATH);
        if (deleted) {
            System.out.println("Vehículo con matrícula '" + matricula + "' eliminado exitosamente del CSV.");
        } else {
            System.out.println("Vehículo con matrícula '" + matricula + "' no encontrado en el CSV.");
        }
    }

    private static void handleDatabaseOperations() {
        int option;
        do {
            System.out.println("\n--- Operaciones con Base de Datos ---");
            System.out.println("1. Insertar Vehículo (DB)");
            System.out.println("2. Obtener Vehículo por Matrícula (DB)");
            System.out.println("3. Actualizar Vehículo (DB)");
            System.out.println("4. Eliminar Vehículo (DB)");
            System.out.println("5. Listar Todos los Vehículos de una Categoría (DB)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            option = getIntInput();

            try (Connection connection = ConexionPostgreSQL.getConnection()) {
                switch (option) {
                    case 1:
                        insertVehiculoIntoDb(connection);
                        break;
                    case 2:
                        getVehiculoFromDb(connection);
                        break;
                    case 3:
                        updateVehiculoInDb(connection);
                        break;
                    case 4:
                        deleteVehiculoFromDb(connection);
                        break;
                    case 5:
                        listVehiculosByCategoryFromDb(connection);
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal.");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (SQLException e) {
                System.err.println("Error de conexión a la base de datos: " + e.getMessage());
            }
        } while (option != 0);
    }

    private static void insertVehiculoIntoDb(Connection connection) {
        System.out.println("\n--- Insertar Vehículo en la Base de Datos ---");
        System.out.print("Tipo de vehículo (Camion, Furgoneta, Trailer): ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Largo: ");
        double largo = getDoubleInput();
        System.out.print("Peso: ");
        double peso = getDoubleInput();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine(); // Consume the newline after double input

        Usable<?> usableVehiculo = null;
        switch (tipo.toUpperCase()) {
            case "CAMION":
                System.out.print("Capacidad de Carga (toneladas): ");
                double capacidadCarga = getDoubleInput();
                usableVehiculo = new Camion(matricula, largo, peso, modelo, capacidadCarga);
                break;
            case "FURGONETA":
                System.out.print("Número de Plazas: ");
                int nPlazas = getIntInput();
                usableVehiculo = new Furgoneta(matricula, largo, peso, modelo, nPlazas);
                break;
            case "TRAILER":
                System.out.print("Número de Remolques: ");
                int numRemolques = getIntInput();
                usableVehiculo = new Trailer(matricula, largo, peso, modelo, numRemolques);
                break;
            default:
                System.out.println("Tipo de vehículo no reconocido.");
                return;
        }

        if (usableVehiculo != null) {
            int result = usableVehiculo.insertarVehiculo(connection);
            if (result > 0) {
                System.out.println("Vehículo insertado exitosamente en la base de datos.");
            } else {
                System.out.println("Fallo al insertar el vehículo en la base de datos.");
            }
        }
    }

    private static void getVehiculoFromDb(Connection connection) {
        System.out.println("\n--- Obtener Vehículo de la Base de Datos ---");
        System.out.print("Tipo de vehículo (Camion, Furgoneta, Trailer): ");
        String tipo = scanner.nextLine().trim();
        System.out.print("Matrícula del vehículo a buscar: ");
        String matricula = scanner.nextLine();

        Vehiculo vehiculo = null;
        switch (tipo.toUpperCase()) {
            case "CAMION":
                Camion camion = new Camion();
                vehiculo = camion.getVehiculo(connection, matricula);
                break;
            case "FURGONETA":
                Furgoneta furgoneta = new Furgoneta();
                vehiculo = furgoneta.getVehiculo(connection, matricula);
                break;
            case "TRAILER":
                Trailer trailer = new Trailer();
                vehiculo = trailer.getVehiculo(connection, matricula);
                break;
            default:
                System.out.println("Tipo de vehículo no reconocido.");
                return;
        }

        if (vehiculo != null) {
            System.out.println("Vehículo encontrado: " + vehiculo);
        } else {
            System.out.println("Vehículo con matrícula '" + matricula + "' no encontrado en la base de datos.");
        }
    }

    private static void updateVehiculoInDb(Connection connection) {
        System.out.println("\n--- Actualizar Vehículo en la Base de Datos ---");
        System.out.print("Tipo de vehículo a actualizar (Camion, Furgoneta, Trailer): ");
        String tipo = scanner.nextLine().trim();
        System.out.print("Matrícula del vehículo a actualizar: ");
        String matricula = scanner.nextLine();

        Usable<?> usableVehiculo = null;
        switch (tipo.toUpperCase()) {
            case "CAMION":
                usableVehiculo = new Camion();
                break;
            case "FURGONETA":
                usableVehiculo = new Furgoneta();
                break;
            case "TRAILER":
                usableVehiculo = new Trailer();
                break;
            default:
                System.out.println("Tipo de vehículo no reconocido.");
                return;
        }

        if (usableVehiculo != null) {
            // The actual update logic is handled inside the actualizarVehiculo method of each class
            // which prompts the user for the field to update.
            int result = usableVehiculo.actualizarVehiculo(connection, matricula);
            if (result > 0) {
                System.out.println("Vehículo actualizado exitosamente en la base de datos.");
            } else if (result == -2) {
                System.out.println("Operación de actualización cancelada o campo no válido.");
            } else {
                System.out.println("No se pudo actualizar el vehículo en la base de datos. Asegúrese que la matrícula existe.");
            }
        }
    }

    private static void deleteVehiculoFromDb(Connection connection) {
        System.out.println("\n--- Eliminar Vehículo de la Base de Datos ---");
        System.out.print("Tipo de vehículo a eliminar (Camion, Furgoneta, Trailer): ");
        String tipo = scanner.nextLine().trim();
        System.out.print("Matrícula del vehículo a eliminar: ");
        String matricula = scanner.nextLine();

        Usable<?> usableVehiculo = null;
        switch (tipo.toUpperCase()) {
            case "CAMION":
                usableVehiculo = new Camion();
                break;
            case "FURGONETA":
                usableVehiculo = new Furgoneta();
                break;
            case "TRAILER":
                usableVehiculo = new Trailer();
                break;
            default:
                System.out.println("Tipo de vehículo no reconocido.");
                return;
        }

        if (usableVehiculo != null) {
            usableVehiculo.eliminarVehiculo(connection, matricula);
            System.out.println("Vehículo eliminado (si existía) de la base de datos.");
        }
    }

    private static void listVehiculosByCategoryFromDb(Connection connection) {
        System.out.println("\n--- Listar Todos los Vehículos de una Categoría (DB) ---");
        System.out.print("Tipo de vehículo a listar (Camion, Furgoneta, Trailer): ");
        String tipo = scanner.nextLine().trim();

        ArrayList<? extends Vehiculo> vehiculos = new ArrayList<>();
        switch (tipo.toUpperCase()) {
            case "CAMION":
                Camion camion = new Camion();
                vehiculos = camion.obtenerTodosVehiculosCategoria(connection);
                break;
            case "FURGONETA":
                Furgoneta furgoneta = new Furgoneta();
                vehiculos = furgoneta.obtenerTodosVehiculosCategoria(connection);
                break;
            case "TRAILER":
                Trailer trailer = new Trailer();
                vehiculos = trailer.obtenerTodosVehiculosCategoria(connection);
                break;
            default:
                System.out.println("Tipo de vehículo no reconocido.");
                return;
        }

        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos de tipo '" + tipo + "' en la base de datos.");
        } else {
            System.out.println("Vehículos de tipo '" + tipo + "':");
            vehiculos.forEach(System.out::println);
        }
    }

    // Helper methods for input
    private static int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Por favor, introduzca un número entero: ");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Por favor, introduzca un número decimal: ");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }
}