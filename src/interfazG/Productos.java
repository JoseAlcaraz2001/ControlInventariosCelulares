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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import objetos.Validar;
/**
 *
 * @author 52341
 */
public class Productos extends javax.swing.JPanel {
    Color c = new Color(234,234,234);
    private boolean band = true;
    private boolean bandConsulta = false;
    private boolean txtField[] = new boolean[5];
    private boolean stock = false;
    
    
    public Productos() {
        initComponents();
        inicializarArreglo();
        //consultaGeneral();  
    }
    
    public void limpiar(){
        jTxtFieldIdPd.setText(null);
        jTxtFieldMarca.setText(null);
        jTxtFieldModelo.setText(null);
        jTxtFieldPrecio.setText(null);
        jTxtFieldStock.setText(null);
        jTxtFieldIdPd.setBackground(Color.WHITE);
        jTxtFieldMarca.setBackground(Color.WHITE);
        jTxtFieldModelo.setBackground(Color.WHITE);
        jTxtFieldPrecio.setBackground(Color.WHITE);
        jTxtFieldStock.setBackground(Color.WHITE);
        jTxtFieldIdPd.requestFocus();
        inicializarArreglo();
        bandConsulta = false;
    }
    
    public void agregar(){
        String idPd, marca, modelo;
        double precio;
        int stock;
        idPd = jTxtFieldIdPd.getText();
        
        if(!registroExiste(idPd))
            try {
                marca = jTxtFieldMarca.getText();
                modelo = jTxtFieldModelo.getText();
                precio = Double.parseDouble(jTxtFieldPrecio.getText());
                stock = Integer.parseInt(jTxtFieldStock.getText());
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into Productos values ('" + idPd + "','" + marca + "','" 
                        + modelo + "','" + precio + "','" + stock + "')");
                ps.executeUpdate();
                consultaGeneral();
                limpiar();
                JOptionPane.showMessageDialog(null, "SE HAN REGISTRADO LOS DATOS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
            }
        else
            JOptionPane.showMessageDialog(null, "YA EXISTE UN REGISTRO\nCON LA CLAVE INTRODUCIDA.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void eliminar(){
        String idPd = jTxtFieldIdPd.getText();
        
        if(registroExiste(idPd)){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?", "Aviso",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("delete from Productos where IdProducto = '" + idPd + "'");
                    ps.executeUpdate();
                    consultaGeneral();
                    limpiar();
                    JOptionPane.showMessageDialog(null, "SE HA ELIMINADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "NO SE PUDO ELIMINAR, EL PRODUCTO HACE REFERENCIA A ALGUNA COMPRA O VENTA", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
               JOptionPane.showMessageDialog(null, "SE HA CANCELADO LA ELIMINACIÓN.", "Aviso", JOptionPane.INFORMATION_MESSAGE); 
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void modificar(){
        String idPd, marca, modelo;
        double precio;
        int stock;
        
        idPd = jTxtFieldIdPd.getText();
        
        if(registroExiste(idPd)){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(opcion == JOptionPane.YES_OPTION){
                    marca = jTxtFieldMarca.getText();
                    modelo = jTxtFieldModelo.getText();
                    precio = Double.parseDouble(jTxtFieldPrecio.getText());
                    stock = Integer.parseInt(jTxtFieldStock.getText());

                    try {
                        PreparedStatement ps = Paneles.conect.prepareStatement("update Productos set Marca = '" + marca + "', Modelo = '" + modelo 
                                               + "', Precio = '" + precio + "', Stock = '" + stock + "' where IdProducto = '" + idPd + "'");
                        ps.executeUpdate();
                        consultaGeneral();
                        limpiar();
                        JOptionPane.showMessageDialog(null, "SE HA ACTUALIZADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e) {
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "SE HAN DESCARTADO LAS MODIFICACIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    consultar();
                }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void consultar(){
        String idPd = jTxtFieldIdPd.getText();
        
        if(registroExiste(idPd)){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from Productos where IdProducto = '" + idPd + "'");

                rs.next();
                jTxtFieldMarca.setText(rs.getString(2));
                jTxtFieldModelo.setText(rs.getString(3));
                jTxtFieldPrecio.setText(Double.toString(rs.getDouble(4)));
                jTxtFieldStock.setText(Integer.toString(rs.getInt(5)));
                bandConsulta = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public boolean registroExiste(String idPd){
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdProducto from Productos where IdProducto = '" + idPd + "'");
            rs.next();
            return !(rs.getString(1).equals(null));
            
        } catch (SQLException e) {
            return false;
        }
        
    }
    
    public void consultaGeneral(){
        String colum[] = {"IdProducto", "Marca", "Modelo", "Precio", "Stock"};
        DefaultTableModel tprod = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        Object datos[] = new Object[5];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from Productos");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getDouble(4);
                datos[4] = rs.getInt(5);
                tprod.addRow(datos);
            }
            tablaProductos.setModel(tprod);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void consultaStock(){
        String colum[] = {"IdProducto", "Marca", "Modelo", "Precio", "Stock"};
        DefaultTableModel tstock = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        int cant = Integer.parseInt(jTxtFieldStock1.getText());
        String operador = String.valueOf(jCBOperador.getSelectedItem());
        
        Object datos[] = new Object[5];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from Productos where Stock " + operador + " " + cant);
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getDouble(4);
                datos[4] = rs.getInt(5);
                tstock.addRow(datos);
            }
            tablaStock.setModel(tstock);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public boolean camposObligatorios(){
        return !(jTxtFieldIdPd.getText().isEmpty() || jTxtFieldMarca.getText().isEmpty() 
                || jTxtFieldModelo.getText().isEmpty() || jTxtFieldPrecio.getText().isEmpty() || jTxtFieldStock.getText().isEmpty()); 
    }
    
    public boolean camposValidos(){
        for(int i = 0; i < txtField.length; i++)
            if(!txtField[i])
                return false;
        return true;
    }
    
    public void inicializarArreglo(){
        for(int i = 0; i < txtField.length; i++)
            txtField[i] = true;
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
        jTxtFieldIdPd = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTxtFieldModelo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTxtFieldPrecio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTxtFieldStock = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonAgregar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonConsultar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonCerrar = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jTxtFieldMarca = new javax.swing.JTextField();
        jButtonModificar = new javax.swing.JButton();
        tablaConsultasP = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaStock = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jTxtFieldStock1 = new javax.swing.JTextField();
        jCBOperador = new javax.swing.JComboBox<>();
        jButtonConsultar1 = new javax.swing.JButton();
        jButtonCerrar1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("IdProducto:");

        jTxtFieldIdPd.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldIdPd.setToolTipText("5 Caracteres (A-Z, 1-9)");
        jTxtFieldIdPd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldIdPdKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Marca:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Modelo:");

        jTxtFieldModelo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldModelo.setToolTipText("1-30 Caracteres (A-Z, a-z, 0-9. +)");
        jTxtFieldModelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldModeloKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Precio($):");

        jTxtFieldPrecio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldPrecio.setToolTipText("2 Decimales (0-999999.99)");
        jTxtFieldPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldPrecioKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Stock:");

        jTxtFieldStock.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldStock.setToolTipText("0-999999");
        jTxtFieldStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldStockKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("--- GESTIONAR PRODUCTOS ---");

        jButtonAgregar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonAgregar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonAgregar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        jButtonEliminar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonEliminar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jButtonConsultar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonConsultar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonConsultar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConsultar.setText("Consultar");
        jButtonConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultarActionPerformed(evt);
            }
        });

        jButtonCerrar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonCerrar.setText("Cerrar");
        jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarActionPerformed(evt);
            }
        });

        jButtonLimpiar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonLimpiar.setText("Limpiar");
        jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarActionPerformed(evt);
            }
        });

        jTxtFieldMarca.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldMarca.setToolTipText("1-20 Caracteres (A-Z, a-z)");
        jTxtFieldMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldMarcaKeyReleased(evt);
            }
        });

        jButtonModificar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonModificar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonModificar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar.setText("Modificar");
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdProducto", "Marca", "Modelo", "Precio", "Stock"
            }
        ));
        tablaProductos.setGridColor(new java.awt.Color(255, 255, 255));
        tablaProductos.setRequestFocusEnabled(false);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaProductos);

        tablaConsultasP.addTab("Productos-General", jScrollPane1);

        tablaStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdProducto", "Marca", "Modelo", "Precio", "Stock"
            }
        ));
        tablaStock.getTableHeader().setReorderingAllowed(false);
        tablaStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaStockMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaStock);

        tablaConsultasP.addTab("Productos-Stock", jScrollPane2);

        jLabel5.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("--- CONSULTAR STOCK ---");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Stock:");

        jTxtFieldStock1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldStock1.setToolTipText("0-999999");
        jTxtFieldStock1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldStock1KeyReleased(evt);
            }
        });

        jCBOperador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<=", "=", ">=" }));

        jButtonConsultar1.setBackground(new java.awt.Color(0, 51, 102));
        jButtonConsultar1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonConsultar1.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConsultar1.setText("Consultar");
        jButtonConsultar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultar1ActionPerformed(evt);
            }
        });

        jButtonCerrar1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonCerrar1.setText("Actualizar");
        jButtonCerrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jButtonAgregar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonEliminar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonModificar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonConsultar))
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonCerrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTxtFieldStock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jTxtFieldPrecio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                                                .addComponent(jTxtFieldModelo, javax.swing.GroupLayout.Alignment.LEADING)))))
                                .addGap(15, 15, 15)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTxtFieldIdPd, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtFieldMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(14, 14, 14)
                        .addComponent(jCBOperador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTxtFieldStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonConsultar1))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(tablaConsultasP, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tablaConsultasP, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(27, 27, 27)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldIdPd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTxtFieldMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCerrar)
                            .addComponent(jButtonLimpiar)
                            .addComponent(jButtonCerrar1))
                        .addGap(25, 25, 25)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAgregar)
                            .addComponent(jButtonEliminar)
                            .addComponent(jButtonConsultar)
                            .addComponent(jButtonModificar))
                        .addGap(77, 77, 77)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBOperador, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTxtFieldStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonConsultar1))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        // TODO add your handling code here:
        Paneles.g.removePanel();
        Paneles.g.auxPd = 0;
    }//GEN-LAST:event_jButtonCerrarActionPerformed

    private void jButtonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_jButtonLimpiarActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if(camposObligatorios())  // Si los campos obligatorios estan llenados
            if(camposValidos()){  // Si todos los campos son validos
                agregar();
                if(Paneles.g.auxC == 1)
                    Paneles.g.compras.idPd();
                if(Paneles.g.auxV == 1)
                    Paneles.g.ventas.idPd();
            }
            else
                JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);              
        else
            JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos", 
            "Aviso", JOptionPane.INFORMATION_MESSAGE);
        tablaConsultasP.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdPd.getText().isEmpty() || !txtField[0])
            jTxtFieldIdPd.requestFocus();
        else
            consultar();
        tablaConsultasP.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonConsultarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdPd.getText().isEmpty() || !txtField[0])
            jTxtFieldIdPd.requestFocus();
        else
            if(bandConsulta){     
                eliminar();
                if(Paneles.g.auxC == 1)
                    Paneles.g.compras.idPd();
                if(Paneles.g.auxV == 1)
                    Paneles.g.ventas.idPd();
            }
            else
                consultar();
        tablaConsultasP.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdPd.getText().isEmpty() || !txtField[0])
            jTxtFieldIdPd.requestFocus();
        else
            if(bandConsulta){
                if(camposObligatorios())  // Si los campos obligatorios estan llenados
                    if(camposValidos()){  // Si todos los campos son validos
                        modificar();
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);              
                else
                    JOptionPane.showMessageDialog(null, "Se deben llenar los \ncampos obligatorios (*)", 
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
            else
                consultar();
        tablaConsultasP.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jTxtFieldIdPdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldIdPdKeyReleased
        // TODO add your handling code here:
        txtField[0] = Validar.validarClave(jTxtFieldIdPd.getText());
        if(!txtField[0])
            jTxtFieldIdPd.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldIdPd.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldIdPdKeyReleased

    private void jTxtFieldMarcaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldMarcaKeyReleased
        // TODO add your handling code here:
        txtField[1] = Validar.validarMarca(jTxtFieldMarca.getText().toLowerCase(), true);
        if(!txtField[1])
            jTxtFieldMarca.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldMarca.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldMarcaKeyReleased

    private void jTxtFieldModeloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldModeloKeyReleased
        // TODO add your handling code here:
        txtField[2] = Validar.validarModelo(jTxtFieldModelo.getText().toLowerCase());
        if(!txtField[2])
            jTxtFieldModelo.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldModelo.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldModeloKeyReleased

    private void jTxtFieldPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldPrecioKeyReleased
        // TODO add your handling code here:
        txtField[3] = Validar.validarPrecio(jTxtFieldPrecio.getText());
        if(!txtField[3])
            jTxtFieldPrecio.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldPrecio.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldPrecioKeyReleased

    private void jTxtFieldStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldStockKeyReleased
        // TODO add your handling code here:
        txtField[4] = Validar.validarCant(jTxtFieldStock.getText(), 0, 99999);
        if(!txtField[4])
            jTxtFieldStock.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldStock.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldStockKeyReleased

    private void tablaProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductosMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaProductos.rowAtPoint(evt.getPoint());
        jTxtFieldIdPd.setText(String.valueOf(tablaProductos.getValueAt(seleccion, 0)));
        jTxtFieldMarca.setText(String.valueOf(tablaProductos.getValueAt(seleccion, 1)));
        jTxtFieldModelo.setText(String.valueOf(tablaProductos.getValueAt(seleccion, 2)));
        jTxtFieldPrecio.setText(String.valueOf(tablaProductos.getValueAt(seleccion, 3)));
        jTxtFieldStock.setText(String.valueOf(tablaProductos.getValueAt(seleccion, 4)));
        bandConsulta = true;
    }//GEN-LAST:event_tablaProductosMouseClicked

    private void jTxtFieldStock1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldStock1KeyReleased
        // TODO add your handling code here:
        stock = Validar.validarCant(jTxtFieldStock1.getText(), 0, 99999);
        if(!stock)
            jTxtFieldStock1.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldStock1.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldStock1KeyReleased

    private void jButtonConsultar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultar1ActionPerformed
        // TODO add your handling code here:
        if(stock){
            tablaConsultasP.setSelectedIndex(1);
            consultaStock();
        }
    }//GEN-LAST:event_jButtonConsultar1ActionPerformed

    private void tablaStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaStockMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaStock.rowAtPoint(evt.getPoint());
        jTxtFieldIdPd.setText(String.valueOf(tablaStock.getValueAt(seleccion, 0)));
        jTxtFieldMarca.setText(String.valueOf(tablaStock.getValueAt(seleccion, 1)));
        jTxtFieldModelo.setText(String.valueOf(tablaStock.getValueAt(seleccion, 2)));
        jTxtFieldPrecio.setText(String.valueOf(tablaStock.getValueAt(seleccion, 3)));
        jTxtFieldStock.setText(String.valueOf(tablaStock.getValueAt(seleccion, 4)));
        bandConsulta = true;
    }//GEN-LAST:event_tablaStockMouseClicked

    private void jButtonCerrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrar1ActionPerformed
        // TODO add your handling code here:
        consultaGeneral();
    }//GEN-LAST:event_jButtonCerrar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonCerrar1;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonConsultar1;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JComboBox<String> jCBOperador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTxtFieldIdPd;
    private javax.swing.JTextField jTxtFieldMarca;
    private javax.swing.JTextField jTxtFieldModelo;
    private javax.swing.JTextField jTxtFieldPrecio;
    private javax.swing.JTextField jTxtFieldStock;
    private javax.swing.JTextField jTxtFieldStock1;
    private javax.swing.JTabbedPane tablaConsultasP;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaStock;
    // End of variables declaration//GEN-END:variables
}
