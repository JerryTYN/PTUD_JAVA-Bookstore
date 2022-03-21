package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme;

import dao.SanPhamDao;


import entity.SanPham;



public class chonSanPhamDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColoredButton btnTimKiem;
	private CustomTable tableSanPham;
	private DefaultTableModel modelSanPham;
	private ColoredButton btnChon;
	private ColoredButton btnQuayLai;

	private CapNhatHoaDonDialog owner;
	private JTextField txtTenSP;
	private JTextField txtNCC;
	private JTextField txtTG;
	private JTextField txtTL;

	private List<SanPham> sanPhams;
	private SanPhamDao sanPhamDAO;
	private DefaultComboBoxModel<String> phanLoaiModel;
	private JComboBox<String> cbbPhanLoai;
	private JLabel lbPage;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private int currentIndex = 0;
	private List<SanPham> dssp;
	private ColoredButton btnlamMoi;

	public chonSanPhamDialog(CapNhatHoaDonDialog owner) {
		super(owner);
		this.owner = owner;
		setSize(owner.getSize());
		setTitle("Chọn Sản Phẩm");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/sanpham.png").getImage());
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

		getContentPane().setBackground(Color.white);

		this.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		this.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

		addNorth();
		addCenter();
		addSouth();
//		setLookAndFeel();
		addEvent();

		sanPhamDAO= new SanPhamDao();
	}

