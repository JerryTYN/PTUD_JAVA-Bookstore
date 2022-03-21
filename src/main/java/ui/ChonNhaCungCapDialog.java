package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;


public class ChonNhaCungCapDialog extends Dialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;	
	private ColoredButton btnTimKiem;
	private ColoredButton btnChon;
	private ColoredButton btnQuayLai;
	private JTextField txtTenNCC;
	private JTextField txtDiaChi;
	private JTextField txtSoFax;
	private CustomTable tableNhaCungCap;
	private DefaultTableModel modelNCC;
	private NhaCungCapDAO nhaCungCapDAO;
	protected List<NhaCungCap> nhaCungCaps;
	private JLabel lbPage;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private int currentIndex = 0;
	protected NhaCungCap nhaCungCap;

	public ChonNhaCungCapDialog(QuanLySanPhamPanel owner) {
		super(getFrame(owner));
		setSize(owner.getSize());
		setTitle("Chọn nhà cung cấp");
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/customer.png").getImage());
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
		this.setBackground(Color.white);
		this.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
		this.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		this.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		JPanel pnlMain;
		this.add(pnlMain = new JPanel(new BorderLayout()));
		pnlMain.setOpaque(false);

		Box boxNorth;
		pnlMain.add(boxNorth = Box.createHorizontalBox(), BorderLayout.NORTH);

		JLabel lbTenNCC = new JLabel("Tên nhà cung cấp");
		lbTenNCC.setFont(UIConstant.NORMAL_FONT);
		txtTenNCC = new JTextField();
		txtTenNCC.setFont(UIConstant.NORMAL_FONT);

		JLabel lbDiaChi = new JLabel("Địa chỉ");
		lbDiaChi.setFont(UIConstant.NORMAL_FONT);
		txtDiaChi = new JTextField();
		txtDiaChi.setFont(UIConstant.NORMAL_FONT);
		JLabel lbSoFax = new JLabel("Số Fax");
		lbSoFax.setFont(UIConstant.NORMAL_FONT);
		txtSoFax = new JTextField();
		txtSoFax.setFont(UIConstant.NORMAL_FONT);

		lbTenNCC.setPreferredSize(new Dimension(120, 20));
		lbDiaChi.setPreferredSize(new Dimension(120, 20));
		lbSoFax.setPreferredSize(new Dimension(120, 20));

		Box boxTT = Box.createVerticalBox();

		Box box1 = Box.createHorizontalBox();
		
		box1.add(Box.createHorizontalStrut(5));
		box1.add(lbTenNCC);
		box1.add(txtTenNCC);
		box1.add(Box.createHorizontalStrut(5));
		box1.add(Box.createHorizontalGlue());

		Box box2 = Box.createHorizontalBox();
		box2.add(Box.createHorizontalGlue());
		box2.add(Box.createHorizontalStrut(5));
		box2.add(lbDiaChi);
		box2.add(txtDiaChi);
		box2.add(Box.createHorizontalStrut(5));
		box2.add(lbSoFax);
		box2.add(txtSoFax);
		box2.add(Box.createHorizontalStrut(5));
		box2.add(Box.createHorizontalGlue());



		Box box5 = Box.createHorizontalBox();

		boxTT.add(box1);
		boxTT.add(Box.createVerticalStrut(5));
		boxTT.add(box2);
		boxTT.add(Box.createVerticalStrut(5));
		boxTT.add(Box.createVerticalStrut(5));
		boxTT.add(box5);		
		boxNorth.add(boxTT);

		btnTimKiem = new ColoredButton("Tìm", new ImageIcon("img/search.png"));
		btnTimKiem.setFont(UIConstant.NORMAL_FONT);
		btnTimKiem.setBackground(UIConstant.PRIMARY_COLOR);
		btnTimKiem.setForeground(Color.white);
		btnTimKiem.setBorderRadius(30);
		box5.add(Box.createHorizontalGlue());
		box5.add(Box.createHorizontalStrut(5));
		box5.add(btnTimKiem);
		box5.add(Box.createHorizontalStrut(5));
		box5.add(Box.createHorizontalGlue());

		tableNhaCungCap = new CustomTable();
		tableNhaCungCap.setModel(modelNCC = new DefaultTableModel(new String[] {"Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Số Fax"}, 0));		

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setOpaque(false);

		JPanel pnlCenter = new JPanel(new BorderLayout());

		pnlMain.add(pnlCenter, BorderLayout.CENTER);

		JScrollPane scroll;
		tabPane.addTab("Danh sách nhà cung cấp", scroll = new JScrollPane(tableNhaCungCap));
		scroll.getViewport().setBackground(Color.white);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		pnlCenter.add(scroll);

		lbPage = new JLabel("Trang 1 trong 1 trang."); lbPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("img/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("img/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("img/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("img/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);
		btnHome.setBorderRadius(20);
		btnEnd.setBorderRadius(20);
		btnBefore.setBorderRadius(20);
		btnNext.setBorderRadius(20);
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
		pnlCenter.setBackground(Color.WHITE);
		btnChon = new ColoredButton("Chọn", new ImageIcon("img/add.png"));
		btnChon.setBackground(UIConstant.PRIMARY_COLOR);
		btnChon.setBorderRadius(30);
		btnQuayLai = new ColoredButton("Quay lại", new ImageIcon("img/back.png"));
		btnQuayLai.setBackground(UIConstant.WARNING_COLOR);
		btnQuayLai.setBorderRadius(30);
		Box boxButtonCTHD;
		pnlMain.add(boxButtonCTHD = Box.createHorizontalBox(), BorderLayout.SOUTH);

		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnChon);
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnQuayLai);
		boxButtonCTHD.add(Box.createHorizontalGlue());

		nhaCungCapDAO = new NhaCungCapDAO();
		
		btnHome.addActionListener(e -> {
			if(nhaCungCaps != null) {
				taiDuLieuLenBang(nhaCungCaps, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(nhaCungCaps != null) {
				taiDuLieuLenBang(nhaCungCaps, nhaCungCaps.size() - nhaCungCaps.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(nhaCungCaps != null) {
				taiDuLieuLenBang(nhaCungCaps, currentIndex   - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(nhaCungCaps != null) {
				taiDuLieuLenBang(nhaCungCaps, currentIndex + 20);
			}
		});

		btnChon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nhaCungCap = nhaCungCaps.get(tableNhaCungCap.getSelectedRow() + currentIndex);
				owner.setNhaCungCapDaChon(nhaCungCap);

				ChonNhaCungCapDialog.this.dispose();
				frame.setEnabled(true);
				frame.setVisible(true);
			}
		});

		btnQuayLai.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {					
				nhaCungCap = null;
				
				ChonNhaCungCapDialog.this.dispose();
				frame.setEnabled(true);
				frame.setVisible(true);
			}
		});

		btnTimKiem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timNCC();
			}
		});
		
		txtTenNCC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				timNCC();
			}
		});
		txtDiaChi.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				timNCC();
			}
		});
		txtSoFax.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				timNCC();
			}
		});
	}

	private void timNCC() {
		modelNCC.setRowCount(0);

		nhaCungCaps = nhaCungCapDAO.findNhaCungCap(txtTenNCC.getText(), txtDiaChi.getText(), txtSoFax.getText());

		taiDuLieuLenBang(nhaCungCaps, 0);
	}

	private void taiDuLieuLenBang(List<NhaCungCap> dsNCC, int minIndex) {
		if(minIndex >= dsNCC.size() || minIndex < 0)
			return;

		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsNCC.size() - 1) / 20 + 1) + " trang.");

		modelNCC.setRowCount(0);
		nhaCungCaps = dsNCC;
		modelNCC.getDataVector().removeAllElements();
		modelNCC.fireTableDataChanged();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsNCC.size())
				break;
			NhaCungCap ncc = dsNCC.get(i);
			modelNCC.addRow(new Object[] {
					ncc.getMaNCC(),ncc.getTenNCC(),ncc.getDiaChi(),ncc.getSoFax()
			});
		}

		currentIndex = minIndex;

	}

	private static JFrame getFrame(Container owner) {
		while(!(owner.getParent() instanceof JFrame))
			owner = owner.getParent();

		frame = (JFrame) owner.getParent();

		return frame;
	}
	
}
