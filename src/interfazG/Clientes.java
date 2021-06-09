/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazG;
import javax.swing.JOptionPane;
import objetos.*;
import java.sql.*;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 52341
 */
public class Clientes extends javax.swing.JPanel {
    private boolean txtField[] = new boolean[8];
    private boolean bandConsulta = false;
    
    public Clientes() {
        initComponents();
        inicializarArreglo();
        //consultaGeneral();
    }
   
    public void agregar(){
        String idCl, nom, apell, calle, col, ciudad, telef, correo;
        idCl = jTxtFieldIdCl.getText();
        
        if(!registroExiste(idCl))
            try {
                nom = jTxtFieldNombre.getText();
                apell = jTxtFieldApellido.getText();
                calle = jTxtFieldCalle.getText();
                col = jTxtFieldColonia.getText();
                ciudad = jTxtFieldCiudad.getText();
                telef = jTxtFieldTelefono.getText();
                correo = jTxtFieldCorreo.getText();
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into Clientes values ('" + idCl + "','" + nom + "','" + apell
                                                     + "','" + calle + "','" + col + "','" + ciudad + "','" + telef + "','" + correo + "')");
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
        String idCl = jTxtFieldIdCl.getText();
        
        if(registroExiste(idCl)){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?", "Aviso",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("delete from Clientes where IdCliente = '" + idCl + "'");
                    ps.executeUpdate();
                    consultaGeneral();
                    limpiar();
                    JOptionPane.showMessageDialog(null, "SE HA ELIMINADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "NO SE PUDO ELIMINAR, EL CLIENTE HACE REFERENCIA A UNA VENTA.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
               JOptionPane.showMessageDialog(null, "SE HA CANCELADO LA ELIMINACIÓN.", "Aviso", JOptionPane.INFORMATION_MESSAGE); 
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void modificar(){
        String idCl, nom, apell, calle, col, ciudad, telef, correo;
        idCl = jTxtFieldIdCl.getText();
        
        if(registroExiste(idCl)){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(opcion == JOptionPane.YES_OPTION){
                    nom = jTxtFieldNombre.getText();
                    apell = jTxtFieldApellido.getText();
                    calle = jTxtFieldCalle.getText();
                    col = jTxtFieldColonia.getText();
                    ciudad = jTxtFieldCiudad.getText();
                    telef = jTxtFieldTelefono.getText();
                    correo = jTxtFieldCorreo.getText();

                    try {
                        PreparedStatement ps = Paneles.conect.prepareStatement("update Clientes set NomC = '" + nom + "', ApellidoC = '" + apell + "', Calle = '" + calle 
                                                + "', Colonia = '" + col + "', Ciudad = '" + ciudad + "', TelefonoC = '" + telef + "', CorreoC = '" + correo + "' where IdCliente = '" + idCl + "'");
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
        String idCl = jTxtFieldIdCl.getText();
        
        if(registroExiste(idCl)){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from Clientes where IdCliente = '" + idCl + "'");

                rs.next();
                jTxtFieldNombre.setText(rs.getString(2));
                jTxtFieldApellido.setText(rs.getString(3));
                jTxtFieldCalle.setText(rs.getString(4));
                jTxtFieldColonia.setText(rs.getString(5));
                jTxtFieldCiudad.setText(rs.getString(6));
                jTxtFieldTelefono.setText(rs.getString(7).replaceAll(" ", ""));
                jTxtFieldCorreo.setText(rs.getString(8));
                bandConsulta = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public boolean registroExiste(String idCl){
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdCliente from Clientes where IdCliente = '" + idCl + "'");
            rs.next();
            return !(rs.getString(1).equals(null));
            
        } catch (SQLException e) {
            return false;
        }
        
    }
    
    public void consultaGeneral(){
        String colum[] = {"IdCliente", "Nombre", "Apellido", "Calle", "Colonia", "Ciudad", "Teléfono", "Correo"};
        DefaultTableModel tcliente= new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        String datos[] = new String[8];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from Clientes");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                datos[7] = rs.getString(8);
                tcliente.addRow(datos);
            }
            
            tablaClientes.setModel(tcliente);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void limpiar(){
        jTxtFieldIdCl.setText(null);
        jTxtFieldNombre.setText(null);
        jTxtFieldApellido.setText(null);
        jTxtFieldCalle.setText(null);
        jTxtFieldColonia.setText(null);
        jTxtFieldCiudad.setText(null);
        jTxtFieldTelefono.setText(null);
        jTxtFieldCorreo.setText(null);
        jTxtFieldIdCl.setBackground(Color.WHITE);
        jTxtFieldNombre.setBackground(Color.WHITE);
        jTxtFieldApellido.setBackground(Color.WHITE);
        jTxtFieldCalle.setBackground(Color.WHITE);
        jTxtFieldColonia.setBackground(Color.WHITE);
        jTxtFieldCiudad.setBackground(Color.WHITE);
        jTxtFieldTelefono.setBackground(Color.WHITE);
        jTxtFieldCorreo.setBackground(Color.WHITE);
        jTxtFieldIdCl.requestFocus();
        inicializarArreglo();
        bandConsulta = false;
    }
    
    public boolean camposObligatorios(){
        return !(jTxtFieldIdCl.getText().isEmpty() || jTxtFieldNombre.getText().isEmpty() 
                || jTxtFieldApellido.getText().isEmpty());
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
        jTxtFieldIdCl = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTxtFieldCalle = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTxtFieldColonia = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        jButtonAgregar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonModificar = new javax.swing.JButton();
        jButtonConsultar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonCerrar = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTxtFieldCiudad = new javax.swing.JTextField();
        jTxtFieldTelefono = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTxtFieldCorreo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTxtFieldNombre = new javax.swing.JTextField();
        jTxtFieldApellido = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("IdCliente:");

        jTxtFieldIdCl.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldIdCl.setToolTipText("5 Caracteres (A-Z, 1-9)");
        jTxtFieldIdCl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldIdClKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Nombre(s):");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Apellido(s):");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Calle:");

        jTxtFieldCalle.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldCalle.setToolTipText("0-30 Caracteres (A-Z, a-z, 0-9, espacios en blanco, acentos, puntos)");
        jTxtFieldCalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldCalleKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Colonia:");

        jTxtFieldColonia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldColonia.setToolTipText("0-30 Caracteres (A-Z, a-z, espacios en blanco, acentos)");
        jTxtFieldColonia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldColoniaKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("--- GESTIONAR CLIENTES ---");

        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdCliente", "Nombre", "Apellido", "Calle", "Colonia", "Ciudad", "Teléfono", "Correo"
            }
        ));
        tablaClientes.setGridColor(new java.awt.Color(255, 255, 255));
        tablaClientes.setRequestFocusEnabled(false);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaClientes);

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

        jButtonModificar.setBackground(new java.awt.Color(0, 51, 102));
        jButtonModificar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButtonModificar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar.setText("Modificar");
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
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

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Ciudad:");

        jTxtFieldCiudad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldCiudad.setToolTipText("0-30 Caracteres (A-Z, a-z, espacios en blanco, acentos)");
        jTxtFieldCiudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldCiudadKeyReleased(evt);
            }
        });

        jTxtFieldTelefono.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldTelefono.setToolTipText("10 Dígitos (0-9)");
        jTxtFieldTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldTelefonoKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Teléfono:");

        jTxtFieldCorreo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldCorreo.setToolTipText("0-40 Caracteres (A-Z, a-z, 1-9, puntos, @, _ )");
        jTxtFieldCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldCorreoKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Correo:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("*");
        jLabel5.setToolTipText("Campo Obligatorio");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("*");
        jLabel6.setToolTipText("Campo Obligatorio");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("*");
        jLabel12.setToolTipText("Campo Obligatorio");

        jTxtFieldNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldNombre.setToolTipText("1-30 Caracteres (A-Z, a-z, espacios en blanco, acentos, puntos)");
        jTxtFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldNombreKeyReleased(evt);
            }
        });

        jTxtFieldApellido.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldApellido.setToolTipText("1-30 Caracteres (A-Z, a-z, espacios en blanco, acentos, puntos)");
        jTxtFieldApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldApellidoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonAgregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonEliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonConsultar))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTxtFieldApellido, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldNombre, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldCalle)
                                    .addComponent(jTxtFieldColonia, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldCiudad, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldTelefono, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldCorreo)
                                    .addComponent(jTxtFieldIdCl, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addComponent(jSeparator2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(74, 74, 74)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(27, 27, 27)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldIdCl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(jTxtFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel12)
                            .addComponent(jTxtFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCerrar)
                            .addComponent(jButtonLimpiar))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAgregar)
                            .addComponent(jButtonEliminar)
                            .addComponent(jButtonModificar)
                            .addComponent(jButtonConsultar))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        // TODO add your handling code here:
        Paneles.g.removePanel();
        Paneles.g.auxCl = 0;
    }//GEN-LAST:event_jButtonCerrarActionPerformed

    private void jButtonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_jButtonLimpiarActionPerformed

    private void jTxtFieldNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldNombreKeyReleased
        // TODO add your handling code here:
        txtField[1] = Validar.validarNombre(jTxtFieldNombre.getText().toLowerCase());
        if(!txtField[1])
            jTxtFieldNombre.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldNombre.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldNombreKeyReleased

    private void jTxtFieldIdClKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldIdClKeyReleased
        // TODO add your handling code here:
        txtField[0] = Validar.validarClave(jTxtFieldIdCl.getText());
        if(!txtField[0])
            jTxtFieldIdCl.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldIdCl.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldIdClKeyReleased

    private void jTxtFieldCalleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldCalleKeyReleased
        // TODO add your handling code here:
        txtField[3] = Validar.validarCalle(jTxtFieldCalle.getText().toLowerCase());
        if(!txtField[3])
            jTxtFieldCalle.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldCalle.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldCalleKeyReleased

    private void jTxtFieldColoniaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldColoniaKeyReleased
        // TODO add your handling code here:
        txtField[4] = Validar.validarDomicilio(jTxtFieldColonia.getText().toLowerCase());
        if(!txtField[4])
            jTxtFieldColonia.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldColonia.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldColoniaKeyReleased

    private void jTxtFieldCiudadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldCiudadKeyReleased
        // TODO add your handling code here:
        txtField[5] = Validar.validarDomicilio(jTxtFieldCiudad.getText().toLowerCase());
        if(!txtField[5])
            jTxtFieldCiudad.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldCiudad.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldCiudadKeyReleased

    private void jTxtFieldTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldTelefonoKeyReleased
        // TODO add your handling code here:
        txtField[6] = Validar.validarTelefono(jTxtFieldTelefono.getText().toLowerCase());
        if(!txtField[6])
            jTxtFieldTelefono.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldTelefono.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldTelefonoKeyReleased

    private void jTxtFieldCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldCorreoKeyReleased
        // TODO add your handling code here:
        txtField[7] = Validar.validarCorreo(jTxtFieldCorreo.getText().toLowerCase());
        if(!txtField[7])
            jTxtFieldCorreo.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldCorreo.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldCorreoKeyReleased

    private void jTxtFieldApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldApellidoKeyReleased
        // TODO add your handling code here:
        txtField[2] = Validar.validarNombre(jTxtFieldApellido.getText().toLowerCase());
        if(!txtField[2])
            jTxtFieldApellido.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldApellido.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldApellidoKeyReleased

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if(camposObligatorios())  // Si los campos obligatorios estan llenados
            if(camposValidos()){  // Si todos los campos son validos
                agregar();
                if(Paneles.g.auxV == 1)
                    Paneles.g.ventas.idCl();
            }
            else
                JOptionPane.showMessageDialog(null, "Revisa los campos \nremarcados", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);              
        else
            JOptionPane.showMessageDialog(null, "Se deben llenar los \ncampos obligatorios (*)", 
            "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdCl.getText().isEmpty() || !txtField[0])
            jTxtFieldIdCl.requestFocus();
        else
            if(bandConsulta){
                eliminar();
                if(Paneles.g.auxV == 1)
                    Paneles.g.ventas.idCl();
            }
            else
                consultar();
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdCl.getText().isEmpty() || !txtField[0])
            jTxtFieldIdCl.requestFocus();
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
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jButtonConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdCl.getText().isEmpty() || !txtField[0])
            jTxtFieldIdCl.requestFocus();
        else
            consultar();
    }//GEN-LAST:event_jButtonConsultarActionPerformed

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        // TODO add your handling code here:
        int seleccion = tablaClientes.rowAtPoint(evt.getPoint());
        jTxtFieldIdCl.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 0)));
        jTxtFieldNombre.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 1)));
        jTxtFieldApellido.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 2)));
        jTxtFieldCalle.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 3)));
        jTxtFieldColonia.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 4)));
        jTxtFieldCiudad.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 5)));
        jTxtFieldTelefono.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 6)).replaceAll(" ", ""));
        jTxtFieldCorreo.setText(String.valueOf(tablaClientes.getValueAt(seleccion, 7)));
        bandConsulta = true;
    }//GEN-LAST:event_tablaClientesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTxtFieldApellido;
    private javax.swing.JTextField jTxtFieldCalle;
    private javax.swing.JTextField jTxtFieldCiudad;
    private javax.swing.JTextField jTxtFieldColonia;
    private javax.swing.JTextField jTxtFieldCorreo;
    private javax.swing.JTextField jTxtFieldIdCl;
    private javax.swing.JTextField jTxtFieldNombre;
    private javax.swing.JTextField jTxtFieldTelefono;
    private javax.swing.JTable tablaClientes;
    // End of variables declaration//GEN-END:variables
}
