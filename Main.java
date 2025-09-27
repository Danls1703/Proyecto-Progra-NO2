import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atletas_db";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        AppManager appManager = null; 

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("✅ Conexión a la base de datos exitosa.");
            
            appManager = new AppManager(conn, scanner);

            int opcion = -1;
            while (opcion != 0) {
                mostrarMenuPrincipal();
                try {
                    opcion = scanner.nextInt();
                    scanner.nextLine();
                    procesarOpcion(opcion, appManager, scanner);
                } catch (java.util.InputMismatchException e) {
                    System.out.println("❌ Entrada no válida. Por favor, ingrese un número.");
                    scanner.nextLine();
                    opcion = -1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error de conexión o de base de datos. Asegúrese que MySQL esté corriendo.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error inesperado en la aplicación.");
        } finally {
            try {
                if (conn != null) conn.close();
                System.out.println("\n👋 Saliendo de la aplicación. ¡Hasta pronto!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n==============================================");
        System.out.println("       📊 Sistema de Gestión de Atletas 🏆     ");
        System.out.println("==============================================");
        System.out.println("1. 🧑‍💻  Registrar Atleta y Entrenamiento"); 
        System.out.println("2. 📈  Consultar Estadísticas");
        System.out.println("3. 💰  Procesar Pago de Planilla (Gestión Financiera)"); 
        System.out.println("4. 💾  Gestión de Persistencia (Guardar/Cargar)"); 
        System.out.println("5. 📄  Generar Reporte CSV"); 
        System.out.println("0. 🚪  Salir");
        System.out.println("==============================================");
        System.out.print("Seleccione una opción: ");
    }

    private static void procesarOpcion(int opcion, AppManager appManager, Scanner scanner) {
        switch (opcion) {
            case 1:
                appManager.registrarAtletaYEntrenamiento();
                break;
            case 2:
                appManager.consultarEstadisticas();
                break;
            case 3:
                appManager.procesarPagoPlanilla(scanner); 
                break;
            case 4:
                mostrarMenuPersistencia(appManager, scanner); 
                break;
            case 5:
                appManager.generarReporteCSV(); 
                break;
            case 0:
                break;
            default:
                System.out.println("❓ Opción no reconocida. Intente de nuevo.");
        }
    }

    private static void mostrarMenuPersistencia(AppManager appManager, Scanner scanner) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- Gestión de Persistencia (BD y JSON) ---");
            System.out.println("1. Guardar en Base de Datos (BD)");
            System.out.println("2. Cargar desde Base de Datos (BD)");
            System.out.println("3. Guardar Atletas y Entrenamientos en JSON"); 
            System.out.println("4. Cargar Atletas y Entrenamientos desde JSON");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1: appManager.guardarEnBD(); break;
                    case 2: appManager.cargarDesdeBD(); break;
                    case 3: appManager.guardarEnJSON(); break;
                    case 4: appManager.cargarDesdeJSON(); break;
                    case 0: break;
                    default: System.out.println("❓ Opción no reconocida.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("❌ Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine();
                opcion = -1;
            }
        }
    }
}