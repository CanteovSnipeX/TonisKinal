
package org.diegocanteo.bean;


public class Platos {
   private  int codigoPlatos;
   private String  cantidad;
   private String nombrePlato;
   private String descripcionPlato;
   private double prescioPlato ;
   private int codigoTipoPlatos;

    public Platos() {
    }

    public Platos(int codigoPlatos, String cantidad, String nombrePlato, String descripcionPlato, double prescioPlato, int codigoTipoPlatos) {
        this.codigoPlatos = codigoPlatos;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.prescioPlato = prescioPlato;
        this.codigoTipoPlatos = codigoTipoPlatos;
    }

    public int getCodigoPlatos() {
        return codigoPlatos;
    }

    public void setCodigoPlatos(int codigoPlatos) {
        this.codigoPlatos = codigoPlatos;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public double getPrescioPlato() {
        return prescioPlato;
    }

    public void setPrescioPlato(double prescioPlato) {
        this.prescioPlato = prescioPlato;
    }

    public int getCodigoTipoPlatos() {
        return codigoTipoPlatos;
    }

    public void setCodigoTipoPlatos(int codigoTipoPlatos) {
        this.codigoTipoPlatos = codigoTipoPlatos;
    }

   
    
    
    
}
