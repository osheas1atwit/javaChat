package demonstration;

@SuppressWarnings( "javadoc" )
public class ChatDEMO extends javax.swing.JFrame {

    public boolean messageReady = false ;
    public String message ;
    public String username ;
    /*
    public void giveTitle(String name)
        {
        this.username = name ;
        setTitle("logged in as:" + name ) ;
        }
    */
    public String getMessageOut()
        {
        if( this.message.contentEquals( "" ) || this.message.contentEquals( " " ) )
            {
            this.messageReady = false ;
            return null ;
            }
        if( this.messageReady )
            {
            this.messageReady = false ;
            return this.message ;
            }
        return null ;
        }
    
    
    public void printMessage( String messageOut )
        {
        messageOut = String.format( "%s\n%s", this.jTextArea1.getText(), messageOut ) ; 
        this.jTextArea1.setText( null ) ;
        this.jTextArea1.setText( messageOut ) ;
        jTextArea1.moveCaretPosition( jTextArea1.getDocument().getLength() );
        }
    

    public ChatDEMO() {
        initComponents();
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

    jTextField1 = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
    setTitle("logged in as: " + this.username );

    jButton1.setText("send");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    
    jTextField1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        jTextField1ActionPerformed(evt);
    }
    });

    setResizable(false);
    jTextArea1.setEditable(false);
    jTextArea1.setColumns(20);
    jTextArea1.setRows(5);
    jTextArea1.setWrapStyleWord(true);
    jTextArea1.setLineWrap( true );
    jTextArea1.moveCaretPosition( jTextArea1.getDocument().getLength() );
    jScrollPane2.setViewportView(jTextArea1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap(50, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jScrollPane2)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton1)))
            .addGap(50, 50, 50))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap(50, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1))
            .addGap(50, 50, 50))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) 
        {                                                
        if( this.jTextField1.getText() != null )
            {
            this.message = this.jTextField1.getText() ;
            this.messageReady = true ; 
            this.jTextField1.setText( null ) ;
            }
        }  
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         

    if( this.jTextField1.getText() != null )
        {
        this.message = this.jTextField1.getText() ;
        this.messageReady = true ; 
        this.jTextField1.setText( null ) ;
        }
        
    }    
    

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatDEMO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatDEMO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatDEMO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatDEMO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatDEMO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
