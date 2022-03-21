package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColoredButton btnDong;

	public AboutDialog(MainFrame frame) {
		super(frame);
				
		setSize(400, 400);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("About SachHay");
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.white);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.setEnabled(true);
			}
			@Override
			public void windowActivated(WindowEvent e) {
				frame.setEnabled(false);
			}
		});
		
		JLabel lbIcon = new JLabel(new ImageIcon("img/logo.jpg"));
		lbIcon.setOpaque(false);
		lbIcon.setVerticalAlignment(JLabel.TOP);
		
		JLabel lbSachHay = new JLabel("SachHay");
		lbSachHay.setFont(new Font("Forte", Font.BOLD, 42));
		
		JLabel lbName = new JLabel("Ứng dụng quản lý nhà Nhà Sách");
		JLabel lbVersion = new JLabel("Phiên bản 1.0");
		JLabel lbMember = new JLabel("Các thành viên thực hiện:");
		JLabel lbMember1 = new JLabel("Nguyễn Đặng Hoàng Thi");
		JLabel lbMember2 = new JLabel("Võ Thành Nhớ");
		JLabel lbMember3 = new JLabel("Bùi Sỹ Sơn");
		JLabel lbMember4 = new JLabel("Nguyễn Tuấn Thanh");
		
		lbName.setFont(UIConstant.NORMAL_FONT);
		lbVersion.setFont(UIConstant.NORMAL_FONT);
		lbMember.setFont(UIConstant.NORMAL_FONT);
		lbMember1.setFont(UIConstant.NORMAL_FONT);
		lbMember2.setFont(UIConstant.NORMAL_FONT);
		lbMember3.setFont(UIConstant.NORMAL_FONT);
		lbMember4.setFont(UIConstant.NORMAL_FONT);
		
		Box boxNorth = Box.createHorizontalBox();
		Box boxCenter = Box.createVerticalBox();
		
		boxNorth.add(Box.createHorizontalGlue());
		boxNorth.add(lbIcon);
		boxNorth.add(Box.createHorizontalStrut(5));
		boxNorth.add(lbSachHay);
		boxNorth.add(Box.createHorizontalGlue());
		
		Box boxName = Box.createHorizontalBox();
		boxName.add(Box.createHorizontalGlue());
		boxName.add(lbName);
		boxName.add(Box.createHorizontalGlue());
		
		Box boxVersion = Box.createHorizontalBox();
		boxVersion.add(Box.createHorizontalGlue());
		boxVersion.add(lbVersion);
		boxVersion.add(Box.createHorizontalGlue());
		
		Box boxMember = Box.createHorizontalBox();
		boxMember.add(Box.createHorizontalGlue());
		boxMember.add(lbMember);
		boxMember.add(Box.createHorizontalGlue());
		
		Box boxMember1 = Box.createHorizontalBox();
		boxMember1.add(Box.createHorizontalGlue());
		boxMember1.add(lbMember1);
		boxMember1.add(Box.createHorizontalGlue());
		
		Box boxMember2 = Box.createHorizontalBox();
		boxMember2.add(Box.createHorizontalGlue());
		boxMember2.add(lbMember2);
		boxMember2.add(Box.createHorizontalGlue());
		
		Box boxMember3 = Box.createHorizontalBox();
		boxMember3.add(Box.createHorizontalGlue());
		boxMember3.add(lbMember3);
		boxMember3.add(Box.createHorizontalGlue());
		
		Box boxMember4 = Box.createHorizontalBox();
		boxMember4.add(Box.createHorizontalGlue());
		boxMember4.add(lbMember4);
		boxMember4.add(Box.createHorizontalGlue());
		
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxName);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxVersion);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxMember);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxMember1);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxMember2);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxMember3);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxMember4);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(Box.createVerticalGlue());
		
		getContentPane().add(boxNorth, BorderLayout.NORTH);
		getContentPane().add(boxCenter, BorderLayout.CENTER);
		
		btnDong = new ColoredButton("Đóng", new ImageIcon("img/back.png"));
		btnDong.setBackground(UIConstant.WARNING_COLOR);
		
		Box boxButton = Box.createVerticalBox();
		Box boxButtonX = Box.createHorizontalBox();
		
		boxButton.add(Box.createVerticalStrut(5));
		boxButton.add(boxButtonX);
		boxButton.add(Box.createVerticalStrut(5));
		
		boxButtonX.add(Box.createHorizontalGlue());
		boxButtonX.add(btnDong);
		boxButtonX.add(Box.createHorizontalGlue());
		
		getContentPane().add(boxButton, BorderLayout.SOUTH);
		
		btnDong.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.dispose();
				
				frame.setEnabled(true);
				frame.setVisible(true);
			}
		});
	}
}
