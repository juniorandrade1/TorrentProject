package ModuleView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

//import modFiles.FFile;
//import modFiles.FileController;
import ModuleFile.FileClass;
import ModuleFile.FileController;
import ModuleSystem.SystemController;


public class MainMenu {

    public static JFrame frame;
    private static JTable table_1;
    private static JPanel panel;

    final static DefaultTableModel model = new DefaultTableModel();

    private static HashMap<String, FileClass> dataTable;

    public static void refreshTable() {
        synchronized(model) {
            refreshTable(model);
        }
    }

    private static void refreshTable(DefaultTableModel model){
        model = new DefaultTableModel();
        table_1 = new JTable(model);
        table_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        table_1.setBounds(20, 56, 761, 525);
        panel.add(table_1);

        model.addColumn("Nome");
        model.addColumn("Caminho");
        model.addColumn("Tamanho");
        model.addColumn("Transferido");
        model.addRow(new Object[]{"Nome", "Caminho", "Tamanho", "Transferido"});


        dataTable = FileController.getFiles();
        for (Entry<String, FileClass> entry : dataTable.entrySet()) {
            FileClass value = entry.getValue();
            String nome = value.getName();
            String path = value.getPath();
            int size = value.getSize();
            double perc = value.getPercentage();
            String sperc = String.format("%.2f", perc);
            Object add[] = new String[]{nome, path, size + " kb", sperc + " %"};
            model.addRow(add);
        }

    }


    public static void init() {
        frame = new JFrame("File Transfer");
        frame.setBounds(100, 100, 807, 633);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        refreshTable(model);


        JButton btnInserir = new JButton("Adicionar Arquivo");
        btnInserir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int res = fc.showOpenDialog(null);
                if(res == JFileChooser.APPROVE_OPTION){
                    File arquivo = fc.getSelectedFile();
                    try {
                        FileController.addFile(arquivo.getPath());
                        JOptionPane.showMessageDialog(null, "Voce escolheu o arquivo: " + arquivo.getName());
                        refreshTable(model);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Arquivo não pode ser adicionado: " + arquivo.getName());
                        e1.printStackTrace();
                    }

                }
                else
                    JOptionPane.showMessageDialog(null, "Voce não selecionou nenhum arquivo.");
            }
        });

        btnInserir.setBounds(20, 11, 161, 30);
        panel.add(btnInserir);



        JButton btnAbrirTorrent = new JButton("Abrir Torrent");
        btnAbrirTorrent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int res = fc.showOpenDialog(null);
                if(res == JFileChooser.APPROVE_OPTION){
                    File arquivo = fc.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "Voce escolheu o arquivo: " + arquivo.getName());
                    JFileChooser fc2 = new JFileChooser();
                    int res2 = fc2.showOpenDialog(null);
                    if(res2 == JFileChooser.APPROVE_OPTION){
                        File arquivo2 = fc2.getSelectedFile();
                        try {
                            FileController.addInfoFile(arquivo.getPath(), arquivo2.getPath());
                            refreshTable(model);
                        } catch (RemoteException e1) {
                            JOptionPane.showMessageDialog(null, "Problema na adição de arquivo de torrent");
                            e1.printStackTrace();
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Voce não selecionou nenhum arquivo.");
                }
                else
                    JOptionPane.showMessageDialog(null, "Voce não selecionou nenhum arquivo.");
            }
        });

        btnAbrirTorrent.setBounds(190, 11, 161, 30);
        panel.add(btnAbrirTorrent);

        JButton btnExit = new JButton("Sair");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exit");
                try {
                    SystemController.finish();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
        btnExit.setBounds(685, 11, 96, 30);
        panel.add(btnExit);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowActivated(WindowEvent arg0) {}

            @Override
            public void windowClosed(WindowEvent arg0) {}

            @Override
            public void windowClosing(WindowEvent arg0) {
                try {
                    SystemController.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void windowDeactivated(WindowEvent arg0) {}

            @Override
            public void windowDeiconified(WindowEvent arg0) {}

            @Override
            public void windowIconified(WindowEvent arg0) {}

            @Override
            public void windowOpened(WindowEvent arg0) {}
        });
    }
}
