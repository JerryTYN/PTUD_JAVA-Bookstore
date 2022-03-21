package entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class NhanVien {
	@Id
	@GeneratedValue(generator = "generator_nhanVienId")
	@GenericGenerator(name = "generator_nhanVienId", strategy = "entity.id_generator.NhanVienStringGeneratorId")
	private String maNhanVien;

	@Column(columnDefinition = "nvarchar(255)")
	private String tenNhanVien;
	private LocalDate ngaySinh;

	private int trangThai;
	private boolean gioiTinh;

	@Column(columnDefinition = "nvarchar(255)")
	private String email;

	@Column(columnDefinition = "nvarchar(255)")
	private String soDienThoai;
	private LocalDate ngayVaoLam;
	private int loaiNhanVien;

	@Column(columnDefinition = "nvarchar(255)")
	private String matKhau;

	public NhanVien(String tenNhanVien, LocalDate ngaySinh, int trangThai, boolean gioiTinh, String email,
			String soDienThoai, LocalDate ngayVaoLam, int loaiNhanVien) {
		super();
		this.tenNhanVien = tenNhanVien;
		this.ngaySinh = ngaySinh;
		this.trangThai = trangThai;
		this.gioiTinh = gioiTinh;
		this.email = email;
		this.soDienThoai = soDienThoai;
		this.ngayVaoLam = ngayVaoLam;
		this.loaiNhanVien = loaiNhanVien;
		this.matKhau = "123456";
	}

	
	

	public NhanVien(String maNhanVien, String tenNhanVien, LocalDate ngaySinh, int trangThai, boolean gioiTinh,
			String email, String soDienThoai, LocalDate ngayVaoLam, int loaiNhanVien, String matKhau) {
		super();
		this.maNhanVien = maNhanVien;
		this.tenNhanVien = tenNhanVien;
		this.ngaySinh = ngaySinh;
		this.trangThai = trangThai;
		this.gioiTinh = gioiTinh;
		this.email = email;
		this.soDienThoai = soDienThoai;
		this.ngayVaoLam = ngayVaoLam;
		this.loaiNhanVien = loaiNhanVien;
		this.matKhau = matKhau;
	}




	public NhanVien() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getTenNhanVien() {
		return tenNhanVien;
	}

	public void setTenNhanVien(String tenNhanVien) {
		this.tenNhanVien = tenNhanVien;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}






	public int getTrangThai() {
		return trangThai;
	}




	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
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

	public LocalDate getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(LocalDate ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public int getLoaiNhanVien() {
		return loaiNhanVien;
	}

	public void setLoaiNhanVien(int loaiNhanVien) {
		this.loaiNhanVien = loaiNhanVien;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	@Override
	public String toString() {
		return "NhanVien [maNhanVien=" + maNhanVien + ", tenNhanVien=" + tenNhanVien + ", ngaySinh=" + ngaySinh
				+ ", trangThai=" + trangThai + ", gioiTinh=" + gioiTinh + ", email=" + email + ", soDienThoai="
				+ soDienThoai + ", ngayVaoLam=" + ngayVaoLam + ", loaiNhanVien=" + loaiNhanVien + ", matKhau=" + matKhau
				+ "]";
	}

}
