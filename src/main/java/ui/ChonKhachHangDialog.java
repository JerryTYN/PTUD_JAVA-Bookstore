package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import dao.KhachHangDAO;
import entity.KhachHang;

public class ChonKhachHangDialog extends Dialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private ColoredButton btnTimKiem;
	private CustomTable tableKhachHang;
	private DefaultTableModel modelKH;
	private ColoredButton btnChon;
	private ColoredButton btnQuayLai;
	private JTextField txtTenKH;
	private JTextField txtDC;
	private JTextField txtSDT;
	private JTextField txtEmail;
	
	private CapNhatHoaDonDialog owner;
	private KhachHangDAO khachHangDAO;
	private List<KhachHang> khachHangs;
	private int currentIndex = 0;
	private JLabel lbPage;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private ColoredButton btnLamMoi;
	

	public ChonKhachHangDialog(CapNhatHoaDonDialog owner) {
		super(owner);
		this.owner = owner;
		setSize(owner.getSize());
		setTitle("Chọn khách hàng");

		
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/customer.png").getImage());
		setLayout(new BorderLayout());
		

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				owner.setEnabled(true);
			}
			@Override
			public void windowActivated(WindowEvent e) {
				owner.setEnabled(false);
			}
		});

		this.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		this.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

		
		addNorth();
		addCenter();
		addSouth();
