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
public class Proveedores extends javax.swing.JPanel {
    private boolean txtField[] = new boolean[6];
    private boolean bandConsulta = false;
    
    public Proveedores() {
        initComponents();
        inicializarArreglo();
        //consultaGeneral();
    }

    public void limpiar(){
        jTxtFieldIdPv.setText(null);
        jTxtFieldNombre.setText(null);
        jTxtFieldApellido.setText(null);
        jTxtFieldMarca.setText(null);
        jTxtFieldTelefono.setText(null);
        jTxtFieldCorreo.setText(null);
        jTxtFieldIdPv.setBackground(Color.WHITE);
        jTxtFieldNombre.setBackground(Color.WHITE);
        jTxtFieldApellido.setBackground(Color.WHITE);;
        jTxtFieldMarca.setBackground(Color.WHITE);
        jTxtFieldTelefono.setBackground(Color.WHITE);
        jTxtFieldCorreo.setBackground(Color.WHITE);
        inicializarArreglo();
        jTxtFieldIdPv.requestFocus();
        bandConsulta = false;
    }
    
    public void agregar(){
        String idPv, nom, apell, marca, telef, correo;
        idPv = jTxtFieldIdPv.getText();
        
        if(!registroExiste(idPv))
            try {
                nom = jTxtFieldNombre.getText();
                apell = jTxtFieldApellido.getText();
                marca = jTxtFieldMarca.getText();
                telef = jTxtFieldTelefono.getText();
                correo = jTxtFieldCorreo.getText();
                PreparedStatement ps = Paneles.conect.prepareStatement("insert into Proveedores values ('" + idPv + "','" + nom + "','" + apell
                                                     + "','" + marca + "','" + telef + "','" + correo + "')");
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
        String idPv = jTxtFieldIdPv.getText();
        
        if(registroExiste(idPv)){
            int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro?", "Aviso",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opcion == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement ps = Paneles.conect.prepareStatement("delete from Proveedores where IdProveedor = '" + idPv + "'");
                    ps.executeUpdate();
                    consultaGeneral();
                    limpiar();
                    JOptionPane.showMessageDialog(null, "SE HA ELIMINADO EL REGISTRO", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "NO SE PUDO ELIMINAR, EL PROVEEDOR HACE REFERENCIA A UNA COMPRA", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
               JOptionPane.showMessageDialog(null, "SE HA CANCELADO LA ELIMINACIÓN.", "Aviso", JOptionPane.INFORMATION_MESSAGE); 
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void modificar(){
        String idPv, nom, apell, marca, telef, correo;
        idPv = jTxtFieldIdPv.getText();
        
        if(registroExiste(idPv)){
            int opcion = JOptionPane.showConfirmDialog(null, "Realizar modificaciones?", "Aviso",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(opcion == JOptionPane.YES_OPTION){
                    nom = jTxtFieldNombre.getText();
                    apell = jTxtFieldApellido.getText();
                    marca = jTxtFieldMarca.getText();
                    telef = jTxtFieldTelefono.getText();
                    correo = jTxtFieldCorreo.getText();

                    try {
                        PreparedStatement ps = Paneles.conect.prepareStatement("update Proveedores set NomP = '" + nom + "', ApellidoP = '" + apell + "', MarcaP = '" 
                                               + marca + "', TelefonoP = '" + telef + "', CorreoP = '" + correo + "' where IdProveedor = '" + idPv + "'");
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
        String idPv = jTxtFieldIdPv.getText();
        
        if(registroExiste(idPv)){
            try {
                Statement leer = Paneles.conect.createStatement();
                ResultSet rs = leer.executeQuery("select * from Proveedores where IdProveedor = '" + idPv + "'");

                rs.next();
                jTxtFieldNombre.setText(rs.getString(2));
                jTxtFieldApellido.setText(rs.getString(3));
                jTxtFieldMarca.setText(rs.getString(4));
                jTxtFieldTelefono.setText(rs.getString(5).replaceAll(" ", ""));
                jTxtFieldCorreo.setText(rs.getString(6));
                bandConsulta = true;
            } catch (SQLException e) {
            }
        }
        else
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL REGISTRO.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public boolean registroExiste(String idPv){
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select IdProveedor from Proveedores where IdProveedor = '" + idPv + "'");
            rs.next();
            return !(rs.getString(1).equals(null));
            
        } catch (SQLException e) {
            return false;
        }
        
    }
    
    public void consultaGeneral(){
        String colum[] = {"IdProveedor", "Nombre", "Apellido", "Marca", "Teléfono", "Correo"};
        DefaultTableModel tprov = new DefaultTableModel(null, colum){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };
        
        String datos[] = new String[6];
        
        try {
            Statement leer = Paneles.conect.createStatement();
            ResultSet rs = leer.executeQuery("select * from Proveedores");
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                tprov.addRow(datos);
            }
            tablaProveedores.setModel(tprov);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public boolean camposObligatorios(){
        return !(jTxtFieldIdPv.getText().isEmpty() || jTxtFieldNombre.getText().isEmpty() 
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
        jLabel2 = new javax.swing.JLabel();
        jTxtFieldNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTxtFieldMarca = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();
        jButtonAgregar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonModificar = new javax.swing.JButton();
        jButtonConsultar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonCerrar = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTxtFieldCorreo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTxtFieldTelefono = new javax.swing.JTextField();
        jTxtFieldApellido = new javax.swing.JTextField();
        jTxtFieldIdPv = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("IdProveedor:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Nombre(s):");

        jTxtFieldNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldNombre.setToolTipText("1-30 Caracteres (A-Z, a-z, espacios en blanco, acentos, puntos)");
        jTxtFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldNombreKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Apellido(s):");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Marca:");

        jTxtFieldMarca.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldMarca.setToolTipText("0-20 Carecteres (A-Z, a-z)");
        jTxtFieldMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldMarcaKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Teléfono:");

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("--- GESTIONAR PROVEEDORES ---");

        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdProveedor", "Nombre", "Apellido", "Marca", "Teléfono", "Correo"
            }
        ));
        tablaProveedores.setGridColor(new java.awt.Color(255, 255, 255));
        tablaProveedores.setRequestFocusEnabled(false);
        tablaProveedores.getTableHeader().setReorderingAllowed(false);
        tablaProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProveedoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaProveedores);

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
        jLabel9.setText("Correo:");

        jTxtFieldCorreo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldCorreo.setToolTipText("0-40 Caracteres (A-Z, a-z, 1-9, puntos, @, _ )");
        jTxtFieldCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldCorreoKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("*");
        jLabel5.setToolTipText("Campo Obligatorio");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("*");
        jLabel6.setToolTipText("Campo Obligatorio");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("*");
        jLabel10.setToolTipText("Campo Obligatorio");

        jTxtFieldTelefono.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldTelefono.setToolTipText("10 Dígitos (0-9)");
        jTxtFieldTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldTelefonoKeyReleased(evt);
            }
        });

        jTxtFieldApellido.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldApellido.setToolTipText("1-30 Caracteres (A-Z, a-z, espacios en blanco, acentos, puntos)");
        jTxtFieldApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldApellidoKeyReleased(evt);
            }
        });