//sửa
	private void timSanPham() {
		loadData();
		modelSanPham.setRowCount(0);
		modelSanPham.fireTableDataChanged();


		sanPhams = sanPhamDAO.timSanPham(txtTenSP.getText(),txtNCC.getText(),txtTG.getText(),txtTL.getText());

		if(sanPhams.size() == 0) {
			return;
		}
		
		taiDuLieuLenBang(sanPhams, 0);
	}
	
	
	private void loadData() {
		dssp = sanPhamDAO.lay20SanPhamGanDay();
		taiDuLieuLenBang(dssp, 0);

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

	private void addCenter() {
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.setOpaque(false);
		this.add(pnlCenter, BorderLayout.CENTER);

		tableSanPham = new CustomTable();
		tableSanPham.setModel(modelSanPham = new DefaultTableModel(new String[] {"Hình ảnh", "Mã sản phẩm", 
				"Tên sản phẩm", "Tác giả", "Nhà cung cấp", "Đơn giá", "Phân loại", 
				"Số lượng", "Thể loại",}, 0));
		tableSanPham.getColumn("Hình ảnh").setCellRenderer(new TableCellRenderer() {

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
		tableSanPham.setRowHeight(60);
		tableSanPham.getColumn("Hình ảnh").setMaxWidth(80);
		tableSanPham.getColumn("Hình ảnh").setMinWidth(80);
		tableSanPham.setFont(UIConstant.NORMAL_FONT);


		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setOpaque(false);

		pnlCenter.add(tabPane, BorderLayout.CENTER);

		JScrollPane scroll;
		tabPane.addTab("Kết quả tìm kiếm", scroll = new JScrollPane(tableSanPham));
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

	private void addNorth() {
		Box boxM = Box.createHorizontalBox();
		Box boxNorth = Box.createVerticalBox();
		boxM.add(Box.createHorizontalStrut(5));
		boxM.add(boxNorth);
		boxM.add(Box.createHorizontalStrut(5));
		this.add(boxM, BorderLayout.NORTH);

		JLabel lbTenSanPham = new JLabel("Tên Sản Phẩm");
		JLabel lbNhaCungCap = new JLabel("Nhà Cung Cấp");
		JLabel lbTacGia = new JLabel("Tác Giả");
		JLabel lbTheLoai = new JLabel("Thể Loại");


		lbTenSanPham.setFont(UIConstant.NORMAL_FONT);		lbTenSanPham.setPreferredSize(new Dimension(120,20));
		lbNhaCungCap.setFont(UIConstant.NORMAL_FONT);		lbNhaCungCap.setPreferredSize(new Dimension(90,20));
		lbTacGia.setFont(UIConstant.NORMAL_FONT);		lbTacGia.setPreferredSize(new Dimension(120,20));
		lbTheLoai.setFont(UIConstant.NORMAL_FONT);		lbTheLoai.setPreferredSize(new Dimension(90,20));


		txtTenSP = new JTextField(30); txtTenSP.setFont(UIConstant.NORMAL_FONT);
		txtNCC = new JTextField(30); txtNCC.setFont(UIConstant.NORMAL_FONT);
		txtTG = new JTextField(30); txtTG.setFont(UIConstant.NORMAL_FONT);
		txtTL = new JTextField(30); txtTL.setFont(UIConstant.NORMAL_FONT);


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

		box1.add(lbTenSanPham); box1.add(Box.createHorizontalStrut(5));
		box1.add(txtTenSP); box1.add(Box.createHorizontalStrut(5));
		box1.add(lbNhaCungCap); box1.add(Box.createHorizontalStrut(5));
		box1.add(txtNCC); box1.add(Box.createHorizontalStrut(5));

		box2.add(lbTacGia); box2.add(Box.createHorizontalStrut(5));
		box2.add(txtTG); box2.add(Box.createHorizontalStrut(5));
		box2.add(lbTheLoai); box2.add(Box.createHorizontalStrut(5));
		box2.add(txtTL); box2.add(Box.createHorizontalStrut(5));


		btnTimKiem = new ColoredButton("Tìm", new ImageIcon("img/search.png")); btnTimKiem.setFont(UIConstant.NORMAL_FONT);
		btnlamMoi = new ColoredButton("Làm mới", new ImageIcon("img/refresh3.png")); btnTimKiem.setFont(UIConstant.NORMAL_FONT);

		btnlamMoi.setBackground(new Color(0x019474D));
		btnlamMoi.setBorderRadius(30);
		btnTimKiem.setBackground(UIConstant.PRIMARY_COLOR);
		btnTimKiem.setBorderRadius(30);

		box4.add(Box.createHorizontalGlue());
		box4.add(btnTimKiem);
		box4.add(Box.createHorizontalStrut(15));
		box4.add(btnlamMoi);
		box4.add(Box.createHorizontalGlue());
	}

	public void taiDuLieuLenBang(List<SanPham> dsSanPham, int minIndex) {
		if(minIndex >= dsSanPham.size() || minIndex < 0)
			return;
		
		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsSanPham.size() - 1) / 20 + 1) + " trang.");
		modelSanPham.setRowCount(0);
		
		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsSanPham.size())
				break;
			
			SanPham item = dsSanPham.get(i);
			String ha = "";
			byte[] b = null;
			if(item.getHinhAnh() == null)
				ha =  "img/image.png";
			else {
				try {
					Blob blob = item.getHinhAnh();

					b = blob.getBytes(1, (int) blob.length());

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			NumberFormat nf = NumberFormat.getInstance(Locale.US);
			nf.setMinimumIntegerDigits(2);
			nf.setMaximumFractionDigits(2);
			if(item.getSoLuong()>0)
			{
			modelSanPham.addRow(new Object[] 
					{
							ha != "" ? ha : b, 
							item.getMaSanPham(),
							item.getTenSanPham(),
							item.getTacGia(),
							item.getNhaCungCap().getTenNCC(), 
							nf.format(item.getGiaThanh()),
							item.getPhanLoai(),
							item.getSoLuong(),
							item.getTheLoai()							
			});
			}
		}

		currentIndex = minIndex;
	}

	private void addEvent() {
		txtTenSP.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		btnlamMoi.addActionListener(e -> {
			txtNCC.setText("");
			txtTenSP.setText("");
			txtTG.setText("");
			txtTL.setText("");
		});
		txtNCC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtTG.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtTL.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
	
		btnHome.addActionListener(e -> {
			if(sanPhams != null) {
				taiDuLieuLenBang(sanPhams, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(sanPhams != null) {
				taiDuLieuLenBang(sanPhams, sanPhams.size() - sanPhams.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(sanPhams != null) {
				taiDuLieuLenBang(sanPhams, currentIndex   - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(sanPhams != null) {
				taiDuLieuLenBang(sanPhams, currentIndex + 20);
			}
		});

		btnChon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tableSanPham.getSelectedRow();
				if(row == -1) {
					UIConstant.showInfo(chonSanPhamDialog.this, "Chưa chọn sản phẩm cần thêm!");
					return;
				}
				
				row += currentIndex;

				SanPham sanpham = sanPhams.get(row);


				int soLuong;
				double donGia;

				do {
					String sl = JOptionPane.showInputDialog(chonSanPhamDialog.this, "Nhập số lượng cần thêm", 1);

					if(sl == null)
						return;

					try {
						soLuong = Integer.parseInt(sl);

						if(soLuong <= 0) {
							UIConstant.showInfo(chonSanPhamDialog.this, "Số lượng phải lớn hơn 0!");
							continue;
						}

						int slTon = sanpham.getSoLuong();

						if(soLuong > slTon) {
							UIConstant.showWarning(chonSanPhamDialog.this, "Số lượng không đủ, chỉ còn " + slTon + "!");
							continue;
						}
						
						break;
					} catch (Exception e2) {
						UIConstant.showError(chonSanPhamDialog.this, "Vui lòng chỉ nhập số nguyên!");
					}
					
				}while(true);
				donGia = sanpham.getGiaThanh();
				owner.themChiTietHD(sanpham, soLuong, sanpham.getGiaThanh());
				
				
				
			}
		});
	
		btnQuayLai.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chonSanPhamDialog.this.dispose();
				owner.setEnabled(true);
				owner.setVisible(true);
			}
		});

		btnTimKiem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timSanPham();
				
			}
		});
	}

	
}