//		setLookAndFeel();
		addEvent();
		
		khachHangDAO = new KhachHangDAO();
	}

	private static JFrame getFrame(Container owner) {
		while(!(owner.getParent() instanceof JFrame))
			owner = owner.getParent();

		frame = (JFrame) owner.getParent();

		return frame;
	}
	
	private void addNorth() {
		Box boxM = Box.createHorizontalBox();
		Box boxNorth = Box.createVerticalBox();
		boxM.add(Box.createHorizontalStrut(5));
		boxM.add(boxNorth);
		boxM.add(Box.createHorizontalStrut(5));
		this.add(boxM, BorderLayout.NORTH);

		JLabel lbten = new JLabel("Họ tên");
		JLabel lbDC = new JLabel("Địa chỉ");
		JLabel lbSDT = new JLabel("Số điện thoại");
		JLabel lbEmail = new JLabel("Email");


		lbten.setFont(UIConstant.NORMAL_FONT);		lbten.setPreferredSize(new Dimension(120,20));
		lbDC.setFont(UIConstant.NORMAL_FONT);		lbDC.setPreferredSize(new Dimension(90,20));
		lbSDT.setFont(UIConstant.NORMAL_FONT);		lbSDT.setPreferredSize(new Dimension(120,20));
		lbEmail.setFont(UIConstant.NORMAL_FONT);		lbEmail.setPreferredSize(new Dimension(90,20));


		txtTenKH  = new JTextField(30); txtTenKH.setFont(UIConstant.NORMAL_FONT);
		txtDC  = new JTextField(30); txtDC.setFont(UIConstant.NORMAL_FONT);
		txtSDT  = new JTextField(30); txtSDT.setFont(UIConstant.NORMAL_FONT);
		txtEmail  = new JTextField(30); txtEmail.setFont(UIConstant.NORMAL_FONT);


		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		Box box3 = Box.createHorizontalBox();
		Box box4 = Box.createHorizontalBox();

		boxNorth.add(box1);
		boxNorth.add(Box.createVerticalStrut(5));
		boxNorth.add(box2);
		boxNorth.add(Box.createVerticalStrut(5));
		boxNorth.add(box3);
		boxNorth.add(Box.createVerticalStrut(5));
		boxNorth.add(box4);
		boxNorth.add(Box.createVerticalStrut(5));

		box1.add(lbten); box1.add(Box.createHorizontalStrut(5));
		box1.add(txtTenKH); box1.add(Box.createHorizontalStrut(5));
		box1.add(lbDC); box1.add(Box.createHorizontalStrut(5));
		box1.add(txtDC); box1.add(Box.createHorizontalStrut(5));

		box2.add(lbSDT); box2.add(Box.createHorizontalStrut(5));
		box2.add(txtSDT); box2.add(Box.createHorizontalStrut(5));
		box2.add(lbEmail); box2.add(Box.createHorizontalStrut(5));
		box2.add(txtEmail); box2.add(Box.createHorizontalStrut(5));


		btnTimKiem = new ColoredButton("Tìm kiếm", new ImageIcon("img/search.png")); btnTimKiem.setFont(UIConstant.NORMAL_FONT);

		btnTimKiem.setBackground(UIConstant.PRIMARY_COLOR);
		btnTimKiem.setBorderRadius(30);
		
		
		btnLamMoi = new ColoredButton("Làm mới",new ImageIcon("img/refresh3.png")); btnTimKiem.setFont(UIConstant.NORMAL_FONT);

		btnLamMoi.setBackground(new Color(0x019474D));
		btnLamMoi.setBorderRadius(30);

		box4.add(Box.createHorizontalGlue());
		box4.add(btnTimKiem);
		box4.add(Box.createHorizontalStrut(15));
		box4.add(btnLamMoi);
		box4.add(Box.createHorizontalGlue());
	}
	
	private void addCenter() {
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.setOpaque(false);
		this.add(pnlCenter, BorderLayout.CENTER);

		tableKhachHang = new CustomTable();
		tableKhachHang.setModel(modelKH = new DefaultTableModel(new String[] {"Mã khách hàng", "Tên khách hàng", 
				"Ngày sinh", "Địa chỉ", "Giới tính", "Email", "Số điện thoại", },
				 0));

		tableKhachHang.setRowHeight(60);

		tableKhachHang.setFont(UIConstant.NORMAL_FONT);


		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setOpaque(false);

		pnlCenter.add(tabPane, BorderLayout.CENTER);

		JScrollPane scroll;
		tabPane.addTab("Kết quả tìm kiếm", scroll = new JScrollPane(tableKhachHang));
		scroll.getViewport().setBackground(Color.white);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		lbPage = new JLabel("Trang 1 trong 1 trang."); lbPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("img/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("img/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("img/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("img/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);

		btnHome.setToolTipText("Trang đầu");
		btnEnd.setToolTipText("Trang cuối");
		btnBefore.setToolTipText("Trang trước");
		btnNext.setToolTipText("Trang kế tiếp");

		Box boxPage = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHome); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBefore); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNext); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEnd); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		boxPage.add(lbPage); boxPage.add(Box.createHorizontalStrut(5));

		pnlCenter.add(boxPage, BorderLayout.SOUTH);
	}
	
	private void addSouth() {
		btnChon = new ColoredButton("Chọn", new ImageIcon("img/add.png"));
		btnChon.setBackground(UIConstant.DANGER_COLOR);
		btnChon.setBorderRadius(30);
		btnQuayLai = new ColoredButton("Quay lại", new ImageIcon("img/back.png"));
		btnQuayLai.setBackground(UIConstant.WARNING_COLOR);
		btnQuayLai.setBorderRadius(30);
		Box boxButtonCTHD;
		this.add(boxButtonCTHD = Box.createHorizontalBox(), BorderLayout.SOUTH);

		boxButtonCTHD.add(Box.createVerticalStrut(50));
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnChon);
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnQuayLai);
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(Box.createVerticalStrut(50));
	}
	
	public void taiDuLieuLenBang(List<KhachHang> dsKhachHang, int minIndex) {
		if(minIndex >= dsKhachHang.size() || minIndex < 0)
			return;
		
		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsKhachHang.size() - 1) / 20 + 1) + " trang.");
		modelKH.setRowCount(0);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsKhachHang.size())
				break;
			
			KhachHang item = dsKhachHang.get(i);
		
			modelKH.addRow(new Object[] {
					item.getMaKhachHang(), item.getTenKhachHang(),
					item.getNgaySinh().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),	item.getDiaChi(), item.isGioiTinh() ? "Nam" : "Nữ",
					item.getEmail(),
					item.getSoDienThoai()
			});
		}
		
		currentIndex = minIndex;
	}
	
	private void timKhachHang() {
		modelKH.setRowCount(0);
		modelKH.fireTableDataChanged();
		khachHangs = khachHangDAO.timKhachHang(txtTenKH.getText(), txtDC.getText(), txtEmail.getText(), txtSDT.getText());
		
		taiDuLieuLenBang(khachHangs, 0);
	}

	private void addEvent() {
		btnLamMoi.addActionListener(e -> {
			txtDC.setText("");
			txtEmail.setText("");
			txtTenKH.setText("");
			txtSDT.setText("");
		});
//		
		txtTenKH.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		txtDC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		txtEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		txtSDT.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		
		btnHome.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, khachHangs.size() - khachHangs.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, currentIndex   - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, currentIndex + 20);
			}
		});
		
		btnChon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tableKhachHang.getSelectedRow();
				if(row == -1) {

					UIConstant.showInfo(null, "Chưa chọn khách hàng!");
					return;
				}
				
				KhachHang kh = khachHangs.get(row + currentIndex);
				
				owner.setKhachHangDuocChon(kh);
				
				ChonKhachHangDialog.this.dispose();
				owner.setEnabled(true);
				owner.setVisible(true);

			}
		});
		
		btnTimKiem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timKhachHang();
			}
		});
		
		btnQuayLai.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ChonKhachHangDialog.this.dispose();
				owner.setEnabled(true);
				owner.setVisible(true);
			}
		});
	}


}
