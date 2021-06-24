
package org.diegocanteo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.diegocanteo.bean.Empresa;
import org.diegocanteo.db.Conexion;
import org.diegocanteo.report.GenerarReporte;
import org.diegocanteo.system.Principal;


public class EmpresaController implements Initializable {

    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
      private Principal escenarioPrincipal;
      private operaciones tipoDeOperaciones = operaciones.NINGUNO;
      private ObservableList<Empresa> listaEmpresa;
      @FXML private TextField txtCodigoEmpresa;
      @FXML private TextField txtNombreEmpresa;
      @FXML private TextField txtDireccionEmpresa;
      @FXML private TextField txtTelefono;
      @FXML private TableView tblEmpresas;
      @FXML private TableColumn colCodigoEmpresa;
      @FXML private TableColumn colNombreEmpresa;
      @FXML private TableColumn colDireccionEmpresa;
      @FXML private TableColumn colTelefonoEmpresa;
      @FXML private Button btnNuevo;
      @FXML private Button btnEliminar;
      @FXML private Button btnEditar;
      @FXML private Button btnReporte;
      @FXML private Button btnPresupuesto;
      
              
    @Override
        public void initialize(URL location, ResourceBundle rusources){
       cargarDatos();
    }
    public void cargarDatos(){
        tblEmpresas.setItems(getEmpresa());
           colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa")); 
           colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
           colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
           colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
    }
   
  
    
    public ObservableList<Empresa> getEmpresa(){
     ArrayList<Empresa> lista = new ArrayList<Empresa>();
     try {
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarEmpresas}");
         ResultSet resultado = procedimiento.executeQuery();
         while(resultado.next()){
             lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                    resultado.getString("nombreEmpresa"),
                                    resultado.getString("direccion"),
                                    resultado.getString("telefono")));
         }
     }catch(Exception e){
        e.printStackTrace();
     }
             
        return listaEmpresa=FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch (tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setDisable(false);
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
        Empresa registro = new Empresa();
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefono.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?, ?, ?)}");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
            txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
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
                   if(tblEmpresas.getSelectionModel().getSelectedItem()!=null){
                    int resultado = JOptionPane.showConfirmDialog(null, "Estas segura de Eliminar el registro?", "Eliminar Empresa",JOptionPane. YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                      if(resultado == JOptionPane.YES_OPTION){
                      try {
                          PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarEmpresa(?)}");
                          procedimiento.setInt(1,((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                          procedimiento.execute();
                          listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedItem());
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
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setDisable(false);
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
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        } 
  }
    
    public void actualizar(){
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresa(?, ?, ?, ?)}");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefono.getText());
            procedimineto.setInt(1, registro.getCodigoEmpresa());
            procedimineto.setString(2, registro.getNombreEmpresa());
            procedimineto.setString(3, registro.getDireccion());
            procedimineto.setString(4, registro.getTelefono());
            procedimineto.execute();
        }catch(Exception e ){
          e.printStackTrace();
        }
    }
    
    
    
    public void generarRepoter (){
        switch(tipoDeOperaciones){
            case NINGUNO:
                imprimirReporte();
            break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnEditar.setDisable(false);
                btnReporte.setText("Reporte");
                btnReporte.setDisable(false);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            
                        
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa",null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper","Reporte Empresa", parametros);
        
                
    }

   
    public void  desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
            txtNombreEmpresa.setEditable(false);
                txtDireccionEmpresa.setEditable(false);
                     txtTelefono.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
            txtNombreEmpresa.setEditable(true);
                txtDireccionEmpresa.setEditable(true);
                    txtTelefono.setEditable(true);
    }
    
     public void limpiarControles(){
        txtCodigoEmpresa.setText("");
            txtNombreEmpresa.setText("");
                txtDireccionEmpresa.setText("");
                    txtTelefono.setText("");
     }
       
    public Principal getEscenarioPrincipal (){
        return escenarioPrincipal;
    
   }
    
    public void  setEscenarioPrincipal (Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
    
   public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }
 
   public void ventaPresupuesto(){
       escenarioPrincipal.ventanaPresupuesto();
   }
   
   public void ventanaServicio(){
       escenarioPrincipal.ventanaServicio();
   }
   
   
}

