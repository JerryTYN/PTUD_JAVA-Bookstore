package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.ParseException;
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
import javax.swing.BoxLayout;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


import com.toedter.calendar.JDateChooser;

import dao.CTHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;

import entity.CTHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.SanPham;

import net.java.balloontip.BalloonTip;

public class CapNhatHoaDonDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private CustomTable tableCTHD;
	private DefaultTableModel modelCTHD;
	private ColoredButton btnXoaCTHD;
	private ColoredButton btnSuaCTHD;
	private ColoredButton btnThemSanPham;
	private JTextField txtMaHoaDon;
	private JDateChooser dateNgayLap;
	private ColoredButton btnChonKhachHang;
	private ColoredButton btnIn;
	private ColoredButton btnLuu;
	private JTextField txtSoLuong;
	private JTextField txtDonGia;
	private ColoredButton btnQuayLai;
	protected ChonKhachHangDialog chonKhachHangDialog;
	private ColoredButton btnXoaRong;
	public static JTextField txtMaKhachHang;
	
	public static JTextField txtTenKhachHang;
	private JTextField txtTongTien;

	private JTextField txtMaSanPham;
	private JTextField txtTenSanPham;
	protected chonSanPhamDialog chonSanPhamDialog;
	private List<CTHoaDon> dsCTHD;
	private HoaDonDAO hoaDonDAO;
	private KhachHang khachHang;
	private HoaDon hoaDon;
	private CTHoaDonDAO ctHoaDonDAO;
	NumberFormat nf = NumberFormat.getInstance(Locale.US);
	public static JTextField txtTienKhachDua;
	public static  JTextField txtTienTra;
	private double tongTien = 0;
	private JComboBox<String> txtGioiTinh;
	private JTextField txtDC;
	private JTextField txtEmail;
	public static JTextField txtSDT;
	private BalloonTip ballSoLuong;
	private BalloonTip ballDG;
	private JPanel pnlRight;
	private double tientra;
	private JDateChooser txtNgaySinh;
	private BalloonTip ballTen;
	private BalloonTip ballNS;
	private BalloonTip ballDC;
	private BalloonTip ballEmail;
	private BalloonTip ballSDT;
	private KhachHangDAO khachHangDAO;
	private List<KhachHang> dsKH;
	private BalloonTip balltienkhachdua;


	public CapNhatHoaDonDialog(Container owner) {
		super(getFrame(owner));

		setSize(owner.getSize());
		setTitle("Lập hóa đơn");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/hoadon.png").getImage());
		setLayout(new BorderLayout());

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
		getContentPane().setBackground(Color.white);

		setBackground(Color.white);

		JPanel pnlMain;
		add(pnlMain = new JPanel(new BorderLayout()));
		pnlMain.setOpaque(false);

		add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		add(Box.createHorizontalStrut(10), BorderLayout.WEST);

		JPanel pnlNorth;
		pnlMain.add(pnlNorth = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorth.setOpaque(false);

		addNorth(pnlNorth);

		JPanel pnlEast;
		pnlMain.add(pnlEast = new JPanel(new BorderLayout()), BorderLayout.EAST);
		pnlEast.setOpaque(false);
		pnlEast.setPreferredSize(new Dimension(300, 200));

		addEast(pnlEast);

		JPanel pnlCenter;
		pnlMain.add(pnlCenter = new JPanel(new BorderLayout()));
		pnlCenter.setOpaque(false);
		pnlCenter.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstant.LINE_COLOR));

		addCenter(pnlCenter);

		JPanel pnlSouth = new JPanel(new BorderLayout());
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setOpaque(false);

		addSouth(pnlSouth);

		add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		addEvent();

		hoaDon = new HoaDon();
		
		hoaDon.setNhanVienLap(MainFrame.getNhanVien());
		hoaDon.setNgayLap(LocalDate.now());
		System.out.println("bb"+hoaDon);
		hoaDonDAO = new HoaDonDAO();
		ctHoaDonDAO = new CTHoaDonDAO();
		khachHangDAO = new KhachHangDAO();
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
		txtMaHoaDon.setText(hoaDon.getMaHoaDon());

		System.out.println("cc"+hoaDon);
		try {
			dateNgayLap.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(hoaDon.getNgayLap().toString()));

		} catch (ParseException e) {
		}

		if(hoaDon.getKhachHang() != null) {
			txtMaKhachHang.setText(hoaDon.getKhachHang().getMaKhachHang());
			txtTenKhachHang.setText(hoaDon.getKhachHang().getTenKhachHang());
			LocalDate localDate2 = hoaDon.getKhachHang().getNgaySinh();// For reference
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedString2 = localDate2.format(formatter2);
			Date date;
			try {

				date = new SimpleDateFormat("dd-MM-yyyy").parse(formattedString2);
				txtNgaySinh.setDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String gt = "";
			if (hoaDon.getKhachHang().isGioiTinh() == true)
				gt = "Nam";
			else
				gt = "Nữ";
			txtGioiTinh.setSelectedItem(gt);
			txtDC.setText(hoaDon.getKhachHang().getDiaChi());
			txtEmail.setText(hoaDon.getKhachHang().getEmail());
			txtSDT.setText(hoaDon.getKhachHang().getSoDienThoai());
		}


		dsCTHD = ctHoaDonDAO.getDsCTHD(hoaDon);

		double tongTien = 0;

		for(CTHoaDon item : dsCTHD) {
			tongTien += item.tinhThanhTien();
			modelCTHD.addRow(new Object[] {
					item.getSanPham().getTenSanPham(), 
					item.getSoLuong(), item.getDonGia(),item.tinhThanhTien() 
			});
		}

		txtTongTien.setText(nf.format(tongTien) + " VND");


		this.setTitle("Xem thông tin hóa đơn");

		pnlRight.setVisible(false);
		btnChonKhachHang.setVisible(false);
		btnChonKhachHang.setEnabled(true);
		btnLuu.setVisible(false);
		btnIn.setVisible(true);
		btnIn.setEnabled(true);
		btnXoaCTHD.setVisible(false);
		btnSuaCTHD.setVisible(false);

		this.setVisible(true);
	}

	private void addSouth(JPanel pnlSouth) {		
		btnQuayLai = new ColoredButton("Quay lại", new ImageIcon("img/back.png"));
		btnQuayLai.setBackground(UIConstant.PRIMARY_COLOR);
		btnQuayLai.setBorderRadius(30);


		Box boxButtonCTHD;
		pnlSouth.add(boxButtonCTHD = Box.createHorizontalBox());

		boxButtonCTHD.add(Box.createHorizontalStrut(1100));

		boxButtonCTHD.add(btnQuayLai);
		boxButtonCTHD.add(Box.createHorizontalGlue());

		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
	}

	private void addCenter(JPanel pnlCenter) {
		JPanel pnlCTHD = new JPanel();
		pnlCTHD.setOpaque(false);
		pnlCTHD.setLayout(new BoxLayout(pnlCTHD, BoxLayout.X_AXIS));

		JPanel pnlLeft = new JPanel();
		pnlLeft.setBorder(BorderFactory.createLineBorder(UIConstant.LINE_COLOR, 1));
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setOpaque(false);

		pnlRight = new JPanel(new BorderLayout());
		pnlRight.setOpaque(false);
		pnlRight.setBorder(BorderFactory.createLineBorder(UIConstant.LINE_COLOR, 1));

		pnlCTHD.add(pnlLeft);
		pnlCTHD.add(Box.createHorizontalStrut(10));
		pnlCTHD.add(pnlRight);

		pnlCenter.add(pnlCTHD, BorderLayout.NORTH);

		JLabel lbMaSanPham = new JLabel("Mã sản phẩm");
		JLabel lbTenSanPham = new JLabel("Tên sản phẩm");
		JLabel lbSL = new JLabel("Số lượng");
		JLabel lbDG = new JLabel("Đơn giá");

		lbMaSanPham.setPreferredSize(new Dimension(83, 20)); lbMaSanPham.setFont(UIConstant.NORMAL_FONT);
		lbTenSanPham.setPreferredSize(new Dimension(83, 20)); lbTenSanPham.setFont(UIConstant.NORMAL_FONT);
		lbSL.setPreferredSize(new Dimension(83, 20)); lbSL.setFont(UIConstant.NORMAL_FONT);
		lbDG.setPreferredSize(new Dimension(83, 20)); lbDG.setFont(UIConstant.NORMAL_FONT);


		txtMaSanPham = new JTextField(); txtMaSanPham.setEnabled(false);
		txtTenSanPham = new JTextField(); txtTenSanPham.setEnabled(false);
		txtSoLuong = new JTextField();
		txtDonGia = new JTextField();txtDonGia.setEnabled(false);


		btnThemSanPham = new ColoredButton("Thêm sản phẩm vào hóa đơn", new ImageIcon("img/add.png"));
		btnThemSanPham.setBackground(UIConstant.PRIMARY_COLOR);
		btnThemSanPham.setToolTipText("Thêm một chi tiết mới vào hóa đơn");
		btnThemSanPham.setBorderRadius(30);

		ballSoLuong = new BalloonTip(txtSoLuong, "Số lượng phải là số nguyên lớn hơn 0!"); ballSoLuong.setVisible(false); ballSoLuong.setCloseButton(null);
		

		Box boxMaTenSanPham = Box.createHorizontalBox();
		Box boxDVSLDG = Box.createHorizontalBox();

		pnlLeft.add(Box.createVerticalStrut(5));
		pnlLeft.add(boxMaTenSanPham);
		pnlLeft.add(Box.createVerticalStrut(5));
		pnlLeft.add(boxDVSLDG);
		pnlLeft.add(Box.createVerticalStrut(5));

		boxMaTenSanPham.add(Box.createHorizontalStrut(5));
		boxMaTenSanPham.add(lbMaSanPham);
		boxMaTenSanPham.add(txtMaSanPham);
		boxMaTenSanPham.add(Box.createHorizontalStrut(5));
		boxMaTenSanPham.add(lbTenSanPham);
		boxMaTenSanPham.add(txtTenSanPham);
		boxMaTenSanPham.add(Box.createHorizontalStrut(5));

		boxDVSLDG.add(Box.createHorizontalStrut(5));

		boxDVSLDG.add(lbSL);
		boxDVSLDG.add(txtSoLuong);
		boxDVSLDG.add(Box.createHorizontalStrut(5));
		boxDVSLDG.add(lbDG);
		boxDVSLDG.add(txtDonGia);
		boxDVSLDG.add(Box.createHorizontalStrut(5));

		Box boxThem = Box.createHorizontalBox();
		boxThem.add(Box.createHorizontalGlue());
		boxThem.add(Box.createHorizontalStrut(5));
		boxThem.add(btnThemSanPham);
		boxThem.add(Box.createHorizontalStrut(5));
		boxThem.add(Box.createHorizontalGlue());

		Box boxRight = Box.createVerticalBox();
		pnlRight.add(boxRight, BorderLayout.NORTH);
		pnlRight.setMaximumSize(new Dimension(200, 60));
		pnlRight.setPreferredSize(new Dimension(250, 60));

		boxRight.add(Box.createVerticalStrut(10));
		boxRight.add(boxThem);


		tableCTHD = new CustomTable();
		tableCTHD.setModel(modelCTHD = new DefaultTableModel(new String[] {"Sản Phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0));
		tableCTHD.setFont(UIConstant.NORMAL_FONT);

		JTabbedPane tabPaneCTHD = new JTabbedPane();
		tabPaneCTHD.setOpaque(false);
		pnlCenter.add(tabPaneCTHD, BorderLayout.CENTER);

		JScrollPane cthdScroll;
		tabPaneCTHD.addTab("Chi tiết hóa đơn", cthdScroll = new JScrollPane(tableCTHD));
		cthdScroll.getViewport().setBackground(Color.white);
		cthdScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		btnXoaCTHD = new ColoredButton("Xoá chi tiết", new ImageIcon("img/delete.png"));
		btnXoaCTHD.setBackground(UIConstant.DANGER_COLOR);
		btnXoaCTHD.setToolTipText("Xoá sản phẩm đã chọn trong bảng");
		btnXoaCTHD.setBorderRadius(30);
		btnSuaCTHD = new ColoredButton("Sửa chi tiết", new ImageIcon("img/modify.png"));
		btnSuaCTHD.setBackground(UIConstant.WARNING_COLOR);
		btnSuaCTHD.setToolTipText("Sửa chi tiết hóa đơn đã chọn trong bảng với thông tin phía trên");
		btnSuaCTHD.setBorderRadius(30);
		btnXoaRong = new ColoredButton("Làm mới", new ImageIcon("img/refresh3.png"));
		btnXoaRong.setBackground(new Color(0x019474D));
		btnXoaRong.setToolTipText("Làm rỗng toàn bộ");
		btnXoaRong.setBorderRadius(30);

		Box box = Box.createVerticalBox();
		pnlCenter.add(box, BorderLayout.SOUTH);

		Box boxButton = Box.createHorizontalBox();
		box.add(Box.createVerticalStrut(5));
		box.add(boxButton);
		box.add(Box.createVerticalStrut(5));

		boxButton.add(Box.createHorizontalStrut(5));
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(btnSuaCTHD);
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(btnXoaCTHD);
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(btnXoaRong);
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(Box.createHorizontalStrut(5));

		JLabel lbTongTien = new JLabel("Tổng tiền");
		lbTongTien.setFont(UIConstant.NORMAL_FONT);
		lbTongTien.setHorizontalAlignment(SwingConstants.RIGHT);

		txtTongTien = new JTextField("0"); txtTongTien.setEnabled(false);
		txtTongTien.setFont(UIConstant.NORMAL_FONT);
		txtTongTien.setHorizontalAlignment(SwingConstants.RIGHT);

		Box boxTongTien = Box.createHorizontalBox();
		boxTongTien.add(Box.createHorizontalStrut(10));
		boxTongTien.add(lbTongTien);
		boxTongTien.add(Box.createHorizontalStrut(5));
		boxTongTien.add(txtTongTien);
		boxTongTien.add(Box.createHorizontalStrut(5));

		box.add(Box.createVerticalStrut(5));
		box.add(boxTongTien);
		box.add(Box.createVerticalStrut(10));
	}

	private void addEast(JPanel pnlEast) {
		pnlEast.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		pnlEast.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		pnlEast.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0x9C9C9C)));

		JLabel lbKH = new JLabel("Khách hàng");
		JLabel lbMaKH = new JLabel("Mã khách hàng");
		JLabel lbTenKH = new JLabel("Tên khách hàng");
		//them
		JLabel lbNgaySinh = new JLabel("Ngày sinh");
		JLabel lbGioiTinh = new JLabel("Giới tính");
		JLabel lbDC = new JLabel("Địa chỉ");
		//them
		JLabel lbEmail = new JLabel("Email");
		JLabel lbSDT = new JLabel("SĐT");

		lbKH.setPreferredSize(new Dimension(100, 20)); lbKH.setFont(UIConstant.NORMAL_FONT);
		lbMaKH.setPreferredSize(new Dimension(100, 20)); lbMaKH.setFont(UIConstant.NORMAL_FONT);
		lbTenKH.setPreferredSize(new Dimension(100, 20)); lbTenKH.setFont(UIConstant.NORMAL_FONT);
		//them
		lbNgaySinh.setPreferredSize(new Dimension(100, 20)); lbTenKH.setFont(UIConstant.NORMAL_FONT);
		lbGioiTinh.setPreferredSize(new Dimension(100, 20)); lbGioiTinh.setFont(UIConstant.NORMAL_FONT);
		lbDC.setPreferredSize(new Dimension(100, 20)); lbDC.setFont(UIConstant.NORMAL_FONT);
		//them
		lbEmail.setPreferredSize(new Dimension(100, 20)); lbSDT.setFont(UIConstant.NORMAL_FONT);
		lbSDT.setPreferredSize(new Dimension(100, 20)); lbSDT.setFont(UIConstant.NORMAL_FONT);

		txtMaKhachHang = new JTextField();
		txtMaKhachHang.setEnabled(false);
		txtTenKhachHang = new JTextField();
		txtTenKhachHang.setEnabled(false);
		txtNgaySinh = new JDateChooser();
		txtNgaySinh.setDateFormatString("dd-MM-yyyy");
		txtNgaySinh.setEnabled(false);
		txtGioiTinh = new JComboBox<String>();
		txtGioiTinh.setFont(UIConstant.NORMAL_FONT);
		txtGioiTinh.setPreferredSize(new Dimension(100,20));
		txtGioiTinh.addItem("Nam");
		txtGioiTinh.addItem("Nữ");
		txtGioiTinh.setEnabled(false);
		txtDC = new JTextField();
		txtDC.setEnabled(false);
		txtEmail = new JTextField();
		txtEmail.setEnabled(false);
		txtSDT = new JTextField();
		txtSDT.setEnabled(false);
		//them
		ballTen = new BalloonTip(txtTenKhachHang,""); ballTen.setVisible(false); ballTen.setCloseButton(null);
		ballNS = new BalloonTip(txtNgaySinh, ""); ballNS.setVisible(false); ballNS.setCloseButton(null);
		ballDC = new BalloonTip(txtDC, ""); ballDC.setVisible(false); ballDC.setCloseButton(null);
		ballEmail = new BalloonTip(txtEmail, ""); ballEmail.setVisible(false); ballEmail.setCloseButton(null);
		ballSDT = new BalloonTip(txtSDT, ""); ballSDT.setVisible(false); ballSDT.setCloseButton(null);
		btnChonKhachHang = new ColoredButton("Chọn khách hàng", new ImageIcon("img/add.png"));
		btnChonKhachHang.setBackground(UIConstant.PRIMARY_COLOR);
		btnChonKhachHang.setBorderRadius(30);
		btnChonKhachHang.setEnabled(false);
		btnIn = new ColoredButton("In hóa đơn", new ImageIcon("img/inhoadon.png"));
		btnIn.setBackground(UIConstant.WARNING_COLOR);
		btnIn.setEnabled(false);
		btnIn.setBorderRadius(30);

		btnLuu = new ColoredButton("Lưu hóa đơn", new ImageIcon("img/save.png"));
		btnLuu.setBackground(UIConstant.PRIMARY_COLOR);
		btnLuu.setBorderRadius(30);
		JPanel pnlHoaDon = new JPanel();
		pnlHoaDon.setOpaque(false);

		pnlEast.add(pnlHoaDon, BorderLayout.NORTH);

		pnlHoaDon.setLayout(new BoxLayout(pnlHoaDon, BoxLayout.Y_AXIS));

		Box boxLoai = Box.createHorizontalBox();
		Box boxMa = Box.createHorizontalBox();
		Box boxKH = Box.createHorizontalBox();
		Box boxMaKH = Box.createHorizontalBox();
		Box boxTenKH = Box.createHorizontalBox();
		Box boxNS = Box.createHorizontalBox();
		Box boxGioiTinh = Box.createHorizontalBox();
		Box boxDiaChi = Box.createHorizontalBox();
		Box boxEmail = Box.createHorizontalBox();
		Box boxSDT = Box.createHorizontalBox();
		Box boxButton = Box.createHorizontalBox();
		Box boxTienKhachDua = Box.createHorizontalBox();
		Box boxTienTraKhach = Box.createHorizontalBox();

		pnlHoaDon.add(Box.createVerticalStrut(15));
		pnlHoaDon.add(boxLoai);
		pnlHoaDon.add(Box.createVerticalStrut(15));
		pnlHoaDon.add(boxMa);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxKH);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxMaKH);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxTenKH);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxNS);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxGioiTinh);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxDiaChi);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxEmail);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxSDT);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxTienKhachDua);
		pnlHoaDon.add(Box.createVerticalStrut(5));
		pnlHoaDon.add(boxTienTraKhach);
		pnlHoaDon.add(Box.createVerticalStrut(20));
		pnlHoaDon.add(boxButton);
		pnlHoaDon.add(Box.createVerticalStrut(5));

		boxKH.add(Box.createHorizontalStrut(5));
		boxKH.add(lbKH);
		boxKH.add(Box.createHorizontalGlue());
		boxKH.add(btnChonKhachHang);
		boxKH.add(Box.createHorizontalStrut(5));

		boxMaKH.add(Box.createHorizontalStrut(15));
		boxMaKH.add(lbMaKH);
		boxMaKH.add(txtMaKhachHang);
		boxMaKH.add(Box.createHorizontalStrut(5));

		boxTenKH.add(Box.createHorizontalStrut(15));
		boxTenKH.add(lbTenKH);
		boxTenKH.add(txtTenKhachHang);
		boxTenKH.add(Box.createHorizontalStrut(5));

		boxNS.add(Box.createHorizontalStrut(15));
		boxNS.add(lbNgaySinh);
		boxNS.add(txtNgaySinh);
		boxNS.add(Box.createHorizontalStrut(5));


		boxGioiTinh.add(Box.createHorizontalStrut(15));
		boxGioiTinh.add(lbGioiTinh);
		boxGioiTinh.add(txtGioiTinh);
		boxGioiTinh.add(Box.createHorizontalStrut(5));

		boxDiaChi.add(Box.createHorizontalStrut(15));
		boxDiaChi.add(lbDC);
		boxDiaChi.add(txtDC);
		boxDiaChi.add(Box.createHorizontalStrut(5));


		boxEmail.add(Box.createHorizontalStrut(15));
		boxEmail.add(lbEmail);
		boxEmail.add(txtEmail);
		boxEmail.add(Box.createHorizontalStrut(5));

		boxSDT.add(Box.createHorizontalStrut(15));
		boxSDT.add(lbSDT);
		boxSDT.add(txtSDT);
		boxSDT.add(Box.createHorizontalStrut(5));

		boxButton.add(Box.createHorizontalStrut(5));
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(btnLuu);
		boxButton.add(Box.createHorizontalStrut(5));
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(btnIn);
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(Box.createHorizontalStrut(5));

		JLabel lbTienKhachDua = new JLabel("Tiền khách đưa");
		lbTienKhachDua.setFont(UIConstant.NORMAL_FONT);
		JLabel lbTienTra = new JLabel("Tiền trả khách");
		lbTienTra.setFont(UIConstant.NORMAL_FONT);

		lbTienTra.setPreferredSize(lbTienKhachDua.getPreferredSize());

		txtTienKhachDua = new JTextField();
		txtTienKhachDua.setEnabled(false);
		balltienkhachdua = new BalloonTip(txtTienKhachDua, "Số lượng phải là số nguyên lớn hơn 0!"); balltienkhachdua.setVisible(false); balltienkhachdua.setCloseButton(null);
		txtTienKhachDua.setFont(UIConstant.NORMAL_FONT);
		txtTienTra = new JTextField(); txtTienTra.setEnabled(false);
		txtTienTra.setFont(UIConstant.NORMAL_FONT);

		boxTienKhachDua.add(Box.createHorizontalStrut(10));
		boxTienKhachDua.add(lbTienKhachDua);
		boxTienKhachDua.add(Box.createHorizontalStrut(5));
		boxTienKhachDua.add(txtTienKhachDua);
		boxTienKhachDua.add(Box.createHorizontalStrut(10));

		boxTienTraKhach.add(Box.createHorizontalStrut(10));
		boxTienTraKhach.add(lbTienTra);
		boxTienTraKhach.add(Box.createHorizontalStrut(5));
		boxTienTraKhach.add(txtTienTra);
		boxTienTraKhach.add(Box.createHorizontalStrut(10));
	}

	private void addNorth(JPanel pnlNorth) {		
		JPanel pnlHD = new JPanel();
		pnlHD.setOpaque(false);
		pnlHD.setLayout(new BoxLayout(pnlHD, BoxLayout.Y_AXIS));
		pnlHD.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIConstant.LINE_COLOR));

		pnlNorth.add(pnlHD, BorderLayout.NORTH);


		JLabel lbMaHD = new JLabel("Mã hóa đơn");


		lbMaHD.setPreferredSize(new Dimension(100, 20)); lbMaHD.setFont(UIConstant.NORMAL_FONT);
		JLabel lbNgayLap = new JLabel("Ngày lập");
		lbNgayLap.setPreferredSize(new Dimension(100, 20)); lbNgayLap.setFont(UIConstant.NORMAL_FONT);

		dateNgayLap = new JDateChooser();
		dateNgayLap.setDate(new Date());
		dateNgayLap.setDateFormatString("dd-MM-yyyy");
		dateNgayLap.setEnabled(false);

		txtMaHoaDon = new JTextField(30); txtMaHoaDon.setEnabled(false);
		txtMaHoaDon.setToolTipText("Mã hóa đơn được tự động khởi tạo.");

		Box boxHD = Box.createHorizontalBox();

		pnlHD.add(Box.createVerticalStrut(10));
		pnlHD.add(boxHD);
		pnlHD.add(Box.createVerticalStrut(10));

		boxHD.add(Box.createHorizontalStrut(5));
		boxHD.add(lbMaHD);
		boxHD.add(txtMaHoaDon);
		boxHD.add(Box.createHorizontalStrut(5));

		boxHD.add(Box.createHorizontalStrut(5));
		boxHD.add(lbNgayLap);
		boxHD.add(dateNgayLap);
		boxHD.add(Box.createHorizontalStrut(5));
	}

	private void addEvent() {
		txtTienKhachDua.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {

					double tienDua = Double.parseDouble(txtTienKhachDua.getText());
					tientra=tienDua - tongTien;
					if(tienDua > 0)
					{
						txtTienTra.setText(nf.format(tientra) +" VND");
					}
					else
					{
						UIConstant.showWarning(CapNhatHoaDonDialog.this, "Vui lòng nhập số tiền lớn hơn 0");
						return;
					}
					balltienkhachdua.setVisible(false);
				} catch (NumberFormatException e2) {
					// TODO: handle exception
					txtTienTra.setText("");
					if(txtTienKhachDua.getText().equals(""))
					{
						balltienkhachdua.setVisible(false);
					}else
					{
						balltienkhachdua.setTextContents("Tiền khách đưa là số");
						balltienkhachdua.setVisible(true);
						return;
					}
					
				}
			}
		});
		//sua
		btnIn.addActionListener(e -> {
			
			Bill bill = new Bill(CapNhatHoaDonDialog.this, hoaDon);
			PrintSupport.printComponent(Bill.textArea);
			String cmds[] = new String[] {"cmd","/c","C:\\Users\\DELL-5570\\OneDrive\\Desktop\\a.pdf"};
			try {
				Runtime.getRuntime().exec(cmds);
			}catch (Exception e1) {
				// TODO: handle exception
			}
			
		});

		btnChonKhachHang.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(chonKhachHangDialog == null)
					chonKhachHangDialog = new ChonKhachHangDialog(CapNhatHoaDonDialog.this);

				chonKhachHangDialog.setVisible(true);
			}
		});


		btnThemSanPham.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(chonSanPhamDialog == null)
					chonSanPhamDialog = new chonSanPhamDialog(CapNhatHoaDonDialog.this);

				chonSanPhamDialog.setVisible(true);
				//them
				btnChonKhachHang.setEnabled(true);
				txtTenKhachHang.setEnabled(true);
				txtSoLuong.setEnabled(true);
				txtTienKhachDua.setEnabled(true);
			}
		});



		btnQuayLai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CapNhatHoaDonDialog.this.dispose();
				frame.setEnabled(true);
				frame.setVisible(true);
			}
		});

		btnLuu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(dsCTHD == null || dsCTHD.size() == 0) {
					UIConstant.showWarning(CapNhatHoaDonDialog.this, "Không có sản phẩm nào trong hóa đơn!");
					return;
				}
				String tkd = txtTienKhachDua.getText().trim();


				if(tkd.isEmpty() )
				{
					UIConstant.showWarning(CapNhatHoaDonDialog.this, "Chưa nhập tiền khách đưa");
					return;
				}
				//them
				
				if( tientra < 0)
				{
					UIConstant.showWarning(CapNhatHoaDonDialog.this, "Tiền Khách Đưa còn thiếu");
					return;
				}



				if(txtMaKhachHang.getText().equals("") && (!txtTenKhachHang.getText().equals("")))
				{
					if(validData())
					{
						LocalDate ngaySinh = null;
						Date date = txtNgaySinh.getDate();
						if(date != null) {
							SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
							String str_date = format1.format(date);

							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
							// convert String to LocalDate
							ngaySinh = LocalDate.parse(str_date, formatter);
						}
						LocalDate now = LocalDate.now();
						Period tuoi = Period.between(ngaySinh, now);
						String ten = txtTenKhachHang.getText().trim();
						String diachi = txtDC.getText().trim();
						String sdt = txtSDT.getText().trim();
						String email = txtEmail.getText().trim();
						boolean gt = txtGioiTinh.getSelectedItem().toString().equals("Nam") ? true : false;
						khachHang = new KhachHang(ten, ngaySinh, diachi, gt, email, sdt);
						if (tuoi.getYears() < 10) {
							UIConstant.showInfo(CapNhatHoaDonDialog.this, "Không nhập khách hàng nhỏ hơn 10 tuổi");
							return;
						}
						if (kiemtraTrungSDT() == false) {
							UIConstant.showWarning(CapNhatHoaDonDialog.this, "Số điện thoại đã được sử dụng!");
							txtSDT.requestFocus();
							txtSDT.selectAll();
							return;
						} 
						 if(kiemtraTrungEmail()==false) {
							UIConstant.showWarning(CapNhatHoaDonDialog.this, "Nhà cung cấp có số fax " + txtEmail.getText() +" đã được sử dụng");
							System.out.println("3");
							txtEmail.requestFocus();
							txtEmail.selectAll();
							return;
						}
						khachHangDAO.addKhachHang(khachHang);

						
						System.out.println("ss"+hoaDon);
						hoaDon.setKhachHang(khachHang);	
					}
					else
					{
						UIConstant.showWarning(CapNhatHoaDonDialog.this, "Thông tin khách hàng chưa đúng!");
						return;
					}


				}

				else if(txtTenKhachHang.getText().equals("") 
						&& (!txtDC.getText().equals(""))
						&& (!txtSDT.getText().equals("")))
				{
					UIConstant.showWarning(CapNhatHoaDonDialog.this, "Vui lòng nhập tên khách hàng!");
					return;
				}

					

				if(hoaDonDAO.lapHoaDon(hoaDon)) {
					System.out.println(hoaDon);
					for(CTHoaDon item : dsCTHD) {
						ctHoaDonDAO.themCTHoaDon(item);
					}

					UIConstant.showInfo(CapNhatHoaDonDialog.this, "Lập hóa đơn thành công!");
					btnIn.setEnabled(true);

				}
				else {
					UIConstant.showInfo(CapNhatHoaDonDialog.this, "Lập hóa đơn không thành công!");
				}
				btnChonKhachHang.setEnabled(false);
				btnThemSanPham.setEnabled(false);
				btnLuu.setEnabled(false);
				btnXoaCTHD.setEnabled(false);
				btnXoaRong.setEnabled(false);
				btnSuaCTHD.setEnabled(false);
				btnIn.setVisible(true);
				btnIn.setEnabled(true);
				txtDonGia.setEnabled(false);
				txtNgaySinh.setEnabled(false);
				txtGioiTinh.setEnabled(false);
				txtDC.setEnabled(false);
				txtEmail.setEnabled(false);
				txtSDT.setEnabled(false);
				txtSoLuong.setEnabled(false);
				txtTienKhachDua.setEnabled(false);
				txtTenKhachHang.setEnabled(false);
			}
			
		});

		btnSuaCTHD.addActionListener(new ActionListener() {



			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tableCTHD.getSelectedRow();

				if(row == -1) {
					UIConstant.showWarning(CapNhatHoaDonDialog.this, "Chưa chọn chi tiết cần sửa đổi!");
					return;
				}

				String soLuong = txtSoLuong.getText();


				int sl = 0;
				try {

					sl = Integer.parseInt(soLuong);
					if(sl <= 0)
						throw new NumberFormatException();
					ballSoLuong.setVisible(false);
				}catch (NumberFormatException e1) {
					ballSoLuong.setTextContents("Số lượng phải là số nguyên lớn hơn 0");
					ballSoLuong.setVisible(true);
					return;
				}
				//sửa
				String donGia = txtDonGia.getText();

				double tam= Double.parseDouble(donGia);
				double dg=tam*sl;
				//				try {
				////					dg = Double.parseDouble(donGia);
				//					 dg=tam*sl;
				//					if(dg <= 0)
				//						throw new NumberFormatException();
				//					ballDG.setVisible(false);
				//				}catch (NumberFormatException e1) {
				//					ballDG.setVisible(true);
				//					return;
				//				}
				int slTon = dsCTHD.get(row).getSanPham().getSoLuong();

				if(sl> slTon) {
					ballSoLuong.setTextContents("Số lượng không đủ, chỉ còn " + slTon + "!");
					ballSoLuong.setVisible(true);
					return;
				}
				dsCTHD.get(row).setSoLuong(sl);
				dsCTHD.get(row).tinhThanhTien();
				modelCTHD.setValueAt(nf.format(sl), row, 1);
				modelCTHD.setValueAt(nf.format(dg), row, 3);


				tongTien = 0;

				for(CTHoaDon item : dsCTHD) {
					tongTien += item.tinhThanhTien();	
				}
				txtTongTien.setText(nf.format(tongTien) + " VND");
				double tienDua = Double.parseDouble(txtTienKhachDua.getText());
				txtTienTra.setText(nf.format(tienDua - tongTien) + " VND");

				UIConstant.showInfo(CapNhatHoaDonDialog.this, "Sửa thành công!");			
			}
		});
		//sửa
		tableCTHD.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(tableCTHD.getSelectedRow() == -1)
					return;

				int row = tableCTHD.getSelectedRow();

				nf.setMinimumIntegerDigits(2);
				nf.setMaximumFractionDigits(2);

				txtMaSanPham.setText(dsCTHD.get(row).getSanPham().getMaSanPham());
				txtTenSanPham.setText(dsCTHD.get(row).getSanPham().getTenSanPham());
				txtSoLuong.setText(dsCTHD.get(row).getSoLuong() + "");
				txtDonGia.setText(dsCTHD.get(row).getDonGia() + "");

			}
		});


		btnXoaRong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtMaSanPham.setText("");
				txtTenSanPham.setText("");
				txtMaKhachHang.setText("");
				txtTenKhachHang.setText("");
				txtSoLuong.setText("");
				txtDonGia.setText("");
				txtTienTra.setText("");
				txtTienKhachDua.setText("");
				txtDC.setText("");
				txtSDT.setText("");
				txtEmail.setText("");
				txtNgaySinh.setCalendar(null);

				//				ballDG.setVisible(false);
				ballSoLuong.setVisible(false);
				hoaDon.setKhachHang(null);
			}
		});

		btnXoaCTHD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tableCTHD.getSelectedRow();

				if(row == -1) {
					UIConstant.showWarning(CapNhatHoaDonDialog.this, "Chưa chọn sản phảm trong chi tiết cần xoá!");
					return;
				}
				int selection = JOptionPane.showConfirmDialog(CapNhatHoaDonDialog.this, "Xác nhận xóa chi tiết này?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
				if(selection == JOptionPane.CANCEL_OPTION)
					return;

				dsCTHD.remove(row);
				modelCTHD.removeRow(row);
				tongTien = 0;

				for(CTHoaDon item : dsCTHD) {
					tongTien += item.tinhThanhTien();	
				}
				txtTongTien.setText(nf.format(tongTien) + " VND");

			}
		});
		//them
		txtTenKhachHang.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtTenKhachHang.getText().trim().isEmpty()) {
					ballTen.setVisible(false);
				} 
				else if (!txtTenKhachHang.getText().trim().matches("[\\p{Lu}[A-Z]][\\p{L}[a-z]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z]]*)*")) {
					ballTen.setTextContents(" + Họ và tên khách hàng phải bắt đầu chữ cái in hoa \n"
							+ " + Không chứa các ký tự đặc biệt và số");
					ballTen.setVisible(true);
				} else 
					ballTen.setVisible(false);
				
				txtNgaySinh.setEnabled(true);
				txtGioiTinh.setEnabled(true);
				txtDC.setEnabled(true);
				txtEmail.setEnabled(true);
				txtSDT.setEnabled(true);
			}
		});
		txtNgaySinh.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				ballNS.setVisible(false);
				Date date = txtNgaySinh.getDate();
				if(date != null) {
					LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if (!ngayHopLe(LocalDate.now(), localDate)) {
						ballNS.setTextContents("Ngày sinh phải trước ngày hiện tại");
						ballNS.setVisible(true);
					}
				}
			}
		});

		txtDC.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				ballDC.setVisible(false);
				//				if (txtDC.getText().trim().isEmpty()) {
				//					ballDC.setTextContents("Địa chỉ không được rỗng");
				//					ballDC.setVisible(false);
				//				}
			}
		});

		txtSDT.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				ballSDT.setVisible(false);
				if(txtSDT.getText().trim().isEmpty())
					ballSDT.setVisible(false);
				else if (!txtSDT.getText().trim().matches("[0]\\d{9}")) {
					ballSDT.setTextContents("Số điện thoại gồm 10 chữ số, bắt đầu là 0");
					ballSDT.setVisible(true);
				}
				else
					ballSDT.setVisible(false);
			}
		});
		txtEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				ballEmail.setVisible(false);
				if(txtEmail.getText().trim().isEmpty())
					ballEmail.setVisible(false);
				else if(!txtEmail.getText().trim().isEmpty()) {
					if (!txtEmail.getText().trim().matches("^[a-z][a-z0-9\\.]{7,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
						ballEmail.setTextContents(
								" + Email phải bắt đầu bằng 1 ký tự \n" + "+ Chỉ chứa ký tự a-z, 0-9 và ký tự dấu chấm (.) \n"
										+ "+ Độ dài tối thiểu là 8, độ dài tối đa là 32 \n"
										+ " +Tên miền có thể là tên miền cấp 1 hoặc cấp 2");
						ballEmail.setVisible(true);
					}
				}
				else
					ballEmail.setVisible(false);
			}
		});







	}
	//sửa
	public void themChiTietHD(SanPham sanpham, int soLuong, double donGia) {

		if(dsCTHD == null)
			dsCTHD = new ArrayList<CTHoaDon>();

		CTHoaDon cthd = new CTHoaDon();
		cthd.setHoaDon(hoaDon);
		cthd.setSanPham(sanpham);
		cthd.setSoLuong(soLuong);
		cthd.setDonGia(donGia);
		if(dsCTHD.contains(cthd)){
			dsCTHD.remove(cthd);
		}


		//		HoaDonDAO tong = new HoaDonDAO();
		//		tong.tinhTongTien(hoaDon);


		//		tongTien += cthd.tinhThanhTien();
		//		txtTongTien.setText(nf.format(tongTien) + " VND");
		String tam="";
		for(CTHoaDon item : dsCTHD) {

		tam =item.getSanPham().getMaSanPham();
		}
		if(tam != sanpham.getMaSanPham())
		{
			dsCTHD.add(cthd);
		}
		else
		{
			UIConstant.showError(CapNhatHoaDonDialog.this, "Sản phẩm đã được chọn!!!");
			return;
		}
		
		tongTien += cthd.tinhThanhTien();
		txtTongTien.setText(nf.format(tongTien) + " VND");
		modelCTHD.addRow(new Object[] {
				sanpham.getTenSanPham(), soLuong, nf.format(donGia), nf.format(soLuong * donGia)
		});
	}
	//them
	public void setKhachHangDuocChon(KhachHang kh) {
		khachHang = kh;

		txtMaKhachHang.setText(kh.getMaKhachHang());
		txtTenKhachHang.setText(kh.getTenKhachHang());

		LocalDate localDate2 = kh.getNgaySinh();// For reference
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedString2 = localDate2.format(formatter2);
		Date date;
		try {

			date = new SimpleDateFormat("dd-MM-yyyy").parse(formattedString2);
			txtNgaySinh.setDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String gt = "";
		if (kh.isGioiTinh() == true)
			gt = "Nam";
		else
			gt = "Nữ";
		txtGioiTinh.setSelectedItem(gt);

		txtDC.setText(kh.getDiaChi());
		txtEmail.setText(kh.getEmail());
		txtSDT.setText(kh.getSoDienThoai());

		hoaDon.setKhachHang(kh);

	}


	private static JFrame getFrame(Container owner) {
		while(!(owner.getParent() instanceof JFrame))
			owner = owner.getParent();

		frame = (JFrame) owner.getParent();

		return frame;
	}

	public void themKhachHangMoi() {
		this.dispose();
		((MainFrame)frame).setEnabled(true);
		((MainFrame)frame).setVisible(true);
		((MainFrame)frame).changeCenter(((MainFrame)frame).getQuanLyKhachHangPanel());
		((MainFrame)frame).getQuanLyKhachHangPanel().themKhachHangMoi();

	}
	//them
	private boolean ngayHopLe(LocalDate ngayHienTai, LocalDate ngaySinh) {
		if (ngaySinh.isBefore(ngayHienTai))
			return true;

		return false;
	}
	private boolean validData() {
		String diaChi = txtDC.getText().trim();
		String email = txtEmail.getText().trim();
		String sdt = txtSDT.getText().trim();
		String ten = txtTenKhachHang.getText().trim();
		Date date = txtNgaySinh.getDate();

		// tên
		if (ten.isEmpty()) {
			ballTen.setTextContents("Tên khách hàng không được rỗng");
			ballTen.setVisible(true);
			return false;
		} 
		else if (!ten.matches("[\\p{Lu}[A-Z]][\\p{L}[a-z]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z]]*)*")) {
			ballTen.setTextContents(" + Họ và tên khách hàng phải bắt đầu chữ cái in hoa \n"
					+ " + Không chứa các ký tự đặc biệt và số");
			ballTen.setVisible(true);
			return false;
		} else 
			ballTen.setVisible(false);

		// ngày
		if(date != null) {
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (!ngayHopLe(LocalDate.now(), localDate)) {
				ballNS.setTextContents("Ngày sinh phải trước ngày hiện tại");
				ballNS.setVisible(true);
				return false;
			}
		}
		else if(date==null)
		{
			ballNS.setTextContents("Ngày sinh không được bỏ trống");
			ballNS.setVisible(true);
			return false;
		}
		ballNS.setVisible(false);

		// số điện thoại
		if (!sdt.matches("[0]\\d{9}")) {
			ballSDT.setTextContents("Số điện thoại gồm 10 chữ số, bắt đầu là 0");
			ballSDT.setVisible(true);
			return false;
		}
		ballSDT.setVisible(false);

		// đia chỉ
		if (diaChi.isEmpty()) {
			ballDC.setTextContents("Địa chỉ không được rỗng");
			ballDC.setVisible(true);
			return false;
		}
		ballDC.setVisible(false);

		// email
		if(!email.isEmpty()) {
			if (!email.matches("^[a-z][a-z0-9\\.]{7,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
				ballEmail.setTextContents(
						" + Email phải bắt đầu bằng 1 ký tự \n" + "+ Chỉ chứa ký tự a-z, 0-9 và ký tự dấu chấm (.) \n"
								+ "+ Độ dài tối thiểu là 8, độ dài tối đa là 32 \n"
								+ " +Tên miền có thể là tên miền cấp 1 hoặc cấp 2");
				ballEmail.setVisible(true);
				return false;
			}
		}
		ballEmail.setVisible(false);
		return true;
	}
	private boolean kiemtraTrungSDT() {
		List<KhachHang> list = new ArrayList<KhachHang>();

		list = khachHangDAO.timKhachHang("", "", "", txtSDT.getText());
		for (KhachHang kh : list) {
			if (list.contains(kh)) {

				return false;
			}
		}
		return true;
	}
	private boolean kiemtraTrungEmail() {
		List<KhachHang> list = new ArrayList<KhachHang>();
		if(txtEmail.getText().equals(""))
			return true;
		list = khachHangDAO.timKhachHang("", "", txtEmail.getText().trim(), "");
		for (KhachHang kh : list) {
			if (list.contains(kh)) {

				return false;
			}
		}
		return true;

	}
}
