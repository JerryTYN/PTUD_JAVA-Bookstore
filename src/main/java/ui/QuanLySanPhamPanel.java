package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

//import dao.KhachHangDAO;
import dao.SanPhamDao;
//import dao.ThuocDAO;
//import entity.KhachHang;
import entity.NhaCungCap;
//import entity.PhanLoai;
import entity.SanPham;
//import entity.Thuoc;
import net.java.balloontip.BalloonTip;

public class QuanLySanPhamPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColoredButton btnSearch;
	private DefaultTableModel model;
	private CustomTable table;
	private ColoredButton btnThem;
	private ColoredButton btnSua;
	
	private ColoredButton btnXoa;
	private ColoredButton btnQuayLai;
	private List<SanPham> dsKetQua;
	private ColoredButton btnChonHinhAnh;
	private ColoredButton btnChonNhaCungCap;
	private NhaCungCap nhaCungCap;
	private ChonNhaCungCapDialog chonNhaCungCapDialog;
	private SanPhamDao sanPhamDAO;
	private JTextField txtMa;

	private ColoredButton btnNhapExcel;
	private final Dimension dimension = new Dimension(100, 25);
	private JTextField txtTen;
	private JTextField txtTacGia;
	private JComboBox<String> cbbGT;
	private JTextField txtNcc;
	private JTextField txtTheLoai;
	private JComboBox<String> cbbPL;
	private JTextField txtSoLuong;
	private JTextField txtHinhAnh;
	private DefaultTableModel modelTimKiem;
	private JLabel lblPage;
	private JLabel lblPageTongSp;
	private ColoredButton btnXoaTrang;
	private ColoredButton btnTrangThai;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JTextField txtTen2;
	private JTextField txtNcc2;
	private JTextField txtTacGia2;
	private JTextField txtTheLoai2;
	private JTextField txtDiaChi;
	private JTextField txtGia;
	private ColoredButton btnXoaTrang2;
	private JTabbedPane tabbedPane;
	private MainFrame mainFrame;
	private DefaultTableModel defaultTable;
	private List<SanPham> sp = null;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private int currentIndex = 0;
	private BalloonTip ballTen;
	private BalloonTip ballTacGia;
	private BalloonTip ballNcc;
	private BalloonTip ballDonGia;
	private BalloonTip ballPhanLoai;
	private BalloonTip ballSoLuong;
	private BalloonTip ballTheLoai;
	private SanPham selectedSanPham;

	public QuanLySanPhamPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setOpaque(true);
		setLayout(new BorderLayout());
		setBackground(Color.white);

		addControls();
		addEvents();

		sanPhamDAO = new SanPhamDao();
		getAllComponents(this).forEach(item -> {
			item.addKeyListener(new KeyAdapter() {
				private boolean isCtrlPressed = false;

				@Override
				public void keyPressed(KeyEvent e) {
					if (isCtrlPressed) {
						// Ctrl + N
						if (e.getKeyCode() == KeyEvent.VK_N)
							btnThem.doClick();
						else if (e.getKeyCode() == KeyEvent.VK_X)
							btnXoa.doClick();
						else if (e.getKeyCode() == KeyEvent.VK_LEFT)
							btnQuayLai.doClick();
						else if (e.getKeyCode() == KeyEvent.VK_E) {
							btnXoaTrang.doClick();
							btnXoaTrang2.doClick();
						}

						else if (e.getKeyCode() == KeyEvent.VK_F)
							btnSearch.doClick();

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

		

		loadData();
		timSanPham();
	}

	public Component getDefaultFocusComponent() {
		return txtTen2;
	}



	   
	private boolean validData() {
		String tenSanPham = txtTen.getText().trim();
		String soLuong = txtSoLuong.getText().trim();
		String giaThanh = txtGia.getText().trim();
		String nhaCungCap = txtNcc.getText().trim();
		String tacGia = txtTacGia.getText().trim();
		String theLoai = txtTheLoai.getText().trim();
		if(tenSanPham.isEmpty() && soLuong.isEmpty() && giaThanh.isEmpty() && nhaCungCap.isEmpty() && tacGia.isEmpty() && theLoai.isEmpty()) {
			UIConstant.showInfo(QuanLySanPhamPanel.this, "Vui l??ng nh???p th??ng tin!");
		}
		if (tenSanPham.isEmpty()) {
			ballTen.setTextContents("T??n s???n ph???m kh??ng ???????c r???ng");
			txtTen.requestFocus();
			ballTen.setVisible(true);
			return false;	
		}
		
		
		else if (!tenSanPham.matches("[\\p{Lu}[A-Za-z0-9-_&.,@/]][\\p{L}[a-z0-9-_&.@/ ]]*(\\s+[\\p{Lu}[A-Za-z0-9-_&.,@/ ]][\\p{L}[a-z0-9-_&.,@/ ]]*)*")) {
			ballTen.setTextContents(
					" +  T??n s???n ph???m ph???i b???t ?????u ch??? c??i ho???c s??? \n" + " + Kh??ng ch???a c??c k?? t??? ?????c bi???t");
			txtTen.requestFocus();
			ballTen.setVisible(true);
			return false;
		}

		if (giaThanh.isEmpty() || !giaThanh.matches("[0-9.,]*")) {
			ballDonGia.setTextContents("Gi?? th??nh kh??ng ???????c r???ng v?? ch??? ch???a s???");
			txtGia.requestFocus();
			ballDonGia.setVisible(true);
			return false;
		}
		
		
		 double gia = Double.parseDouble(giaThanh);
		if (gia <= 0) {
			ballDonGia.setTextContents("Gi?? th??nh ph???i l???n h??n 0");
			txtGia.requestFocus();
			ballDonGia.setVisible(true);
			return false;
		} else 
			ballDonGia.setVisible(false);
		
		if (soLuong.isEmpty() || !soLuong.matches("[0-9]*")) {
			ballSoLuong.setTextContents("S??? l?????ng kh??ng ???????c r???ng v?? ch??? ch???a s???");
			txtSoLuong.requestFocus();
			ballSoLuong.setVisible(true);
			return false;
		}
		int sl = Integer.parseInt(soLuong);
		if (sl <= 0) {
			ballSoLuong.setTextContents("S??? l?????ng ph???i l???n h??n 0");
			txtSoLuong.requestFocus();
			ballSoLuong.setVisible(true);
			return false;
		} else
			ballSoLuong.setVisible(false);
		
		String phanLoai = "";
		if (cbbPL.getSelectedIndex() != -1) {
			
			if(cbbPL.getSelectedIndex()==0) {			
				txtTacGia.setEnabled(false);	
				txtTheLoai.setEnabled(false);
				tacGia = "" ;
				theLoai = "";
				phanLoai = cbbPL.getSelectedItem().toString();
				
			}
			else if(cbbPL.getSelectedIndex() ==1) {
				
				if (tacGia.isEmpty()) {
					ballTacGia.setTextContents("T??c gi??? kh??ng ???????c r???ng");
					txtTacGia.requestFocus();
					ballTacGia.setVisible(true);
					return false;
				} else if (!tacGia.matches("[\\p{Lu}[A-Z]][\\p{L}[a-z-_&.,/' ]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z-_&.,/' ]]*)*")) {
					ballTacGia.setTextContents(
							" +  T??n t??c gi??? ph???i b???t ?????u ch??? c??i in hoa \n" + " + Kh??ng ch???a c??c k?? t??? ?????c bi???t v?? s???");
					txtTacGia.requestFocus();
					ballTacGia.setVisible(true);
					return false;
				} else
					ballTacGia.setVisible(false);
				
				if (theLoai.isEmpty()) {
					ballTheLoai.setTextContents("Th??? lo???i kh??ng ???????c r???ng");
					txtTheLoai.requestFocus();
					ballTheLoai.setVisible(true);
					return false;
				} else if (!theLoai.matches("[\\p{Lu}[A-Za-z]][\\p{L}[a-z-_&.,/ ]]*(\\s+[\\p{Lu}[a-z]][\\p{L}[a-z-_&.,/ ]]*)*")) {
					ballTheLoai.setTextContents(
							" +  Th??? lo???i ph???i b???t ?????u ch??? c??i in hoa \n" + " + Kh??ng ch???a c??c k?? t??? ?????c bi???t v?? s???");
					txtTheLoai.requestFocus();
					ballTheLoai.setVisible(true);
					return false;
				} else
					ballTheLoai.setVisible(false);
				
			}
		}
		if(nhaCungCap.isEmpty()) {
			ballNcc.setTextContents("Nh?? cung c???p kh??ng ???????c ????? tr???ng");
			ballNcc.setVisible(true);
			return false;
		}
		else
			ballNcc.setVisible(false);
		
		
		
		return true;
	}
	


	private void addControls() {

		jPanel1 = new JPanel(new BorderLayout());
		jPanel1.setOpaque(false);
		jPanel2 = new JPanel(new BorderLayout());
		jPanel2.setOpaque(false);

		JPanel pnlTitle = new JPanel();
		pnlTitle.setOpaque(false);
		JLabel lblHeader = new JLabel("Qu???n L?? S???n Ph???m");
		lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
		lblHeader.setHorizontalAlignment(JLabel.CENTER);
		lblHeader.setForeground(new Color(255, 127, 0));
		pnlTitle.add(lblHeader);

		Box boxNorth = Box.createVerticalBox();
		boxNorth.add(pnlTitle);

		addNorth();
		addCenter();
		addSouth();
		addNorth2();

		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("T??m ki???m", jPanel2);

		tabbedPane.addTab("C???p nh???t s???n ph???m", jPanel1);

		tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

		boxNorth.add(tabbedPane);
		this.add(boxNorth, BorderLayout.NORTH);

	}

	private void addSouth() {
		Box boxSouth = Box.createVerticalBox();
		boxSouth.add(Box.createVerticalStrut(20));
		JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 20));

		btnQuayLai = new ColoredButton("Quay l???i", new ImageIcon("img//back.png"));
		btnQuayLai.setFont(UIConstant.NORMAL_FONT);
		btnQuayLai.setBorderRadius(30);
		btnQuayLai.setBackground(UIConstant.PRIMARY_COLOR);

		pnlSouth.add(btnQuayLai);
		pnlSouth.setOpaque(false);
		boxSouth.add(pnlSouth);
		this.add(boxSouth, BorderLayout.SOUTH);
	}

	private void addCenter() {

		Box boxCenter = Box.createVerticalBox();

		JPanel pnlDanhSach = new JPanel(new BorderLayout());

		String[] columns = new String[] { "H??nh ???nh", "M?? s???n ph???m", "T??n s???n ph???m", "T??c gi???", "Nh?? cung c???p",
				"????n gi??", "Ph??n lo???i", "S??? l?????ng", "Th??? lo???i" };

		model = new DefaultTableModel(columns, 0);
		table = new CustomTable(model);

		table.getColumn("H??nh ???nh").setCellRenderer(new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if(column == 0) {
					if(value instanceof String)
						return new ImageItem(new ImageIcon(value.toString()));
					else
						return new ImageItem((byte[])value);
				}

				return (Component) value;

			}
		});

		table.setRowHeight(60);
		table.getColumn("H??nh ???nh").setMaxWidth(80);
		table.getColumn("H??nh ???nh").setMinWidth(80);
		table.setFont(UIConstant.NORMAL_FONT);
		table.setAutoCreateRowSorter(true);

		JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.getViewport().setBackground(Color.WHITE);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Danh s??ch s???n ph???m", pane);

		lblPage = new JLabel("Trang 1 trong 1 trang.");
		lblPage.setFont(UIConstant.NORMAL_FONT);
		lblPageTongSp = new JLabel("T???ng S???n Ph???m: ");
		lblPageTongSp.setFont(UIConstant.NORMAL_FONT);
		btnHome = new ColoredButton(new ImageIcon("img/double_left.png"));
		btnHome.setBackground(UIConstant.LINE_COLOR);
		btnHome.setBorderRadius(20);
		btnEnd = new ColoredButton(new ImageIcon("img/double_right.png"));
		btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnEnd.setBorderRadius(20);
		btnBefore = new ColoredButton(new ImageIcon("img/left.png"));
		btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnBefore.setBorderRadius(20);
		btnNext = new ColoredButton(new ImageIcon("img/right.png"));
		btnNext.setBackground(UIConstant.LINE_COLOR);
		btnNext.setBorderRadius(20);

		btnHome.setToolTipText("Trang ?????u");
		btnEnd.setToolTipText("Trang cu???i");
		btnBefore.setToolTipText("Trang tr?????c");
		btnNext.setToolTipText("Trang k??? ti???p");

		Box boxPage = Box.createHorizontalBox();
		Box boxPage1 = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHome);
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBefore);
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNext);
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEnd);
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		

		boxPage.add(lblPage);
		boxPage.add(Box.createHorizontalStrut(5));
		
		
		boxPage1.add(Box.createHorizontalStrut(5));
		boxPage1.add(Box.createHorizontalGlue());
		boxPage1.add(lblPageTongSp);
		boxPage1.add(Box.createHorizontalStrut(2));
		
		
		boxCenter.add(tabbedPane);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxPage1);
		boxCenter.add(boxPage);
		pnlDanhSach.add(boxCenter, BorderLayout.CENTER);
		pnlDanhSach.setOpaque(false);

		this.add(pnlDanhSach, BorderLayout.CENTER);
	}

	private void addNorth2() {

		JPanel pnlCenter = new JPanel(new BorderLayout());
		Box boxMain = Box.createVerticalBox();
		Box boxLine2 = Box.createHorizontalBox();

		JLabel lblTen2 = new JLabel("T??n s???n ph???m:");
		lblTen2.setFont(UIConstant.NORMAL_FONT);
		txtTen2 = new JTextField(1);
		txtTen2.setFont(UIConstant.NORMAL_FONT);
		lblTen2.setPreferredSize(dimension);

		JLabel lblNcc2 = new JLabel("Nh?? cung c???p:");
		lblNcc2.setFont(UIConstant.NORMAL_FONT);
		txtNcc2 = new JTextField(1);
		txtNcc2.setFont(UIConstant.NORMAL_FONT);
		lblNcc2.setPreferredSize(dimension);

		boxLine2.add(lblTen2);
		boxLine2.add(txtTen2);
		boxLine2.add(Box.createHorizontalStrut(20));
		boxLine2.add(lblNcc2);
		boxLine2.add(txtNcc2);

		// line 3
		Box boxLine3 = Box.createHorizontalBox();

		JLabel lblTacGia2 = new JLabel("T??c gi???:");
		lblTacGia2.setFont(UIConstant.NORMAL_FONT);
		txtTacGia2 = new JTextField(1);
		txtTacGia2.setFont(UIConstant.NORMAL_FONT);
		lblTacGia2.setPreferredSize(lblTen2.getMaximumSize());
		lblTacGia2.setPreferredSize(dimension);

		JLabel lblTheLoai2 = new JLabel("Th??? lo???i:");
		txtTheLoai2 = new JTextField(1);
		lblTheLoai2.setFont(UIConstant.NORMAL_FONT);
		txtTheLoai2.setFont(UIConstant.NORMAL_FONT);
		lblTheLoai2.setPreferredSize(lblNcc2.getMaximumSize());
		lblTheLoai2.setPreferredSize(dimension);

		boxLine3.add(lblTacGia2);
		boxLine3.add(txtTacGia2);
		boxLine3.add(Box.createHorizontalStrut(20));
		boxLine3.add(lblTheLoai2);
		boxLine3.add(txtTheLoai2);
		// buton

		JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
		btnSearch = new ColoredButton("T??m ki???m", new ImageIcon("img//search.png"));
		btnSearch.setFont(UIConstant.NORMAL_FONT);
		btnSearch.setBorderRadius(30);
		btnSearch.setBackground(UIConstant.PRIMARY_COLOR);

		btnXoaTrang2 = new ColoredButton("L??m m???i", new ImageIcon("img//refresh3.png"));
		btnXoaTrang2.setBackground(new Color(0x019474D));
		btnXoaTrang2.setFont(UIConstant.NORMAL_FONT);
		btnXoaTrang2.setBorderRadius(30);

		pnlButton.add(btnSearch);

		pnlButton.add(btnXoaTrang2);
		pnlButton.setOpaque(false);

		boxMain.add(Box.createVerticalStrut(20));
		boxMain.add(boxLine2);
		boxMain.add(Box.createVerticalStrut(20));
		boxMain.add(boxLine3);

		pnlCenter.add(boxMain, BorderLayout.CENTER);
		pnlCenter.add(pnlButton, BorderLayout.SOUTH);
		pnlCenter.setOpaque(false);

		jPanel2.add(pnlCenter, BorderLayout.NORTH);

	}

	/// C???p nh???t kh??ch h??ng
	private void addNorth() {

		JPanel pnlCenter = new JPanel(new BorderLayout());
		Box boxCapnhat = Box.createVerticalBox();
		Box box2 = Box.createHorizontalBox();

		JLabel lblMaSP = new JLabel("M?? s???n ph???m:");
		lblMaSP.setFont(UIConstant.NORMAL_FONT);
		txtMa = new JTextField(1);
		txtMa.setFont(UIConstant.NORMAL_FONT);
		txtMa.setEnabled(false);
		lblMaSP.setPreferredSize(dimension);

		JLabel lblTenSP = new JLabel("T??n s???n ph???m:");
		lblTenSP.setFont(UIConstant.NORMAL_FONT);
		txtTen = new JTextField(1);
		txtTen.setFont(UIConstant.NORMAL_FONT);
		lblTenSP.setPreferredSize(dimension);

		box2.add(lblMaSP);
		box2.add(txtMa);
		box2.add(Box.createHorizontalStrut(20));
		box2.add(lblTenSP);
		box2.add(txtTen);

		// line 3
		Box box3 = Box.createHorizontalBox();

		JLabel lblTacGia = new JLabel("T??c gi???:");
		lblTacGia.setFont(UIConstant.NORMAL_FONT);
		txtTacGia = new JTextField(1);
		txtTacGia.setFont(UIConstant.NORMAL_FONT);
		lblTacGia.setPreferredSize(lblMaSP.getMaximumSize());
		lblTacGia.setPreferredSize(dimension);

		JLabel lblTheLoai = new JLabel("Th??? lo???i:");
		txtTheLoai = new JTextField(1);
		lblTheLoai.setFont(UIConstant.NORMAL_FONT);
		txtTheLoai.setFont(UIConstant.NORMAL_FONT);
		lblTheLoai.setPreferredSize(lblTenSP.getMaximumSize());
		lblTheLoai.setPreferredSize(dimension);

		box3.add(lblTacGia);
		box3.add(txtTacGia);
		box3.add(Box.createHorizontalStrut(20));
		box3.add(lblTheLoai);
		box3.add(txtTheLoai);

		// line 4
		Box box4 = Box.createHorizontalBox();

		JLabel lblPhanLoai = new JLabel("Ph??n Lo???i:");
		lblPhanLoai.setFont(UIConstant.NORMAL_FONT);
		lblPhanLoai.setPreferredSize(lblMaSP.getMaximumSize());
		lblPhanLoai.setPreferredSize(dimension);
		cbbPL = new JComboBox<String>();
		cbbPL.setFont(UIConstant.NORMAL_FONT);
		
		cbbPL.addItem("D???ng c??? h???c t???p");
		cbbPL.addItem("S??ch");
		cbbPL.setSelectedIndex(1);
		JLabel lblGia = new JLabel("Gi?? th??nh:");
		txtGia = new JTextField(1);
		lblGia.setFont(UIConstant.NORMAL_FONT);
		txtGia.setFont(UIConstant.NORMAL_FONT);
		lblGia.setPreferredSize(lblTenSP.getMaximumSize());
		lblGia.setPreferredSize(dimension);

		JLabel lblSoLuong = new JLabel("S??? l?????ng:");
		txtSoLuong = new JTextField(1);
		lblSoLuong.setFont(UIConstant.NORMAL_FONT);
		txtSoLuong.setFont(UIConstant.NORMAL_FONT);
		lblSoLuong.setPreferredSize(lblTenSP.getMaximumSize());
		lblSoLuong.setPreferredSize(dimension);

		box4.add(lblPhanLoai);
		box4.add(cbbPL);
		box4.add(Box.createHorizontalStrut(20));
		box4.add(lblGia);
		box4.add(txtGia);
		box4.add(Box.createHorizontalStrut(10));
		box4.add(lblSoLuong);
		box4.add(txtSoLuong);

		// line 5
		Box box5 = Box.createHorizontalBox();
		JLabel lblNCC = new JLabel("Nh?? cung c???p:");
		txtNcc = new JTextField(1);
		lblNCC.setFont(UIConstant.NORMAL_FONT);
		txtNcc.setFont(UIConstant.NORMAL_FONT);
		txtNcc.setEnabled(false);
		lblNCC.setPreferredSize(lblTenSP.getMaximumSize());
		lblNCC.setPreferredSize(lblMaSP.getMaximumSize());
		lblNCC.setPreferredSize(dimension);
		btnChonNhaCungCap = new ColoredButton("Ch???n nh?? cung c???p");
		btnChonNhaCungCap.setBackground(UIConstant.PRIMARY_COLOR);

		box5.add(lblNCC);
		box5.add(Box.createHorizontalStrut(0));
		box5.add(txtNcc);
		box5.add(Box.createHorizontalStrut(5));
		box5.add(btnChonNhaCungCap);
		box5.add(Box.createHorizontalStrut(5));
		// line 6
		Box box6 = Box.createHorizontalBox();
		JLabel lblHinhAnh = new JLabel("H??nh ???nh:");
		txtHinhAnh = new JTextField(1);
		lblHinhAnh.setFont(UIConstant.NORMAL_FONT);
		txtHinhAnh.setFont(UIConstant.NORMAL_FONT);
		txtHinhAnh.setEnabled(false);
		lblHinhAnh.setPreferredSize(lblTenSP.getMaximumSize());

		lblHinhAnh.setPreferredSize(dimension);
		btnChonHinhAnh = new ColoredButton("Ch???n h??nh ???nh");
		btnChonHinhAnh.setBackground(UIConstant.PRIMARY_COLOR);

		box6.add(lblHinhAnh);
		box6.add(Box.createHorizontalStrut(0));
		box6.add(txtHinhAnh);
		box6.add(Box.createHorizontalStrut(32));
		box6.add(btnChonHinhAnh);
		box6.add(Box.createHorizontalStrut(5));

		// buton

		JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

		btnThem = new ColoredButton("Th??m s???n ph???m", new ImageIcon("img/add.png"));
		btnThem.setFont(UIConstant.NORMAL_FONT);
		btnThem.setBorderRadius(30);
		btnThem.setBackground(UIConstant.PRIMARY_COLOR);
		
		btnNhapExcel = new ColoredButton("Th??m s???n ph???m t??? exel", new ImageIcon("img/file1.png"));
		btnNhapExcel.setFont(UIConstant.NORMAL_FONT);
		btnNhapExcel.setBorderRadius(30);
		btnNhapExcel.setBackground(UIConstant.PRIMARY_COLOR);

		btnSua = new ColoredButton("S???a th??ng tin", new ImageIcon("img/modify.png"));
		btnSua.setFont(UIConstant.NORMAL_FONT);
		btnSua.setBorderRadius(30);
		btnSua.setBackground(UIConstant.PRIMARY_COLOR);

		btnXoaTrang = new ColoredButton("L??m m???i", new ImageIcon("img//refresh3.png"));
		btnXoaTrang.setBackground(new Color(0x019474D));
		btnXoaTrang.setFont(UIConstant.NORMAL_FONT);
		btnXoaTrang.setBorderRadius(30);

		btnXoa = new ColoredButton("X??a s???n ph???m", new ImageIcon("img//delete.png"));
		btnXoa.setBackground(UIConstant.DANGER_COLOR);
		btnXoa.setFont(UIConstant.NORMAL_FONT);
		btnXoa.setBorderRadius(30);
		
		pnlButton.add(btnThem);
		pnlButton.add(btnSua);
		pnlButton.add(btnNhapExcel);
		pnlButton.add(btnXoa);

		pnlButton.add(btnXoaTrang);
	
		pnlButton.setOpaque(false);

		ballTen = new BalloonTip(txtTen, "T??n s???n ph???m kh??ng ???????c r???ng!");
		ballTen.setVisible(false);
		ballTen.setCloseButton(null);
		ballTacGia = new BalloonTip(txtTacGia, "T??n t??c gi??? kh??ng ???????c r???ng!");
		ballTacGia.setVisible(false);
		ballTacGia.setCloseButton(null);
		ballTheLoai = new BalloonTip(txtTheLoai, "Th??? lo???i kh??ng ???????c r???ng!");
		ballTheLoai.setVisible(false);
		ballTheLoai.setCloseButton(null);
		ballDonGia = new BalloonTip(txtGia, "Gi?? th??nh kh??ng ???????c r???ng, ch??? nh???p s??? v?? l???n h??n !");
		ballDonGia.setVisible(false);
		ballDonGia.setCloseButton(null);
		ballSoLuong = new BalloonTip(txtSoLuong, "S??? l?????ng kh??ng ???????c r???ng!");
		ballSoLuong.setVisible(false);
		ballSoLuong.setCloseButton(null);
		ballNcc = new BalloonTip(txtNcc, "Vui l??ng ch???n nh?? cung c???p!");
		ballNcc.setVisible(false);
		ballNcc.setCloseButton(null);

		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box2);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box4);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box3);
		
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box5);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box6);
		
		pnlCenter.add(boxCapnhat, BorderLayout.CENTER);
		pnlCenter.add(pnlButton, BorderLayout.SOUTH);
		pnlCenter.setOpaque(false);

		jPanel1.add(pnlCenter, BorderLayout.NORTH);

	}

	public void setNhaCungCapDaChon(NhaCungCap nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
		txtNcc.setText(nhaCungCap.getTenNCC());
	}


	private void addEvents() {
		this.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("ancestor")) {
					tabbedPane.setSelectedIndex(0);
				}
			}
		});
