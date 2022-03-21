package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import dao.HoaDonDAO;
import dao.NhanVienDao;
import dao.SanPhamDao;
import entity.HoaDon;
import entity.NhanVien;
import entity.SanPham;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

public class ThongKePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JDateChooser dateFrom;
	private JDateChooser dateTo;
	private ColoredButton btnThongKe;
	private ColoredButton btnQuayLai;
	private ColoredButton btnXoaRong;
	private ColoredButton btnXoaRong1;
	private HoaDonDAO hoaDonDAO;
	private CustomTable tableKetQua;
	private DefaultTableModel modelKQ;
	private JScrollPane scroll;
	private JLabel lbSoHoaDon;
	private JLabel lbTongDoanhThu;
	private NumberFormat nf = NumberFormat.getInstance(Locale.US);
	private MainFrame mainFrame;
	private JLabel lbPage;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private List<HoaDon> dsHoaDon;
	private int currentIndex = 0;
	private JTabbedPane tabPane;
	private JCheckBox ckbHetHang;
	private JCheckBox ckbSapHet;
	private JCheckBox ckbChuaBanLanNao;
	private ColoredButton btnThongKeSanPham;
	private JLabel lbTongHetHang;
	private JLabel lbTongSapHet;
	private JLabel lbTongChuaBan;
	private CustomTable tableSanPham;
	private DefaultTableModel modelSanPham;
	private JLabel lbPageSanPham;
	private ColoredButton btnHomeSanPham;
	private ColoredButton btnEndSanPham;
	private ColoredButton btnBeforeSanPham;
	private ColoredButton btnNextSanPham;

	private SanPhamDao sanPhamDAO;
	private List<SanPham> dsSanPham;
	private int currentSanPhamIndex;
	private ColoredButton btnChart;
	private static NhanVien nhanVien;
	private JComboBox<String> cbxNhanVien = new JComboBox<String>();
	private NhanVienDao nvDao = new NhanVienDao();
	private List<NhanVien> dsNV;
	public ThongKePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		//setLookAndFeel();

		setOpaque(true);
		setBackground(Color.white);
		setLayout(new BorderLayout());

		tabPane = new JTabbedPane();
		tabPane.setOpaque(false);
		tabPane.setBorder(null);
		this.add(tabPane);

		JPanel pnlTKDT = new JPanel(new BorderLayout());
		pnlTKDT.setOpaque(false);
		tabPane.addTab("Thống kê doanh thu", pnlTKDT);

		JPanel pnlNorth;
		pnlTKDT.add(pnlNorth = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorth.setOpaque(false);

		addNorthTKDT(pnlNorth);

		JPanel pnlCenter;
		pnlTKDT.add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenter.setOpaque(false);

		addCenterTKDT(pnlCenter);

		JPanel pnlSouth = new JPanel(new BorderLayout());
		this.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setOpaque(false);

		addSouth(pnlSouth);

		JPanel pnlTKSanPham = new JPanel(new BorderLayout());
		pnlTKSanPham.setOpaque(false);
		tabPane.addTab("Thống kê Sản phẩm", pnlTKSanPham);

		JPanel pnlNorthTKSanPham;
		pnlTKSanPham.add(pnlNorthTKSanPham = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorthTKSanPham.setOpaque(false);

		addNorthTKSanPham(pnlNorthTKSanPham);

		JPanel pnlCenterTKSanPham;
		pnlTKSanPham.add(pnlCenterTKSanPham = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenterTKSanPham.setOpaque(false);

		addCenterTKSanPham(pnlCenterTKSanPham);

		JPanel pnlSouthTKSanPham = new JPanel(new BorderLayout());
		pnlTKSanPham.add(pnlSouthTKSanPham, BorderLayout.SOUTH);
		pnlSouthTKSanPham.setOpaque(false);

		addEvent();

		hoaDonDAO = new HoaDonDAO();
		sanPhamDAO = new SanPhamDao();
	}

	public static NhanVien getNhanVien() {
		return nhanVien;
	}
	private void addCenterTKSanPham(JPanel pnlCenter) {
		tableSanPham = new CustomTable();
		JScrollPane scroll = new JScrollPane(tableSanPham);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(BorderFactory.createTitledBorder(""));

		pnlCenter.add(scroll, BorderLayout.CENTER);

		tableSanPham.setModel(modelSanPham = new DefaultTableModel(new String[] {"Hình ảnh", "Mã sản phẩm", "Tên sản phẩm", "Tác giả", "Nhà cung cấp",
				"Đơn giá", "Phân loại", "Số lượng", "Thể loại","Trạng thái"}, 0));
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

		lbPageSanPham = new JLabel("Trang 1 trong 1 trang."); lbPageSanPham.setFont(UIConstant.NORMAL_FONT);

		btnHomeSanPham = new ColoredButton(new ImageIcon("img/double_left.png")); btnHomeSanPham.setBackground(UIConstant.LINE_COLOR);
		btnEndSanPham = new ColoredButton(new ImageIcon("img/double_right.png")); btnEndSanPham.setBackground(UIConstant.LINE_COLOR);
		btnBeforeSanPham = new ColoredButton(new ImageIcon("img/left.png")); btnBeforeSanPham.setBackground(UIConstant.LINE_COLOR);
		btnNextSanPham = new ColoredButton(new ImageIcon("img/right.png")); btnNextSanPham.setBackground(UIConstant.LINE_COLOR);

		btnHomeSanPham.setToolTipText("Trang đầu");
		btnEndSanPham.setToolTipText("Trang cuối");
		btnBeforeSanPham.setToolTipText("Trang trước");
		btnNextSanPham.setToolTipText("Trang kế tiếp");

		btnHomeSanPham.setBorderRadius(20);
		btnEndSanPham.setBorderRadius(20);
		btnBeforeSanPham.setBorderRadius(20);
		btnNextSanPham.setBorderRadius(20);

		Box boxPage = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHomeSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBeforeSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNextSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEndSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		boxPage.add(lbPageSanPham); boxPage.add(Box.createHorizontalStrut(5));

		pnlCenter.add(boxPage, BorderLayout.SOUTH);
	}


	private void addNorthTKSanPham(JPanel pnlNorth) {
		JLabel lbOrder = new JLabel("Thống kê sản phẩm");
		lbOrder.setFont(new Font("Arial", Font.BOLD, 20));
		lbOrder.setHorizontalAlignment(JLabel.CENTER);
		lbOrder.setForeground(new Color(255, 127, 0));

		pnlNorth.add(lbOrder, BorderLayout.NORTH);

		Box boxY = Box.createVerticalBox();

		pnlNorth.add(boxY, BorderLayout.SOUTH);

		JLabel lbName = new JLabel("Chọn tiêu chí thống kê:");
		lbName.setForeground(new Color(0x646464));
		lbName.setFont(UIConstant.NORMAL_FONT);

		ckbHetHang = new JCheckBox("Sản phẩm hết hàng"); ckbHetHang.setFont(UIConstant.NORMAL_FONT); ckbHetHang.setOpaque(false);
		ckbSapHet = new JCheckBox("Sản phẩm sắp hết"); ckbSapHet.setFont(UIConstant.NORMAL_FONT); ckbSapHet.setOpaque(false);
		ckbChuaBanLanNao = new JCheckBox("Sản phẩm chưa bán lần nào"); ckbChuaBanLanNao.setFont(UIConstant.NORMAL_FONT); ckbChuaBanLanNao.setOpaque(false);

		JPanel pnlTieuChi = new JPanel(new BorderLayout());
		pnlTieuChi.setBackground(UIConstant.BELOW_COLOR);
		pnlTieuChi.setBorder(BorderFactory.createLineBorder(new Color(0xEBE9E9)));
		pnlTieuChi.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		pnlTieuChi.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		pnlTieuChi.add(lbName, BorderLayout.NORTH);

		Box boxTieuChi = Box.createHorizontalBox();
		boxTieuChi.add(Box.createHorizontalGlue());
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(ckbHetHang);
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(ckbSapHet);
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(ckbChuaBanLanNao);
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(Box.createHorizontalGlue());

		pnlTieuChi.add(boxTieuChi, BorderLayout.CENTER);

		Box boxBtn = Box.createHorizontalBox();
		boxBtn.add(Box.createHorizontalGlue());
		boxBtn.add(btnThongKeSanPham = new ColoredButton("Thống kê", new ImageIcon("img/statistic.png")));
		boxBtn.add(Box.createHorizontalStrut(10));
		boxBtn.add(btnXoaRong= new ColoredButton("Làm mới", new ImageIcon("img/refresh3.png")));
		boxBtn.add(Box.createHorizontalGlue());
		btnXoaRong.setBackground(new Color(0x019474D));
		btnThongKeSanPham.setBackground(UIConstant.PRIMARY_COLOR);
		btnThongKeSanPham.setFont(UIConstant.NORMAL_FONT);
		btnThongKeSanPham.setBorderRadius(30);
		btnXoaRong.setBorderRadius(30);

		boxY.add(Box.createVerticalStrut(5));
		boxY.add(pnlTieuChi);
		boxY.add(Box.createVerticalStrut(10));
		boxY.add(boxBtn);
		boxY.add(Box.createVerticalStrut(10));

		lbTongHetHang = addItem(boxY, "Tổng sản phẩm hết hàng");

		lbTongSapHet = addItem(boxY, "Tổng sản phẩm sắp hết");
		lbTongChuaBan = addItem(boxY, "Tổng sản phẩm chưa bán được lần nào");
	}

	private void taiDuLieuLenBang(List<HoaDon> dsHD, int minIndex) {
		if(minIndex >= dsHD.size() || minIndex < 0)
			return;

		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsHD.size() - 1) / 20 + 1) + " trang.");

		modelKQ.setRowCount(0);
		dsHoaDon = dsHD;
		modelKQ.getDataVector().removeAllElements();
		modelKQ.fireTableDataChanged();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsHD.size())
				break;
			HoaDon hd = dsHD.get(i);
			SwingUtilities.invokeLater(() -> {
				modelKQ.addRow(new Object[] {
						hd.getMaHoaDon(), hd.getKhachHang() == null ? null : hd.getKhachHang().getTenKhachHang(),
								hd.getNhanVienLap() == null ? null : hd.getNhanVienLap().getTenNhanVien(), hd.getNgayLap().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										nf.format(hoaDonDAO.tinhTongTien(hd))

				});


			});
		}

		currentIndex  = minIndex;

	}


	private void taiDuLieuLenBangSanPham(List<SanPham> dsSanPham, int minIndex) {
		if(minIndex >= dsSanPham.size() || minIndex < 0)
			return;

		lbPageSanPham.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsSanPham.size() - 1) / 20 + 1) + " trang.");

		modelSanPham.setRowCount(0);
		this.dsSanPham = dsSanPham;
		modelSanPham.getDataVector().removeAllElements();
		modelSanPham.fireTableDataChanged();

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsSanPham.size())
				break;
			SanPham SanPham = dsSanPham.get(i);


			SwingUtilities.invokeLater(() -> {
				String ha = "";
				byte[] b = null;
				if(SanPham.getHinhAnh() == null)
					ha =  "img/image.png";
				else {
					try {
						Blob blob = SanPham.getHinhAnh();

						b = blob.getBytes(1, (int) blob.length());

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} 

				modelSanPham.addRow(new Object[] {ha != "" ? ha : b, SanPham.getMaSanPham(), SanPham.getTenSanPham(),
						SanPham.getTacGia(),
						SanPham.getNhaCungCap() != null ? SanPham.getNhaCungCap().getTenNCC() : null,
								SanPham.getGiaThanh(), SanPham.getPhanLoai(), SanPham.getSoLuong(), SanPham.getTheLoai(),SanPham.TrangThai()});
				if(modelSanPham.getRowCount() == 0) {
					UIConstant.showInfo(ThongKePanel.this, "Không tìm thấy kết quả phù hợp!");
				}			
			});
		}

		currentSanPhamIndex  = minIndex;

	}



	private void addEvent() {
		btnHomeSanPham.addActionListener(e -> {
			if(dsSanPham != null) {
				taiDuLieuLenBangSanPham(dsSanPham, 0);
			}
		});

		btnEndSanPham.addActionListener(e -> {
			if(dsSanPham != null) {
				taiDuLieuLenBangSanPham(dsSanPham, dsSanPham.size() - dsSanPham.size() % 20);
			}
		});

		btnBeforeSanPham.addActionListener(e -> {
			if(dsSanPham != null) {
				taiDuLieuLenBangSanPham(dsSanPham, currentSanPhamIndex  - 20);
			}
		});

		btnNextSanPham.addActionListener(e -> {
			if(dsSanPham != null) {
				taiDuLieuLenBangSanPham(dsSanPham, currentSanPhamIndex + 20);
			}
		});

		//
		btnHome.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, dsHoaDon.size() - dsHoaDon.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, currentIndex  - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, currentIndex + 20);
			}
		});

		btnQuayLai.addActionListener((e) -> {
			mainFrame.changeCenter(mainFrame.getTrangChuPanel());
		});

		btnChart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DefaultCategoryDataset  data = new DefaultCategoryDataset();

				NumberFormat nf = NumberFormat.getInstance(Locale.US);
				nf.setMinimumIntegerDigits(2);
				nf.setMaximumFractionDigits(2);
				LocalDate fromDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getDate()));
				LocalDate toDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getDate()));

				String manv;

				if(cbxNhanVien.getSelectedItem().toString().equalsIgnoreCase("")) {
					manv = "";
				}else {
					NhanVien nv = nvDao.getNhanVienTheoTen(cbxNhanVien.getSelectedItem().toString());
					manv = nv.getMaNhanVien().toString();
				}
				if(fromDate.isAfter(toDate)==true) {
					UIConstant.showInfo(ThongKePanel.this, "Khoảng thời gian thống kê không hợp lí!");
					modelKQ.setRowCount(0);	
				}else {

					Object[] kq = hoaDonDAO.thongKeHoaDon(fromDate,toDate, manv);
					@SuppressWarnings("unchecked")
					List<HoaDon> list = (List<HoaDon>) kq[0];

					if(kq[1]== null) {
						UIConstant.showInfo(ThongKePanel.this, "Không có dữ liệu phù hợp!");
					} else {
						double tongTien = (double) kq[1];
						long noDay = (toDate.getDayOfYear() - fromDate.getDayOfYear()) / (24 * 3600 * 1000);
						for(int i = 0;i<=noDay;i++) {


							for (HoaDon hd : list) {
								double tam = 0;
								tam+=hoaDonDAO.getHoaDonTheo1Ngay(hd.getNgayLap());
								data.addValue(tam, "", hd.getNgayLap());
							}
						}

						JFreeChart chart = ChartFactory.createBarChart3D("Doanh thu hóa đơn", "Thời gian", "Doanh Thu", data, PlotOrientation.VERTICAL, false, true, false);
						CategoryPlot p = chart.getCategoryPlot();
						p.setRangeGridlinePaint(Color.BLACK);
						ChartFrame frame = new ChartFrame("Bieu do doanh thu", chart);
						frame.setVisible(true);

						frame.setSize(1000, 800);
						frame.setLocationRelativeTo(null);
					}
				}

			}

		});

		btnThongKe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				modelKQ.setRowCount(0);
				lbSoHoaDon.setText("0");
				lbTongDoanhThu.setText("0");
				scroll.setBorder(BorderFactory.createTitledBorder("Có " + 0 + " kết quả."));
				LocalDate fromDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getDate()));
				LocalDate toDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getDate()));
				String date = fromDate.toString();
				String manv;

				if(cbxNhanVien.getSelectedItem().toString().equalsIgnoreCase("")) {
					manv = "";
				}else {
					NhanVien nv = nvDao.getNhanVienTheoTen(cbxNhanVien.getSelectedItem().toString());
					manv = nv.getMaNhanVien().toString();
				}
				if(fromDate.isAfter(toDate)==true) {
					UIConstant.showInfo(ThongKePanel.this, "Khoảng thời gian thống kê không hợp lí!");
					modelKQ.setRowCount(0);	
				}
				else {

					try {
						Object[] kq = hoaDonDAO.thongKeHoaDon(fromDate, toDate, manv);

						@SuppressWarnings("unchecked")
						List<HoaDon> list = (List<HoaDon>) kq[0];
						double tongTien = (double) kq[1];

						modelKQ.setRowCount(0);
						scroll.setBorder(BorderFactory.createTitledBorder("Có " + list.size() + " kết quả."));

						nf.setMinimumIntegerDigits(2);
						nf.setMaximumFractionDigits(2);

						taiDuLieuLenBang(list, 0);
						lbSoHoaDon.setText(list.size() + "");
						lbTongDoanhThu.setText(nf.format(tongTien) + "");

					} catch (Exception e2) {
						UIConstant.showInfo(ThongKePanel.this, "Không có kết quả phù hợp!");

					}	
				}

			}
		});

		btnThongKeSanPham.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isHetHang = ckbHetHang.isSelected();
				boolean isSapHet = ckbSapHet.isSelected();
				boolean isChuaBanLanNao = ckbChuaBanLanNao.isSelected();

				if(!isHetHang && !isSapHet && !isChuaBanLanNao) {
					dsSanPham = null;
					modelSanPham.setRowCount(0);
					lbTongHetHang.setText("0");
					lbTongSapHet.setText("0");
					lbTongChuaBan.setText("0");
					UIConstant.showInfo(ThongKePanel.this, "Chưa chọn tiêu chí thống kê!");
					return;
				}

				Object[] kq = sanPhamDAO.thongKeSanPham(isHetHang, isSapHet, isChuaBanLanNao);

				int soHetHang = (int) kq[0];
				int soSapHet = (int) kq[1];
				int soChuaBanLanNao = (int) kq[2];
				if(soSapHet == 0 && soChuaBanLanNao == 0 && soHetHang== 0) {

					UIConstant.showInfo(ThongKePanel.this, "Không có kết quả!");

				}
				@SuppressWarnings("unchecked")
				List<SanPham> list = (List<SanPham>) kq[3];

				if(list.size() > 0) {

					modelKQ.setRowCount(0);
					scroll.setBorder(BorderFactory.createTitledBorder("Có " + list.size() + " kết quả."));

					nf.setMinimumIntegerDigits(2);
					nf.setMaximumFractionDigits(2);


					taiDuLieuLenBangSanPham(list, 0);

				}

				lbTongHetHang.setText(soHetHang + "");
				lbTongSapHet.setText(soSapHet + "");
				lbTongChuaBan.setText(soChuaBanLanNao + "");
			}
		});

		btnXoaRong.addActionListener(e -> {
			ckbHetHang.setSelected(false);
			ckbSapHet.setSelected(false);
			ckbChuaBanLanNao.setSelected(false);
			ckbSapHet.setEnabled(true);
			ckbChuaBanLanNao.setEnabled(true);
			ckbHetHang.setEnabled(true);
			modelSanPham.setRowCount(0);
			lbTongHetHang.setText("0");
			lbTongSapHet.setText("0");
			lbTongChuaBan.setText("0");
			scroll.setBorder(BorderFactory.createTitledBorder("Có " + 0 + " kết quả."));
		});
		btnXoaRong1.addActionListener(e -> {


			dateFrom.setDate(new Date());
			dateTo.setDate(new Date());
			modelKQ.setRowCount(0);
			lbSoHoaDon.setText("0");
			lbTongDoanhThu.setText("0");
			modelKQ.setRowCount(0);
			scroll.setBorder(BorderFactory.createTitledBorder("Có " + 0 + " kết quả."));
		});


	}



	private void addSouth(JPanel pnlSouth) {
		btnQuayLai = new ColoredButton("Quay lại", new ImageIcon("img/back.png"));
		btnQuayLai.setBackground(UIConstant.PRIMARY_COLOR);
		btnQuayLai.setBorderRadius(30);


		Box boxButtonCTHD;
		pnlSouth.add(boxButtonCTHD = Box.createHorizontalBox(), BorderLayout.EAST);

		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnQuayLai);
		boxButtonCTHD.add(Box.createHorizontalGlue());

		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
	}

	private void addCenterTKDT(JPanel pnlCenter) {
		tableKetQua = new CustomTable();
		scroll = new JScrollPane(tableKetQua);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(BorderFactory.createTitledBorder(""));

		pnlCenter.add(scroll, BorderLayout.CENTER);

		tableKetQua.setModel(modelKQ = new DefaultTableModel(
				new String[] {"Mã hóa đơn", "Khách hàng", "Nhân viên lập", "Ngày lập", "Tổng tiền"}
				, 0));

		lbPage = new JLabel("Trang 1 trong 1 trang."); lbPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("img/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("img/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("img/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("img/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);

		btnHome.setToolTipText("Trang đầu");
		btnEnd.setToolTipText("Trang cuối");
		btnBefore.setToolTipText("Trang trước");
		btnNext.setToolTipText("Trang kế tiếp");

		btnBefore.setBorderRadius(20);
		btnEnd.setBorderRadius(20);
		btnHome.setBorderRadius(20);
		btnNext.setBorderRadius(20);



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

	private JLabel addItem(Box parent, String name) {
		Box box = Box.createHorizontalBox();

		parent.add(box);
		parent.add(Box.createVerticalStrut(10));

		box.setOpaque(true);
		box.setBackground(UIConstant.BELOW_COLOR);
		box.setBorder(BorderFactory.createLineBorder(new Color(0xEBE9E9)));

		JLabel lbName = new JLabel(name);
		lbName.setForeground(new Color(0x646464));
		lbName.setFont(UIConstant.NORMAL_FONT);

		JLabel lbKQ = new JLabel("0");
		lbKQ.setForeground(new Color(0x646464));
		lbKQ.setFont(UIConstant.NORMAL_FONT);


		Box boxPage = Box.createVerticalBox();

		box.add(Box.createHorizontalStrut(20));
		box.add(boxPage);
		box.add(Box.createHorizontalGlue());
		box.add(lbKQ);
		box.add(Box.createHorizontalStrut(20));

		boxPage.add(Box.createVerticalStrut(10));
		boxPage.add(lbName);
		boxPage.add(Box.createVerticalStrut(10));

		return lbKQ;
	}

	private void addNorthTKDT(JPanel pnlNorth) {
		JLabel lbOrder = new JLabel("Thống kê doanh thu");
		lbOrder.setFont(new Font("Arial", Font.BOLD, 20));
		lbOrder.setHorizontalAlignment(JLabel.CENTER);
		lbOrder.setForeground(new Color(255, 127, 0));

		pnlNorth.add(lbOrder, BorderLayout.NORTH);

		Box boxY = Box.createVerticalBox();

		pnlNorth.add(boxY, BorderLayout.SOUTH);

		JLabel lbName = new JLabel("Chọn khoảng thời gian cần thống kê:");
		lbName.setForeground(new Color(0x646464));
		lbName.setFont(UIConstant.NORMAL_FONT);

		JLabel lbFrom = new JLabel("Từ ngày");
		lbFrom.setForeground(new Color(0x646464));
		lbFrom.setFont(UIConstant.NORMAL_FONT);

		JLabel lbTo = new JLabel("Đến ngày");
		lbTo.setForeground(new Color(0x646464));
		lbTo.setFont(UIConstant.NORMAL_FONT);

		dateFrom = new JDateChooser(new Date());
		dateFrom.setDateFormatString("dd-MM-yyyy");
		dateFrom.setFont(UIConstant.NORMAL_FONT);
		dateTo = new JDateChooser(new Date());
		dateTo.setDateFormatString("dd-MM-yyyy");
		dateTo.setFont(UIConstant.NORMAL_FONT);

		JPanel pnlDate = new JPanel(new BorderLayout());
		pnlDate.setBackground(UIConstant.BELOW_COLOR);
		pnlDate.setBorder(BorderFactory.createLineBorder(new Color(0xEBE9E9)));
		pnlDate.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		pnlDate.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		Box boxDate = Box.createHorizontalBox();
		boxDate.add(Box.createHorizontalStrut(20));
		boxDate.add(lbName);
		boxDate.add(Box.createHorizontalStrut(10));
		boxDate.add(lbFrom);
		boxDate.add(Box.createHorizontalStrut(10));
		boxDate.add(dateFrom);
		boxDate.add(Box.createHorizontalStrut(20));
		boxDate.add(lbTo);
		boxDate.add(Box.createHorizontalStrut(10));
		boxDate.add(dateTo);
		boxDate.add(Box.createHorizontalStrut(20));

		pnlDate.add(boxDate, BorderLayout.CENTER);
		Box boxChonNhanVien = Box.createHorizontalBox();
		boxChonNhanVien.setFont(new Font("Arial", Font.BOLD, 20));
		boxChonNhanVien.setForeground(new Color(255, 127, 0));
		JLabel lblChonNhanVien = new JLabel("Thống kê theo nhân viên:");
		//		cbxNhanVien = 
		cbxNhanVien.addItem("");
		dsNV = nvDao.lay20NhanVienGanDay();
		System.out.println(dsNV);
		for (NhanVien nvs : dsNV) {
			cbxNhanVien.addItem(nvs.getTenNhanVien().toString());
		}
		boxChonNhanVien.add(Box.createHorizontalStrut(20));
		boxChonNhanVien.add(lblChonNhanVien);
		boxChonNhanVien.add(Box.createHorizontalStrut(10));
		boxChonNhanVien.add(cbxNhanVien);
		Box boxBtn = Box.createHorizontalBox();
		boxBtn.add(Box.createHorizontalGlue());

		boxBtn.add(btnThongKe = new ColoredButton("Thống kê", new ImageIcon("img/statistic.png")));
		boxBtn.add(Box.createHorizontalStrut(10));
		boxBtn.add(btnXoaRong1 = new ColoredButton("Làm mới", new ImageIcon("img/refresh3.png")));
		boxBtn.add(Box.createHorizontalStrut(10));
		boxBtn.add(btnChart = new ColoredButton("Biểu đồ", new ImageIcon("img/bd2.png")));
		boxBtn.add(Box.createHorizontalGlue());


		btnChart.setBackground(UIConstant.PRIMARY_COLOR);
		btnChart.setFont(UIConstant.NORMAL_FONT);

		btnThongKe.setBackground(UIConstant.PRIMARY_COLOR);
		btnThongKe.setFont(UIConstant.NORMAL_FONT);
		btnThongKe.setBorderRadius(30);
		btnXoaRong1.setBorderRadius(30);
		btnChart.setBorderRadius(30);
		btnXoaRong1.setBackground(new Color(0x019474D));
		btnXoaRong1.setToolTipText("Làm rỗng toàn bộ");

		boxY.add(Box.createVerticalStrut(5));
		boxY.add(pnlDate);
		boxY.add(Box.createVerticalStrut(5));
		boxY.add(boxChonNhanVien);
		boxY.add(Box.createVerticalStrut(10));
		boxY.add(boxBtn);
		boxY.add(Box.createVerticalStrut(10));

		lbSoHoaDon = addItem(boxY, "Tổng hóa đơn");
		lbTongDoanhThu = addItem(boxY, "Tổng doanh thu");
	}


}
