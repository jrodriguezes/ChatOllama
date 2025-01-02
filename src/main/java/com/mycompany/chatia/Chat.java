/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.chatia;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.*;
import java.net.*;

/**
 *
 * @author jerem
 */
public class Chat extends javax.swing.JFrame {

    JFileChooser Selector = new JFileChooser();
    File File;

    public Chat() {
        initComponents();
        TxA_ChatBot.setEditable(false);
        ActionListeners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Txt_UserChat = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxA_ChatBot = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Txt_UserChat.setBackground(new java.awt.Color(51, 51, 51));
        Txt_UserChat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Txt_UserChat.setForeground(new java.awt.Color(153, 153, 153));
        Txt_UserChat.setText("¿En qué puedo ayudarte?");
        Txt_UserChat.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(Txt_UserChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 397, 670, 57));

        TxA_ChatBot.setBackground(new java.awt.Color(51, 51, 51));
        TxA_ChatBot.setColumns(20);
        TxA_ChatBot.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TxA_ChatBot.setForeground(new java.awt.Color(255, 255, 255));
        TxA_ChatBot.setLineWrap(true);
        TxA_ChatBot.setRows(5);
        TxA_ChatBot.setWrapStyleWord(true);
        TxA_ChatBot.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane1.setViewportView(TxA_ChatBot);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 10, 670, 350));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 153, 153));
        jButton1.setText("Archivo");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 370, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 460));

        pack();
    }// </editor-fold>//GEN-END:initComponents
//
    public String Send_To_Python(String userMessage) {
        StringBuilder Response = new StringBuilder();
        try {

            Socket Socket = new Socket("localhost", 8080);

            // Enviar mensaje al servidor
            PrintWriter Out = new PrintWriter(Socket.getOutputStream(), true);
            Out.println(userMessage);

            // Leer respuesta del servidor
            BufferedReader In = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
            String line;
            while ((line = In.readLine()) != null) {
                Response.append(line).append("\n");
            }

            // Cerrar conexion
            Out.close();
            In.close();
            Socket.close();
        } catch (IOException e) {
            Response.append("Error al conectar con el servidor Python: ").append(e.getMessage());
        }
        return Response.toString().trim(); // Trim elimina el salto de linea extra al final
    }

    private void ActionListeners() {
        Txt_UserChat.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Txt_UserChat.getText().equals("¿En qué puedo ayudarte?")) {
                    Txt_UserChat.setText("");
                    Txt_UserChat.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Txt_UserChat.getText().isEmpty()) {
                    Txt_UserChat.setText("¿En qué puedo ayudarte?");
                    Txt_UserChat.setForeground(new Color(153, 153, 153));
                }
            }
        }
        );

        // Evento al presionar Enter en el campo de texto
        Txt_UserChat.addActionListener(e -> {
            String User_Message = Txt_UserChat.getText().trim();
            if (!User_Message.isEmpty()) {
                // Mostrar mensaje del usuario
                TxA_ChatBot.append("Tú: " + User_Message + "\n");
                Txt_UserChat.setText("");

                // Enviar mensaje a Python y obtener respuesta
                String Bot_Response = Send_To_Python(User_Message);

                // Mostrar respuesta progresivamente
                new Thread(() -> {
                    for (char c : Bot_Response.toCharArray()) {
                        TxA_ChatBot.append(String.valueOf(c));
                        try {
                            Thread.sleep(50); // Controla la velocidad de escritura (50 ms por carácter)
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    TxA_ChatBot.append("\n");
                }).start();
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat().setVisible(true);
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea TxA_ChatBot;
    private javax.swing.JTextField Txt_UserChat;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
