
package org.diegocanteo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.diegocanteo.system.Principal;




public class MenuPrincipalController implements Initializable{
  private Principal escenarioPrincipal ;

  
  @Override 
    public void initialize(URL location, ResourceBundle rusources) {
  
    }
  
    public Principal getEscenarioPrincipal (){
        return escenarioPrincipal;
    
   }
    
    public void  setEscenarioPrincipal (Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
   
    public void  ventanaProgrmador(){
        escenarioPrincipal.ventanaProgramador(); 
    }

   public void ventanaEmpresa (){
       escenarioPrincipal.ventanaEmpresa();
  
   }
    
   public void  ventanaTipoEmpleado(){
       escenarioPrincipal.ventanaTipoEmpleado();
   }
   
   public void ventanaEmpleado(){
       escenarioPrincipal.ventaEmpleado();
   }
   
   public void ventaPresupuesto(){
       escenarioPrincipal.ventanaPresupuesto();
   }
   
   public void ventanaPlatos(){
       escenarioPrincipal.ventanaPlatos();
   }
   public void ventanaProducto(){
       escenarioPrincipal.ventanaProducto();
   }
 
   public void ventanaServicio(){
       escenarioPrincipal.ventanaServicio();
   }
   public void ventanaTipoPlato(){
    escenarioPrincipal.ventanaTipoPlato();
   }
   
}
