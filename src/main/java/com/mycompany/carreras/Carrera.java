/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.carreras;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author alumno
 */
public class Carrera extends javax.swing.JFrame {

    /**
     * Creates new form Carrera
     */
   private final int META = 800;
    private Thread[] hilos;
    private boolean alguienHaGanado = false;
    private JProgressBar[] barras;
    private JButton jButton1,reiniciarButton;
    private JLabel[] cochesImagenes = new JLabel[4];
    
    public Carrera() {
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        // Crear botón para iniciar la carrera
        jButton1 = new JButton("Iniciar Carrera");
        jButton1.addActionListener(evt -> iniciarCarrera());
        
        // Crear botón para reiniciar la carrera
       reiniciarButton = new JButton("Reiniciar Carrera");
       reiniciarButton.addActionListener(evt -> reiniciarCarrera());
       
        // Crear barras de progreso
        barras = new JProgressBar[4];
        for (int i = 0; i < barras.length; i++) {
            barras[i] = new JProgressBar(0, META);
            barras[i].setStringPainted(true);
        }

        
    
    // JPanel de carretera
        JPanel carreteraPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                
                //Dibuja la hierba 
                g.setColor(Color.GREEN);
                g.fillRect(0,0,getWidth(),100);
                g.fillRect(0, 300, getWidth(), getHeight() - 300); // Hierba inferior
                
                
                // Dibuja la carretera con carriles
                g.setColor(Color.GRAY);
                g.fillRect(0, 100, getWidth(), 200); // Carretera

                // Dibuja los carriles
                g.setColor(Color.WHITE);
                int carrilAltura = 50;
                for (int i = 1; i <= 3; i++) {
                    g.drawLine(0, 100 + (i * carrilAltura), getWidth(), 100 + (i * carrilAltura)); // Carriles
                }

                

                // Dibuja las líneas discontinuas en los carriles de izquierda a derecha
                g.setColor(Color.WHITE);
                for (int i = 0; i < getWidth(); i += 30) { // Líneas discontinuas
                    // Líneas para el primer carril
                    // Líneas para el primer carril
                    g.drawLine(i, 125, i + 15, 125); // Carril 1 (Posición Y: 125)

                    // Líneas para el segundo carril
                    g.drawLine(i, 175, i + 15, 175); // Carril 2 (Posición Y: 175)

                    // Líneas para el tercer carril
                    g.drawLine(i, 225, i + 15, 225); // Carril 3 (Posición Y: 225)
                
                   //Lineas para el cuarto carril
                    g.drawLine(i,275,i +15 ,275);
                
                
                }
                
                //Linea de meta
                g.setColor(Color.white);
                int  metaX=800;
                int metaAncho=30;
                g.fillRect(metaX,100,metaAncho, 200);
            }
        };
        carreteraPanel.setLayout(null);
        carreteraPanel.setPreferredSize(new Dimension(1000, 300));
        
        //Nombres de las imagenes
        String[] nombresImagenes={"/Rayo.png","/chick.png", "/franchesco.png", "/sheriff.png"};
        
        // Agregar los coches (imágenes) en sus respectivos carriles
       for (int i = 0; i < cochesImagenes.length; i++) {
    try {
        // Usar la ruta relativa desde la carpeta resources
        URL imgURL = getClass().getResource(nombresImagenes[i]);
        System.out.println("Ruta de la imagen para coche " + (i + 1) + ": " + imgURL);

        if (imgURL != null) {
            // Crear y redimensionar el icono
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            cochesImagenes[i] = new JLabel(scaledIcon);
            cochesImagenes[i].setBounds(0, 100 + (i * 50), 50, 50);
            carreteraPanel.add(cochesImagenes[i]);

            System.out.println("Cargada imagen para coche " + (i + 1) + " desde: " + imgURL);
        } else {
            System.err.println("No se ha podido encontrar la imagen para el coche " + (i + 1));
        }
    } catch (Exception e) {
        System.err.println("Error al cargar la imagen para el coche " + (i + 1) + ": " + e.getMessage());
    }
}

        // JPanel de barras de progreso
        JPanel barraPanel = new JPanel();
        barraPanel.setLayout(new GridLayout(4, 1)); // 4 barras
        for (JProgressBar barra : barras) {
            barraPanel.add(barra);
        }

        // Crear layout
        setLayout(new BorderLayout());
        add(barraPanel, BorderLayout.SOUTH);  // Barra de progreso en la parte inferior
        add(carreteraPanel, BorderLayout.CENTER); // Panel de carretera al centro
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(jButton1);
        buttonPanel.add(reiniciarButton);
        add(buttonPanel, BorderLayout.NORTH); // Botones al norte
        
        // Ajustar ventana
        setTitle("Carrera de Coches");
        setSize(1000, 500);  // Aseguramos que la ventana es suficientemente grande
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void iniciarCarrera() {
        jButton1.setEnabled(false);
        hilos = new Thread[4];
        alguienHaGanado = false;

        for (int i = 0; i < hilos.length; i++) {
            final int indice = i; // Para usar en la lambda
            hilos[i] = new Thread(() -> moverCoche(barras[indice], indice + 1));
            hilos[i].start();
        }
    }
    
    private void reiniciarCarrera(){
        alguienHaGanado=false;
        for (int i=0; i <barras.length;i++){
            barras[i].setValue(0);
            barras[i].setString("Coche"+(i+1)+"-0m");
            cochesImagenes[i].setLocation(0,100+(i*50));
            cochesImagenes[i].repaint();
        }
    }

    private void moverCoche(JProgressBar barra, int cocheNumero) {
    Random random = new Random();
    int progreso = 0;

    while (progreso < META && !alguienHaGanado) {
        progreso += random.nextInt(10) + 1;
        final int valorProgreso = Math.min(progreso, META);

        SwingUtilities.invokeLater(() -> {
            barra.setValue(valorProgreso);
            barra.setString("Coche " + cocheNumero + " - " + valorProgreso + "m");

            // Animación suave de los coches
            cochesImagenes[cocheNumero - 1].setLocation(valorProgreso, 100 + (cocheNumero - 1) * 50);
        
            cochesImagenes[cocheNumero-1].repaint();
        });

        try {
            Thread.sleep(random.nextInt(100) + 50);
        } catch (InterruptedException e) {
            return;
        }
    }

    verificarGanador(cocheNumero);
}


    private synchronized void verificarGanador(int coche) {
        if (alguienHaGanado) {
            return;
        }
        alguienHaGanado = true;

        StringBuilder mensaje = new StringBuilder("¡Coche " + coche + " ha ganado!\n\n");
        mensaje.append("Distancia final:\n");

        // Mostrar la distancia recorrida por cada coche
        for (int i = 0; i < hilos.length; i++) {
            mensaje.append("Coche ").append(i + 1).append(": ")
                    .append(Math.min(META, barras[i].getValue()))  // Acceder a las barras directamente
                    .append(" m\n");
        }

        // Mostrar el mensaje con distancias finales
        JOptionPane.showMessageDialog(this, mensaje.toString(), "Resultado de la carrera", JOptionPane.INFORMATION_MESSAGE);

        
        //Reactivar el boton de iniciar carrera
        jButton1.setEnabled(true);
        
        // Detener los hilos activos
        for (Thread hilo : hilos) {
            if (hilo.isAlive()) {
                hilo.interrupt();
            }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Carrera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Carrera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Carrera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Carrera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Carrera().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
