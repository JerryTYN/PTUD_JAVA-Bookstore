
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;

//import slideshow.Slideshow;

public class TrangChuPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	Dimension dim;
	JLabel lb1;
	JRadioButton r1, r2, r3;
	ButtonGroup bg ;
	Thread th;
	public TrangChuPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initComponents();
		
	}

	private void initComponents() {
		//
		
		JPanel pnlMain = new JPanel();
		JPanel pnlC = new JPanel();
		JPanel pnlE = new JPanel();
		JPanel pnlD = new JPanel();
		JPanel pnlA = new JPanel();
		JPanel pnlCham = new JPanel();
		JLabel lbIcon = new JLabel();
		JLabel lbNhaSach = new JLabel();
		JLabel lbDescription = new JLabel();
		JLabel lbName = new JLabel();
		
//		setLookAndFeel();
		


		setBackground(new java.awt.Color(255, 255, 255));
		setLayout(new java.awt.BorderLayout());
		
		pnlMain.setOpaque(false);
		pnlMain.setLayout(new BoxLayout(pnlMain,BoxLayout.Y_AXIS));

		pnlC.setOpaque(false);
		pnlC.setLayout(new BoxLayout(pnlC,BoxLayout.LINE_AXIS));

		lbIcon.setIcon(new ImageIcon("img/logo.jpg"));
		
		pnlC.add(lbIcon);

		pnlE.setOpaque(false);
		pnlE.setLayout(new javax.swing.BoxLayout(pnlE, javax.swing.BoxLayout.Y_AXIS));

		lbNhaSach.setFont(new Font("Forte", 0, 36)); // NOI18N
		lbNhaSach.setForeground(new Color(255,127,0));
		lbNhaSach.setText("SachHay");
		
		pnlE.add(lbNhaSach);

		lbDescription.setText("Hệ Thống Quản Lý Nhà Sách");
		lbDescription.setFont(new Font("Times New Roman", 0, 20));
		lbDescription.setForeground(new Color(255,127,0));
		pnlE.add(lbDescription);

		pnlC.add(pnlE);
		pnlC.add(new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0)));

		pnlMain.add(pnlC);
		pnlMain.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(32767,20)));
		
	
		pnlD.add(lbName);
		pnlD.setOpaque(false);
		pnlD.setLayout(new BoxLayout(pnlD,BoxLayout.LINE_AXIS));
		lbName.setText("SachHay Store");
		lbName.setFont(new Font("Forte", 0, 60));
		lbName.setForeground(new Color(255,127,0));
		pnlMain.add(pnlD);
		add(pnlMain,BorderLayout.NORTH);

		pnlA.setOpaque(false);
		pnlA.setLayout(new javax.swing.BoxLayout(pnlA, BoxLayout.Y_AXIS));

		pnlA.add(new Box.Filler(new Dimension(160, 0), new Dimension(160,0), new java.awt.Dimension(160, 32767)));
		lb1 = new JLabel(new ImageIcon("img/ban1.jpg"));

		pnlA.add(lb1);

		pnlA.add(new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0)));
		pnlA.add(new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767)));
		pnlCham.setLayout(new javax.swing.BoxLayout(pnlCham, BoxLayout.LINE_AXIS));
		pnlA.add(pnlCham,BorderLayout.EAST);
		add(pnlA,BorderLayout.CENTER);
		th = new Thread(rr);
		th.start();
	}

	
    
	Runnable rr = () ->{
		try {
			while(true) {

				th.sleep(3000);
				lb1.setIcon(new ImageIcon("img/ban2.jpg"));
				
				
				th.sleep(3000);
				lb1.setIcon(new ImageIcon("img/ban3.jpg"));

				th.sleep(3000);
				lb1.setIcon(new ImageIcon("img/ban1.jpg"));

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	};
   


}
