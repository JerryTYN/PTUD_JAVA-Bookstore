package entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CTHoaDon_PK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hoaDon;
	private String sanPham;

	public CTHoaDon_PK() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CTHoaDon_PK(String hoaDon, String sanPham) {
		super();
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
	}

	public String getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(String hoaDon) {
		this.hoaDon = hoaDon;
	}

	public String getsanPham() {
		return sanPham;
	}

	public void setSanPham(String sanPham) {
		this.sanPham = sanPham;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hoaDon == null) ? 0 : hoaDon.hashCode());
		result = prime * result + ((sanPham == null) ? 0 : sanPham.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CTHoaDon_PK other = (CTHoaDon_PK) obj;
		if (hoaDon == null) {
			if (other.hoaDon != null)
				return false;
		} else if (!hoaDon.equals(other.hoaDon))
			return false;
		if (sanPham == null) {
			if (other.sanPham != null)
				return false;
		} else if (!sanPham.equals(other.sanPham))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CTHoaDon_PK [hoaDon=" + hoaDon + ", sanPham=" + sanPham + "]";
	}
	
}
