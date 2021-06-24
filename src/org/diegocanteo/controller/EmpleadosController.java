
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
import org.diegocanteo.bean.Empleado;
import org.diegocanteo.system.Principal;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.diegocanteo.bean.TipoEmpleado;
import org.diegocanteo.db.Conexion;


public class EmpleadosController implements Initializable{
   
        
   private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
   private Principal escenarioPrincipal;
   private operaciones tipoDeOperacion = operaciones.NINGUNO;
   private ObservableList<Empleado> listaEmpleado;
   private ObservableList<TipoEmpleado> listaTipoEmpleado;
   @FXML private TextField txtCodigoEmpleado;
   @FXML private TextField txtNumeroEmpleado;
   @FXML private TextField txtNombreEmpleado;
   @FXML private TextField txtApellidoEmpleado;
   @FXML private TextField txtDireccion;
   @FXML private TextField txtTelefono;
   @FXML private TextField txtGradoCocinero;
   @FXML private ComboBox cmbCodigoTipoEmpleado;
   @FXML private TableView tblEmpleados;
   @FXML private TableColumn colCodigoEmpleado;
   @FXML private TableColumn colNumeroEmpleado;
   @FXML private TableColumn colNombreEmpleado;
   @FXML private TableColumn colApellidoEmpleado;
   @FXML private TableColumn colDireccion;
   @FXML private TableColumn colTelefono;
   @FXML private TableColumn colGradoCocinero;
   @FXML private TableColumn colCodigoTipoEmpleado;
   @FXML private Button btnNuevo;
   @FXML private Button btnEditar;
   @FXML private Button btnReporte;
   @FXML private Button btnEliminar;
   
    @Override   
    public void initialize(URL lacation, ResourceBundle rusources){
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    }

   public void cargarDatos(){
       tblEmpleados.setItems(getEmpleado());
       colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
       colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("numeroEmpleado"));
       colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
       colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
       colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
       colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
       colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
       colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoTipoEmpleado"));
   }
   
public void seleccionarElemento(){
    txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
    txtNumeroEmpleado.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
    txtNombreEmpleado.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado()));
    txtApellidoEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
    txtDireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
    txtTelefono.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
    txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
    cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmplado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));  
}




   public ObservableList<Empleado>getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                         resultado.getString("numeroEmpleado"),
                        resultado.getString("nombreEmpleado"),
                        resultado.getString("apellidoEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
       }catch(Exception e ){
           e.printStackTrace();
       }
       
       return listaEmpleado = FXCollections.observableArrayList(lista);
   }
   
   
    public ObservableList <TipoEmpleado>getTipoEmpleado(){
          ArrayList<TipoEmpleado> lista= new ArrayList<TipoEmpleado>(); 
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarTipoEmpleado}");
              ResultSet resultado = procedimiento.executeQuery();
              while (resultado.next()){
                  lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                             resultado.getString("descripcion")));
              }
          }catch(Exception e){
            e.printStackTrace();
          }
          
          return listaTipoEmpleado = FXCollections.observableArrayList(lista);
      }
    
    
    
    
       public TipoEmpleado buscarTipoEmplado(int codigoTipoEmpleado){
       TipoEmpleado resultado = null;
       try{
           PreparedStatement procedimiento  = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
           procedimiento.setInt(1, codigoTipoEmpleado);
           ResultSet registro = procedimiento.executeQuery();
           while(registro.next()){
               resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                             registro.getString("descripcion"));  
           }
       }catch(Exception e){
           e.printStackTrace();
       }
       return resultado;
           
       }
    
    
    
    public void nuevo (){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break ;
                
        }
    }
   
   
   public void guardar(){
       Empleado registro = new Empleado();
       registro.setNumeroEmpleado(txtNumeroEmpleado.getText());
       registro.setNombreEmpleado(txtNombreEmpleado.getText());
       registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
       registro.setDireccionEmpleado(txtDireccion.getText());
       registro.setTelefonoContacto(txtTelefono.getText());
       registro.setGradoCocinero(txtGradoCocinero.getText());  
       registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
           procedimiento.setString(1, registro.getNumeroEmpleado());
           procedimiento.setString(2, registro.getNombreEmpleado());
           procedimiento.setString(3, registro.getApellidoEmpleado());
           procedimiento.setString(4, registro.getDireccionEmpleado());
           procedimiento.setString(5, registro.getTelefonoContacto());
           procedimiento.setString(6, registro.getGradoCocinero());
           procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
           procedimiento.execute();
           listaEmpleado.add(registro);
       }catch(Exception e ){
           e.printStackTrace();
       }
   }
   
   public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                    int resultado = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar este registro","Eliminar registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                      if(resultado == JOptionPane.YES_OPTION){
                        try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_EliminarEmpleado(?)}");
                                procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());   
                                procedimiento.execute();
                                listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedItem());
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
      switch (tipoDeOperacion){
          case NINGUNO:
              if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnNuevo.setDisable(true);
                  btnEliminar.setDisable(true);
                  activarControles();
                  tipoDeOperacion = operaciones.ACTUALIZAR;
              }else{
                  JOptionPane.showMessageDialog(null, "Debe Seleccionar un Elemento");
              }
          break;
          case ACTUALIZAR:
              actualizar();
              btnEditar.setText("Editar");
              btnReporte.setText("Repote");
              btnEliminar.setDisable(false);
              btnNuevo.setDisable(true);
              tipoDeOperacion = operaciones.NINGUNO;
              cargarDatos();
              break; 
      }
  }
  
  public void actualizar(){
      try{
      PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?, ?, ?, ?, ?, ?, ?) } ");
      Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
      registro.setNumeroEmpleado(txtNumeroEmpleado.getText());
      registro.setNombreEmpleado(txtNombreEmpleado.getText());
      registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
      registro.setDireccionEmpleado(txtDireccion.getText());
      registro.setTelefonoContacto(txtTelefono.getText());
      registro.setGradoCocinero(txtGradoCocinero.getText());
      procedimiento.setInt(1,registro.getCodigoEmpleado());
      procedimiento.setString(2, registro.getNumeroEmpleado());
      procedimiento.setString(3, registro.getNombreEmpleado());
      procedimiento.setString(4, registro.getApellidoEmpleado());
      procedimiento.setString(5, registro.getDireccionEmpleado());
      procedimiento.setString(6, registro.getTelefonoContacto());
      procedimiento.setString(7,registro.getGradoCocinero());
      procedimiento.execute();
      }catch(Exception e){
          e.printStackTrace();
      }
  }
  
  
  

    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoCocinero.setEditable(false);

    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtGradoCocinero.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtNombreEmpleado.setText("");
        txtApellidoEmpleado.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtGradoCocinero.setText("");
    }
   
     public Principal getEscenarioPrincipal (){
        return escenarioPrincipal;
    
   }
    
    public void  setEscenarioPrincipal (Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
    
    public void  ventanaTipoEmpleado(){
       escenarioPrincipal.ventanaTipoEmpleado();
   }
     public void menuprincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
