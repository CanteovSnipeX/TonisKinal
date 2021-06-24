
package org.diegocanteo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.diegocanteo.bean.Empresa;
import org.diegocanteo.bean.Servicio;
import org.diegocanteo.db.Conexion;
import org.diegocanteo.report.GenerarReporte;
import org.diegocanteo.system.Principal;


public class ServicioController  implements Initializable{
private enum operaciones{ NUEVO, GUARDAR , ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
private Principal escenarioPrincipal;
private operaciones tipoDeOperacion = operaciones.NINGUNO;
private ObservableList <Servicio> listarServicios;
private ObservableList <Empresa> listaEmpresa;
private DatePicker fecha ;
@FXML private TextField txtCodigoServicio; 
@FXML private TextField txtTipoServicio;
@FXML private TextField txtHoraServicio;
@FXML private TextField txtLugarservicio;
@FXML private TextField txtTelefonoContacto;
@FXML private ComboBox cmbCodigoEmpresa;
@FXML private GridPane grpFechaServicio;
@FXML private TableView tblServicios;
@FXML private TableColumn colCodigoServicio;
@FXML private TableColumn colFechaServicio;
@FXML private TableColumn colTipoServicio;
@FXML private TableColumn colHoraServicio;
@FXML private TableColumn colLugarServicio;
@FXML private TableColumn colTelefonoContacto;
@FXML private TableColumn colCodigoEmpresa;
@FXML private Button btnNuevo;
@FXML private Button btnEditar;
@FXML private Button btnEliminar;
@FXML private Button btnReportar;

    @Override
    public void initialize(URL location, ResourceBundle rusources) {
        cargarDatos();
        fecha = new  DatePicker (Locale.ENGLISH);
        fecha.setDateFormat(new  SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Todasy");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/diegocanteo/resource/DatePicker.css");
        grpFechaServicio.add(fecha,0,0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        }
    
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));
    }
    
    
    public void seleccionarElemento(){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        txtLugarservicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    

    
    public ObservableList <Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarServicio}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                       resultado.getDate("fechaServicio"),
                                       resultado.getString("tipoServicio"),
                                       resultado.getString("horaServicio"),
                                       resultado.getString("lugarServicio"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e ){
        e.printStackTrace();
        } 
        return listarServicios = FXCollections.observableArrayList(lista);
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
          
        return listaEmpresa = FXCollections.observableArrayList(lista);
  }
  
  
  
     public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(
                                 registro.getInt("codigoEmpresa"),
                                 registro.getString("nombreEmpresa"),
                                 registro.getString("direccion"),
                                 registro.getString("telefono"));
            }            
        }catch(Exception e){
            e.printStackTrace();
        }
                    return resultado;
        }
    
        
     
     public void nuevo (){
         switch(tipoDeOperacion){
             case NINGUNO:
                 activarControles();
                 btnNuevo.setText("Guardar");
                 btnEliminar.setDisable(false);
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReportar.setDisable(true);
                 tipoDeOperacion =  operaciones.GUARDAR ;
                 break ;
             case  GUARDAR :
                 guardar();
                 desactivarControles();
                 limpiarControles();
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setDisable(false);
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReportar.setDisable(false);
                 tipoDeOperacion =  operaciones.NINGUNO ;
                 cargarDatos();
                 break;
         }
         
     }
     
     
    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarservicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new  java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2,registro.getTipoServicio());
            procedimiento.setString(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listarServicios.add(registro);
        }catch(Exception e){
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
                btnReportar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
          default:
              if(tblServicios.getSelectionModel().getSelectedItem() !=null){
                        int resultado = JOptionPane.showConfirmDialog(null, "Estas seguro de Eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resultado == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                                procedimiento.setInt(1,((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());    
                                procedimiento.execute();
                                listarServicios.remove(tblServicios.getSelectionModel().getSelectedItem());
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
              if(tblServicios.getSelectionModel().getSelectedItem() !=null){
                  btnEditar.setText("Actualizar");
                  btnReportar.setText("Cancelar");
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
              btnReportar.setText("Repote");
              btnEliminar.setDisable(false);
              btnNuevo.setDisable(false);
              tipoDeOperacion = operaciones.NINGUNO;
              cargarDatos();
              break; 
      }
  }
     
  
  public void actualizar(){
      try {
          PreparedStatement  procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?)}");
          Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
          registro.setFechaServicio(fecha.getSelectedDate());
          registro.setTipoServicio(txtTipoServicio.getText());
          registro.setHoraServicio(txtHoraServicio.getText());
          registro.setLugarServicio(txtLugarservicio.getText());
          registro.setTelefonoContacto(txtTelefonoContacto.getText());
          registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
          procedimiento.setInt(1,registro.getCodigoServicio());
          procedimiento.setDate(2, new  java.sql.Date(registro.getFechaServicio().getTime()));
          procedimiento.setString(3,registro.getTipoServicio());
          procedimiento.setString(4, registro.getHoraServicio());
          procedimiento.setString(5, registro.getLugarServicio());
          procedimiento.setString(6, registro.getTelefonoContacto());
          procedimiento.execute();
      }catch(Exception e){
          e.printStackTrace();
      }
  }   
  
     public void generarRepoter (){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnEditar.setDisable(false);
                btnReportar.setText("Reporte");
                btnReportar.setDisable(false);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion= operaciones.NINGUNO;
                break;
            
                        
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa",null);
        GenerarReporte.mostrarReporte("Reporte Presupuesto.jasper","Reporte Presupuesto", parametros);
        
                
    }
  
  
  
  
  
  
  
  
    
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarservicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        grpFechaServicio.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void activarControles (){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarservicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        grpFechaServicio.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarservicio.setText("");
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
    }
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }

     public void menuprincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
