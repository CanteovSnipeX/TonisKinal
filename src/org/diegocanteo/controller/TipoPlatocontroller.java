
package org.diegocanteo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.diegocanteo.bean.TipoPlato;
import org.diegocanteo.db.Conexion;
import org.diegocanteo.system.Principal;


public class TipoPlatocontroller  implements Initializable{
    private Object registro;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    @FXML private TextField txtcodigoTipoPlato ;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tbltipoPlatos;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;

    @Override
    public void initialize(URL location, ResourceBundle rescurces) {
       cargarDatos();
    }
    
    
    
  public void cargarDatos(){
      tbltipoPlatos.setItems(getTipoPlato());
      colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlatos"));
      colDescripcionPlato.setCellValueFactory(new PropertyValueFactory <TipoPlato, String >("descripcionTipo"));
  }
  
  
  
  public void seleccinarElemento (){
      txtcodigoTipoPlato.setText(String.valueOf(((TipoPlato)tbltipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlatos()));
      txtDescripcion.setText(((TipoPlato)tbltipoPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo());  
  }
  
  
  public ObservableList<TipoPlato>getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
           PreparedStatement  procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
           ResultSet resultado = procedimiento.executeQuery();
           while (resultado.next()){
               lista.add(new TipoPlato(resultado.getInt("codigoTipoPlatos"),
                                       resultado.getString("descripcionTipo")));
           }
 
        }catch(Exception e ){
         e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
  
  
  
  
    public void nuevo(){
       switch(tipoDeOperaciones){
             case NINGUNO:
                 activarControles();
                 btnNuevo.setText("Guardar");
                 btnEliminar.setDisable(false);
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 tipoDeOperaciones = operaciones.GUARDAR;
                 break;
             case GUARDAR:
                 guardar();
                 desactivarControles();
                 limpiarControles();
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEliminar.setDisable(false);
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);
                 tipoDeOperaciones = operaciones.NINGUNO;
                 cargarDatos();
                 break;
         }
     }
   
  public void guardar(){
         TipoPlato registro = new TipoPlato();
         registro.setDescripcionTipo(txtDescripcion .getText());
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
             procedimiento.setString(1,registro.getDescripcionTipo());
             procedimiento.execute();
             listaTipoPlato.add(registro);
         }catch(Exception e ){
             e.printStackTrace();
         }
     }
            
    public void eliminar(){
       switch(tipoDeOperaciones){
           case GUARDAR:
               desactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnNuevo.setDisable(false);
               btnEliminar.setText("Eliminar");
               btnEliminar.setDisable(false);
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               tipoDeOperaciones  = operaciones.NINGUNO;
               break;
           default:
               if (tbltipoPlatos.getSelectionModel().getSelectedItem()!=null){
                    int resultado = JOptionPane.showConfirmDialog(null,"Estas segura de Eliminar el registro?", "Eliminar Empresa",JOptionPane. YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            procedimiento.setInt(1,((TipoPlato)tbltipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlatos());
                            procedimiento.execute();
                            listaTipoPlato.remove(tbltipoPlatos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
               }else{
                   JOptionPane.showMessageDialog(null,"Debe Seleccionar un Elemento");
               }
        }
   }
    
    
    
 public void editar(){
     switch (tipoDeOperaciones){
         case NINGUNO:
             if(tbltipoPlatos.getSelectionModel().getSelectedItem() != null){
                 btnEditar.setText("Actualizar");
                 btnReporte.setText("Cancelar");
                 btnNuevo.setDisable(true);
                 btnEliminar.setDisable(true);
                 activarControles();
                 tipoDeOperaciones = operaciones.ACTUALIZAR;
             }else{
                 JOptionPane.showMessageDialog(null, "Debe Selecionar Un Elmento ");
             }
         break;
         case ACTUALIZAR:
             actualizar();
             btnEditar.setText("Editar");
             btnReporte.setText("Reporte");
             btnEliminar.setDisable(false);
             btnNuevo.setDisable(false);
             tipoDeOperaciones = operaciones.NINGUNO;
             cargarDatos();
             break; 
     }
     
 }
   
   
   public void  actualizar (){
       try{
           PreparedStatement  procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
           TipoPlato  registro = (TipoPlato)tbltipoPlatos.getSelectionModel().getSelectedItem();
           registro.setDescripcionTipo(txtDescripcion.getText());
           procedimiento.setInt(1,registro.getCodigoTipoPlatos());
           procedimiento.setString(2,registro.getDescripcionTipo());
           procedimiento.execute();
           limpiarControles();
       }catch(Exception e ){
           e.printStackTrace();
       }
       
   }
   
  
  
  
  
   
    public void desactivarControles(){
        txtcodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
       
    }
    
    public void activarControles(){
        txtcodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtcodigoTipoPlato.setText("");
        txtDescripcion.setText("");
    }
            
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuprincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaPlatos(){
       escenarioPrincipal.ventanaPlatos();
   }

}
