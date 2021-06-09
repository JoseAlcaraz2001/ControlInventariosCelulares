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
public class Compras extends javax.swing.JPanel {
    private boolean txtField[] = new boolean[4];
    private boolean txtField2[] = new boolean[2];
    private boolean bandConsultaEC = false;
    private boolean bandConsultaDC = false;
    private double subtotal = 0, total = 0;
    private int stock = 0;
    
    public Compras() {
        initComponents();
        /*
        idPv();
        idC();
        idPd();
        nomProveedor();
        infoProducto();
        consultaGeneralEC();
        consultaGeneralDC(); 
        */
    }
    
    public void idPv(){    
        DefaultComboBoxModel pv = new DefaultComboBoxModel();
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdProveedor from Proveedores");
            
            while(rs.next())
                pv.addElement(rs.getString(1));
            
            jCBIdPv.setModel(pv);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void idC(){    
        DefaultComboBoxModel c = new DefaultComboBoxModel();
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdCompra from EncabezadoCompras");
            
            while(rs.next())
                c.addElement(rs.getString(1));
            
            jCBIdC.setModel(c);
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
    
    public void limpiarEC(){
        jTxtFieldIdC.setText(null);
        jTxtFieldAnio.setText(null);
        jTxtFieldMes.setText(null);
        jTxtFieldDia.setText(null);
        jTxtFieldTotal.setText("0");
        jTxtFieldIdC.setBackground(Color.WHITE);
        jTxtFieldAnio.setBackground(Color.WHITE);
        jTxtFieldMes.setBackground(Color.WHITE);
        jTxtFieldDia.setBackground(Color.WHITE);
        jTxtFieldIdC.requestFocus();
        bandConsultaEC = false;
        inicializarArreglo(txtField, false);
    }
    
    public void limpiarDC(){
        jTxtFieldCosto.setText(null);
        jTxtFieldUnidades.setText(null);
        jTxtFieldSubtotal.setText("0");
        jTxtFieldCosto.setBackground(Color.WHITE);
        jTxtFieldUnidades.setBackground(Color.WHITE);
        bandConsultaDC = false;
        inicializarArreglo(txtField2, false);
    }
    
    public void agregarEC(){
        String idC, idPv, dia, mes, anio;
        double total = 0;
        
        idC = jTxtFieldIdC.getText();
        
        if(!registroExiste(idC, "IdCompra", "EncabezadoCompras")){
            idPv = String.valueOf(jCBIdPv.getSelectedItem());
            try {
                dia = jTxtFieldDia.getText();
                mes = jTxtFieldMes.getText();
                anio  = jTxtFieldAnio.getText();
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into EncabezadoCompras values ('" + idC + "','" + idPv + "','" 
                                                                       + dia + "','" + mes + "','" + anio + "'," + total + ")");
                ps.executeUpdate();
                idC();
                consultaGeneralEC();
                limpiarEC();
                JOptionPane.showMessageDialog(null, "SE HAN REGISTRADO LOS DATOS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e, "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "YA EXISTE UN REGISTRO\nCON LA CLAVE INTRODUCIDA.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void eliminarEC(){
        String idC = jTxtFieldIdC.getText();
        int i = 1;
        
        if(registroExiste(idC, "IdCompra", "EncabezadoCompras")){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?\nNota: Se eliminaran los registros asociados.", "Aviso",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    Statement leer = Paneles.conect.createStatement();
                    ResultSet rs = leer.executeQuery("select Unidades, IdProducto from DetalleCompras where IdCompra = '" + idC + "'");
                    PreparedStatement ps;
                    
                    while(rs.next()){
                        ps = Paneles.conect.prepareStatement("update Productos set Stock -= " + rs.getInt(i++) + " where IdProducto = '" + rs.getString(i) + "'");
                        ps.executeUpdate();
                        i = 1;
                    }
                    ps = Paneles.conect.prepareStatement("delete from DetalleCompras where IdCompra = '" + idC +  "'");
                    ps.executeUpdate();
                    ps = Paneles.conect.prepareStatement("delete from EncabezadoCompras where IdCompra = '" + idC + "'");
                    ps.executeUpdate();
                    idC();
                    consultaGeneralEC();
                    consultaGeneralDC();
                    limpiarEC();
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
    
    public void modificarEC(){
        String idC, idPv, dia, mes, anio;
        
        idC = jTxtFieldIdC.getText();
        
        if(registroExiste(idC, "IdCompra", "EncabezadoCompras")){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(opcion == JOptionPane.YES_OPTION){
                idPv = String.valueOf(jCBIdPv.getSelectedItem());
                dia = jTxtFieldDia.getText();
                mes = jTxtFieldMes.getText();
                anio  = jTxtFieldAnio.getText();
                
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("update EncabezadoCompras set IdProveedor = '" + idPv + "', DiaC = '" + dia
                                           + "', MesC = '" + mes + "', AnioC = '" + anio + "' where IdCompra = '" + idC + "'");
                    ps.executeUpdate();                   
                    consultaGeneralEC();
                    limpiarEC();
                    JOptionPane.showMessageDialog(null, "SE HA ACTUALIZADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "SE HAN DESCARTADO LAS MODIFICACIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                consultarEC();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultarEC(){
        String idC = jTxtFieldIdC.getText();
        
        if(registroExiste(idC, "IdCompra", "EncabezadoCompras")){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from EncabezadoCompras where IdCompra = '" + idC + "'");

                rs.next();
                jCBIdPv.setSelectedItem(rs.getString(2));
                jTxtFieldDia.setText(rs.getString(3));
                jTxtFieldMes.setText(rs.getString(4));
                jTxtFieldAnio.setText(rs.getString(5));
                jTxtFieldTotal.setText(Double.toString(rs.getDouble(6)));
                inicializarArreglo(txtField, true);
                bandConsultaEC = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultaGeneralEC(){
        String colum[] = {"IdCompra", "IdProveedor", "Dia", "Mes", "Año", "Total"};
        DefaultTableModel tEC = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        Object datos[] = new Object[6];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from EncabezadoCompras");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getDouble(6);
                tEC.addRow(datos);
            }
            
            tablaEncabezadoC.setModel(tEC);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void agregarDC(){
        String idC, idPd;
        double costo;
        int unid;
        
        idC = String.valueOf(jCBIdC.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());;
        
        if(!registroExiste(idC+idPd, "IdCompra+IdProducto", "DetalleCompras")){
            costo = Double.parseDouble(jTxtFieldCosto.getText());
            unid = Integer.parseInt(jTxtFieldUnidades.getText());
            try {
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into DetalleCompras values ('" + idC + "','" + idPd + "'," 
                                                                       + costo + "," + unid + "," + subtotal + ")");
                ps.executeUpdate();
                actualizarTotal(idC);
                ps = Paneles.conect.prepareStatement("update EncabezadoCompras set TotalC = " + total + " where IdCompra = '" + idC + "'");
                ps.executeUpdate();
                ps = Paneles.conect.prepareStatement("update Productos set Stock += " + unid + " where IdProducto = '" + idPd + "'");
                ps.executeUpdate();
                
                infoProducto();
                consultaGeneralEC();
                consultaGeneralDC();
                limpiarDC();
                JOptionPane.showMessageDialog(null, "SE HAN REGISTRADO LOS DATOS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "YA EXISTE UN REGISTRO\nCON LA CLAVE INTRODUCIDA.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void eliminarDC(){
        String idC, idPd;
        idC = String.valueOf(jCBIdC.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());;
        
        if(registroExiste(idC+idPd, "IdCompra+IdProducto", "DetalleCompras")){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?", "Aviso",
                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("update Productos set Stock -= " + stock + " where IdProducto = '" + idPd + "'");
                    ps.executeUpdate();
                    ps = Paneles.conect.prepareStatement("delete from DetalleCompras where IdCompra+IdProducto = '" + idC + idPd + "'");
                    ps.executeUpdate();
                    actualizarTotal(idC);
                    ps = Paneles.conect.prepareStatement("update EncabezadoCompras set TotalC = " + total + " where IdCompra = '" + idC + "'");
                    ps.executeUpdate();
                    
                    infoProducto();
                    consultaGeneralEC(); 
                    consultaGeneralDC();
                    limpiarDC();
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
    
    public void modificarDC(){
        String idC, idPd;
        double costo;
        int unid;
        
        idC = String.valueOf(jCBIdC.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());
        
        if(registroExiste(idC+idPd, "IdCompra+IdProducto", "DetalleCompras")){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(opcion == JOptionPane.YES_OPTION){
                costo = Double.parseDouble(jTxtFieldCosto.getText());
                unid = Integer.parseInt(jTxtFieldUnidades.getText());
                calcularSubtotal();

                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("update DetalleCompras set Costo = " + costo 
                                           + ", Unidades = " + unid + ", SubtotalC = " + subtotal + " where IdCompra+IdProducto = '" + idC+idPd + "'");
                    ps.executeUpdate();
                    actualizarTotal(idC);
                    ps = Paneles.conect.prepareStatement("update EncabezadoCompras set TotalC = " + total + " where IdCompra = '" + idC + "'");
                    ps.executeUpdate();         
                    stock = unid - stock;
                    ps = Paneles.conect.prepareStatement("update Productos set Stock += " + stock + " where IdProducto = '" + idPd + "'");
                    ps.executeUpdate();
                    
                    infoProducto();
                    consultaGeneralEC();
                    consultaGeneralDC();
                    limpiarDC();
                    JOptionPane.showMessageDialog(null, "SE HA ACTUALIZADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e, "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "SE HAN DESCARTADO LAS MODIFICACIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                consultarEC();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultarDC(){
        String idC, idPd;
        int unid;
        idC = String.valueOf(jCBIdC.getSelectedItem());
        idPd = String.valueOf(jCBIdPd.getSelectedItem());
        
        if(registroExiste(idC+idPd, "IdCompra+IdProducto", "DetalleCompras")){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from DetalleCompras where IdCompra+IdProducto = '" + idC + idPd + "'");

                rs.next();
                jTxtFieldCosto.setText(Double.toString(rs.getDouble(3)));
                unid = rs.getInt(4);
                stock = unid;
                jTxtFieldUnidades.setText(Integer.toString(unid));
                jTxtFieldSubtotal.setText(Double.toString(rs.getDouble(5)));
                inicializarArreglo(txtField2, true);
                bandConsultaDC = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultaGeneralDC(){
        String colum[] = {"IdCompra", "IdProducto", "Costo", "Unidades", "Subtotal"};
        DefaultTableModel tDC = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        Object datos[] = new Object[5];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from DetalleCompras");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getDouble(3);
                datos[3] = rs.getInt(4);
                datos[4] = rs.getDouble(5);
                tDC.addRow(datos);
            }
            
            tablaDetalleC.setModel(tDC);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void consultarDetalles(){
        String colum[] = {"IdCompra", "IdProducto", "Marca", "Modelo", "Costo", "Unidades", "Subtotal"};
        DefaultTableModel tdetalles = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        String idC = jTxtFieldIdC.getText();
        Object datos[] = new Object[7];
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdCompra, Productos.IdProducto, Marca, Modelo, Costo, Unidades, SubtotalC from DetalleCompras, Productos "
                    + "where IdCompra = '" + idC + "' and DetalleCompras.IdProducto = Productos.IdProducto");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getDouble(5);
                datos[5] = rs.getInt(6);
                datos[6] = rs.getDouble(7);
                tdetalles.addRow(datos);
            }
            
            tablaDetalles.setModel(tdetalles);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizarTotal(String idC){
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select sum(SubtotalC) from DetalleCompras where IdCompra = '" + idC + "'");
            rs.next();
            total = rs.getDouble(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public boolean camposObligatoriosEC(){
        return !(jTxtFieldIdC.getText().isEmpty() ||  jTxtFieldAnio.getText().isEmpty()
                || jTxtFieldMes.getText().isEmpty() || jTxtFieldDia.getText().isEmpty());
    }
   
    public boolean camposObligatoriosDC(){
        return !(jTxtFieldCosto.getText().isEmpty() || jTxtFieldUnidades.getText().isEmpty());
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
        double costo;
        int unidades;
        costo = Double.parseDouble(jTxtFieldCosto.getText());
        unidades = Integer.parseInt(jTxtFieldUnidades.getText());
        subtotal = costo * unidades;
        jTxtFieldSubtotal.setText(Double.toString(subtotal));
    }
    
    public void nomProveedor(){
        try {
            String idPv;
            idPv = String.valueOf(jCBIdPv.getSelectedItem());
            
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select NomP, ApellidoP from Proveedores where IdProveedor = '"  + idPv + "'");
            rs.next();
            jTxtFieldNombreP.setText(rs.getString(1) + " " + rs.getString(2));   
        } catch (SQLException e) {
        }   
    }
    
    public void infoProducto(){
        try {
            String idPd;
            idPd = String.valueOf(jCBIdPd.getSelectedItem());
            
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select Marca, Modelo, Stock from Productos where IdProducto = '"  + idPd + "'");
            rs.next();
            jTxtFieldProducto.setText(rs.getString(1) + " " + rs.getString(2));
            jTxtFieldStock.setText(Integer.toString(rs.getInt(3)));       
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
        jTxtFieldIdC = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTxtFieldAnio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTxtFieldTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalleC = new javax.swing.JTable();
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
        jTxtFieldCosto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTxtFieldSubtotal = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButtonLimpiarDC = new javax.swing.JButton();
        jButtonCerrar = new javax.swing.JButton();
        jTxtFieldUnidades = new javax.swing.JTextField();
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
        jTxtFieldNombreP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCBIdPv = new javax.swing.JComboBox<>();
        jCBIdC = new javax.swing.JComboBox<>();
        jCBIdPd = new javax.swing.JComboBox<>();
        encabezadoCompras = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaEncabezadoC = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1120, 732));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("IdCompra:");

        jTxtFieldIdC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldIdC.setToolTipText("5 Caracteres (A-Z, 1-9)");
        jTxtFieldIdC.setNextFocusableComponent(jCBIdPv);
        jTxtFieldIdC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldIdCKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("IdProveedor:");

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
        jLabel1.setText("--- ENCABEZADO COMPRAS ---");

        tablaDetalleC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdCompra", "IdProducto", "Costo", "Unidades", "Subtotal"
            }
        ));
        tablaDetalleC.setGridColor(new java.awt.Color(255, 255, 255));
        tablaDetalleC.setRequestFocusEnabled(false);
        tablaDetalleC.getTableHeader().setReorderingAllowed(false);
        tablaDetalleC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDetalleCMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaDetalleC);

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
        jLabel5.setText("--- DETALLE COMPRAS ---");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("IdCompra:");

        jTxtFieldProducto.setEditable(false);
        jTxtFieldProducto.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldProducto.setToolTipText("5 Caracteres (A-Z, 1-9)");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Costo($):");

        jTxtFieldCosto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldCostoKeyReleased(evt);
            }
        });

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
        jLabel20.setText("Unidades:");

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

        jTxtFieldUnidades.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldUnidades.setToolTipText("1-99999");
        jTxtFieldUnidades.setNextFocusableComponent(jButtonAgregar2);
        jTxtFieldUnidades.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldUnidadesKeyReleased(evt);
            }
        });

        jButtonConsultar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonConsultar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonConsultar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConsultar.setText("Consultar");
        jButtonConsultar.setNextFocusableComponent(jCBIdC);
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

        jTxtFieldNombreP.setEditable(false);
        jTxtFieldNombreP.setBackground(new java.awt.Color(234, 234, 234));
        jTxtFieldNombreP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTxtFieldNombreP.setToolTipText("5 Caracteres (A-Z, 1-9)");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Proveedor:");

        jCBIdPv.setNextFocusableComponent(jTxtFieldDia);
        jCBIdPv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBIdPvActionPerformed(evt);
            }
        });

        jCBIdPd.setNextFocusableComponent(jTxtFieldCosto);
        jCBIdPd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBIdPdActionPerformed(evt);
            }
        });

        tablaEncabezadoC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdCompra", "IdProveedor", "Dia", "Mes", "Año", "Total"
            }
        ));
        tablaEncabezadoC.setGridColor(new java.awt.Color(255, 255, 255));
        tablaEncabezadoC.setRequestFocusEnabled(false);
        tablaEncabezadoC.getTableHeader().setReorderingAllowed(false);
        tablaEncabezadoC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEncabezadoCMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaEncabezadoC);

        encabezadoCompras.addTab("EncabezadoCompras", jScrollPane3);

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdCompra", "IdProducto", "Marca", "Modelo", "Costo", "Unidades", "Subtotal"
            }
        ));
        tablaDetalles.setGridColor(new java.awt.Color(255, 255, 255));
        tablaDetalles.setRequestFocusEnabled(false);
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        tablaDetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDetallesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tablaDetalles);

        encabezadoCompras.addTab("Detalles", jScrollPane5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(encabezadoCompras, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel7)
                                                .addComponent(jLabel6))
                                            .addGap(29, 29, 29)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jTxtFieldIdC, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(45, 45, 45)
                                                    .addComponent(jLabel2)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jCBIdPv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addComponent(jTxtFieldNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
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
                                                .addComponent(jTxtFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))))))
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
                                            .addComponent(jTxtFieldProducto)
                                            .addComponent(jTxtFieldCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jCBIdC, 0, 174, Short.MAX_VALUE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel21)
                                                    .addComponent(jLabel18))
                                                .addGap(8, 8, 8))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel20)
                                                .addGap(18, 18, 18)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTxtFieldUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTxtFieldIdC, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCBIdPv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(jCBIdC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBIdPd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(jTxtFieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel20)
                            .addComponent(jTxtFieldUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(encabezadoCompras, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLimpiarECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarECActionPerformed
        // TODO add your handling code here:
        limpiarEC();
    }//GEN-LAST:event_jButtonLimpiarECActionPerformed

    private void jButtonDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetallesActionPerformed
        // TODO add your handling code here:
        if(bandConsultaEC){
            encabezadoCompras.setSelectedIndex(1);
            consultarDetalles();
        }
    }//GEN-LAST:event_jButtonDetallesActionPerformed

    private void jButtonLimpiarDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarDCActionPerformed
        // TODO add your handling code here:
        limpiarDC();
    }//GEN-LAST:event_jButtonLimpiarDCActionPerformed

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        // TODO add your handling code here:
        Paneles.g.removePanel();
        Paneles.g.auxC = 0;
    }//GEN-LAST:event_jButtonCerrarActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if(jCBIdPv.getItemCount() > 0)
            if(camposObligatoriosEC()) // Si los campos obligatorios estan llenados
                if(camposValidos(txtField)) // Si todos los campos son validos
                    agregarEC();
                else
                    JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);    
            else
                JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY PROVEEDORES REGISTRADOS", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoCompras.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if(jCBIdPv.getItemCount() > 0)
            if(jTxtFieldIdC.getText().isEmpty() || !txtField[0])
                jTxtFieldIdC.requestFocus();
            else
                if(bandConsultaEC)
                    eliminarEC();
                else
                    consultarEC();
        else
            JOptionPane.showMessageDialog(null, "NO HAY PROVEEDORES REGISTRADOS", 
            "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoCompras.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        // TODO add your handling code here:
        if(jCBIdPv.getItemCount() > 0)
            if(jTxtFieldIdC.getText().isEmpty() || !txtField[0])
                jTxtFieldIdC.requestFocus();
            else
                if(bandConsultaEC)
                    if(camposObligatoriosEC())  // Si los campos obligatorios estan llenados
                        if(camposValidos(txtField))  // Si todos los campos son validos
                            modificarEC();
                        else
                            JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);              
                    else
                        JOptionPane.showMessageDialog(null, "Se deben llenar los \ncampos obligatorios (*)", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                else
                    consultarEC();
        else
            JOptionPane.showMessageDialog(null, "NO HAY PROVEEDORES REGISTRADOS", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoCompras.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jButtonConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultarActionPerformed
        // TODO add your handling code here:
        if(jCBIdPv.getItemCount() > 0)
            if(jTxtFieldIdC.getText().isEmpty() || !txtField[0])
                jTxtFieldIdC.requestFocus();
            else
                consultarEC();
        else
            JOptionPane.showMessageDialog(null, "NO HAY PROVEEDORES REGISTRADOS", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        encabezadoCompras.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonConsultarActionPerformed

    private void jButtonAgregar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdC.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                if(camposObligatoriosDC())       // Si los campos obligatorios estan llenados
                    if(camposValidos(txtField2))  // Si todos los campos son validos
                        agregarDC();
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
            JOptionPane.showMessageDialog(null, "NO HAY COMPRAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonAgregar2ActionPerformed

    private void jButtonEliminar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdC.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                if(bandConsultaDC)
                    eliminarDC();
                else
                    consultarDC();
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY COMPRAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
   
    }//GEN-LAST:event_jButtonEliminar2ActionPerformed

    private void jButtonModificar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdC.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                if(bandConsultaDC)
                    if(camposObligatoriosDC())  // Si los campos obligatorios estan llenados
                        if(camposValidos(txtField2))  // Si todos los campos son validos
                            modificarDC();
                        else
                            JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);              
                    else
                        JOptionPane.showMessageDialog(null, "Se deben llenar los todos los campos", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                else
                    consultarDC();
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY COMPRAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
             
    }//GEN-LAST:event_jButtonModificar2ActionPerformed

    private void jButtonConsultar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultar2ActionPerformed
        // TODO add your handling code here:
        if(jCBIdC.getItemCount() > 0)
            if(jCBIdPd.getItemCount() > 0)
                consultarDC();
            else
                JOptionPane.showMessageDialog(null, "NO HAY PRODUCTOS REGISTRADOS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "NO HAY COMPRAS REGISTRADAS.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonConsultar2ActionPerformed

    private void jTxtFieldIdCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldIdCKeyReleased
        // TODO add your handling code here:
        txtField[0] = Validar.validarClave(jTxtFieldIdC.getText());
        if(!txtField[0])
            jTxtFieldIdC.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldIdC.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldIdCKeyReleased

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

    private void jTxtFieldUnidadesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldUnidadesKeyReleased
        // TODO add your handling code here:
        txtField2[1] = Validar.validarCant(jTxtFieldUnidades.getText(), 1, 99999);
        if(!txtField2[1])
            jTxtFieldUnidades.setBackground(Color.LIGHT_GRAY);
        else{
            if(txtField2[0])
                calcularSubtotal();
            jTxtFieldUnidades.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jTxtFieldUnidadesKeyReleased

    private void jTxtFieldCostoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldCostoKeyReleased
        // TODO add your handling code here:
        txtField2[0] = Validar.validarPrecio(jTxtFieldCosto.getText());
        if(!txtField2[0])
            jTxtFieldCosto.setBackground(Color.LIGHT_GRAY);
        else{
            if(txtField2[1])
                calcularSubtotal();
            jTxtFieldCosto.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jTxtFieldCostoKeyReleased

    private void tablaEncabezadoCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEncabezadoCMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaEncabezadoC.rowAtPoint(evt.getPoint());
        jTxtFieldIdC.setText(String.valueOf(tablaEncabezadoC.getValueAt(seleccion, 0)));
        jCBIdPv.setSelectedItem(String.valueOf(tablaEncabezadoC.getValueAt(seleccion, 1)));
        jTxtFieldDia.setText(String.valueOf(tablaEncabezadoC.getValueAt(seleccion, 2)));
        jTxtFieldMes.setText(String.valueOf(tablaEncabezadoC.getValueAt(seleccion, 3)));
        jTxtFieldAnio.setText(String.valueOf(tablaEncabezadoC.getValueAt(seleccion, 4)));
        jTxtFieldTotal.setText(String.valueOf(tablaEncabezadoC.getValueAt(seleccion, 5)));
        inicializarArreglo(txtField, true);
        bandConsultaEC = true;
    }//GEN-LAST:event_tablaEncabezadoCMouseClicked

    private void tablaDetalleCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDetalleCMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaDetalleC.rowAtPoint(evt.getPoint());
        jCBIdC.setSelectedItem(String.valueOf(tablaDetalleC.getValueAt(seleccion, 0)));
        jCBIdPd.setSelectedItem(String.valueOf(tablaDetalleC.getValueAt(seleccion, 1)));
        jTxtFieldCosto.setText(String.valueOf(tablaDetalleC.getValueAt(seleccion, 2)));
        jTxtFieldUnidades.setText(String.valueOf(tablaDetalleC.getValueAt(seleccion, 3)));
        stock = (Integer)(tablaDetalleC.getValueAt(seleccion, 3));
        jTxtFieldSubtotal.setText(String.valueOf(tablaDetalleC.getValueAt(seleccion, 4)));
        inicializarArreglo(txtField2, true);
        bandConsultaDC = true;
    }//GEN-LAST:event_tablaDetalleCMouseClicked

    private void tablaDetallesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDetallesMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaDetalles.rowAtPoint(evt.getPoint());
        jCBIdC.setSelectedItem(String.valueOf(tablaDetalles.getValueAt(seleccion, 0)));
        jCBIdPd.setSelectedItem(String.valueOf(tablaDetalles.getValueAt(seleccion, 1)));
        jTxtFieldCosto.setText(String.valueOf(tablaDetalles.getValueAt(seleccion, 4)));
        jTxtFieldUnidades.setText(String.valueOf(tablaDetalles.getValueAt(seleccion, 5)));
        stock = (Integer)(tablaDetalles.getValueAt(seleccion, 5));
        jTxtFieldSubtotal.setText(String.valueOf(tablaDetalles.getValueAt(seleccion, 6)));
        inicializarArreglo(txtField2, true);
        bandConsultaDC = true;
    }//GEN-LAST:event_tablaDetallesMouseClicked

    private void jCBIdPvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBIdPvActionPerformed
        // TODO add your handling code here:
        nomProveedor();
    }//GEN-LAST:event_jCBIdPvActionPerformed

    private void jCBIdPdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBIdPdActionPerformed
        // TODO add your handling code here:
        infoProducto();
    }//GEN-LAST:event_jCBIdPdActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane encabezadoCompras;
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
    private javax.swing.JComboBox<String> jCBIdC;
    private javax.swing.JComboBox<String> jCBIdPd;
    private javax.swing.JComboBox<String> jCBIdPv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JTextField jTxtFieldCosto;
    private javax.swing.JTextField jTxtFieldDia;
    private javax.swing.JTextField jTxtFieldIdC;
    private javax.swing.JTextField jTxtFieldMes;
    private javax.swing.JTextField jTxtFieldNombreP;
    private javax.swing.JTextField jTxtFieldProducto;
    private javax.swing.JTextField jTxtFieldStock;
    private javax.swing.JTextField jTxtFieldSubtotal;
    private javax.swing.JTextField jTxtFieldTotal;
    private javax.swing.JTextField jTxtFieldUnidades;
    private javax.swing.JTable tablaDetalleC;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTable tablaEncabezadoC;
    // End of variables declaration//GEN-END:variables
}