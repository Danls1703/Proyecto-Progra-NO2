import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class AppManager {
    private Connection dbConnection;
    private Scanner scanner;
    private List<Atleta> listaAtletas; 
    private GestorFinanciero gestorFinanciero;
    private PersistenciaManager persistenciaManager;
    private AtletaDAO atletaDAO;

    public AppManager(Connection conn, Scanner scanner) {
        this.dbConnection = conn;
        this.scanner = scanner;
        this.listaAtletas = new ArrayList<>();
        this.gestorFinanciero = new GestorFinanciero();
        this.persistenciaManager = new PersistenciaManager();
        this.atletaDAO = new AtletaDAO(conn);

        this.cargarDesdeJSON();
        System.out.println("🛠️ AppManager inicializado con " + this.listaAtletas.size() + " atletas cargados.");
    }

    public void registrarAtletaYEntrenamiento() {
        System.out.println("\n--- 1. Registrar Atleta y Entrenamiento ---");
        
        Atleta miAtleta = new Atleta();
        
        System.out.print("Nombre: "); String nombreAtleta = scanner.nextLine();
        System.out.print("Apellido: "); String apellidoAtleta = scanner.nextLine();
        System.out.print("País: "); String paisAtleta = scanner.nextLine();
        System.out.print("Edad: "); int edadAtleta = Integer.parseInt(scanner.nextLine());
        System.out.print("Deporte (ej: Natacion): "); String nombreDeporte = scanner.nextLine();
        System.out.print("Especialidad del Deporte (ej: Acuatico): "); String especialidadDeporte = scanner.nextLine();
        System.out.print("Tipo de Deporte (ej: Estilo libre): "); String tipoDeporte = scanner.nextLine();

        Deporte deporte = new Deporte(nombreDeporte, especialidadDeporte, tipoDeporte);
        
        miAtleta.registrar(nombreAtleta, apellidoAtleta, paisAtleta, edadAtleta, nombreDeporte);
        miAtleta.setDeporte(deporte);

        SesionEntrenamiento sesion = new SesionEntrenamiento();
        System.out.println("\n--- Ingrese los datos de la sesión de entrenamiento ---");
        System.out.print("Especialidad de la sesión (ej: Natacion): "); sesion.setEspecialidad(scanner.nextLine());
        System.out.print("Tipo de sesión (ej: Resistencia): "); sesion.setTipo(scanner.nextLine());
        System.out.print("¿Se realizó en el extranjero? (s/n): "); sesion.setEsExtranjero(scanner.nextLine().equalsIgnoreCase("s"));
        sesion.setFecha(new Date());

        System.out.println("\n--- Ingrese las marcas para la sesión ---");
        System.out.print("Unidad de medida de las marcas (ej: segundos, metros): "); String unidadMarca = scanner.nextLine();
        double mejorMarcaSesion = Double.MIN_VALUE;

        while (true) {
            System.out.print("Ingrese el valor de la marca (o 'fin' para terminar): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("fin")) { break; }
            try {
                double valorMarca = Double.parseDouble(input);
                Marca marca = new Marca(valorMarca, unidadMarca, new Date());
                sesion.agregarMarcas(marca);
                
                if (valorMarca > mejorMarcaSesion) {
                    mejorMarcaSesion = valorMarca;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        }
        
        if (miAtleta.actualizarMejorMarca(mejorMarcaSesion)) {
            sesion.setSuperoMejorMarcaPersonal(true);
            System.out.println("🎉 ¡Felicidades! Se ha superado la mejor marca personal del atleta.");
        } else {
            sesion.setSuperoMejorMarcaPersonal(false);
        }

        miAtleta.agregarSesion(sesion);
        this.listaAtletas.add(miAtleta);

        this.guardarEnBD(miAtleta, sesion);
        this.guardarEnJSON();

        System.out.println("\n✅ Atleta y entrenamiento registrados y guardados.");
    }

    public void consultarEstadisticas() {
        System.out.println("\n--- 2. Consultar Estadísticas ---");
        if (listaAtletas.isEmpty()) {
            System.out.println("No hay atletas registrados.");
            return;
        }

        System.out.println("Atletas disponibles:");
        for (int i = 0; i < listaAtletas.size(); i++) {
            System.out.println((i + 1) + ". " + listaAtletas.get(i).getNombre() + " " + listaAtletas.get(i).getApellido());
        }
        System.out.print("Seleccione el número del atleta para ver estadísticas: ");
        
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < listaAtletas.size()) {
                Atleta atleta = listaAtletas.get(index);
                atleta.estadisticas_debut();
                System.out.println("Mejor Marca Personal histórica: " + atleta.getMejorMarcaPersonal());
                System.out.println("Número de sesiones registradas: " + atleta.getSesiones().size());
                
                if (!atleta.getSesiones().isEmpty()) {
                    SesionEntrenamiento ultimaSesion = atleta.getSesiones().get(atleta.getSesiones().size() - 1);
                    System.out.println("\n--- Última Sesión (" + ultimaSesion.getTipo() + ") ---");
                    Marca mejorMarca = ultimaSesion.mejorMarca();
                    if (mejorMarca != null) {
                        System.out.println("Mejor marca de la sesión: " + mejorMarca.getValor() + " " + mejorMarca.getUnidad());
                        System.out.println("Promedio de marcas de la sesión: " + ultimaSesion.promedio() + " " + mejorMarca.getUnidad());
                    }
                }
            } else {
                System.out.println("Número de atleta no válido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida.");
        }
    }

    public void procesarPagoPlanilla(Scanner scanner) {
        System.out.println("\n--- 3. Procesar Pago de Planilla (Gestión Financiera) ---");
        if (listaAtletas.isEmpty()) {
            System.out.println("No hay atletas para procesar planilla.");
            return;
        }
        
        System.out.println("Atletas disponibles:");
        for (int i = 0; i < listaAtletas.size(); i++) {
            System.out.println((i + 1) + ". " + listaAtletas.get(i).getNombre() + " " + listaAtletas.get(i).getApellido());
        }
        System.out.print("Seleccione el número del atleta para calcular el pago: ");
        
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < listaAtletas.size()) {
                Atleta atleta = listaAtletas.get(index);
                
                System.out.println("\nCálculo de pago mensual para " + atleta.getNombre() + " " + atleta.getApellido() + ":");
                double pago = gestorFinanciero.calcularPagoMensual(atleta);
                
                System.out.println("\n==============================================");
                System.out.printf("💸 TOTAL A PAGAR: $%.2f\n", pago);
                System.out.println("==============================================");
                
            } else {
                System.out.println("Número de atleta no válido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida.");
        }
    }

    public void generarReporteCSV() {
        System.out.println("\n--- 5. Generar Reporte CSV ---");
        if (listaAtletas.isEmpty()) {
            System.out.println("No hay datos para generar el reporte.");
            return;
        }

        if (persistenciaManager.exportarReporteCSV(listaAtletas)) {
            System.out.println("✅ Reporte de entrenamientos exportado con éxito a 'reporte_entrenamientos.csv'.");
        } else {
            System.out.println("❌ Error al generar el reporte CSV.");
        }
    }

    private void guardarEnBD(Atleta atleta, SesionEntrenamiento sesion) {
        try {
            int atletaId = atletaDAO.guardarAtleta(atleta);
            if (atletaId != -1) {
                int sesionId = atletaDAO.guardarSesion(sesion, atletaId);
                if (sesionId != -1) {
                    atletaDAO.guardarMarcas(sesion.getMarcas(), sesionId);
                    System.out.println("✅ Datos guardados en BD con Atleta ID: " + atletaId);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al guardar en BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void guardarEnBD() {
        System.out.println("\n--- Guardar todos los Atletas en Base de Datos ---");
        System.out.println("⚠️ Esta función requeriría lógica compleja (UPSERT) para actualizar datos existentes.");
        System.out.println("Se recomienda usarla solo para el guardado inicial de la lógica anterior.");
    }
    
    public void cargarDesdeBD() {
        System.out.println("\n--- Cargar Atletas desde Base de Datos ---");
        try {
            this.listaAtletas = atletaDAO.cargarTodosLosAtletas(); 
            System.out.println("✅ Carga desde BD completada. " + listaAtletas.size() + " atletas cargados.");
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar desde BD: " + e.getMessage());
        }
    }

    public void guardarEnJSON() {
        System.out.println("\n--- Guardar en Archivo JSON (Respaldo) ---");
        if (persistenciaManager.guardarAtletasEnJSON(listaAtletas)) {
            System.out.println("✅ Respaldo guardado en 'atletas_backup.json'.");
        } else {
            System.out.println("❌ Error al guardar el respaldo JSON.");
        }
    }

    public void cargarDesdeJSON() {
        System.out.println("\n--- Cargar desde Archivo JSON (Restaurar) ---");
        List<Atleta> cargados = persistenciaManager.cargarAtletasDesdeJSON();
        if (!cargados.isEmpty()) {
            this.listaAtletas = cargados;
            System.out.println("✅ Datos restaurados desde JSON. " + listaAtletas.size() + " atletas cargados.");
        } else {
            System.out.println("❌ No se pudo cargar o el archivo JSON estaba vacío/no existe.");
        }
    }
}