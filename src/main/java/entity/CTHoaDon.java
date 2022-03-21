package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(CTHoaDon_PK.class)
public class CTHoaDon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "maHoaDon")
	private HoaDon hoaDon;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "maSanPham")
	private SanPham sanPham;
	private int soLuong;
	private double donGia;
	
	public CTHoaDon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CTHoaDon(HoaDon hoaDon, SanPham sanPham) {
		super();
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
	}

	public CTHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong, double donGia) {
		super();
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public double tinhThanhTien() {
		return soLuong * donGia;
	}

	@Override
	public String toString() {
		return "CTHoaDon [hoaDon=" + hoaDon + ", sanPham=" + sanPham + ", soLuong=" + soLuong + ", donGia=" + donGia
				+ "]";
	}
	
}
