
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
import org.diegocanteo.bean.Productos;
import org.diegocanteo.db.Conexion;
import org.diegocanteo.system.Principal;


    public class ProductosController  implements Initializable {
    
    private Object registro;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Productos> listarProductos;
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField  txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReportar;
    @FXML private Button btnEliminar;  
    
    @Override
    public void initialize(URL location, ResourceBundle rusources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory< Productos, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Productos, String >("cantidad"));        
    }
    
    
    
   public void seleccionarElemento(){
       txtCodigoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
       txtNombreProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
       txtCantidad.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCantidad());
    } 
    
 
    public ObservableList <Productos>getProductos(){
      ArrayList<Productos> lista= new ArrayList<Productos>(); 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos(resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getString("cantidad")));                   
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return listarProductos =  FXCollections.observableArrayList(lista);
    }
    
    
    
    public  void nuevo (){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportar.setDisable(true);
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
                btnReportar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    
    public void guardar(){
         Productos registro = new Productos();
         registro.setNombreProducto(txtNombreProducto.getText());
         registro.setCantidad(txtCantidad.getText());
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
             procedimiento.setString(1,registro.getNombreProducto());
             procedimiento.setString(2,registro.getCantidad());
             procedimiento.execute();
             listarProductos.add(registro);
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
                btnReportar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                   if(tblProductos.getSelectionModel().getSelectedItem()!=null){
                    int resultado = JOptionPane.showConfirmDialog(null, "Estas segura de Eliminar el registro?", "Eliminar Empresa",JOptionPane. YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                      if(resultado == JOptionPane.YES_OPTION){
                      try {
                          PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarEmpresa(?)}");
                          procedimiento.setInt(1,((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                          procedimiento.execute();
                          listarProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                          limpiarControles();
                      }catch(Exception e){
                          e.printStackTrace();
                         }
                    }
                }else{
                      JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }   
       
      }
       
       
       
        public void editar(){
     switch (tipoDeOperaciones){
         case NINGUNO:
             if(tblProductos.getSelectionModel().getSelectedItem() != null){
                 btnEditar.setText("Actualizar");
                 btnReportar.setText("Cancelar");
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
             btnReportar.setText("Reporte");
             btnEliminar.setDisable(false);
             btnNuevo.setDisable(false);
             tipoDeOperaciones = operaciones.NINGUNO;
             cargarDatos();
             break; 
     }
     
 }
        
        
        public void actualizar(){
            try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?)}");
            Productos registro = (Productos)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(txtCantidad.getText());
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setString(2, registro.getCantidad());
            procedimiento.execute();
            limpiarControles();
            }catch(Exception e ){
                e.printStackTrace();
            }
        }
   
    
    
    
    
  public void desactivarControles (){
      txtCodigoProducto.setEditable(false);
      txtNombreProducto.setEditable(false);
      txtCantidad.setEditable(false);
  } 
  
  public void activarControles(){
      txtCodigoProducto.setEditable(false);
      txtNombreProducto.setEditable(true);
      txtCantidad.setEditable(true);
  }
    
  public void limpiarControles (){
      txtCodigoProducto.setText(" ");
      txtNombreProducto.setText(" ");
      txtCantidad.setText("  ");
      
  }
    
     public Principal getEscenarioPrincipal(){
     return escenarioPrincipal;
    
   }
    
    public void  setEscenarioPrincipal (Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
     public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
  
     }
    
}
