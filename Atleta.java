import java.util.ArrayList;
import java.util.List;

public class Atleta {
    private String nombre;
    private String apellido;
    private String pais;
    private int edad;
    private Deporte deporte;
    private List<SesionEntrenamiento> sesiones;
    private double mejorMarcaPersonal = 0.0;

    public Atleta() {
        this.sesiones = new ArrayList<>();
    }

    public void registrar(String nombre, String apellido, String pais, int edad, String nombreDeporte) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
        this.edad = edad;
        this.deporte = new Deporte(nombreDeporte, "", "");
        System.out.println("Atleta registrado: " + this.nombre + " " + this.apellido);
    }
    
    public void estadisticas_debut(){
        System.out.println("--- Estadísticas de Debut ---");
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Apellido: " + this.apellido);
        System.out.println("Edad: " + this.edad);
        System.out.println("País: " + this.pais);
        if (this.deporte != null) {
            System.out.println("Deporte: " + this.deporte.getNombre());
        }
        System.out.println("-----------------------------");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }
    
    public List<SesionEntrenamiento> getSesiones() {
        return sesiones;
    }

    public void agregarSesion(SesionEntrenamiento sesion) {
        this.sesiones.add(sesion);
    }

    public double getMejorMarcaPersonal() {
        return mejorMarcaPersonal;
    }

    public void setMejorMarcaPersonal(double mejorMarcaPersonal) {
        this.mejorMarcaPersonal = mejorMarcaPersonal;
    }

    public boolean actualizarMejorMarca(double nuevaMarca) {
        if (nuevaMarca > this.mejorMarcaPersonal) { 
            this.mejorMarcaPersonal = nuevaMarca;
            return true;
        }
        return false;
    }
}