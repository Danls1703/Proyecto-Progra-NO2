import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtletaDAO {
    private final Connection conn;

    public AtletaDAO(Connection conn) {
        this.conn = conn;
    }

    public int guardarAtleta(Atleta atleta) throws SQLException {
        String sql = "INSERT INTO Atletas (nombre, apellido, pais, edad, nombre_deporte, especialidad_deporte, tipo_deporte) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, atleta.getNombre());
            pstmt.setString(2, atleta.getApellido());
            pstmt.setString(3, atleta.getPais());
            pstmt.setInt(4, atleta.getEdad());
            pstmt.setString(5, atleta.getDeporte().getNombre());
            pstmt.setString(6, atleta.getDeporte().getEspecialidad());
            pstmt.setString(7, atleta.getDeporte().getTipo());
            
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    public int guardarSesion(SesionEntrenamiento sesion, int atletaId) throws SQLException {
        String sql = "INSERT INTO Sesiones (atleta_id, especialidad, tipo, fecha, es_extranjero) VALUES (?, ?, ?, NOW(), ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, atletaId);
            pstmt.setString(2, sesion.getEspecialidad());
            pstmt.setString(3, sesion.getTipo());
            pstmt.setBoolean(4, sesion.isEsExtranjero()); 
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    public void guardarMarcas(List<Marca> marcas, int sesionId) throws SQLException {
        String sql = "INSERT INTO Marcas (sesion_id, valor, unidad, fecha_registro) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Marca marca : marcas) {
                pstmt.setInt(1, sesionId);
                pstmt.setDouble(2, marca.getValor());
                pstmt.setString(3, marca.getUnidad());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    public List<Atleta> cargarTodosLosAtletas() throws SQLException {
        System.out.println("⚠️ Lógica de 'Cargar Todos los Atletas' desde BD pendiente.");
        return new ArrayList<>();
    }
}