        jTxtFieldIdPv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTxtFieldIdPv.setToolTipText("5 Caracteres (A-Z, 1-9)");
        jTxtFieldIdPv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtFieldIdPvKeyReleased(evt);
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
                    .addComponent(jLabel10))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButtonAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonConsultar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7))
                                .addGap(15, 15, 15)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtFieldIdPv, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTxtFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTxtFieldMarca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                    .addComponent(jTxtFieldApellido, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTxtFieldNombre, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(jTxtFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(144, 144, 144)
                            .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
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
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jTxtFieldIdPv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel10)
                            .addComponent(jTxtFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCerrar)
                            .addComponent(jButtonLimpiar))
                        .addGap(25, 25, 25)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAgregar)
                            .addComponent(jButtonEliminar)
                            .addComponent(jButtonModificar)
                            .addComponent(jButtonConsultar))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdPv.getText().isEmpty() || !txtField[0])
            jTxtFieldIdPv.requestFocus();
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

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        // TODO add your handling code here:
        Paneles.g.removePanel();
        Paneles.g.auxPv = 0;
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
                if(Paneles.g.auxPv == 1)
                    Paneles.g.compras.idPv();
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
        if(jTxtFieldIdPv.getText().isEmpty() || !txtField[0])
            jTxtFieldIdPv.requestFocus();
        else
            if(bandConsulta){
                eliminar();
                if(Paneles.g.auxPv == 1)
                    Paneles.g.compras.idPv();
            }
            else
                consultar();
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jButtonConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultarActionPerformed
        // TODO add your handling code here:
        if(jTxtFieldIdPv.getText().isEmpty() || !txtField[0])
            jTxtFieldIdPv.requestFocus();
        else
            consultar();
    }//GEN-LAST:event_jButtonConsultarActionPerformed

    private void jTxtFieldIdPvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldIdPvKeyReleased
        // TODO add your handling code here:
        txtField[0] = Validar.validarClave(jTxtFieldIdPv.getText());
        if(!txtField[0])
            jTxtFieldIdPv.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldIdPv.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldIdPvKeyReleased

    private void jTxtFieldNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldNombreKeyReleased
        // TODO add your handling code here:
        txtField[1] = Validar.validarNombre(jTxtFieldNombre.getText().toLowerCase());
        if(!txtField[1])
            jTxtFieldNombre.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldNombre.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldNombreKeyReleased

    private void jTxtFieldApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldApellidoKeyReleased
        // TODO add your handling code here:
        txtField[2] = Validar.validarNombre(jTxtFieldApellido.getText().toLowerCase());
        if(!txtField[2])
            jTxtFieldApellido.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldApellido.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldApellidoKeyReleased

    private void jTxtFieldMarcaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldMarcaKeyReleased
        // TODO add your handling code here:
        txtField[3] = Validar.validarMarca(jTxtFieldMarca.getText().toLowerCase(), false);
        if(!txtField[3])
            jTxtFieldMarca.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldMarca.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldMarcaKeyReleased

    private void jTxtFieldTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldTelefonoKeyReleased
        // TODO add your handling code here:
        txtField[4] = Validar.validarTelefono(jTxtFieldTelefono.getText().toLowerCase());
        if(!txtField[4])
            jTxtFieldTelefono.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldTelefono.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldTelefonoKeyReleased

    private void jTxtFieldCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFieldCorreoKeyReleased
        // TODO add your handling code here:
        txtField[5] = Validar.validarCorreo(jTxtFieldCorreo.getText().toLowerCase());
        if(!txtField[5])
            jTxtFieldCorreo.setBackground(Color.LIGHT_GRAY);
        else
            jTxtFieldCorreo.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTxtFieldCorreoKeyReleased

    private void tablaProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProveedoresMouseClicked
        // TODO add your handling code here
        int seleccion = tablaProveedores.rowAtPoint(evt.getPoint());
        jTxtFieldIdPv.setText(String.valueOf(tablaProveedores.getValueAt(seleccion, 0)));
        jTxtFieldNombre.setText(String.valueOf(tablaProveedores.getValueAt(seleccion, 1)));
        jTxtFieldApellido.setText(String.valueOf(tablaProveedores.getValueAt(seleccion, 2)));
        jTxtFieldMarca.setText(String.valueOf(tablaProveedores.getValueAt(seleccion, 3)));
        jTxtFieldTelefono.setText(String.valueOf(tablaProveedores.getValueAt(seleccion, 4)).replaceAll(" ", ""));
        jTxtFieldCorreo.setText(String.valueOf(tablaProveedores.getValueAt(seleccion, 5)));
        bandConsulta = true;
    }//GEN-LAST:event_tablaProveedoresMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JTextField jTxtFieldCorreo;
    private javax.swing.JTextField jTxtFieldIdPv;
    private javax.swing.JTextField jTxtFieldMarca;
    private javax.swing.JTextField jTxtFieldNombre;
    private javax.swing.JTextField jTxtFieldTelefono;
    private javax.swing.JTable tablaProveedores;
    // End of variables declaration//GEN-END:variables
}
