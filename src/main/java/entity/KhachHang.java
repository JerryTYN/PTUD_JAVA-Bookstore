package entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class KhachHang {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "generator_khachHangId")
	@GenericGenerator(name = "generator_khachHangId", strategy = "entity.id_generator.KhachHangStringGeneratorId")
	private String maKhachHang;

	@Column(columnDefinition = "nvarchar(255)")
	private String tenKhachHang;
	
	@Column(columnDefinition = "nvarchar(255)")
	private String diaChi;
	
	@Column(columnDefinition = "bit")
	private boolean gioiTinh;
	
	@Column(columnDefinition = "nvarchar(255)",nullable = true)
	private String email;
	
	@Column(columnDefinition = "datetime")
	private LocalDate ngaySinh;

	@Column(columnDefinition = "nvarchar(255)")
	private String soDienThoai;
	
	public KhachHang() {
	}

	public KhachHang(String maKhachHang, String tenKhachHang, LocalDate ngaySinh, String diaChi, boolean gioiTinh,
			String soDienThoai) {
		super();
		this.maKhachHang = maKhachHang;
		this.tenKhachHang = tenKhachHang;
		this.ngaySinh = ngaySinh;
		this.diaChi = diaChi;
		this.gioiTinh = gioiTinh;
		this.soDienThoai = soDienThoai;
	}

	public KhachHang(String tenKhachHang, LocalDate ngaySinh, String diaChi, boolean gioiTinh,
			String email, String soDienThoai) {
		super();
		this.tenKhachHang = tenKhachHang;
		this.ngaySinh = ngaySinh;
		this.diaChi = diaChi;
		this.gioiTinh = gioiTinh;
		this.email = email;
		this.soDienThoai = soDienThoai;
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public boolean isGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", tenKhachHang=" + tenKhachHang + ", ngaySinh=" + ngaySinh
				+ ", diaChi=" + diaChi + ", gioiTinh=" + gioiTinh + ", email=" + email + ", soDienThoai=" + soDienThoai
				+ "]";
	}
	
	
	
}
