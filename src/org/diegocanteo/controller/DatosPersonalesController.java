package org.diegocanteo.controller;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.diegocanteo.system.Principal;





public class DatosPersonalesController  implements  Initializable{
    private Principal escenarioPrincipal;

      @Override 

    public void initialize(URL location, ResourceBundle rescurces) {
       
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
        
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
 
    
    public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }
   
}

