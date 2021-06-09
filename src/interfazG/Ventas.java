/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazG;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import objetos.Validar;

/**
 *
 * @author 52341
 */
public class Ventas extends javax.swing.JPanel {
    private boolean txtField[] = new boolean[4];
    private boolean txtField2[] = new boolean[2];
    private boolean bandConsultaEV = false;
    private boolean bandConsultaDV = false;
    private double subtotal = 0, total = 0, precio = 0;
    private int stock = 0;
    
    public Ventas() {
        initComponents();
        /*
        idCl();
        idV();
        idPd();
        nomCliente();
        infoProducto();
        consultaGeneralEV();
        consultaGeneralDV(); 
        */
    }
    
    public void idCl(){    
        DefaultComboBoxModel cl = new DefaultComboBoxModel();
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdCliente from Clientes");
            
            while(rs.next())
                cl.addElement(rs.getString(1));
            
            jCBIdCl.setModel(cl);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void idV(){    
        DefaultComboBoxModel v = new DefaultComboBoxModel();
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdVenta from EncabezadoVentas");
            
            while(rs.next())
                v.addElement(rs.getString(1));
            
            jCBIdV.setModel(v);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void idPd(){    
        DefaultComboBoxModel pd = new DefaultComboBoxModel();
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdProducto from Productos");
            
            while(rs.next())
                pd.addElement(rs.getString(1));

            jCBIdPd.setModel(pd);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void limpiarEV(){
        jTxtFieldIdV.setText(null);
        jTxtFieldAnio.setText(null);
        jTxtFieldMes.setText(null);
        jTxtFieldDia.setText(null);
        jTxtFieldTotal.setText("0");
        jTxtFieldIdV.setBackground(Color.WHITE);
        jTxtFieldAnio.setBackground(Color.WHITE);
        jTxtFieldMes.setBackground(Color.WHITE);
        jTxtFieldDia.setBackground(Color.WHITE);
        jTxtFieldIdV.requestFocus();
        bandConsultaEV = false;
        inicializarArreglo(txtField, false);
    }
    
    public void limpiarDV(){
        jTxtFieldDesc.setText(null);
        jTxtFieldCant.setText(null);
        jTxtFieldSubtotal.setText("0");
        jTxtFieldPrecio.setText(null);
        jTxtFieldDesc.setBackground(Color.WHITE);
        jTxtFieldCant.setBackground(Color.WHITE);
        bandConsultaDV = false;
        inicializarArreglo(txtField2, false);
    }
    
    public void agregarEV(){
        String idV, idCl, dia, mes, anio;
        double total = 0;
        
        idV = jTxtFieldIdV.getText();
        
        if(!registroExiste(idV, "IdVenta", "EncabezadoVentas")){
            idCl = String.valueOf(jCBIdCl.getSelectedItem());
            try {
                dia = jTxtFieldDia.getText();
                mes = jTxtFieldMes.getText();
                anio  = jTxtFieldAnio.getText();
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into EncabezadoVentas values ('" + idV + "','" + idCl + "','" 
                                                                       + dia + "','" + mes + "','" + anio + "','" + total + "')");
                ps.executeUpdate();
                idV();
                consultaGeneralEV();
                limpiarEV();
                JOptionPane.showMessageDialog(null, "SE HAN REGISTRADO LOS DATOS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e, "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "YA EXISTE UN REGISTRO\nCON LA CLAVE INTRODUCIDA.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void eliminarEV(){
        String idV = jTxtFieldIdV.getText();
        int i = 1;
        
        if(registroExiste(idV, "IdVenta", "EncabezadoVentas")){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?\nNota: Se eliminaran los registros asociados.", "Aviso",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    Statement leer = Paneles.conect.createStatement();
                    ResultSet rs = leer.executeQuery("select Cant, IdProducto from DetalleVentas where IdVenta = '" + idV + "'");
                    PreparedStatement ps;
                    
                    while(rs.next()){
                        ps = Paneles.conect.prepareStatement("update Productos set Stock += " + rs.getInt(i++) + " where IdProducto = '" + rs.getString(i) + "'");
                        ps.executeUpdate();
                        i = 1;
                    }
                    ps = Paneles.conect.prepareStatement("delete from DetalleVentas where IdVenta = '" + idV +  "'");
                    ps.executeUpdate();
                    ps = Paneles.conect.prepareStatement("delete from EncabezadoVentas where IdVenta = '" + idV + "'");
                    ps.executeUpdate();
                    idV();
                    consultaGeneralEV();
                    consultaGeneralDV();
                    limpiarEV();
                    JOptionPane.showMessageDialog(null, "SE HA ELIMINADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {  
                    JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
               JOptionPane.showMessageDialog(null, "SE HA CANCELADO LA ELIMINACIÓN.", "Aviso", JOptionPane.INFORMATION_MESSAGE); 
        }         
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void modificarEV(){
        String idV, idCl, dia, mes, anio;
        
        idV = jTxtFieldIdV.getText();
        
        if(registroExiste(idV, "IdVenta", "EncabezadoVentas")){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(opcion == JOptionPane.YES_OPTION){
                idCl = String.valueOf(jCBIdCl.getSelectedItem());
                dia = jTxtFieldDia.getText();
                mes = jTxtFieldMes.getText();
                anio  = jTxtFieldAnio.getText();
                
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("update EncabezadoVentas set IdCliente = '" + idCl + "', DiaV = '" + dia
                                           + "', MesV = '" + mes + "', AnioV = '" + anio + "' where IdVenta = '" + idV + "'");
                    ps.executeUpdate();                   
                    consultaGeneralEV();
                    limpiarEV();
                    JOptionPane.showMessageDialog(null, "SE HA ACTUALIZADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "SE HAN DESCARTADO LAS MODIFICACIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                consultarEV();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultarEV(){
        String idV = jTxtFieldIdV.getText();
        
        if(registroExiste(idV, "IdVenta", "EncabezadoVentas")){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from EncabezadoVentas where IdVenta = '" + idV + "'");

                rs.next();
                jCBIdCl.setSelectedItem(rs.getString(2));
                jTxtFieldDia.setText(rs.getString(3));
                jTxtFieldMes.setText(rs.getString(4));
                jTxtFieldAnio.setText(rs.getString(5));
                jTxtFieldTotal.setText(Double.toString(rs.getDouble(6)));
                inicializarArreglo(txtField, true);
                bandConsultaEV = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultaGeneralEV(){
        String colum[] = {"IdVenta", "IdCliente", "Dia", "Mes", "Año", "Total"};
        DefaultTableModel tEV = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        Object datos[] = new Object[6];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from EncabezadoVentas");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getDouble(6);
                tEV.addRow(datos);
            }
            
            tablaEncabezadoV.setModel(tEV);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void agregarDV(){
        String idV, idPd;
        double precioV;
        int desc, cant;
        
        idV = String.valueOf(jCBIdV.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());;
        
        if(!registroExiste(idV+idPd, "IdVenta+IdProducto", "DetalleVentas")){
            precioV = Double.parseDouble(jTxtFieldPrecio.getText());
            desc = Integer.parseInt(jTxtFieldDesc.getText());
            cant = Integer.parseInt(jTxtFieldCant.getText());
            
            try {
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into DetalleVentas values ('" + idV + "','" + idPd + "'," 
                                                                       + precioV + "," + desc + "," + cant + "," + subtotal + ")");
                ps.executeUpdate();
                actualizarTotal(idV);
                ps = Paneles.conect.prepareStatement("update EncabezadoVentas set TotalV = " + total + " where IdVenta = '" + idV + "'");
                ps.executeUpdate();
                ps = Paneles.conect.prepareStatement("update Productos set Stock -= " + cant + " where IdProducto = '" + idPd + "'");
                ps.executeUpdate();
                
                infoProducto();
                consultaGeneralEV();
                consultaGeneralDV();
                limpiarDV();
                JOptionPane.showMessageDialog(null, "SE HAN REGISTRADO LOS DATOS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "YA EXISTE UN REGISTRO\nCON LA CLAVE INTRODUCIDA.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void eliminarDV(){
        String idV, idPd;
        idV = String.valueOf(jCBIdV.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());;
        
        if(registroExiste(idV+idPd, "IdVenta+IdProducto", "DetalleVentas")){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?", "Aviso",
                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("update Productos set Stock += " + stock + " where IdProducto = '" + idPd + "'");
                    ps.executeUpdate();
                    ps = Paneles.conect.prepareStatement("delete from DetalleVentas where IdVenta+IdProducto = '" + idV + idPd + "'");
                    ps.executeUpdate();
                    actualizarTotal(idV);
                    ps = Paneles.conect.prepareStatement("update EncabezadoVentas set TotalV = " + total + " where IdVenta = '" + idV + "'");
                    ps.executeUpdate();
                    
                    infoProducto();
                    consultaGeneralEV(); 
                    consultaGeneralDV();
                    limpiarDV();
                    JOptionPane.showMessageDialog(null, "SE HA ELIMINADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                }
            }
            else
                JOptionPane.showMessageDialog(null, "SE HA CANCELADO LA ELIMINACIÓN.", "Aviso", JOptionPane.INFORMATION_MESSAGE);    
        }         
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void modificarDV(){
        String idV, idPd;
        double precio;
        int desc, cant;
        
        idV = String.valueOf(jCBIdV.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());
        
        if(registroExiste(idV+idPd, "IdVenta+IdProducto", "DetalleVentas")){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(opcion == JOptionPane.YES_OPTION){
                precio = Double.parseDouble(jTxtFieldPrecio.getText());
                desc = Integer.parseInt(jTxtFieldDesc.getText());
                cant = Integer.parseInt(jTxtFieldCant.getText());
                calcularSubtotal();

                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("update DetalleVentas set PrecioV = " + precio + ", Descuento = " + desc 
                                           + ", Cant = " + cant + ", SubtotalV = " + subtotal + " where IdVenta+IdProducto = '" + idV+idPd + "'");
                    ps.executeUpdate();
                    actualizarTotal(idV);
                    ps = Paneles.conect.prepareStatement("update EncabezadoVentas set TotalV = " + total + " where IdVenta = '" + idV + "'");
                    ps.executeUpdate();         
                    stock = stock - cant;
                    ps = Paneles.conect.prepareStatement("update Productos set Stock += " + stock + " where IdProducto = '" + idPd + "'");
                    ps.executeUpdate();
                    
                    infoProducto();
                    consultaGeneralEV();
                    consultaGeneralDV();
                    limpiarDV();
                    JOptionPane.showMessageDialog(null, "SE HA ACTUALIZADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e, "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "SE HAN DESCARTADO LAS MODIFICACIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                consultarEV();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultarDV(){
        String idV, idPd;
        int cant;
        idV = String.valueOf(jCBIdV.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());
        
        if(registroExiste(idV+idPd, "IdVenta+IdProducto", "DetalleVentas")){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from DetalleVentas where IdVenta+IdProducto = '" + idV + idPd + "'");

                rs.next();
                jTxtFieldPrecio.setText(Double.toString(rs.getDouble(3)));
                jTxtFieldDesc.setText(Integer.toString(4));
                cant = rs.getInt(5);
                stock = cant;
                jTxtFieldCant.setText(Integer.toString(cant));
                jTxtFieldSubtotal.setText(Double.toString(rs.getDouble(6)));
                inicializarArreglo(txtField2, true);
                calcularSubtotal();
                bandConsultaDV = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultaGeneralDV(){
        String colum[] = {"IdVenta", "IdProducto", "Precio", "Descuento(%)", "Cantidad", "Subtotal"};
        DefaultTableModel tDV = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        Object datos[] = new Object[6];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from DetalleVentas");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getDouble(3);
                datos[3] = rs.getInt(4);
                datos[4] = rs.getInt(5);
                datos[5] = rs.getDouble(6);
                tDV.addRow(datos);
            }
            
            tablaDetalleV.setModel(tDV);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void consultarDetalles(){
        String colum[] = {"IdVenta", "IdProducto", "Marca", "Modelo", "Precio", "Desc(%)", "Cant", "Subtotal"};
        DefaultTableModel tdetalles = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        String idV = jTxtFieldIdV.getText();
        Object datos[] = new Object[8];
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdVenta, Productos.IdProducto, Marca, Modelo, PrecioV, Descuento, Cant, SubtotalV from DetalleVentas, Productos "
                    + "where IdVenta = '" + idV + "' and DetalleVentas.IdProducto = Productos.IdProducto");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getDouble(5);
                datos[5] = rs.getInt(6);
                datos[6] = rs.getInt(7);
                datos[7] = rs.getDouble(8);
                tdetalles.addRow(datos);
            }
            
            tablaDetalles.setModel(tdetalles);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizarTotal(String idV){
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select sum(SubtotalV) from DetalleVentas where IdVenta = '" + idV + "'");
            rs.next();
            total = rs.getDouble(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public boolean camposObligatoriosEC(){
        return !(jTxtFieldIdV.getText().isEmpty() ||  jTxtFieldAnio.getText().isEmpty()
                || jTxtFieldMes.getText().isEmpty() || jTxtFieldDia.getText().isEmpty());
    }
   
    public boolean camposObligatoriosDC(){
        return !(jTxtFieldDesc.getText().isEmpty() || jTxtFieldCant.getText().isEmpty());
    }
      
    public boolean camposValidos(boolean a[]){
        for(int i = 0; i < a.length; i++)
            if(!a[i])
                return false;
        return true;
    }
    
    public void inicializarArreglo(boolean a[], boolean value){
        for(int i = 0; i < a.length; i++)
            a[i] = value;
    }
    
    public boolean registroExiste(String id, String atrib, String tabla){
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select " + atrib + " from " + tabla + " where " + atrib + " = '" + id + "'");
            rs.next();
            return !(rs.getString(1).equals(null));
            
        } catch (SQLException e) {
            return false;
        }     
    }
    
    public void calcularSubtotal(){
        double precioV;
        int desc, cant;
        desc = Integer.parseInt(jTxtFieldDesc.getText());
        cant = Integer.parseInt(jTxtFieldCant.getText());
        precioV = precio * ((100.0-desc)/100.0);
        precioV = Math.round(precioV*100.0)/100.0;
        jTxtFieldPrecio.setText(Double.toString(precioV));
        subtotal = precioV * cant;
        subtotal = precioV = Math.round(subtotal*100.0)/100.0;
        jTxtFieldSubtotal.setText(Double.toString(subtotal));
    }
    
    public void nomCliente(){
        try {
            String idCl;
            idCl = String.valueOf(jCBIdCl.getSelectedItem());
            
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select NomC, ApellidoC from Clientes where IdCliente = '"  + idCl + "'");
            rs.next();
            jTxtFieldNombreC.setText(rs.getString(1) + " " + rs.getString(2));   
        } catch (SQLException e) {
        }   
    }
    
    public void infoProducto(){
        try {
            String idPd;
            idPd = String.valueOf(jCBIdPd.getSelectedItem());
            
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select Marca, Modelo, Stock, Precio from Productos where IdProducto = '"  + idPd + "'");
            rs.next();
            jTxtFieldProducto.setText(rs.getString(1) + " " + rs.getString(2));
            jTxtFieldStock.setText(Integer.toString(rs.getInt(3))); 
            precio = (Double)(rs.getDouble(4));
        } catch (SQLException e) {
        }   
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jTxtFieldIdV = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTxtFieldAnio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTxtFieldTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalleV = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonLimpiarEC = new javax.swing.JButton();
        jTxtFieldDia = new javax.swing.JTextField();
        jTxtFieldMes = new javax.swing.JTextField();
        jButtonDetalles = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jTxtFieldProducto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTxtFieldPrecio = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTxtFieldSubtotal = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButtonLimpiarDC = new javax.swing.JButton();
        jButtonCerrar = new javax.swing.JButton();
        jTxtFieldCant = new javax.swing.JTextField();
        jButtonConsultar = new javax.swing.JButton();
        jButtonModificar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonAgregar = new javax.swing.JButton();
        jButtonConsultar2 = new javax.swing.JButton();
        jButtonModificar2 = new javax.swing.JButton();
        jButtonEliminar2 = new javax.swing.JButton();
        jButtonAgregar2 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jTxtFieldStock = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTxtFieldNombreC = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCBIdCl = new javax.swing.JComboBox<>();
        jCBIdV = new javax.swing.JComboBox<>();
        jCBIdPd = new javax.swing.JComboBox<>();
        encabezadoVentas = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaEncabezadoV = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jTxtFieldDesc = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1120, 732));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("IdVenta:");

        jTxtFieldIdV.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldIdV.setToolTipText("5 Caracteres (A-Z, 1-9)");
        jTxtFieldIdV.setNextFocusableComponent(jCBIdCl);
        jTxtFieldIdV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldIdVKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("IdCliente:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Día/Mes/Año:");

        jTxtFieldAnio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldAnio.setToolTipText("aaaa");
        jTxtFieldAnio.setNextFocusableComponent(jButtonAgregar);
        jTxtFieldAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldAnioKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Total:");

        jTxtFieldTotal.setEditable(false);
        jTxtFieldTotal.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldTotal.setText("0");
        jTxtFieldTotal.setToolTipText("");
        jTxtFieldTotal.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("--- ENCABEZADO VENTAS ---");

        tablaDetalleV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdCompra", "IdProducto", "Precio", "Desc(%)", "Cant", "Subtotal"
            }
        ));
        tablaDetalleV.setGridColor(new java.awt.Color(255, 255, 255));
        tablaDetalleV.setRequestFocusEnabled(false);
        tablaDetalleV.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaDetalleV);

        jButtonLimpiarEC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonLimpiarEC.setText("Limpiar");
        jButtonLimpiarEC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarECActionPerformed(evt);
            }
        });

        jTxtFieldDia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldDia.setToolTipText("dd");
        jTxtFieldDia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldDiaKeyReleased(evt);
            }
        });

        jTxtFieldMes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldMes.setToolTipText("mm");
        jTxtFieldMes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldMesKeyReleased(evt);
            }
        });

        jButtonDetalles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonDetalles.setText("Ver detalles...");
        jButtonDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetallesActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("--- DETALLE VENTAS ---");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("IdVenta:");

        jTxtFieldProducto.setEditable(false);
        jTxtFieldProducto.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldProducto.setToolTipText("5 Caracteres (A-Z, 1-9)");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Precio:");

        jTxtFieldPrecio.setEditable(false);
        jTxtFieldPrecio.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldPrecio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Subtotal:");

        jTxtFieldSubtotal.setEditable(false);
        jTxtFieldSubtotal.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldSubtotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldSubtotal.setText("0");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("IdProducto:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Cant:");

        jButtonLimpiarDC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonLimpiarDC.setText("Limpiar");
        jButtonLimpiarDC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarDCActionPerformed(evt);
            }
        });

        jButtonCerrar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonCerrar.setText("Cerrar");
        jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarActionPerformed(evt);
            }
        });

        jTxtFieldCant.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldCant.setToolTipText("1-99999");
        jTxtFieldCant.setNextFocusableComponent(jButtonAgregar2);
        jTxtFieldCant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldCantKeyReleased(evt);
            }
        });

        jButtonConsultar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonConsultar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonConsultar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConsultar.setText("Consultar");
        jButtonConsultar.setNextFocusableComponent(jCBIdV);
        jButtonConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultarActionPerformed(evt);
            }
        });

        jButtonModificar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonModificar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonModificar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar.setText("Modificar");
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jButtonEliminar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jButtonAgregar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAgregar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        jButtonConsultar2.setBackground(new java.awt.Color(0, 51, 102));
        jButtonConsultar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonConsultar2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConsultar2.setText("Consultar");
        jButtonConsultar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultar2ActionPerformed(evt);
            }
        });

        jButtonModificar2.setBackground(new java.awt.Color(0, 51, 102));
        jButtonModificar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonModificar2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar2.setText("Modificar");
        jButtonModificar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificar2ActionPerformed(evt);
            }
        });

        jButtonEliminar2.setBackground(new java.awt.Color(0, 51, 102));
        jButtonEliminar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonEliminar2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminar2.setText("Eliminar");
        jButtonEliminar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminar2ActionPerformed(evt);
            }
        });

        jButtonAgregar2.setBackground(new java.awt.Color(0, 51, 102));
        jButtonAgregar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAgregar2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregar2.setText("Agregar");
        jButtonAgregar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregar2ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setText("Producto:");

        jTxtFieldStock.setEditable(false);
        jTxtFieldStock.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldStock.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldStock.setToolTipText("5 Caracteres (A-Z, 1-9)");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("Stock:");

        jTxtFieldNombreC.setEditable(false);
        jTxtFieldNombreC.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldNombreC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldNombreC.setToolTipText("5 Caracteres (A-Z, 1-9)");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Cliente:");

        jCBIdCl.setNextFocusableComponent(jTxtFieldDia);
        jCBIdCl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBIdClActionPerformed(evt);
            }
        });

        jCBIdPd.setNextFocusableComponent(jTxtFieldPrecio);
        jCBIdPd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBIdPdActionPerformed(evt);
            }
        });

        tablaEncabezadoV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdVenta", "IdCliente", "Dia", "Mes", "Año", "Total"
            }
        ));
        tablaEncabezadoV.setGridColor(new java.awt.Color(255, 255, 255));
        tablaEncabezadoV.setRequestFocusEnabled(false);
        tablaEncabezadoV.getTableHeader().setReorderingAllowed(false);
        tablaEncabezadoV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEncabezadoVMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaEncabezadoV);

        encabezadoVentas.addTab("EncabezadoVentas", jScrollPane3);

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdCompra", "IdProducto", "Marca", "Modelo", "Precio", "Desc(%)", "Cant", "Subtotal"
            }
        ));
        tablaDetalles.setGridColor(new java.awt.Color(255, 255, 255));
        tablaDetalles.setRequestFocusEnabled(false);
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tablaDetalles);

        encabezadoVentas.addTab("Detalles", jScrollPane5);

        jTxtFieldDesc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldDesc.setToolTipText("0-100");
        jTxtFieldDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldDescKeyReleased(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("Desc%:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(encabezadoVentas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(195, 195, 195)
                            .addComponent(jButtonAgregar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonModificar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonConsultar))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonLimpiarEC, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(jButtonDetalles))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTxtFieldIdV, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel2)
                                                .addGap(18, 18, 18)
                                                .addComponent(jCBIdCl, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTxtFieldNombreC, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTxtFieldDia, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTxtFieldMes, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTxtFieldAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTxtFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(77, 77, 77)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButtonLimpiarDC, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButtonAgregar2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonEliminar2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonModificar2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonConsultar2))
                                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(2, 2, 2))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldSubtotal)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTxtFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel22)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jTxtFieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTxtFieldProducto)
                                            .addComponent(jCBIdV, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel21)
                                            .addComponent(jLabel20))
                                        .addGap(8, 8, 8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTxtFieldCant, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTxtFieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jCBIdPd, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addGap(92, 92, 92))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBIdCl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jTxtFieldIdV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldNombreC, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtFieldDia, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtFieldMes, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonLimpiarEC)
                            .addComponent(jButtonDetalles)
                            .addComponent(jButtonCerrar))
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAgregar)
                            .addComponent(jButtonEliminar)
                            .addComponent(jButtonModificar)
                            .addComponent(jButtonConsultar)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jCBIdV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBIdPd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(jTxtFieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel22)
                                .addComponent(jTxtFieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTxtFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16)
                                .addComponent(jLabel20)
                                .addComponent(jTxtFieldCant, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(20, 20, 20)
                        .addComponent(jButtonLimpiarDC)
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAgregar2)
                            .addComponent(jButtonEliminar2)
                            .addComponent(jButtonModificar2)
                            .addComponent(jButtonConsultar2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(encabezadoVentas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLimpiarECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarECActionPerformed
        // TODO add your handling code here:
        limpiarEV();
    }//GEN-LAST:event_jButtonLimpiarECActionPerformed

    private void jButtonDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetallesActionPerformed
        // TODO add your handling code here:
        if(bandConsultaEV){
            encabezadoVentas.setSelectedIndex(1);
            consultarDetalles();
        }
    }//GEN-LAST:event_jButtonDetallesActionPerformed

    private void jButtonLimpiarDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarDCActionPerformed
        // TODO add your handling code here:
        limpiarDV();
    }//GEN-LAST:event_jButtonLimpiarDCActionPerformed

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        // TODO add your handling code here:
        Paneles.g.removePanel();
        Paneles.g.auxV = 0;
    }//GEN-LAST:event_jButtonCerrarActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if(jCBIdCl.getItemCount() > 0)
            if(camposObligatoriosEC()) // Si los campos obligatorios estan llenados
                if(camposValidos(txtField)) // Si todos los campos son validos
                    agregarEV();
                else
                    JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);    
            else
                JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY CLIENTES REGISTRADOS", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoVentas.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if(jCBIdCl.getItemCount() > 0)
            if(jTxtFieldIdV.getText().isEmpty() || !txtField[0])
                jTxtFieldIdV.requestFocus();
            else
                if(bandConsultaEV)
                    eliminarEV();
                else
                    consultarEV();
        else
            JOptionPane.showMessageDialog(null, "NO HAY CLIENTES REGISTRADOS", 
            "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoVentas.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        // TODO add your handling code here:
        if(jCBIdCl.getItemCount() > 0)
            if(jTxtFieldIdV.getText().isEmpty() || !txtField[0])
                jTxtFieldIdV.requestFocus();
            else
                if(bandConsultaEV)
                    if(camposObligatoriosEC())  // Si los campos obligatorios estan llenados
                        if(camposValidos(txtField))  // Si todos los campos son validos
                            modificarEV();
                        else
                            JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);              
                    else
                        JOptionPane.showMessageDialog(null, "Se deben llenar los \ncampos obligatorios (*)", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                else
                    consultarEV();
        else
            JOptionPane.showMessageDialog(null, "NO HAY CLIENTES REGISTRADOS", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoVentas.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jButtonConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultarActionPerformed
        // TODO add your handling code here:
        if(jCBIdCl.getItemCount() > 0)
            if(jTxtFieldIdV.getText().isEmpty() || !txtField[0])
                jTxtFieldIdV.requestFocus();
            else
                consultarEV();
        else
            JOptionPane.showMessageDialog(null, "NO HAY CLIENTES REGISTRADOS", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoVentas.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonConsultarActionPerformed

    private void jButtonAgregar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdV.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                if(camposObligatoriosDC())       // Si los campos obligatorios estan llenados
                    if(camposValidos(txtField2))  // Si todos los campos son validos
                        agregarDV();
                    else
                        JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos", 
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY VENTAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonAgregar2ActionPerformed

    private void jButtonEliminar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdV.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                if(bandConsultaDV)
                    eliminarDV();
                else
                    consultarDV();
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY VENTAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
   
    }//GEN-LAST:event_jButtonEliminar2ActionPerformed

    private void jButtonModificar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdV.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                if(bandConsultaDV)
                    if(camposObligatoriosDC())  // Si los campos obligatorios estan llenados
                        if(camposValidos(txtField2))  // Si todos los campos son validos
                            modificarDV();
                        else
                            JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);              
                    else
                        JOptionPane.showMessageDialog(null, "Se deben llenar los todos los campos", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                else
                    consultarDV();
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY VENTAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
             
    }//GEN-LAST:event_jButtonModificar2ActionPerformed

    private void jButtonConsultar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdV.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                consultarDV();
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY VENTAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonConsultar2ActionPerformed

    private void jTxtFieldIdVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldIdVKeyReleased
        // TODO add your handling code here:
        txtField[0] = Validar.validarClave(jTxtFieldIdV.getText());
        if(!txtField[0])
            jTxtFieldIdV.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldIdV.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldIdVKeyReleased

    private void jTxtFieldAnioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldAnioKeyReleased
        // TODO add your handling code here:
        txtField[3] = Validar.validarAnio(jTxtFieldAnio.getText());
        if(!txtField[3])
            jTxtFieldAnio.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldAnio.setBackground(Color.WHITE);
        if(!jTxtFieldDia.getText().isEmpty())
            jTxtFieldDiaKeyReleased(evt);
    }//GEN-LAST:event_jTxtFieldAnioKeyReleased

    private void jTxtFieldMesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldMesKeyReleased
        // TODO add your handling code here:
        txtField[2] = Validar.validarMes(jTxtFieldMes.getText());
        if(!txtField[2])
            jTxtFieldMes.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldMes.setBackground(Color.WHITE);
        if(!jTxtFieldDia.getText().isEmpty())
            jTxtFieldDiaKeyReleased(evt);
    }//GEN-LAST:event_jTxtFieldMesKeyReleased

    private void jTxtFieldDiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldDiaKeyReleased
        // TODO add your handling code here:
        if(txtField[2] && txtField[3]){ // Si el año y mes estan validados
            txtField[1] = Validar.validarDia(jTxtFieldDia.getText(), jTxtFieldMes.getText(), jTxtFieldAnio.getText());
            if(!txtField[1])
                jTxtFieldDia.setBackground(Color.LIGHT_GRAY);
            else
                jTxtFieldDia.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jTxtFieldDiaKeyReleased

    private void jTxtFieldCantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldCantKeyReleased
        // TODO add your handling code here:
        txtField2[1] = Validar.validarCant(jTxtFieldCant.getText(), 1, 99999);
        if(!txtField2[1])
            jTxtFieldCant.setBackground(Color.LIGHT_GRAY);
        else{
            if(txtField2[0])
                calcularSubtotal();
            jTxtFieldCant.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jTxtFieldCantKeyReleased

    private void tablaEncabezadoVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEncabezadoVMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaEncabezadoV.rowAtPoint(evt.getPoint());
        jTxtFieldIdV.setText(String.valueOf(tablaEncabezadoV.getValueAt(seleccion, 0)));
        jCBIdCl.setSelectedItem(String.valueOf(tablaEncabezadoV.getValueAt(seleccion, 1)));
        jTxtFieldDia.setText(String.valueOf(tablaEncabezadoV.getValueAt(seleccion, 2)));
        jTxtFieldMes.setText(String.valueOf(tablaEncabezadoV.getValueAt(seleccion, 3)));
        jTxtFieldAnio.setText(String.valueOf(tablaEncabezadoV.getValueAt(seleccion, 4)));
        jTxtFieldTotal.setText(String.valueOf(tablaEncabezadoV.getValueAt(seleccion, 5)));
        inicializarArreglo(txtField, true);
        bandConsultaEV = true;
    }//GEN-LAST:event_tablaEncabezadoVMouseClicked

    private void jCBIdClActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBIdClActionPerformed
        // TODO add your handling code here:
        nomCliente();
    }//GEN-LAST:event_jCBIdClActionPerformed

    private void jCBIdPdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBIdPdActionPerformed
        // TODO add your handling code here:
        infoProducto();
    }//GEN-LAST:event_jCBIdPdActionPerformed

    private void jTxtFieldDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldDescKeyReleased
        // TODO add your handling code here:
        txtField2[0] = Validar.validarDesc(jTxtFieldDesc.getText());
        if(!txtField2[0])
            jTxtFieldDesc.setBackground(Color.LIGHT_GRAY);
        else{
            if(txtField2[1])
                calcularSubtotal();
            jTxtFieldDesc.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jTxtFieldDescKeyReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane encabezadoVentas;
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonAgregar2;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonConsultar2;
    private javax.swing.JButton jButtonDetalles;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonEliminar2;
    private javax.swing.JButton jButtonLimpiarDC;
    private javax.swing.JButton jButtonLimpiarEC;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonModificar2;
    private javax.swing.JComboBox<String> jCBIdCl;
    private javax.swing.JComboBox<String> jCBIdPd;
    private javax.swing.JComboBox<String> jCBIdV;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextField jTxtFieldAnio;
    private javax.swing.JTextField jTxtFieldCant;
    private javax.swing.JTextField jTxtFieldDesc;
    private javax.swing.JTextField jTxtFieldDia;
    private javax.swing.JTextField jTxtFieldIdV;
    private javax.swing.JTextField jTxtFieldMes;
    private javax.swing.JTextField jTxtFieldNombreC;
    private javax.swing.JTextField jTxtFieldPrecio;
    private javax.swing.JTextField jTxtFieldProducto;
    private javax.swing.JTextField jTxtFieldStock;
    private javax.swing.JTextField jTxtFieldSubtotal;
    private javax.swing.JTextField jTxtFieldTotal;
    private javax.swing.JTable tablaDetalleV;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTable tablaEncabezadoV;
    // End of variables declaration//GEN-END:variables
}