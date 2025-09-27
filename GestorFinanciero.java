public class GestorFinanciero {
    private static final double PAGO_POR_ENTRENAMIENTO = 50.0;
    private static final double BONO_EXTRANJERO = 200.0;    
    private static final double BONO_MEJOR_MARCA = 500.0;   

    public double calcularPagoMensual(Atleta atleta) {
        double pagoTotal = 0.0;

        int numEntrenamientos = atleta.getSesiones().size();
        pagoTotal += numEntrenamientos * PAGO_POR_ENTRENAMIENTO;
        
        System.out.println("   - Pago base por " + numEntrenamientos + " entrenamientos: $" + (numEntrenamientos * PAGO_POR_ENTRENAMIENTO));

        int bonosExtranjero = 0;
        for (SesionEntrenamiento sesion : atleta.getSesiones()) {
            if (sesion.isEsExtranjero()) {
                bonosExtranjero++;
            }
        }
        pagoTotal += bonosExtranjero * BONO_EXTRANJERO;
        System.out.println("   - Bonos por entrenamiento en el extranjero: $" + (bonosExtranjero * BONO_EXTRANJERO));

        int bonosMarca = 0;
        for (SesionEntrenamiento sesion : atleta.getSesiones()) {
            if (sesion.superoMejorMarcaPersonal()) { 
                bonosMarca++;
            }
        }
        pagoTotal += bonosMarca * BONO_MEJOR_MARCA;
        System.out.println("   - Bonos por superar mejor marca: $" + (bonosMarca * BONO_MEJOR_MARCA));

        return pagoTotal;
    }
}