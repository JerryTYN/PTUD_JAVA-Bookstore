package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entity.CTHoaDon;
import entity.HoaDon;

public class HoaDonDAO {
	private SessionFactory sessionFactory;
	

	public HoaDonDAO() {
		sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
	}

	public boolean lapHoaDon(HoaDon hoaDon) {
		boolean result = false;

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			session.save(hoaDon);

			tran.commit();
			result = true;

		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}

	public double tinhTongTien(HoaDon hoaDon) {
		double result = 0;

		for(CTHoaDon item : new CTHoaDonDAO().getDsCTHD(hoaDon))
			result += item.tinhThanhTien();

		return result;
	}

	public List<HoaDon> lay20HoaDonGanDay() {
		List<HoaDon> result = new ArrayList<HoaDon>();

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			result = session.createNativeQuery("select  * from HoaDon order by maHoaDon desc", HoaDon.class).getResultList();

			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}

	public List<HoaDon> timHoaDon(String ma, String tenKH, String tenNV, LocalDate ngayLap) {
		List<HoaDon> result = new ArrayList<HoaDon>();

		String ngay = null;

		if(ma.trim().equals(""))
			ma = null;
		if(tenKH.trim().equals(""))
			tenKH = null;
		if(tenNV.trim().equals(""))
			tenNV = null;
		if(ngayLap != null)
			ngay = ngayLap.toString();


		String query = "select * from HoaDon hd left outer join KhachHang kh on hd.maKhachHang = kh.maKhachHang left outer join NhanVien nv on hd.maNhanVien = nv.maNhanVien ";

		if(ma != null || tenKH != null || tenNV !=null || ngay !=null)
			query += "where ";

		if(ma != null)
			query += "maHoaDon like :ma ";
		if(tenKH != null) {
			if(ma == null)
				query += "tenKhachHang like :tenKH ";
			else
				query += "and tenKhachHang like :tenKH ";
		}
		if(tenNV != null) {
			if(ma == null && tenKH == null)
				query += "tenNhanVien like :tenNV ";
			else
				query += "and tenNhanVien like :tenNV ";
		}
		if(ngay != null) {
			if(ma == null && tenKH == null && tenNV == null)
				query += "ngayLap = :ngay ";
			else
				query += "and ngayLap = :ngay ";
		}

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			NativeQuery<HoaDon> nativeQuery = session.createNativeQuery(query, HoaDon.class);
			if(ma != null)
				nativeQuery.setParameter("ma", "%" + ma + "%");
			if(tenKH != null)
				nativeQuery.setParameter("tenKH", "%" + tenKH + "%");
			if(tenNV != null)
				nativeQuery.setParameter("tenNV", "%" + tenNV + "%");
			if(ngay != null)
				nativeQuery.setParameter("ngay", ngay);

			result = nativeQuery.getResultList();

			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}

	public Object[] thongKeHoaDon(LocalDate fromDate, LocalDate toDate, String maNhanVien) {
		Object[] result = new Object[2];

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();
		

		try {
			List<HoaDon> list;
			double sum;
			if(maNhanVien.trim().equals("")) {
				list = session.createNativeQuery("select * from HoaDon where ngayLap >= :x and ngayLap <= :y order by ngayLap", HoaDon.class)
						.setParameter("x", fromDate.toString())
						.setParameter("y", toDate.toString())
						.getResultList();

				 sum = (double) session.createNativeQuery("select SUM(donGia * soLuong) from CTHoaDon " + 
						"where maHoaDon in (select maHoaDon from HoaDon where ngayLap >= :x and ngayLap <= :y)")
						.setParameter("x", fromDate.toString())
						.setParameter("y", toDate.toString())
						.getSingleResult();
			}else {
				list = session.createNativeQuery("select * from HoaDon where ngayLap >= :x and ngayLap <= :y and maNhanVien like '"+ maNhanVien+"' order by ngayLap", HoaDon.class)
						.setParameter("x", fromDate.toString())
						.setParameter("y", toDate.toString())
						.getResultList();

				sum = (double) session.createNativeQuery("select SUM(donGia * soLuong) from CTHoaDon " + 
						"where maHoaDon in (select maHoaDon from HoaDon where ngayLap >= :x and ngayLap <= :y and maNhanVien like '"+ maNhanVien+"')")
						.setParameter("x", fromDate.toString())
						.setParameter("y", toDate.toString())
						.getSingleResult();
			}
			

			result[0] = list;
			result[1] = sum;

			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		}

		return result;
	}


	public double getHoaDonTheo1Ngay(LocalDate ngay)  {
		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();
		double tam = 0;
		try {
	
			double sum = (double) session.createNativeQuery("select SUM(donGia * soLuong) from CTHoaDon join HoaDon ON CTHoaDon.maHoaDon = HoaDon.maHoaDon where HoaDon.ngayLap = :x")
					.setParameter("x", ngay.toString())
					.getSingleResult();
			tam=sum;
			tran.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
		}
		
		return tam;
	}

}
