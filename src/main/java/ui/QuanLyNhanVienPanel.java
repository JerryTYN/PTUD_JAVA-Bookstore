
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;



import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.toedter.calendar.JDateChooser;

import dao.NhanVienDao;
import entity.NhanVien;
import net.java.balloontip.BalloonTip;

public class QuanLyNhanVienPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultTableModel defaultTable;
	private CustomTable tableNV;
	private JScrollPane scroll;
	private JPasswordField txtNewPasssWord;
	private JPasswordField txtConfirmPassWord;
	private ColoredButton btnHide;
	private ColoredButton btnChange;
	private JComboBox<String> jcbLoaiNV;
	private ColoredButton btnThem;
	private ColoredButton btnSua;
	private ColoredButton btnQuayLai;
	private ColoredButton btnXoa;
	private JTextField txtMaNV;
	private JTextField txtTenNV;
	private JTextField txtDiaChi;
	private JTextField txtEmail;
	private JTextField txtSoDienThoai;
	private JDateChooser dateNgayVaoLam;
	private JDateChooser dateNgaySinh;
	private JComboBox<String> jcbGioiTinh;
	private ColoredButton btnTim;
	private NhanVienDao dao_NV;
	private MainFrame mainFrame;
	private JTextField txtTKTenNV;
	private JTextField txtTKEmail;
	private JTextField txtTKSoDienThoai;
	private JDateChooser dateTKNgayVaoLam;
	private JComboBox<String> jcbTKLoaiNV;
	private JComboBox<String> jcbTKTrangThaiNV;
	private JComboBox<String> jcbTrangThai;
	private ColoredButton btnXoaRong2;
	private ColoredButton btnXoaRong1;
	private List<NhanVien> dsNVs;
	private int currentIndex = 0;
	private BalloonTip ballNgaySinh;
	private BalloonTip ballTenNV;
	private BalloonTip ballNgayVaoLam;
	private BalloonTip ballDiaChi;
	private BalloonTip ballEmail;
	private BalloonTip ballSoDienThoai;
	private static int year, month, date, yearFuture, leapOrNotLeap;
	private JTabbedPane tabPaneTT;

	public QuanLyNhanVienPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());
		setBackground(Color.white);

		addNorth();
		addCenter();
		addEvent();

		getAllComponents(this).forEach(item -> {
			item.addKeyListener(new KeyAdapter() {
				private boolean isCtrlPressed = false;

				@Override
				public void keyPressed(KeyEvent e) {
					if (isCtrlPressed) {
						//Ctrl + N
						if (e.getKeyCode() == KeyEvent.VK_N)
							btnThem.doClick();
						else if (e.getKeyCode() == KeyEvent.VK_X)
							btnXoa.doClick();
						else if (e.getKeyCode() == KeyEvent.VK_LEFT)
							btnQuayLai.doClick();
						else if (e.getKeyCode() == KeyEvent.VK_E) {
							btnXoaRong1.doClick();
							btnXoaRong2.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_F)
							btnTim.doClick();

					} else if (e.getKeyCode() == KeyEvent.VK_CONTROL)
						isCtrlPressed = true;
					else
						isCtrlPressed = false;
				}

				@Override
				public void keyReleased(KeyEvent e) {
					isCtrlPressed = false;
				}
			});
		});

		dao_NV = new NhanVienDao();
		loadData();

	}

	public Component getDefaultFocusComponent() {
		return txtTenNV;
	}

	public List<Component> getAllComponents(Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	private void taiDuLieuLenBang(List<NhanVien> dsNV) {
		dsNVs = dsNV;
		defaultTable.getDataVector().removeAllElements();
		defaultTable.fireTableDataChanged();
		new Thread(() -> {

			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			for (NhanVien nhanVien : dsNVs) {

				SwingUtilities.invokeLater(() -> {
					String loainv = "";
					if (nhanVien.getLoaiNhanVien() == 0)
						loainv = "Quản lý viên";
					else
						loainv = "Nhân viên bán hàng";

					String trangThai = "";
					if (nhanVien.getTrangThai() == 0)
						trangThai = "Đang làm";
					else if (nhanVien.getTrangThai() == 1)
						trangThai = "Tạm nghỉ";

					String gioitinh = "";
					if (nhanVien.isGioiTinh() == true)
						gioitinh = "Nam";
					else
						gioitinh = "Nữ";

					defaultTable.addRow(new Object[] { nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(),
							nhanVien.getNgaySinh().format(format), gioitinh, nhanVien.getEmail(),
							nhanVien.getSoDienThoai(), nhanVien.getNgayVaoLam().format(format), loainv, trangThai,
							nhanVien.getMatKhau() });
				});

			}
		}).start();
		;

	}

	private void loadData() {
		dsNVs = dao_NV.lay20NhanVienGanDay();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		for (NhanVien nhanVien : dsNVs) {
			String loainv = "";
			if (nhanVien.getLoaiNhanVien() == 0)
				loainv = "Quản lý viên";
			else
				loainv = "Nhân viên bán hàng";

			String trangThai = "";
			if (nhanVien.getTrangThai() == 0)
				trangThai = "Đang làm";
			else
				trangThai = "Tạm nghỉ";

			String gioitinh = "";
			if (nhanVien.isGioiTinh() == true)
				gioitinh = "Nam";
			else
				gioitinh = "Nữ";

			defaultTable.addRow(new Object[] { nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(),
					nhanVien.getNgaySinh().format(format), gioitinh, nhanVien.getEmail(), nhanVien.getSoDienThoai(),
					nhanVien.getNgayVaoLam().format(format), loainv, trangThai, nhanVien.getMatKhau() });
		}
	}

	private void addNorth() {

		JPanel pnlTitle = new JPanel();
		pnlTitle.setOpaque(false);
		JLabel lblHeader = new JLabel("Quản Lý Nhân Viên");
		lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
		lblHeader.setHorizontalAlignment(JLabel.CENTER);
		lblHeader.setForeground(new Color(255, 127, 0));

		pnlTitle.add(lblHeader);

		JPanel pnlNorth = new JPanel();
		this.add(pnlNorth = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorth.setOpaque(false);

		tabPaneTT = new JTabbedPane();
		tabPaneTT.setOpaque(false);
		tabPaneTT.setPreferredSize(new Dimension(100, 250));

		pnlNorth.add(Box.createVerticalStrut(15));

		pnlNorth.add(pnlTitle, BorderLayout.NORTH);
		pnlNorth.add(tabPaneTT, BorderLayout.CENTER);

		// Tab Thông tin
		Box boxLine1, boxLine2, boxLine3, boxLine4, boxThongTin;
		boxLine1 = Box.createHorizontalBox();
		boxLine2 = Box.createHorizontalBox();
		boxLine3 = Box.createHorizontalBox();
		boxLine4 = Box.createHorizontalBox();
		boxThongTin = Box.createVerticalBox();

		JLabel lblMaNV = new JLabel("Mã nhân viên:");
		JLabel lblTenNV = new JLabel("Tên nhân viên:");
		JLabel lblNgaySinh = new JLabel("Ngày sinh:");
		JLabel lblTrangThai = new JLabel("Trạng thái:");
		JLabel lblGioiTinh = new JLabel("Giới tính:");
		JLabel lblLoaiNV = new JLabel("Loại nhân viên:");
		JLabel lblEmail = new JLabel("Email:");
		JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
		JLabel lblNgayVaoLam = new JLabel("Ngày vào làm:");

		lblMaNV.setFont(UIConstant.NORMAL_FONT);
		lblMaNV.setPreferredSize(new Dimension(90, 20));
		lblTenNV.setFont(UIConstant.NORMAL_FONT);
		lblTenNV.setPreferredSize(new Dimension(90, 20));
		lblNgaySinh.setFont(UIConstant.NORMAL_FONT);
		lblNgaySinh.setPreferredSize(new Dimension(90, 20));
		lblTrangThai.setFont(UIConstant.NORMAL_FONT);
		lblTrangThai.setPreferredSize(new Dimension(70, 20));
		lblGioiTinh.setFont(UIConstant.NORMAL_FONT);
		lblGioiTinh.setPreferredSize(new Dimension(70, 20));
		lblLoaiNV.setFont(UIConstant.NORMAL_FONT);
		lblLoaiNV.setPreferredSize(new Dimension(90, 20));
		lblEmail.setFont(UIConstant.NORMAL_FONT);
		lblEmail.setPreferredSize(new Dimension(90, 20));
		lblSoDienThoai.setFont(UIConstant.NORMAL_FONT);
		lblSoDienThoai.setPreferredSize(new Dimension(90, 20));
		lblNgayVaoLam.setFont(UIConstant.NORMAL_FONT);
		lblNgayVaoLam.setPreferredSize(new Dimension(90, 20));

		txtMaNV = new JTextField();
		txtMaNV.setEditable(false);
		txtMaNV.setFont(UIConstant.NORMAL_FONT);
		txtTenNV = new JTextField();
		txtTenNV.setFont(UIConstant.NORMAL_FONT);
		txtDiaChi = new JTextField();
		txtDiaChi.setFont(UIConstant.NORMAL_FONT);
		txtEmail = new JTextField();
		txtEmail.setFont(UIConstant.NORMAL_FONT);
		txtSoDienThoai = new JTextField();
		txtSoDienThoai.setFont(UIConstant.NORMAL_FONT);

		dateNgayVaoLam = new JDateChooser();
		dateNgayVaoLam.setDateFormatString("dd-MM-yyyy");
		dateNgayVaoLam.setFont(UIConstant.NORMAL_FONT);
		dateNgaySinh = new JDateChooser();
		dateNgaySinh.setDateFormatString("dd-MM-yyyy");
		dateNgaySinh.setFont(UIConstant.NORMAL_FONT);

		jcbTrangThai = new JComboBox<String>();
		jcbTrangThai.setFont(UIConstant.NORMAL_FONT);
		jcbTrangThai.addItem("Tạm nghỉ");
		jcbTrangThai.addItem("Đang làm");
		jcbTrangThai.setPreferredSize(new Dimension(100, 20));
		jcbGioiTinh = new JComboBox<String>();
		jcbGioiTinh.setFont(UIConstant.NORMAL_FONT);
		jcbGioiTinh.addItem("Nam");
		jcbGioiTinh.addItem("Nữ");
		jcbGioiTinh.setPreferredSize(new Dimension(100, 20));
		jcbLoaiNV = new JComboBox<String>();
		jcbLoaiNV.setFont(UIConstant.NORMAL_FONT);
		jcbLoaiNV.setPreferredSize(new Dimension(200, 20));
		jcbLoaiNV.addItem("Nhân viên bán hàng");
		jcbLoaiNV.addItem("Quản lý viên");

		boxLine1.add(Box.createHorizontalStrut(10));
		boxLine1.add(lblMaNV);
		boxLine1.add(txtMaNV);
		boxLine1.add(Box.createHorizontalStrut(10));
		boxLine1.add(lblNgaySinh);
		boxLine1.add(dateNgaySinh);
		dateNgaySinh.setPreferredSize(new Dimension(100, 20));
		txtMaNV.setPreferredSize(new Dimension(390, 20));
		boxLine1.add(Box.createHorizontalStrut(10));

		boxLine2.add(Box.createHorizontalStrut(10));
		boxLine2.add(lblTenNV);
		boxLine2.add(txtTenNV);
		boxLine2.add(Box.createHorizontalStrut(10));
		boxLine2.add(lblNgayVaoLam);
		boxLine2.add(dateNgayVaoLam);
		dateNgayVaoLam.setPreferredSize(new Dimension(100, 20));
		txtTenNV.setPreferredSize(new Dimension(390, 20));
		boxLine2.add(Box.createHorizontalStrut(10));

		boxLine3.add(Box.createHorizontalStrut(10));
		boxLine3.add(lblEmail);
		boxLine3.add(txtEmail);
		boxLine3.add(Box.createHorizontalStrut(10));
		boxLine3.add(lblGioiTinh);
		boxLine3.add(jcbGioiTinh);
		boxLine3.add(Box.createHorizontalStrut(10));

		boxLine4.add(Box.createHorizontalStrut(10));
		boxLine4.add(lblSoDienThoai);
		boxLine4.add(txtSoDienThoai);
		boxLine4.add(Box.createHorizontalStrut(10));
		boxLine4.add(lblTrangThai);
		boxLine4.add(jcbTrangThai);
		boxLine4.add(Box.createHorizontalStrut(10));
		boxLine4.add(lblLoaiNV);
		boxLine4.add(jcbLoaiNV);
		txtEmail.setPreferredSize(new Dimension(170, 20));
		txtSoDienThoai.setPreferredSize(new Dimension(90, 20));
		boxLine4.add(Box.createHorizontalStrut(10));

		boxThongTin.add(Box.createVerticalStrut(10));
		boxThongTin.add(boxLine1);
		boxThongTin.add(Box.createVerticalStrut(10));
		boxThongTin.add(boxLine2);
		boxThongTin.add(Box.createVerticalStrut(10));
		boxThongTin.add(boxLine3);
		boxThongTin.add(Box.createVerticalStrut(10));
		boxThongTin.add(boxLine4);
		boxThongTin.add(Box.createVerticalStrut(10));

		Box boxButton = Box.createHorizontalBox();
		btnThem = addButtonTo(boxButton, "Thêm nhân viên", "img/add.png", UIConstant.PRIMARY_COLOR);
		btnThem.setBorderRadius(30);
		btnSua = addButtonTo(boxButton, "Sửa nhân viên", "img/modify.png", UIConstant.PRIMARY_COLOR);
		btnSua.setBorderRadius(30);

		btnXoa = addButtonTo(boxButton, "Xoá nhân viên", "img/delete.png", UIConstant.DANGER_COLOR);
		btnXoa.setBorderRadius(30);
		btnXoaRong1 = addButtonTo(boxButton, "Làm mới", "img/refresh3.png", new Color(0x019474D));
		btnXoaRong1.setBorderRadius(30);
		boxThongTin.add(Box.createVerticalStrut(5));
		boxThongTin.add(boxButton);
		boxThongTin.add(Box.createVerticalStrut(10));

		ballNgaySinh = new BalloonTip(dateNgaySinh, "Ngày sinh phải trước ngày hiện tại");
		ballNgaySinh.setVisible(false);
		ballNgaySinh.setCloseButton(null);
		ballTenNV = new BalloonTip(txtTenNV, " + Họ và tên nhân viên phải bắt đầu chữ cái in hoa \n"

				+ " + Không chứa các ký tự đặc biệt và số");
		ballTenNV.setVisible(false);
		ballTenNV.setCloseButton(null);
		ballNgayVaoLam = new BalloonTip(dateNgayVaoLam, "Ngày vào làm phải trước ngày hiện tại và sau ngày sinh");
		ballNgayVaoLam.setVisible(false);
		ballNgayVaoLam.setCloseButton(null);
		ballDiaChi = new BalloonTip(txtDiaChi, "Địa chỉ không được rỗng");
		ballDiaChi.setVisible(false);
		ballDiaChi.setCloseButton(null);
		ballEmail = new BalloonTip(txtEmail,
				" + Email phải bắt đầu bằng 1 ký tự \n" + "+ Chỉ chứa ký tự a-z, 0-9 và ký tự dấu chấm (.) \n"
						+ "+ Độ dài tối thiểu là 8, độ dài tối đa là 32 \n"
						+ "+ Tên miền có thể là tên miền cấp 1 hoặc cấp 2");
		ballEmail.setVisible(false);
		ballEmail.setCloseButton(null);
		ballSoDienThoai = new BalloonTip(txtSoDienThoai, "Số điện thoại không được rỗng, gồm 10 số, bắt đầu bằng số 0");
		ballSoDienThoai.setVisible(false);
		ballSoDienThoai.setCloseButton(null);

		// Tab tìm kiếm

		Box boxLineTK2, boxLineTK3, boxLineTK4, boxTimKiem;
		boxLineTK2 = Box.createHorizontalBox();
		boxLineTK3 = Box.createHorizontalBox();
		boxLineTK4 = Box.createHorizontalBox();
		boxTimKiem = Box.createVerticalBox();

		JLabel lblTKTenNV = new JLabel("Tên nhân viên:");
		JLabel lblTKLoaiNV = new JLabel("Loại nhân viên:");
		JLabel lblTKTrangThaiNV = new JLabel("Trạng thái:");
		JLabel lblTKEmail = new JLabel("Email:");
		JLabel lblTKSoDienThoai = new JLabel("Số điện thoại:");
		JLabel lblTKNgayVaoLam = new JLabel("Ngày vào làm:");

		lblTKTenNV.setFont(UIConstant.NORMAL_FONT);
		lblTKTenNV.setPreferredSize(new Dimension(90, 20));
		lblTKTrangThaiNV.setFont(UIConstant.NORMAL_FONT);
		lblTKTrangThaiNV.setPreferredSize(new Dimension(90, 20));
		lblTKLoaiNV.setFont(UIConstant.NORMAL_FONT);
		lblTKLoaiNV.setPreferredSize(new Dimension(90, 20));
		lblTKEmail.setFont(UIConstant.NORMAL_FONT);
		lblTKEmail.setPreferredSize(new Dimension(90, 20));
		lblTKSoDienThoai.setFont(UIConstant.NORMAL_FONT);
		lblTKSoDienThoai.setPreferredSize(new Dimension(90, 20));
		lblTKNgayVaoLam.setFont(UIConstant.NORMAL_FONT);
		lblTKNgayVaoLam.setPreferredSize(new Dimension(90, 20));

		txtTKTenNV = new JTextField();
		txtTKTenNV.setFont(UIConstant.NORMAL_FONT);
		txtTKEmail = new JTextField();
		txtTKEmail.setFont(UIConstant.NORMAL_FONT);
		txtTKSoDienThoai = new JTextField();
		txtTKSoDienThoai.setFont(UIConstant.NORMAL_FONT);

		dateTKNgayVaoLam = new JDateChooser();
		dateTKNgayVaoLam.setDateFormatString("dd-MM-yyyy");
		dateTKNgayVaoLam.setFont(UIConstant.NORMAL_FONT);

		jcbTKTrangThaiNV = new JComboBox<String>();
		jcbTKTrangThaiNV.setFont(UIConstant.NORMAL_FONT);
		jcbTKTrangThaiNV.addItem("");
		jcbTKTrangThaiNV.addItem("Đang làm");
		jcbTKTrangThaiNV.addItem("Tạm nghỉ");

		jcbTKLoaiNV = new JComboBox<String>();
		jcbTKLoaiNV.setFont(UIConstant.NORMAL_FONT);
		jcbTKLoaiNV.addItem("");
		jcbTKLoaiNV.addItem("Nhân viên bán hàng");
		jcbTKLoaiNV.addItem("Quản lý viên");

		boxLineTK2.add(Box.createHorizontalStrut(50));
		boxLineTK2.add(lblTKTenNV);
		boxLineTK2.add(txtTKTenNV);
		boxLineTK2.add(Box.createHorizontalStrut(30));
		boxLineTK2.add(lblTKNgayVaoLam);
		boxLineTK2.add(dateTKNgayVaoLam);
		dateTKNgayVaoLam.setPreferredSize(new Dimension(100, 20));
		txtTKTenNV.setPreferredSize(new Dimension(390, 20));
		boxLineTK2.add(Box.createHorizontalStrut(50));

		boxLineTK3.add(Box.createHorizontalStrut(50));
		boxLineTK3.add(lblTKEmail);
		boxLineTK3.add(txtTKEmail);
		boxLineTK3.add(Box.createHorizontalStrut(30));
		boxLineTK3.add(lblTKLoaiNV);
		boxLineTK3.add(jcbTKLoaiNV);
		jcbTKLoaiNV.setPreferredSize(new Dimension(150, 20));
		boxLineTK3.add(Box.createHorizontalStrut(50));

		boxLineTK4.add(Box.createHorizontalStrut(50));
		boxLineTK4.add(lblTKSoDienThoai);
		boxLineTK4.add(txtTKSoDienThoai);
		txtTKSoDienThoai.setPreferredSize(new Dimension(390, 20));
		boxLineTK4.add(Box.createHorizontalStrut(30));
		boxLineTK4.add(lblTKTrangThaiNV);
		boxLineTK4.add(jcbTKTrangThaiNV);
		jcbTKTrangThaiNV.setPreferredSize(new Dimension(150, 20));
		boxLineTK4.add(Box.createHorizontalStrut(50));

		boxTimKiem.add(Box.createVerticalStrut(20));
		boxTimKiem.add(boxLineTK2);
		boxTimKiem.add(Box.createVerticalStrut(20));
		boxTimKiem.add(boxLineTK3);
		boxTimKiem.add(Box.createVerticalStrut(20));
		boxTimKiem.add(boxLineTK4);
		boxTimKiem.add(Box.createVerticalStrut(20));

		Box boxTKButton = Box.createHorizontalBox();
		btnTim = addButtonTo(boxTKButton, "Tìm kiếm", "img/search.png", UIConstant.PRIMARY_COLOR);
		btnTim.setBorderRadius(30);
		btnXoaRong2 = addButtonTo(boxTKButton, "Làm mới", "img/refresh3.png", new Color(0x019474D));
		btnXoaRong2.setBorderRadius(30);
		boxTimKiem.add(Box.createVerticalStrut(5));
		boxTimKiem.add(boxTKButton);
		boxTimKiem.add(Box.createVerticalStrut(10));

		tabPaneTT.addTab("Tìm kiếm", boxTimKiem);
		tabPaneTT.add("Cập nhật nhân viên", boxThongTin);
		tabPaneTT.setFont(new Font("Arial", Font.BOLD, 12));

	}

	private void addCenter() {

		// Center
		JPanel pnlCenter = new JPanel();
		this.add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenter.setOpaque(false);

		JTabbedPane tabPaneNCC = new JTabbedPane();
		tabPaneNCC.setOpaque(false);
		tabPaneNCC.setPreferredSize(new Dimension(100, 200));

		pnlCenter.add(tabPaneNCC, BorderLayout.CENTER);

		String[] header = { "Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Giới tính", "Email", "Số điện thoại",
				"Ngày vào làm", "Loại nhân viên", "Trạng thái" };
		defaultTable = new DefaultTableModel(header, 0);
		tableNV = new CustomTable();
		tableNV.setModel(defaultTable);
		scroll = new JScrollPane(tableNV, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scroll.getViewport().setBackground(Color.white);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		tabPaneNCC.addTab("Danh sách nhân viên", scroll);
		tabPaneNCC.setFont(new Font("Arial", Font.BOLD, 12));

		JPanel pnlSouth = new JPanel(new BorderLayout());

		Box boxLineButton;

		boxLineButton = Box.createHorizontalBox();
		btnQuayLai = addButtonTo(boxLineButton, "Quay Lại", "img/back.png", UIConstant.PRIMARY_COLOR);
		btnQuayLai.setBorderRadius(30);
		pnlSouth.setOpaque(false);
		pnlSouth.add(boxLineButton, BorderLayout.EAST);
		pnlCenter.add(pnlSouth, BorderLayout.SOUTH);

	}

	private void addEvent() {
		this.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("ancestor")) {
					tabPaneTT.setSelectedIndex(0);
				}
			}
		});

		dateNgaySinh.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (dateNgaySinh.getDate() == null || dateNgaySinh.getDate().after(new Date()))
					ballNgaySinh.setVisible(true);
				else
					ballNgaySinh.setVisible(false);
			}
		});

		txtTenNV.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtTenNV.getText().isEmpty() || !txtTenNV.getText()
						.matches("[\\p{Lu}[A-Z]][\\p{L}[a-z]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z]]*)*"))
					ballTenNV.setVisible(true);
				else
					ballTenNV.setVisible(false);
			}
		});

		dateNgayVaoLam.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (dateNgayVaoLam.getDate() == null || dateNgayVaoLam.getDate().after(new Date())
						|| dateNgayVaoLam.getDate().before(dateNgaySinh.getDate()))
					ballNgayVaoLam.setVisible(true);
				else
					ballNgayVaoLam.setVisible(false);
			}
		});

		txtDiaChi.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtDiaChi.getText().isEmpty())
					ballDiaChi.setVisible(true);
				else
					ballDiaChi.setVisible(false);
			}
		});

		txtEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!txtEmail.getText().isEmpty())
					if (!txtEmail.getText().matches("^[a-z][a-z0-9\\.]{7,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$"))
						ballEmail.setVisible(true);
					else
						ballEmail.setVisible(false);
			}
		});

		txtSoDienThoai.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtSoDienThoai.getText().isEmpty() || !txtSoDienThoai.getText().matches("0\\d{9}"))
					ballSoDienThoai.setVisible(true);
				else
					ballSoDienThoai.setVisible(false);
			}
		});

		txtTKEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timNV();
			}
		});

		txtTKSoDienThoai.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timNV();
			}
		});

		txtTKTenNV.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timNV();
			}
		});

		tableNV.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					if (tableNV.getSelectedRow() == -1)
						return;
					int row = tableNV.getSelectedRow();

					String temp = defaultTable.getValueAt(row, 2).toString();
					Date date = new SimpleDateFormat("dd-MM-yyyy").parse(temp);

					String temp2 = defaultTable.getValueAt(row, 6).toString();
					Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(temp2);

					txtMaNV.setText(defaultTable.getValueAt(row, 0).toString());
					txtTenNV.setText(defaultTable.getValueAt(row, 1).toString());
					dateNgaySinh.setDate(date);
					jcbGioiTinh.setSelectedItem(defaultTable.getValueAt(row, 3));
					txtEmail.setText(defaultTable.getValueAt(row, 4).toString());
					txtSoDienThoai.setText(defaultTable.getValueAt(row, 5).toString());
					jcbLoaiNV.setSelectedItem(defaultTable.getValueAt(row, 7));
					dateNgayVaoLam.setDate(date2);
					jcbTrangThai.setSelectedItem(defaultTable.getValueAt(row, 8));

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		btnThem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (validData()) {
					boolean gt;
					if (jcbGioiTinh.getSelectedItem().toString().equals("Nam"))
						gt = true;
					else
						gt = false;

					int lnv;
					if (jcbLoaiNV.getSelectedItem().toString().equalsIgnoreCase("Nhân viên bán hàng"))
						lnv = 1;
					else
						lnv = 0;
					int tt;

					if (jcbTrangThai.getSelectedItem().toString().equalsIgnoreCase("Đang làm"))
						tt = 0;
					else
						tt = 1;

					Date dateNS = dateNgaySinh.getDate();
					LocalDate lcdNS = dateNS.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					System.out.println(lcdNS);
					LocalDate now = LocalDate.now();
					System.out.println(now);

					Period tuoi = Period.between(lcdNS, now);
					Date dateNVL = dateNgayVaoLam.getDate();
					LocalDate lcdNVL = dateNVL.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					Period tgLam = Period.between(lcdNS, lcdNVL);
					NhanVien nv = new NhanVien(txtTenNV.getText(), lcdNS, tt, gt,
							txtEmail.getText() == "" ? null : txtEmail.getText(), txtSoDienThoai.getText(), lcdNVL,
							lnv);
					if (tuoi.getYears() < 18) {
						UIConstant.showInfo(QuanLyNhanVienPanel.this, "Nhân viên chưa đủ 18 tuổi");
						return;
					} else if (tgLam.getYears() < 18) {
						UIConstant.showInfo(QuanLyNhanVienPanel.this, " Nhân viên vào làm phải đủ 18 tuổi");
						return;
					} else if (!kiemtraTrungEmail()) {
						UIConstant.showInfo(QuanLyNhanVienPanel.this,
								"Email " + txtEmail.getText() + " đã được sử dụng");
						txtEmail.requestFocus(true);
						txtEmail.selectAll();
					} else if (!kiemtraTrungSDT()) {
						UIConstant.showInfo(QuanLyNhanVienPanel.this,
								"Số điện thoại " + txtSoDienThoai.getText() + " đã được sử dụng");
						txtSoDienThoai.requestFocus(true);
						txtSoDienThoai.selectAll();
					}

					else if (dao_NV.addNhanVien(nv)) {

						String loainv = "";
						if (lnv == 0)
							loainv = "Quản lý viên";
						else
							loainv = "Nhân viên bán hàng";

						String trangthainv = "";
						if (tt == 0)
							trangthainv = "Đang làm";
						else
							trangthainv = "Tạm nghỉ";

						String gioitinh = "";
						if (gt == true)
							gioitinh = "Nam";
						else
							gioitinh = "Nữ";

						DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

						UIConstant.showInfo(QuanLyNhanVienPanel.this, "Thêm thành công");
						defaultTable.addRow(new Object[] { nv.getMaNhanVien(), nv.getTenNhanVien(),
								nv.getNgaySinh().format(format), gioitinh, nv.getEmail(), nv.getSoDienThoai(),
								nv.getNgayVaoLam().format(format), loainv, trangthainv, nv.getMatKhau() });
						XoaRong();

					} else
						UIConstant.showWarning(QuanLyNhanVienPanel.this, "Thêm không thành công");

				}
			}
		});

		btnXoa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tableNV.getSelectedRow();
				if (row == -1) {
					UIConstant.showInfo(QuanLyNhanVienPanel.this, "Chưa chọn nhân viên!");
					return;
				}

				if (JOptionPane.showConfirmDialog(QuanLyNhanVienPanel.this, "Bạn có chắc là muốn xóa!", "Xác nhận",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}

				if (dao_NV.deleteNhanVien(txtMaNV.getText().toString())) {

					UIConstant.showInfo(QuanLyNhanVienPanel.this, "Xoá thành công");

					defaultTable.removeRow(row);
					XoaRong();
				} else {
					UIConstant.showWarning(QuanLyNhanVienPanel.this, "Xoá không thành công");

				}
			}
		});

		btnSua.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabPaneTT.getSelectedIndex() == 0)
					tabPaneTT.setSelectedIndex(1);
				else {
					int row = tableNV.getSelectedRow();
					if (row == -1) {
						UIConstant.showInfo(QuanLyNhanVienPanel.this, "Chưa chọn nhân viên!");
						return;
					} else {
						if (validData()) {
							boolean gt;
							if (jcbGioiTinh.getSelectedItem().toString().equals("Nam"))
								gt = true;
							else
								gt = false;

							int lnv;
							if (jcbLoaiNV.getSelectedItem().toString().equalsIgnoreCase("Quản lý viên"))
								lnv = 0;
							else
								lnv = 1;

							int tt;

							if (jcbTrangThai.getSelectedItem().toString().equalsIgnoreCase("Đang làm"))
								tt = 0;
							else
								tt = 1;

							LocalDate ngaySinh = null;
							Date date = dateNgaySinh.getDate();
							if (date != null) {
								SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
								String str_date = format1.format(date);

								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								// convert String to LocalDate
								ngaySinh = LocalDate.parse(str_date, formatter);
							}

							LocalDate ngayVaoLam = null;
							Date dateNVL = dateNgayVaoLam.getDate();
							if (dateNVL != null) {
								SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
								String str_date = format1.format(dateNVL);

								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								// convert String to LocalDate
								ngayVaoLam = LocalDate.parse(str_date, formatter);
							}

							String manv = tableNV.getValueAt(tableNV.getSelectedRow(), 0).toString();

							NhanVien nv = dao_NV.getNhanVien(manv);

							NhanVien nvs = new NhanVien(txtMaNV.getText(), txtTenNV.getText(), ngaySinh, tt, gt,
									txtEmail.getText(), txtSoDienThoai.getText(), ngayVaoLam, lnv, nv.getMatKhau());

							nvs.setMaNhanVien(manv);
							String tam = nvs.getSoDienThoai();
							String tam1 = nvs.getEmail();
							for (NhanVien nhanV : dsNVs) {
								if (!manv.contains(nhanV.getMaNhanVien())) {
									if (tam1.contains(nhanV.getEmail())) {
										UIConstant.showWarning(null,
												"Nhân viên có email " + txtEmail.getText() + " đã được sử dụng");
										txtEmail.requestFocus();
										txtEmail.selectAll();
										return;
									} else if (tam.contains(nhanV.getSoDienThoai())) {
										UIConstant.showWarning(null, "Nhân viên có số điện thoại "
												+ txtSoDienThoai.getText() + " đã tồn tại");
										txtSoDienThoai.requestFocus();
										txtSoDienThoai.selectAll();
										return;
									}
								}
							}

							if (dao_NV.updateNhanVien(nvs)) {

								String loainv = "";
								if (lnv == 0)
									loainv = "Quản lý viên";
								else
									loainv = "Nhân viên bán hàng";

								String trangthainv = "";
								if (tt == 0)
									trangthainv = "Đang làm";
								else
									trangthainv = "Tạm nghỉ";

								String gioitinh = "";
								if (gt == true)
									gioitinh = "Nam";
								else
									gioitinh = "Nữ";

								LocalDate localNS = nvs.getNgaySinh();
								DateTimeFormatter fTimeFormatterNS = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								String fStringNS = localNS.format(fTimeFormatterNS);
								LocalDate now = LocalDate.now();
								System.out.println(now);

								Period tuoi = Period.between(localNS, now);
								
								if (tuoi.getYears() < 18) {
									UIConstant.showInfo(QuanLyNhanVienPanel.this, "Nhân viên chưa đủ 18 tuổi");
									return;
								}
								
								
								LocalDate localNVL = nvs.getNgayVaoLam();
								DateTimeFormatter fTimeFormatterNVL = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								String fStringNVL = localNVL.format(fTimeFormatterNVL);
								Period tgLam = Period.between(localNS, localNVL);
								if (tgLam.getYears() < 18) {
									UIConstant.showInfo(QuanLyNhanVienPanel.this, "Nhân viên chưa đủ tuổi vào làm");
									return;
								}
								UIConstant.showInfo(QuanLyNhanVienPanel.this, "Sửa thành công");

								defaultTable.removeRow(tableNV.getSelectedRow());

								defaultTable.addRow(new Object[] { nv.getMaNhanVien(), nv.getTenNhanVien(), fStringNS,
										gioitinh, nv.getEmail(), nv.getSoDienThoai(), fStringNVL, loainv, trangthainv,
										nv.getMatKhau() });
								timNV();
								XoaRong();
							} else
								UIConstant.showWarning(QuanLyNhanVienPanel.this, "Sửa không thành công");
						}
					}
				}
			}
		});

		btnTim.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				timNV();
			}
		});

		btnQuayLai.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCenter(mainFrame.getTrangChuPanel());
			}
		});

		btnXoaRong1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				XoaRong();

			}
		});

		btnXoaRong2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timNV();
				XoaRongTK();

			}
		});

	}

	private void timNV() {
		new Thread(() -> {
			List<NhanVien> list = new ArrayList<NhanVien>();

			int lnv;

			if (jcbTKLoaiNV.getSelectedItem().toString().equalsIgnoreCase("Quản lý viên"))
				lnv = 0;
			else if (jcbTKLoaiNV.getSelectedItem().toString().equalsIgnoreCase("Nhân viên bán hàng"))
				lnv = 1;
			else
				lnv = 2;

			int tt;

			if (jcbTKTrangThaiNV.getSelectedItem().toString().equalsIgnoreCase("Đang làm"))
				tt = 0;
			else if (jcbTKTrangThaiNV.getSelectedItem().toString().equalsIgnoreCase("Tạm nghỉ"))
				tt = 1;
			else
				tt = 3;

			LocalDate lcdNVL = null;

			if (dateTKNgayVaoLam.getDate() != null)
				lcdNVL = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateTKNgayVaoLam.getDate()));

			list = dao_NV.findNhanVien(txtTKTenNV.getText(), tt, txtTKEmail.getText(), txtTKSoDienThoai.getText(),
					lcdNVL, lnv);

			if (list.size() != 0) {
				taiDuLieuLenBang(list);

			} else {
				dsNVs.clear();
				defaultTable.setRowCount(0);
			}
		}).start();
		;

	}

	private ColoredButton addButtonTo(Box box, String name, String path, Color color) {
		ColoredButton btn = new ColoredButton(name, new ImageIcon(path));
		btn.setFont(UIConstant.NORMAL_FONT);
		btn.setBackground(color);

		Box boxButton = Box.createHorizontalBox();
		boxButton.add(btn);
		boxButton.add(Box.createHorizontalStrut(10));

		box.add(boxButton);

		return btn;
	}

	private JPasswordField addInputItemTo(Box box, String name) {
		JLabel label = new JLabel(name);
		label.setPreferredSize(new Dimension(110, 25));
		label.setFont(UIConstant.NORMAL_FONT);
		JPasswordField text = new JPasswordField();

		Box boxItem = Box.createHorizontalBox();
		boxItem.add(Box.createHorizontalStrut(15));
		boxItem.add(Box.createHorizontalGlue());
		boxItem.add(label);
		boxItem.add(Box.createHorizontalStrut(5));
		boxItem.add(text);
		boxItem.add(Box.createHorizontalGlue());
		boxItem.add(Box.createHorizontalStrut(15));

		box.add(Box.createVerticalStrut(5));
		box.add(boxItem);
		box.add(Box.createVerticalStrut(5));

		return text;
	}

	private void XoaRong() {
		if (tableNV.getSelectedRow() != -1)
			tableNV.clearSelection();
		txtTenNV.setText("");
		txtMaNV.setText("");
		txtEmail.setText("");
		jcbTrangThai.setSelectedItem("");
		txtSoDienThoai.setText("");
		jcbGioiTinh.setSelectedItem("");
		jcbLoaiNV.setSelectedItem("");
		dateNgaySinh.setCalendar(null);
		dateNgayVaoLam.setCalendar(null);

		ballNgaySinh.setVisible(false);
		ballTenNV.setVisible(false);
		ballNgayVaoLam.setVisible(false);
		ballDiaChi.setVisible(false);
		ballEmail.setVisible(false);
		ballSoDienThoai.setVisible(false);

		txtMaNV.requestFocus();
	}

	private void XoaRongTK() {
		txtTKTenNV.setText("");
		txtTKEmail.setText("");
		txtTKSoDienThoai.setText("");
		jcbTKLoaiNV.setSelectedItem("");
		dateTKNgayVaoLam.setCalendar(null);
		txtTKTenNV.requestFocus();
	}

	private boolean validData() {

		String tennv = txtTenNV.getText().trim();
		String sodt = txtSoDienThoai.getText().trim();
		String email = txtEmail.getText().trim();

		// Ngày sinh: Ngày sinh phải trước ngày hiện tại
		if (dateNgaySinh.getDate() == null || dateNgaySinh.getDate().after(new Date())) {
			ballNgaySinh.setVisible(true);
			return false;
		} else
			ballNgaySinh.setVisible(false);

		// Tên nhân viên: Không được rỗng, không chứa ký tự đặc biệt
		if (tennv.isEmpty() || !tennv.matches("[\\p{Lu}[A-Z]][\\p{L}[a-z]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z]]*)*")) {
			ballTenNV.setVisible(true);
			return false;
		} else
			ballTenNV.setVisible(false);

		// Ngày vào làm: Trước ngày hiện tại và sau ngày sinh
		if (dateNgayVaoLam.getDate() == null || dateNgayVaoLam.getDate().after(new Date())
				|| dateNgayVaoLam.getDate().before(dateNgaySinh.getDate())) {
			ballNgayVaoLam.setVisible(true);
			return false;
		} else
			ballNgayVaoLam.setVisible(false);

		// Email: Được phép rỗng, chỉ được chứa các kí tự '@', '_', '.', '-'
		if (!email.isEmpty()) {
			if (!email.matches("^[a-z][a-z0-9\\.]{7,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
				ballEmail.setVisible(true);
				return false;
			} else
				ballEmail.setVisible(false);
		}

		// Số điện thoại: Không được rỗng, gồm 10 số, bắt đầu bằng số 0
		if (sodt.isEmpty() || !sodt.matches("0\\d{9}")) {
			ballSoDienThoai.setVisible(true);
			return false;
		} else
			ballSoDienThoai.setVisible(false);

		return true;

	}

	private boolean kiemtraTrungEmail() {
		List<NhanVien> list = new ArrayList<NhanVien>();

		list = dao_NV.findNhanVienTrung(txtEmail.getText().trim(), "");
		for (NhanVien nv : list) {
			if (list.contains(nv)) {

				return false;
			}
		}
		return true;

	}

	private boolean kiemtraTrungSDT() {
		List<NhanVien> list = new ArrayList<NhanVien>();

		list = dao_NV.findNhanVienTrung("", txtSoDienThoai.getText().trim());
		for (NhanVien nv : list) {
			if (list.contains(nv)) {

				return false;
			}
		}
		return true;
	}
}
