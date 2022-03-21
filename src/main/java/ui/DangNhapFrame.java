package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.ui.FlatOptionPaneUI;

import dao.DatabaseConnection;
import dao.NhanVienDao;
import entity.NhanVien;

public class DangNhapFrame extends JFrame implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font normalFont = new Font("Times new roman", Font.PLAIN, 16);
	private JTextField txtTenTK;
	private JPasswordField txtMatKhau;
	private ColoredButton btnDangNhap;
	private ColoredButton btnThoat;
	private NhanVienDao nv_dao;

	public DangNhapFrame() {
		setSize(900, 500);

		setLocationRelativeTo(null);

		setUndecorated(true);
		setOpacity(0.95f);

		nv_dao = new NhanVienDao();
		ImageIcon icon = new ImageIcon("img/logo.jpg");

		setIconImage(icon.getImage());

		addControls();

		addEvent();
	}

	private void addEvent() {
		btnDangNhap.addActionListener(this);
		btnThoat.addActionListener(this);
		btnDangNhap.addKeyListener(this);
		btnThoat.addKeyListener(this);
		txtMatKhau.addKeyListener(this);
		txtTenTK.addKeyListener(this);
	}

	private void addControls() {
		Box boxMain = Box.createHorizontalBox();
		getContentPane().add(boxMain, BorderLayout.CENTER);

		JPanel pnlLeft = new JPanel();
		pnlLeft.setBackground(new Color(255, 165, 79));

		JPanel pnlRight = new JPanel();
		pnlRight.setBackground(new Color(245, 245, 245));

		boxMain.add(pnlLeft);
		boxMain.add(pnlRight);

		Box boxLeft = Box.createVerticalBox();
		pnlLeft.add(boxLeft);

		JLabel lblImage = new JLabel(new ImageIcon("img\\lg2.png"));

		JLabel lblTitle = new JLabel("SachHay Store");
		lblTitle.setFont(new Font("Forte", Font.BOLD, 62));
		lblTitle.setForeground(new Color(139, 71, 38));
		JLabel lblName1 = new JLabel("Phần mềm quản lý nhà sách SachHay Store");
		lblName1.setFont(new Font("Times new roman", Font.PLAIN, 24));
		lblName1.setForeground(Color.white);
		JLabel lblName2 = new JLabel("Hệ thống quản lý nhà sách");
		lblName2.setFont(new Font("Times new roman", Font.PLAIN, 22));

		lblName2.setForeground(Color.white);
		JLabel lblName3 = new JLabel("Hiện đại - Tiện lợi - Nhanh chóng");
		lblName3.setFont(new Font("Times new roman", Font.PLAIN, 20));
		lblName3.setForeground(Color.white);

		Box boxImage = Box.createHorizontalBox();
		boxImage.add(Box.createHorizontalGlue());
		boxImage.add(lblImage);
		boxImage.add(Box.createHorizontalGlue());

		Box boxTitle = Box.createHorizontalBox();
		boxTitle.add(Box.createHorizontalGlue());
		boxTitle.add(lblTitle);
		boxTitle.add(Box.createHorizontalGlue());

		Box boxName1 = Box.createHorizontalBox();
		boxName1.add(Box.createHorizontalGlue());
		boxName1.add(lblName1);
		boxName1.add(Box.createHorizontalGlue());

		Box boxName2 = Box.createHorizontalBox();
		boxName2.add(Box.createHorizontalGlue());
		boxName2.add(lblName2);
		boxName2.add(Box.createHorizontalGlue());

		Box boxName3 = Box.createHorizontalBox();
		boxName3.add(Box.createHorizontalGlue());
		boxName3.add(lblName3);
		boxName3.add(Box.createHorizontalGlue());

		boxLeft.add(Box.createVerticalStrut(100));
		boxLeft.add(boxImage);
		boxLeft.add(boxTitle);
		boxLeft.add(boxName1);
		boxLeft.add(Box.createVerticalStrut(30));
		boxLeft.add(boxName2);
		boxLeft.add(boxName3);
		boxLeft.add(Box.createVerticalStrut(100));

		Box boxRight = Box.createVerticalBox();
		pnlRight.add(boxRight);

		JLabel lblHeader = new JLabel("Đăng nhập vào hệ thống SachHay Store");
		lblHeader.setFont(new Font("Times new roman", Font.BOLD, 20));

		Box boxHeader = Box.createHorizontalBox();
		boxHeader.add(Box.createHorizontalGlue());
		boxHeader.add(lblHeader);
		boxHeader.add(Box.createHorizontalGlue());

		Box boxForm = Box.createVerticalBox();

		JLabel lblTenTK = new JLabel("Tên tài khoản");
		lblTenTK.setFont(normalFont);
		JLabel lblMatKhau = new JLabel("Mật khẩu");
		lblMatKhau.setFont(normalFont);
		JLabel lblHinhTenTK = new JLabel(new ImageIcon("img/username.png"));
		lblHinhTenTK.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, UIConstant.LINE_COLOR));
		JLabel lblHinhMatKhau = new JLabel(new ImageIcon("img/password.png"));
		lblHinhMatKhau.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, UIConstant.LINE_COLOR));
		txtTenTK = new JTextField("NV02", 30);
		txtTenTK.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, UIConstant.LINE_COLOR));
		txtTenTK.setFont(normalFont);
		txtMatKhau = new JPasswordField("123456", 30);
		txtMatKhau.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, UIConstant.LINE_COLOR));
		txtMatKhau.setFont(normalFont);
		btnDangNhap = new ColoredButton("Đăng nhập");
		btnDangNhap.setBorderRadius(30);
		btnDangNhap.setBackground(UIConstant.PRIMARY_COLOR);
		btnDangNhap.setFont(normalFont);
		btnThoat = new ColoredButton("Thoát");
		btnThoat.setBorderRadius(30);
		btnThoat.setBackground(UIConstant.DANGER_COLOR);
		btnThoat.setFont(normalFont);

		Box boxLabelTen = Box.createHorizontalBox();
		boxLabelTen.add(lblTenTK);
		boxLabelTen.add(Box.createHorizontalGlue());

		Box boxLabelMK = Box.createHorizontalBox();
		boxLabelMK.add(lblMatKhau);
		boxLabelMK.add(Box.createHorizontalGlue());

		Box boxTenTK = Box.createHorizontalBox();
		boxTenTK.add(lblHinhTenTK);
		boxTenTK.add(txtTenTK);

		Box boxMatKhau = Box.createHorizontalBox();
		boxMatKhau.add(lblHinhMatKhau);
		boxMatKhau.add(txtMatKhau);

		Box boxButton = Box.createHorizontalBox();
		boxButton.add(btnDangNhap);
		boxButton.add(Box.createHorizontalStrut(20));
		boxButton.add(btnThoat);

		boxForm.add(boxLabelTen);
		boxForm.add(Box.createVerticalStrut(5));
		boxForm.add(boxTenTK);
		boxForm.add(Box.createVerticalStrut(20));
		boxForm.add(boxLabelMK);
		boxForm.add(Box.createVerticalStrut(5));
		boxForm.add(boxMatKhau);
		boxForm.add(Box.createVerticalStrut(20));
		boxForm.add(boxButton);

		boxRight.add(Box.createVerticalStrut(80));
		boxRight.add(boxHeader);
		boxRight.add(Box.createVerticalStrut(30));
		boxRight.add(boxForm);
		boxRight.add(Box.createVerticalStrut(80));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThoat))
			System.exit(0);
		if (o.equals(btnDangNhap))
			try {
				dangNhap();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	private void dangNhap() throws ParseException {
		if (txtTenTK.getText().isEmpty() || txtMatKhau.getPassword().length <= 0) {
			UIConstant.showWarning(DangNhapFrame.this, "Tên tài khoản và mật khẩu không được bỏ trống!");

			return;
		}
		NhanVien nv = nv_dao.getNhanVien(txtTenTK.getText());
		if (nv.getTrangThai() != 0) {
			UIConstant.showWarning(DangNhapFrame.this, "Nhân viên đang nghỉ!");
			return;
		}

		NhanVien nhanVien = nv_dao.kiemTraDangNhap(txtTenTK.getText(), String.valueOf(txtMatKhau.getPassword()));
		if (nhanVien == null) {
			UIConstant.showWarning(DangNhapFrame.this, "Tên tài khoản hoặc mật khẩu không chính xác!");
			return;
		}

		MainFrame mainFrame = new MainFrame(nhanVien);
		mainFrame.setDangNhapFrame(this);
		this.setVisible(false);

		mainFrame.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Object o = e.getSource();

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (o.equals(btnDangNhap))
				try {
					dangNhap();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (o.equals(txtMatKhau))
				try {
					dangNhap();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (o.equals(btnThoat))
				System.exit(0);
			if (o.equals(txtTenTK))
				txtTenTK.transferFocus();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed1(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o.equals(btnThoat))
			System.exit(0);
	}

	public static void main(String[] args) {
		new DangNhapFrame().setVisible(true);
	}

}
