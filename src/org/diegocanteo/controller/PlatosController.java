
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.diegocanteo.bean.Platos;
import org.diegocanteo.bean.TipoPlato;
import org.diegocanteo.db.Conexion;
import org.diegocanteo.system.Principal;


public class PlatosController implements Initializable {
private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
   private Principal escenarioPrincipal;
   private operaciones tipoDeOperaciones = operaciones.NINGUNO;
   private ObservableList<Platos> listaPlatos;
   private ObservableList<TipoPlato> listaTipoPlato;
   @FXML private TextField txtCodigoPlato;
   @FXML private TextField txtCantidad;
   @FXML private TextField txtNombrePlato;
   @FXML private TextField txtDescripcion;
   @FXML private TextField txtPrecio;
   @FXML private ComboBox cmbCodigotipoPlato;
   @FXML private TableView tblPlatos;
   @FXML private TableColumn colCodigoPlato;
   @FXML private TableColumn colCantidad;
   @FXML private TableColumn colNombrePlato;
   @FXML private TableColumn colDescripcionPlato;
   @FXML private TableColumn colPrecioPlato;
   @FXML private TableColumn colCodigoTipoPlatos;
   @FXML private Button btnNuevo;
   @FXML private Button btnEditar;
   @FXML private Button btnReporte;
   @FXML private Button btnEliminar;

   
    @Override
    public void initialize(URL location, ResourceBundle rusources) {
        cargarDatos();
        cmbCodigotipoPlato.setItems(getTipoPlato());
    }
    
    
    
    public void cargarDatos (){
    tblPlatos.setItems(getPlatos()); 
    colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer >("codigoPlatos"));
    colCantidad.setCellValueFactory(new PropertyValueFactory <Platos,String>("cantidad"));
    colNombrePlato.setCellValueFactory(new PropertyValueFactory <Platos,String>("nombrePlato"));
    colDescripcionPlato.setCellValueFactory(new PropertyValueFactory <Platos,String> ("descripcionPlato"));
    colPrecioPlato.setCellValueFactory(new PropertyValueFactory <Platos, Double>("prescioPlato"));
    colCodigoTipoPlatos.setCellValueFactory(new PropertyValueFactory <Platos ,Integer>("codigoTipoPlatos"));
    }
    
    
    
    
   public void seleccionarElemento(){
       txtCodigoPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlatos()));
       txtCantidad.setText((((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
       txtNombrePlato.setText((((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
       txtDescripcion.setText(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
       txtPrecio.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getPrescioPlato()));
       cmbCodigotipoPlato.getSelectionModel().select(buscarTipoPlatos(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlatos()));
}
   

   
    
    public ObservableList<Platos>getPlatos(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
            PreparedStatement procedimiento =  Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Platos(resultado.getInt("codigoPlatos"),
                                resultado.getString("cantidad"),
                                resultado.getString("nombrePlato"),
                                resultado.getString("descripcionPlato"),
                                resultado.getDouble("precioPlato"),
                                resultado.getInt("CodigoTipoPlatos")));
                
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return  listaPlatos = FXCollections.observableArrayList(lista);
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
  
  
  
   public TipoPlato buscarTipoPlatos(int codigoTipoPlatos){
       TipoPlato resultado = null;
       try{
           PreparedStatement procedimiento  = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
           procedimiento.setInt(1, codigoTipoPlatos);
           ResultSet registro = procedimiento.executeQuery();
           while(registro.next()){
               resultado = new TipoPlato(registro.getInt("codigoTipoPlatos"),
                                         registro.getString("descripcionTipo"));
           }
       }catch(Exception e){
           e.printStackTrace();
       }
       return resultado;
           
       }
  
   
   public void nuevo (){
        switch (tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.GUARDAR;
                break ;
            case  GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDefaultButton(true);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.NINGUNO;
                break ;
                
        }
    }   
    
   
      public void guardar(){
         Platos  registro = new Platos();
         registro.setCantidad(txtCantidad.getText());
         registro.setNombrePlato(txtNombrePlato.getText());
         registro.setDescripcionPlato(txtDescripcion.getText());
         registro.setPrescioPlato(Double.parseDouble(txtPrecio.getText()));
         registro.setCodigoTipoPlatos(((TipoPlato)cmbCodigotipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlatos());  
         
         try{
             PreparedStatement  procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_AgregarPlato(?,?,?,?,?)}");
             procedimiento.setString(1,registro.getCantidad());
             procedimiento.setString(2,registro.getNombrePlato());
             procedimiento.setString(3, registro.getDescripcionPlato());
             procedimiento.setDouble(4, registro.getPrescioPlato());
             procedimiento.setInt(5, registro.getCodigoTipoPlatos());
             
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
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
                    int resultado = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar este registro","Eliminar registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                      if(resultado == JOptionPane.YES_OPTION){
                        try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_EliminarEmpleado(?)}");
                                procedimiento.setInt(1,((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlatos());   
                                procedimiento.execute();
                                listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedItem());
                                limpiarControles();
                        }catch(Exception e ){
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
              if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnNuevo.setDisable(true);
                  btnEliminar.setDisable(true);
                  activarControles();
                  tipoDeOperaciones = operaciones.ACTUALIZAR;
              }else{
                  JOptionPane.showMessageDialog(null, "Debe Seleccionar un Elemento");
              }
          break;
          case ACTUALIZAR:
              //actualizar();
              btnEditar.setText("Editar");
              btnReporte.setText("Repote");
              btnEliminar.setDisable(false);
              btnNuevo.setDisable(false);
              tipoDeOperaciones = operaciones.NINGUNO;
              cargarDatos();
              break; 
      }
  }
    
    
    
    
    
    public  void  actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?}");
             Platos   registro = (Platos)tblPlatos.getSelectionModel().getSelectedItem();
             registro.setCantidad(txtCantidad.getText());
             registro.setNombrePlato(txtNombrePlato.getText());
             registro.setDescripcionPlato(txtDescripcion.getText());
             registro.setPrescioPlato(Double.parseDouble(txtPrecio.getText()));
             registro.setCodigoTipoPlatos(((Platos)cmbCodigotipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlatos());
             procedimiento.setInt(1,registro.getCodigoPlatos());
             procedimiento.setString(2,registro.getCantidad());
             procedimiento.setString(3,registro.getNombrePlato());
             procedimiento.setString(4,registro.getDescripcionPlato());
             procedimiento.setDouble(5,registro.getPrescioPlato());
             procedimiento.execute();     
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecio.setEditable(false);
        cmbCodigotipoPlato.setDisable(false);

    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecio.setEditable(true);
       cmbCodigotipoPlato.setDisable(false);

    }
    
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        cmbCodigotipoPlato.getSelectionModel().clearSelection();
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
    
    public void ventanaTipoPlato(){
    escenarioPrincipal.ventanaTipoPlato();
   }
}
