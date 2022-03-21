package ui;



import java.awt.Dimension;



import javax.swing.JDialog;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import dao.CTHoaDonDAO;
import dao.HoaDonDAO;
import entity.CTHoaDon;
import entity.HoaDon;


import java.awt.Font;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;



public class Bill extends JDialog{
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private HoaDonDAO hoaDonDAO;
	public static JTextArea textArea;
	public static String maHD;
	public static int kt=0;

	
	public  Bill (JDialog owner, HoaDon hoaDon){

		setBounds(100, 100, 712, 577);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);

		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setBounds(5, 5, 693, 535);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String kh = CapNhatHoaDonDialog.txtTenKhachHang.getText().equals("")?"Không có thông tin khách hàng":hoaDon.getKhachHang().getTenKhachHang();
		String sdt = CapNhatHoaDonDialog.txtSDT.getText().equals("")?"Không có thông tin khách hàng":hoaDon.getKhachHang().getSoDienThoai();

		textArea.append("\n				NHÀ SÁCH SACHHAY STORE \n");
		textArea.append("\n		12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Thành Phố Hồ Chí Minh \n");          
		 
		textArea.append("	    ---------------------------------------------------------------------\n\n");
		textArea.append("			PHIẾU THANH TOÁN \n");
		textArea.append("\n			Số Hóa Đơn          :" + hoaDon.getMaHoaDon() + "\n");
		textArea.append("			Ngày Lập Hóa Đơn    :" +  hoaDon.getNgayLap().toString() + "\n");
		textArea.append("			Nhân Viên           :" + hoaDon.getNhanVienLap().getTenNhanVien() + "\n");
		textArea.append("	    ---------------------------------------------------------------------\n\n");
		textArea.append("			THÔNG TIN KHÁCH HÀNG \n");
		textArea.append("\n			Tên khách hàng      :  " + kh + "\n");
		textArea.append("			Số điện thoại       :  " + sdt + "\n");
		textArea.append("\n      ----------------------------------------------------------------------------------------\n");
		textArea.append(String.format("   |%-35s|%-15s|%-15s|%-17s|\n", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"));
		textArea.append("      ----------------------------------------------------------------------------------------\n");
		
		DecimalFormat df = new DecimalFormat("#,###,###,### VNĐ");
		NumberFormat num = NumberFormat.getNumberInstance();

		List<CTHoaDon> ds = new CTHoaDonDAO().getDsCTHD(hoaDon);

		for(CTHoaDon item : ds) {
			JTextArea txtTenSanPham = new JTextArea(item.getSanPham().getTenSanPham());
			txtTenSanPham.setFont(new Font("Times new roman", Font.PLAIN, 16));
			txtTenSanPham.setMaximumSize(new Dimension(250, 50));
			txtTenSanPham.setLineWrap(true);
			txtTenSanPham.setWrapStyleWord(true);
			textArea.append(String.format("   |%-35s|%-15s|%-15s|%-17s|\n",txtTenSanPham.getText(), item.getSoLuong() + " ", item.getDonGia() + "", df.format(item.tinhThanhTien())));
			textArea.append("      ----------------------------------------------------------------------------------------\n");
		}
		double cusMoney = 0;
		textArea.append(String.format("\n							Tổng tiền      : %20s VND" , df.format(new HoaDonDAO().tinhTongTien(hoaDon))));
		kt = CapNhatHoaDonDialog.txtTienKhachDua.getText().equals("")? 1:0;
		if(kt == 0) {
			try {
				cusMoney = num.parse(CapNhatHoaDonDialog.txtTienKhachDua.getText()).doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			textArea.append(String.format("\n							Tiền khách trả : %20s" , df.format(cusMoney)));
			textArea.append(String.format("\n							Tiền thừa      : %20s" , CapNhatHoaDonDialog.txtTienTra.getText()));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			textArea.append("\n\n			Ngày xuất hóa đơn  :  " + sdf.format(date) +"\n");
		}
			
		contentPanel.add(textArea);
	}
}
