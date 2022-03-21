package entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class HoaDon {
	@Id
	@GeneratedValue(generator = "generator_hoaDonId")
	@GenericGenerator(name = "generator_hoaDonId", strategy = "entity.id_generator.HoaDonStringGeneratorId")
	private String maHoaDon;
	
	@ManyToOne
	@JoinColumn(name = "maNhanVien")
	private NhanVien nhanVienLap;
	
	@ManyToOne
	@JoinColumn(name = "maKhachHang")
	private KhachHang khachHang;
	private LocalDate ngayLap;
	
	public HoaDon() {
		super();
	}
	public HoaDon(NhanVien nhanVienLap, KhachHang khachHang, LocalDate ngayLap) {
		this();
		this.nhanVienLap = nhanVienLap;
		this.khachHang = khachHang;
		this.ngayLap = ngayLap;
	}
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public NhanVien getNhanVienLap() {
		return nhanVienLap;
	}
	public void setNhanVienLap(NhanVien nhanVienLap) {
		this.nhanVienLap = nhanVienLap;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public LocalDate getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
	}
	
	
	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", nhanVienLap=" + nhanVienLap + ", khachHang=" + khachHang
				+ ", ngayLap=" + ngayLap + "]";
	}
}
