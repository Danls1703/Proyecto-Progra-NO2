import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SesionEntrenamiento {
    private String especialidad;
    private String tipo;
    private Date fecha;
    private Time duracion;
    private List<Marca> marcas = new ArrayList<>();
    private boolean esExtranjero = false;
    private boolean superoMejorMarcaPersonal = false;

    public SesionEntrenamiento() {
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

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getDuracion() {
        return duracion;
    }
    public void setDuracion(Time duracion) {
        this.duracion = duracion;
    }

    public boolean isEsExtranjero() {
        return esExtranjero;
    }
    public void setEsExtranjero(boolean esExtranjero) {
        this.esExtranjero = esExtranjero;
    }

    public boolean superoMejorMarcaPersonal() {
        return superoMejorMarcaPersonal;
    }
    public void setSuperoMejorMarcaPersonal(boolean superoMejorMarcaPersonal) {
        this.superoMejorMarcaPersonal = superoMejorMarcaPersonal;
    }

    public void agregarMarcas(Marca marca){
        this.marcas.add(marca);
    }
    
    public double promedio() {
        if (marcas.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Marca marca : marcas) {
            suma += marca.getValor();
        }
        return suma / marcas.size();
    }
    
    public Marca mejorMarca() {
        if (marcas.isEmpty()) {
            return null;
        }
        Marca mejor = marcas.get(0);
        for (int i = 1; i < marcas.size(); i++) {
            if (marcas.get(i).getValor() > mejor.getValor()) {
                mejor = marcas.get(i);
            }
        }
        return mejor;
    }

    public List<Marca> getMarcas() {
        return marcas;
    }
}
