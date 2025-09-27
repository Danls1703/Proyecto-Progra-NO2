public class Deporte {
    private String nombre;
    private String especialidad;
    private String tipo;

    public Deporte() {}

    public Deporte(String nombre, String especialidad, String tipo) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.tipo = tipo;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    } 
}