//		
		txtTen2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtNcc2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtTacGia2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtTheLoai2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});

		btnXoaTrang2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				txtTen2.setText("");
				txtTen2.requestFocus();
				txtNcc2.setText("");
				txtTacGia2.setText("");
				txtTheLoai2.setText("");
				loadData();
			}
		});
		btnXoaTrang.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtMa.setText("T??? ?????ng t???o");
				txtTen.setText("");
				txtTacGia.setText("");
				txtTheLoai.setText("");
				cbbPL.setSelectedIndex(0);
				txtGia.setText("");
				txtSoLuong.setText("");
				txtHinhAnh.setText("");
				txtTen.requestFocus();
				txtNcc.setText("");

				ballTen.setVisible(false);
				ballTacGia.setVisible(false);
				ballNcc.setVisible(false);
				ballDonGia.setVisible(false);
				ballSoLuong.setVisible(false);
				ballTheLoai.setVisible(false);
				txtTen2.setText("");
				txtTen2.requestFocus();
				txtNcc2.setText("");
				txtTacGia2.setText("");
				txtTheLoai2.setText("");
				cbbPL.setEnabled(true);
				timSanPham();

			}
		});
		
		btnHome.addActionListener(e -> {
			
			if(dsKetQua !=null) {
				taiDuLieuLenBang(dsKetQua, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			
			if(dsKetQua !=null) {
				taiDuLieuLenBang(dsKetQua, dsKetQua.size() - dsKetQua.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			
			if(dsKetQua !=null) {
				taiDuLieuLenBang(dsKetQua, currentIndex  - 20);
			}
		});

		btnNext.addActionListener(e -> {
			
			if(dsKetQua !=null) {
				taiDuLieuLenBang(dsKetQua, currentIndex + 20);
			}
		});

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				
				int row = table.getSelectedRow();
				if (row == -1)
					return;
				txtHinhAnh.setText(table.getValueAt(row, 0).toString());
				txtMa.setText(table.getValueAt(row, 1).toString());
				txtTen.setText(table.getValueAt(row, 2).toString());
				txtTacGia.setText(table.getValueAt(row, 3).toString());
				txtNcc.setText(table.getValueAt(row, 4).toString());
				txtGia.setText(table.getValueAt(row, 5).toString().replaceAll("[-+.^:,]", ""));
				
				cbbPL.setSelectedItem(
						table.getValueAt(row, 6).toString().equals("D???ng c??? h???c t???p") ? "D???ng c??? h???c t???p" : "S??ch");
				
				txtSoLuong.setText(table.getValueAt(row, 7).toString());
				txtTheLoai.setText(table.getValueAt(row, 8).toString());
						
			}
		});


		btnXoa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row == -1) {
					UIConstant.showInfo(QuanLySanPhamPanel.this, "B???n ch??a ch???n s???n ph???m ????? x??a");
					return;
				} else {
					int click = JOptionPane.showConfirmDialog(QuanLySanPhamPanel.this,
							"B???n c?? ch???c ch??n mu???n x??a kh??ng ?", "C???nh b??o", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, new ImageIcon("img//warning.png"));
					if (click == JOptionPane.YES_OPTION) {
						if (sanPhamDAO.xoaSanPham(txtMa.getText().toString().trim())) {
							UIConstant.showInfo(QuanLySanPhamPanel.this, "X??a th??nh c??ng");
							model.removeRow(row);
							dsKetQua.remove(row - currentIndex );
							taiDuLieuLenBang(dsKetQua, 0);
						} else
							UIConstant.showInfo(QuanLySanPhamPanel.this, "X??a th???t b???i");
					} else
						return;
				}
			}
		});


		btnQuayLai.addActionListener((e) -> {
			mainFrame.changeCenter(mainFrame.getTrangChuPanel());
		});

		btnChonHinhAnh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setFileFilter(new FileNameExtensionFilter("img file (*.png,*.jpg)", "png", "jpg"));
				int selection = fileChooser.showOpenDialog(QuanLySanPhamPanel.this);

				if (selection == JFileChooser.OPEN_DIALOG) {
					File selectedFile = fileChooser.getSelectedFile();
					if (!selectedFile.exists()) {
						UIConstant.showWarning(QuanLySanPhamPanel.this, "File kh??ng t???n t???i!");
						return;
					} else {
						txtHinhAnh.setText(selectedFile.getAbsolutePath());
					}

				}
			}
		});
		btnChonNhaCungCap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chonNhaCungCapDialog == null)
					chonNhaCungCapDialog = new ChonNhaCungCapDialog(QuanLySanPhamPanel.this);

				chonNhaCungCapDialog.setVisible(true);
			}
		});

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtTen2.getText().equals("") && txtTacGia2.getText().equals("") && txtNcc2.getText().equals("") && txtTheLoai2.getText().equals(""))
				{
					UIConstant.showWarning(QuanLySanPhamPanel.this, "Ch??a nh???p n???i dung t??m ki???m");
				}else {
				timSanPham();
				}
				if(model.getRowCount()== 0) {
					UIConstant.showWarning(QuanLySanPhamPanel.this, "Kh??ng c?? k???t qu??? ph?? h???p");
				}
			}
		});
		btnThem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			
				if(validData()) {
					String tenSanPham = txtTen.getText().trim();
					String tacGia = txtTacGia.getText().trim();
					String theLoai = txtTheLoai.getText().trim();
					String giaThanh = txtGia.getText();
					double giathanh = Double.parseDouble(giaThanh);
					String soLuong = txtSoLuong.getText();
					int sl = Integer.parseInt(soLuong);
					Blob hinhAnh = null;
					if (txtHinhAnh.getText() != "") {
						try {
							FileInputStream inputStream = new FileInputStream(txtHinhAnh.getText());
							hinhAnh = new SerialBlob(
									Files.readAllBytes(new File(txtHinhAnh.getText().trim()).toPath()));
							inputStream.close();
						} catch (SQLException | IOException e1) {
							e1.printStackTrace();
						}
					}
					String phanLoai = "";
					if (cbbPL.getSelectedIndex() != -1) {
						
						if(cbbPL.getSelectedIndex()==0) {
							
							txtTacGia.setEnabled(false);	
							txtTheLoai.setEnabled(false);
							tacGia = "" ;
							theLoai = "";
							phanLoai = cbbPL.getSelectedItem().toString();
							
						}else {
							phanLoai = cbbPL.getSelectedItem().toString();
						}
						
					}
					
					SanPham sanPham = new SanPham(tenSanPham, nhaCungCap, theLoai, phanLoai, sl, tacGia, giathanh,
							hinhAnh);
					if (sanPhamDAO.themSanPham(sanPham)) {
						UIConstant.showInfo(QuanLySanPhamPanel.this, "Th??m th??nh c??ng!");
						dsKetQua.add(sanPham);
						loadData();

					} else
						UIConstant.showInfo(QuanLySanPhamPanel.this, "S???n ph???m ???? t???n t???i!");
				
				}
			}
		});
		btnQuayLai.addActionListener((e) -> {
			mainFrame.changeCenter(mainFrame.getTrangChuPanel());
		});
		
		cbbPL.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(cbbPL.getSelectedIndex()==0)
				{
					txtTacGia.setEnabled(false);	
					txtTheLoai.setEnabled(false);
				}
				else {
					txtTacGia.setEnabled(true);	
					txtTheLoai.setEnabled(true);
				}
			}
		});
		
		btnNhapExcel.addActionListener(e -> {			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setFileFilter(new FileNameExtensionFilter("Excel file (*.xls)", "xls"));

			int selection = fileChooser.showOpenDialog(QuanLySanPhamPanel.this);

			if(selection == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				if(sanPhamDAO.nhapSanPhamTuExcel(selectedFile))
					UIConstant.showInfo(QuanLySanPhamPanel.this, "Nh???p t??? file excel th??nh c??ng!");
				else
					UIConstant.showError(QuanLySanPhamPanel.this, "Nh???p t??? file excel kh??ng th??nh c??ng!");
			}
		});
		
		btnSua.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validData()) {
				if (tabbedPane.getSelectedIndex() == 0)
					tabbedPane.setSelectedIndex(1);
				else {
					int row = table.getSelectedRow();
					if (row == -1) {
						UIConstant.showWarning(QuanLySanPhamPanel.this, "Ch??a ch???n s???n ph???m");

					} else {

						row += currentIndex;
							String ma = txtMa.getText().trim();
							String tenSanPham = txtTen.getText().trim();
							String tacGia = txtTacGia.getText().trim();
							String theLoai = txtTheLoai.getText().trim();
							String phanLoai = "";
							if (cbbPL.getSelectedIndex() != -1)
								phanLoai = cbbPL.getSelectedItem().toString();
							else
								phanLoai = cbbPL.getEditor().getItem().toString().trim();
							String giaThanh = txtGia.getText();
							double giathanh = Double.parseDouble(giaThanh);
							String soLuong = txtSoLuong.getText();
							int sl = Integer.parseInt(soLuong);
							Blob hinhAnh = null;
							if (txtHinhAnh.getText() != "") {
								try {
									FileInputStream inputStream = new FileInputStream(txtHinhAnh.getText());
									hinhAnh = new SerialBlob(
											Files.readAllBytes(new File(txtHinhAnh.getText().trim()).toPath()));
									inputStream.close();
								} catch (SQLException | IOException e1) {
									e1.printStackTrace();
								}
							}

							SanPham sanPham = new SanPham(tenSanPham, nhaCungCap, theLoai, phanLoai, sl, tacGia,
									giathanh, hinhAnh);
							sanPham.setMaSanPham(ma);
							if (sanPhamDAO.capNhatSanPham(sanPham)) {

								UIConstant.showInfo(QuanLySanPhamPanel.this, "S???a th??nh c??ng");
								

								dsKetQua.set(table.getSelectedRow() + currentIndex, sanPham);
								taiDuLieuLenBang(dsKetQua, 0);
						
							} else
								UIConstant.showWarning(QuanLySanPhamPanel.this, "S???a kh??ng th??nh c??ng");

						}
					}
				}
			}
		});

	}

	private void loadData() {
		dsKetQua= new ArrayList<SanPham>();
		dsKetQua = sanPhamDAO.lay20SanPhamGanDay();
		
		taiDuLieuLenBang(dsKetQua, 0);

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

	private void timSanPham() {
		String tenSanPham = txtTen2.getText().trim();
		String nhaCungCap = txtNcc2.getText().trim();
		String tacGia = txtTacGia2.getText().trim();
		String theLoai = txtTheLoai2.getText().trim();
		dsKetQua = sanPhamDAO.timSanPham(tenSanPham, nhaCungCap, tacGia, theLoai);

		taiDuLieuLenBang(dsKetQua, 0);
	}

	private void taiDuLieuLenBang(List<SanPham> dsSanPham, int minIndex) {
		if (dsSanPham.size() == 0) {
			model.setRowCount(0);
			lblPage.setText("Trang " + 1 + "trong" + 1 + " trang.");
			return;
		}	
	
		if (minIndex >= dsSanPham.size() || minIndex < 0)
			return;
		lblPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsSanPham.size() - 1) / 20 + 1) + " trang.");
		lblPageTongSp.setText("T???ng S???n Ph???m: "+dsSanPham.size());
		model.setRowCount(0);
		model.getDataVector().removeAllElements();
		model.fireTableDataChanged();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);

		for (int i = minIndex; i < minIndex + 20; i++) {
			if (i >= dsSanPham.size())
				break;
			SanPham sanPham = dsSanPham.get(i);
			SwingUtilities.invokeLater(() -> {
				String ha = "";
				byte[] b = null;
				if (sanPham.getHinhAnh() == null)
					ha = "img/image.png";
				else {
					try {
						Blob blob = sanPham.getHinhAnh();

						b = blob.getBytes(1, (int) blob.length());

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				model.addRow(new Object[] { ha != "" ? ha : b, sanPham.getMaSanPham(), sanPham.getTenSanPham(),
						sanPham.getTacGia(),
						sanPham.getNhaCungCap() != null ? sanPham.getNhaCungCap().getTenNCC() : null,
						nf.format(sanPham.getGiaThanh()), sanPham.getPhanLoai(), sanPham.getSoLuong(), sanPham.getTheLoai() });

			});
		}

		currentIndex = minIndex;

	}

}
