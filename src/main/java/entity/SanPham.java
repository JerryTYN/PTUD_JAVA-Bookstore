package entity;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class SanPham {

	@Id
	@GeneratedValue(generator = "generator_sanPhamId")
	@GenericGenerator(name = "generator_sanPhamId", strategy = "entity.id_generator.SanPhamStringGeneratorId")
	private String maSanPham;
	
	@Column(columnDefinition = "ntext")
	private String TenSanPham;
	
	@ManyToOne
	@JoinColumn(name = "maNCC")
	private NhaCungCap nhaCungCap;

	@Column(columnDefinition = "nvarchar(255)")
	private String theLoai;

	@Column(columnDefinition = "nvarchar(255)")
	private String phanLoai;
	
	private int soLuong;
	@Column(columnDefinition = "nvarchar(255)")
    private String tacGia;
	private double giaThanh;
	private Blob hinhAnh;

	public SanPham() {
		super();
		
	}
	
	
	public SanPham(String tenSanPham, NhaCungCap nhaCungCap, String theLoai, String phanLoai, int soLuong,
			String tacGia, double giaThanh, Blob hinhAnh) {
		super();
		TenSanPham = tenSanPham;
		this.nhaCungCap = nhaCungCap;
		this.theLoai = theLoai;
		this.phanLoai = phanLoai;
		this.soLuong = soLuong;
		this.tacGia = tacGia;
		this.giaThanh = giaThanh;
		this.hinhAnh = hinhAnh;
		
	}

	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getTenSanPham() {
		return TenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		TenSanPham = tenSanPham;
	}
	public NhaCungCap getNhaCungCap() {
		return nhaCungCap;
	}
	public void setNhaCungCap(NhaCungCap nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
	}
	public String getTheLoai() {
		return theLoai;
	}
	public void setTheLoai(String theLoai) {
		this.theLoai = theLoai;
	}
	public String getPhanLoai() {
		return phanLoai;
	}
	public void setPhanLoai(String phanLoai) {
		this.phanLoai = phanLoai;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public String getTacGia() {
		return tacGia;
	}
	public void setTacGia(String tacGia) {
		this.tacGia = tacGia;
	}
	public double getGiaThanh() {
		return giaThanh;
	}
	public void setGiaThanh(double giaThanh) {
		this.giaThanh = giaThanh;
	}
	public Blob getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(Blob hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	
public String TrangThai() {
		
		String trangThai = null ;
		if(soLuong ==0 ) {
			trangThai = "Hết hàng";
		}
		else if(soLuong >0 && soLuong <=10){
				trangThai="Sắp hết";
			}
		else
			trangThai="Chưa bán";
		return trangThai;
		
	}
	
	@Override
	public String toString() {
		return "SanPham [maSanPham=" + maSanPham + ", TenSanPham=" + TenSanPham + ", theLoai=" + theLoai + ", phanLoai="
				+ phanLoai + ", soLuong=" + soLuong + ", tacGia=" + tacGia + ", giaThanh=" + giaThanh + ", hinhAnh="
				+ hinhAnh + "]";
	}
	
	
	
	
}
