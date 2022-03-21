package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dao.DatabaseConnection;

import javax.swing.UIManager.LookAndFeelInfo;

import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;

public class FlashScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;

	public FlashScreen() {
		setSize(500, 300);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setOpacity((float) 0.85);
		this.getContentPane().setBackground(new Color(255, 231, 186));
		ImageIcon icon = new ImageIcon("img/logo.jpg");
		
		setIconImage(icon.getImage());
		

	
		addControls();

		addEvent();
	}

	
	
	private void addEvent() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				
				Thread connect = new Thread( () -> {
					DatabaseConnection.getInstance();
				});
				
				Thread th = new Thread(() -> {
					for (int i = 0; i <= 100; i++) {
						int j = i;
						SwingUtilities.invokeLater(() -> {
							progressBar.setValue(j);
						});
						try {
							Thread.sleep(20);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
			
					setVisible(false);
					DangNhapFrame dangNhapFrame = new DangNhapFrame();
					dangNhapFrame.setVisible(true);
					
				});
				
				connect.start();
				th.start();
			}
		});
	}



	private void addControls() {
		Box boxMain;
		boxMain = Box.createVerticalBox();
		getContentPane().add(boxMain, BorderLayout.CENTER);
		
		
		JLabel lblIcon = new JLabel(new ImageIcon("img/logo5.png"));
		
		Box boxImg = Box.createHorizontalBox();
		boxImg.add(Box.createHorizontalGlue());
		boxImg.add(lblIcon, BorderLayout.CENTER);
		boxImg.add(Box.createHorizontalGlue());

		boxMain.add(Box.createVerticalStrut(20));
		boxMain.add(boxImg);
		boxMain.add(Box.createVerticalStrut(20));

		JLabel lblIn4 = new JLabel("Ứng dụng quản lý nhà sách SachHay Store");
		lblIn4.setFont(new Font("Times new roman", Font.BOLD, 26));
		lblIn4.setForeground(Color.DARK_GRAY);
		lblIn4.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel lbVersion = new JLabel("Phiên bản 1.0");
		
		Box boxIn4 = Box.createHorizontalBox();
		boxIn4.add(Box.createHorizontalGlue());
		boxIn4.add(lblIn4);
		boxIn4.add(Box.createHorizontalGlue());
		
		Box boxVersion = Box.createHorizontalBox();
		boxVersion.add(Box.createHorizontalGlue());
		boxVersion.add(lbVersion);
		boxVersion.add(Box.createHorizontalGlue());
		
		boxMain.add(boxIn4);
		boxMain.add(boxVersion);

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(255 ,211, 155));

		Box boxProgress = Box.createHorizontalBox();

		boxProgress.add(Box.createHorizontalStrut(20));
		boxProgress.add(progressBar);
		boxProgress.add(Box.createHorizontalStrut(20));

		boxMain.add(boxProgress);
		boxMain.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		boxMain.setForeground(new Color(255,127,0));
		
	
	}

}
