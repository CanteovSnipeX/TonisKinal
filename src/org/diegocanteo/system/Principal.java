
package org.diegocanteo.system;


import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.diegocanteo.controller.DatosPersonalesController;
import org.diegocanteo.controller.EmpleadosController;
import org.diegocanteo.controller.EmpresaController;
import org.diegocanteo.controller.MenuPrincipalController;
import org.diegocanteo.controller.PlatosController;
import org.diegocanteo.controller.PresupuestoController;
import org.diegocanteo.controller.ProductosController;
import org.diegocanteo.controller.ServicioController;
import org.diegocanteo.controller.TipoEmpleadoController;
import org.diegocanteo.controller.TipoPlatocontroller;

/**
 *
 * @author DiegoCanteo
 */
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/diegocanteo/view/";
    private Stage escenarioPrincipal; 
    private Scene escena;
   

    @Override
    
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal App");
        escenarioPrincipal.getIcons().add(new Image("/org/diegocanteo/image/Icono.png"));       
        //Parent root = FXMLLoader.load(getClass().getResource("/org/diegocanteo/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene (root);
        //escenarioPrincipal.setScene(escena);
        menuPrincipal();
        escenarioPrincipal.show();   
    }
    
  
    public void menuPrincipal(){
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",603,400);
            menuPrincipal.setEscenarioPrincipal(this );
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    
    public void ventanaProgramador(){
        try {
            DatosPersonalesController datosPersonales = (DatosPersonalesController)cambiarEscena("DatosPersonalesView.fxml",557,372);
            datosPersonales.setEscenarioPrincipal(this);
        }catch (Exception a){
            a.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try{
        EmpresaController Empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml",725,460);
         Empresa.setEscenarioPrincipal(this);
        }catch(Exception c){
            c.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",644,469);
            tipoEmpleado.setEscenarioPrincipal(this);
        }catch (Exception e ){
            e.printStackTrace();
        }
        
    }
    
    public void ventaEmpleado(){
        try{
            EmpleadosController empleado = (EmpleadosController)cambiarEscena("EmpleadosView.fxml",818,474);
            empleado.setEscenarioPrincipal(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml",436,489);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e ){
            e.printStackTrace();
            
        }
    }
    
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatocontroller tipoplato = (TipoPlatocontroller)cambiarEscena("TipoPlatoView.fxml", 600,375);
           tipoplato.setEscenarioPrincipal(this);
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaPlatos(){
        try{
        PlatosController plato = (PlatosController)cambiarEscena("PlatosView.fxml",668,420);
        plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   
      public void ventanaProducto(){
        try{
            ProductosController producto = (ProductosController)cambiarEscena("ProductosView.fxml",600,387);
               producto.setEscenarioPrincipal(this);
        }catch(Exception e){
         e.printStackTrace();

        }
    }

  public void  ventanaServicio(){
      try{
          ServicioController servicio = (ServicioController)cambiarEscena("ServicioView.fxml",822,463);
          servicio.setEscenarioPrincipal(this);
      }catch(Exception e){
          e.printStackTrace();
      }
  }
    
  /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    public Initializable cambiarEscena (String fxml,int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML= new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return  resultado; 
    }  
}
