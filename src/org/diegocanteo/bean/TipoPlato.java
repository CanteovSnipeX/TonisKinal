
package org.diegocanteo.bean;


public class TipoPlato{
 private int codigoTipoPlatos;
 private String descripcionTipo;

    public TipoPlato() {
    }

    public TipoPlato(int codigoTipoPlatos, String descripcionTipo) {
        this.codigoTipoPlatos = codigoTipoPlatos;
        this.descripcionTipo = descripcionTipo;
    }

    public int getCodigoTipoPlatos() {
        return codigoTipoPlatos;
    }

    public void setCodigoTipoPlatos(int codigoTipoPlatos) {
        this.codigoTipoPlatos = codigoTipoPlatos;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }

 public String toString (){
         return getCodigoTipoPlatos()+ "  " +getDescripcionTipo();
    }

          
}
