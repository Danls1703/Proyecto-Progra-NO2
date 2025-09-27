import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class PersistenciaManager {
    private static final String JSON_PATH = "atletas_backup.json";
    private static final String CSV_PATH = "reporte_entrenamientos.csv";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PersistenciaManager() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); 
    }

    public boolean guardarAtletasEnJSON(List<Atleta> atletas) {
        try {
            objectMapper.writeValue(new File(JSON_PATH), atletas);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Atleta> cargarAtletasDesdeJSON() {
        try {
            return objectMapper.readValue(new File(JSON_PATH), new TypeReference<List<Atleta>>() {});
        } catch (FileNotFoundException e) {
            System.out.println("Archivo JSON no encontrado. Se inicia con lista vacía.");
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean exportarReporteCSV(List<Atleta> atletas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {
            pw.println("Atleta;Pais;Deporte;SesionTipo;Fecha;MejorMarca;Unidad");

            for (Atleta atleta : atletas) {
                for (SesionEntrenamiento sesion : atleta.getSesiones()) {
                    Marca mejorMarca = sesion.mejorMarca();
                    String linea = String.format("%s %s;%s;%s;%s;%s;%s;%s",
                            atleta.getNombre(), atleta.getApellido(), atleta.getPais(),
                            atleta.getDeporte().getNombre(), sesion.getTipo(),
                            sesion.getFecha(),
                            mejorMarca != null ? mejorMarca.getValor() : "N/A",
                            mejorMarca != null ? mejorMarca.getUnidad() : "N/A");
                    pw.println(linea);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}