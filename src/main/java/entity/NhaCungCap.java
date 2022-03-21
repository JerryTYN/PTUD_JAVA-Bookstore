package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class NhaCungCap {
	@Id
	@GeneratedValue(generator = "generator_nhaCCId")
	@GenericGenerator(name = "generator_nhaCCId", strategy = "entity.id_generator.NhaCungCapStringGeneratorId")
	private String maNCC;

	@Column(columnDefinition = "nvarchar(255)")
	private String tenNCC;

	@Column(columnDefinition = "nvarchar(255)")
	private String diaChi;

	@Column(columnDefinition = "nvarchar(255)")
	private String soFax;
	
	public NhaCungCap() {
	}
	public NhaCungCap(String tenNCC, String diaChi, String soFax) {
		this.tenNCC = tenNCC;
		this.diaChi = diaChi;
		this.soFax = soFax;
	}
	public String getMaNCC() {
		return maNCC;
	}
	public void setMaNCC(String maNCC) {
		this.maNCC = maNCC;
	}
	public String getTenNCC() {
		return tenNCC;
	}
	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getSoFax() {
		return soFax;
	}
	public void setSoFax(String soFax) {
		this.soFax = soFax;
	}
	@Override
	public String toString() {
		return "NhaCungCap [maNCC=" + maNCC + ", tenNCC=" + tenNCC + ", diaChi=" + diaChi + ", soFax=" + soFax + "]";
	}
	
	
}
