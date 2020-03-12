
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author TheProthean
 */
public class Main extends javax.swing.JFrame {

    File[] files;
    
    HashSet<String> attrNames = new HashSet<>(Arrays.asList("width",
            "height", "bitDepth", "compressionMethod"));

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Format", "Color depth", "xDPI", "yDPI", "WidthxHeight", "Compression"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jMenu1.setText("File");

        jMenuItem2.setText("Open");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem1.setText("Exit");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private static String[] allowed_formats = {"pcx", "jpg", "gif", "tif", "bmp", "png"};
    
    private static boolean isImage(File file) {
        String fileName = file.getName();
        int separatorPos = fileName.lastIndexOf(".");
        if (separatorPos != -1 && separatorPos != 0) {
            String ext = fileName.substring(separatorPos + 1);
            for (String frm: allowed_formats)
                if (frm.equals(ext))
                    return true;
        } 
        return false;
    }
    
    HashSet<String> jpg_tags = new HashSet<>(Arrays.asList("Compression Type",
            "Data Precision", "Image Height", "Image Width",
            "X Resolution", "Y Resolution"));
    HashSet<String> gif_tags = new HashSet<>(Arrays.asList(
            "Bits per Pixel", "Image Height", "Image Width",
            "Width", "Height"));
    HashSet<String> tif_tags = new HashSet<>(Arrays.asList("Compression",
            "Bits Per Sample", "Samples Per Pixel", "Image Height", "Image Width",
            "X Resolution", "Y Resolution"));
    HashSet<String> pcx_tags = new HashSet<>(Arrays.asList(
            "Bits Per Pixel", "X Max", "Y Max",
            "Horizontal DPI", "Vertical DPI"));
    HashSet<String> png_tags = new HashSet<>(Arrays.asList("Compression Type",
            "Bits Per Sample", "Image Height", "Image Width",
            "Pixels Per Unit X", "Pixels Per Unit Y", "Unit Specifier"));
    HashSet<String> bmp_tags = new HashSet<>(Arrays.asList("Compression",
            "Bits Per Pixel", "Image Height", "Image Width",
            "X Pixels per Meter", "Y Pixels per Meter"));
    String[] tags_ = {"Name", "Extension", "Compression Type", "Data Precision", "Height", "Width", "X Res.", "Y Res."};
    private HashMap<String, String> getFileInfo(File file){
        Metadata metadata = null;
        HashMap<String, String> data = new HashMap<String, String>();
        int separatorPos = file.getName().lastIndexOf(".");
        String format = file.getName().substring(separatorPos + 1);
        data.put("format", file.getName().substring(separatorPos + 1));
        data.put("name", file.getName().substring(0, separatorPos));
        HashSet<String> tags = null;
        if (format.equals("pcx")) {
            tags = pcx_tags;
        } else if (format.equals("jpg")) {
            tags = jpg_tags;
        } else if (format.equals("gif")) {
            tags = gif_tags;
        } else if (format.equals("tif")) {
            tags = tif_tags;
        } else if (format.equals("png")) {
            tags = png_tags;
        } else if (format.equals("bmp")) {
            tags = bmp_tags;
        } 
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(directory.getName() + " | " 
                            + tag.getTagName());
                    if(tags.contains(tag.getTagName())) {
                        System.out.println(tag.getTagName() + " " + tag.getDescription());
                        data.put(tag.getTagName(), tag.getDescription());
                    }
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file.getName().matches("\\d+х\\d+х\\d+[a-zA-Z0-9+]*\\.[a-zA-Z]+")) {
                String[] data2 = file.getName().split("\\.");
                String[] data1 = data2[0].split("х");
                String inch = "";

                int i = 0;
                for(; '0' <= data1[2].charAt(i) && '9' >= data1[2].charAt(i); i++){
                    inch+= data1[2].charAt(i);
                    if (i + 1 == data1[2].length()) {
                        break;
                    }
                }
                data.put("X Resolution", inch);
                data.put("Y Resolution", inch);
            }
        }

        return data;
    }
    
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JFileChooser jfc = new JFileChooser("Choose file or folder");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setMultiSelectionEnabled(true);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            jfc.getSelectedFiles();
            files = jfc.getSelectedFiles();
            ArrayList<File> fileData = new ArrayList<>();
            for (File f : files) {
                if (f.isDirectory()) {
                    for (File fd : f.listFiles()) {
                        if (isImage(fd)) {
                            fileData.add(fd);
                        }
                    }
                }
                else {
                    if(isImage(f))
                        fileData.add(f);
                }
            }
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            for (File file: fileData) {
                HashMap<String, String> data = getFileInfo(file);
                String[] row = null;
                String format = data.get("format");
                if (format.equals("pcx")) {
                    row = new String[]{data.get("name"),
                        data.get("format"),
                        data.get("Bits Per Pixel"),
                        data.get("Horizontal DPI"),
                        data.get("Vertical DPI"),
                        data.get("X Max")
                        + "x"
                        + data.get("Y Max"),
                        null};
                } else if (format.equals("jpg")) {
                    row = new String[]{data.get("name"),
                        data.get("format"),
                        data.get("Data Precision").split(" ")[0],
                        data.get("X Resolution").split(" ")[0],
                        data.get("Y Resolution").split(" ")[0],
                        data.get("Image Width").split(" ")[0]
                        + "x"
                        + data.get("Image Height").split(" ")[0],
                        data.get("Compression Type")};
                } else if (format.equals("gif")) {
                    row = new String[]{data.get("name"),
                        data.get("format"),
                        data.get("Bits per Pixel"),
                        data.get("X Resolution"),
                        data.get("Y Resolution"),
                        data.get("Image Width").split(" ")[0]
                        + "x"
                        + data.get("Image Height").split(" ")[0],
                        null};
                } else if (format.equals("tif")) {
                    int depth = (int)(Double.parseDouble(data.get("Bits Per Sample").split(" ")[0]) * 
                            Double.parseDouble(data.get("Samples Per Pixel").split(" ")[0]));
                    row = new String[]{data.get("name"),
                        data.get("format"),
                        String.valueOf(depth),
                        data.get("X Resolution").split(" ")[0],
                        data.get("Y Resolution").split(" ")[0],
                        data.get("Image Width").split(" ")[0]
                        + "x"
                        + data.get("Image Height").split(" ")[0],
                        data.get("Compression")};
                } else if (format.equals("png")) {
                    if (data.get("Unit Specifier").equals("Metres")) {
                        data.put("Pixels Per Unit X", 
                                String.valueOf((int)(Double.parseDouble(data.get("Pixels Per Unit X")) / 39.3701)));
                        data.put("Pixels Per Unit Y", 
                                String.valueOf((int)(Double.parseDouble(data.get("Pixels Per Unit Y")) / 39.3701)));
                    }
                    row = new String[]{data.get("name"),
                        data.get("format"),
                        data.get("Bits Per Sample"),
                        data.get("Pixels Per Unit X"),
                        data.get("Pixels Per Unit Y"),
                        data.get("Image Width").split(" ")[0]
                        + "x"
                        + data.get("Image Height").split(" ")[0],
                        data.get("Compression Type")};
                } else if (format.equals("bmp")) {
                    int xDPI = (int)(Double.parseDouble(data.get("X Pixels per Meter")) / 39.3701);
                    int yDPI = (int)(Double.parseDouble(data.get("Y Pixels per Meter")) / 39.3701);
                    if (xDPI == 0 || yDPI == 0) {
                        String xRes = data.get("X Resolution");
                        String yRes = data.get("Y Resolution");
                        if (xRes != null) {
                            xDPI = (int)(Double.parseDouble(xRes));
                        }
                        if (yRes != null) {
                            yDPI = (int)(Double.parseDouble(yRes));
                        }
                    }
                    row = new String[]{data.get("name"),
                        data.get("format"),
                        data.get("Bits Per Pixel"),
                        String.valueOf(xDPI),
                        String.valueOf(yDPI),
                        data.get("Image Width").split(" ")[0]
                        + "x"
                        + data.get("Image Height").split(" ")[0],
                        data.get("Compression")};
                }
                model.addRow(row);
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}