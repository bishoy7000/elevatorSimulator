package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import elevator.Main;

import javax.swing.JMenu;
import java.awt.List;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Icon;

public class SimulationWindow {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulationWindow window = new SimulationWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SimulationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 950, 579);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel();
		label.setBounds(33, 28, 422, 318);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel();
		label_1.setBounds(485, 28, 422, 318);
		frame.getContentPane().add(label_1);
		
	        JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(157, 382, 631, 87);
			frame.getContentPane().add(scrollPane);
			table = new JTable();
			scrollPane.setViewportView(table);
			
			JLabel lblSimulationResults = new JLabel("Simulation Results");
			lblSimulationResults.setBounds(409, 357, 110, 14);
			frame.getContentPane().add(lblSimulationResults);
			
			JButton btnStartSimulation = new JButton("Start Simulation");
			btnStartSimulation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					elevator.Main main = new Main();
						try {
							main.launchSimulation();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					String[] columns = new String[] {
				            "Field","Material A", "Material B", "Material C"
				        };
				        //actual data for the table in a 2d array
				        Object[][] data = new Object[][] {
				            {"Number of transfered Boxs", main.traveledMatA, main.traveledMatB, main.traveledMatC },
				            {"Avgerage Number of transfered per Hour", (double)main.traveledMatA/Main.RUN_TIME, (double)main.traveledMatB/Main.RUN_TIME, (double)main.traveledMatC/Main.RUN_TIME},
				            {"Total Delay Time (Mins)", main.delayA, main.delayB, main.delayC },
				            {"Average Waiting Time per Box", main.delayA/main.traveledMatA, main.delayB/main.traveledMatB, main.delayC/main.traveledMatC },
				        };
						DefaultTableModel model = new DefaultTableModel(data,columns);
						table.setModel(model);
						table.getColumnModel().getColumn(0).setMinWidth(300);
						table.getColumnModel().getColumn(1).setMinWidth(1);
						table.getColumnModel().getColumn(2).setMinWidth(1);
						table.getColumnModel().getColumn(3).setMinWidth(1);	
					model.fireTableDataChanged();
					
					//show graphs
					try 
					{
						BufferedImage  img = ImageIO.read(new File("DelayChart.jpeg"));
						ImageIcon icon = new ImageIcon(img);
						label.setIcon(icon);
					}catch (Exception e1){	}
					
					try 
					{
						BufferedImage  img = ImageIO.read(new File("IdealPercentage.jpeg"));
						ImageIcon icon = new ImageIcon(img);
						label_1.setIcon(icon);
					}catch (Exception e1){	}
				}
			});
			btnStartSimulation.setBounds(399, 506, 138, 23);
			frame.getContentPane().add(btnStartSimulation);
	}
}
