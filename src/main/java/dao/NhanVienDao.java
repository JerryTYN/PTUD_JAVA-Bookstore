package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entity.NhanVien;


public class NhanVienDao {
	private SessionFactory sessionFactory;

	public NhanVienDao() {
		sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
	}
	public NhanVien kiemTraDangNhap(String userName, String password) {
		NhanVien nv = null;
		
		Session session = DatabaseConnection.getInstance().getSessionFactory().getCurrentSession();
		
		Transaction tran = session.beginTransaction();
		
		try {
			nv = session.find(NhanVien.class, userName);
			
			if(!nv.getMatKhau().equals(password))
				nv = null;
			
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}
		
		return nv;
	}
	public NhanVien getNhanVienTheoTen(String tennv) {
		NhanVien nv = new NhanVien();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			String sql = "select * from NhanVien where tenNhanVien like N'"+tennv+"'";
//			nv = session.find(NhanVien.class, tennv);
			nv = session.createNativeQuery(sql,NhanVien.class).getSingleResult();
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return nv;
	}
	public List<NhanVien> lay20NhanVienGanDay() {
		List<NhanVien> list = new ArrayList<NhanVien>();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			list = session.createNativeQuery("select top 20 * from NhanVien order by maNhanVien desc", NhanVien.class).getResultList();
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return list;
	}
	
	public NhanVien getNhanVien(String manv) {
		NhanVien nv = new NhanVien();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			nv = session.find(NhanVien.class, manv);
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return nv;
	}
	
	public boolean addNhanVien(NhanVien nv) {
		
		boolean flag = false;
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			session.save(nv);
			transaction.commit();
			
			flag = true;
			
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}

	public boolean deleteNhanVien(String maNV) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			String sqlQuery = "delete from NhanVien where maNhanVien = :x";
			session.createNativeQuery(sqlQuery).setParameter("x", maNV).executeUpdate();

			transaction.commit();
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean updateNhanVien(NhanVien nv) {
		
		boolean flag = false;
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			session.update(nv);

			transaction.commit();
			
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	
	
	public List<NhanVien> findNhanVien(String tennv, int trangThai, String email, String sdt, LocalDate ngayvaolam, int loainv){
		
		List<NhanVien> list = new ArrayList<NhanVien>();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		if(tennv.trim().equals(""))
			tennv = null;
		if(email.trim().equals(""))
			email = null;
		if(sdt.trim().equals(""))
			sdt = null;
		
		String query = "select * from NhanVien ";
		
		if(tennv != null  || email !=null || sdt !=null || ngayvaolam != null  || loainv != 2 || trangThai != 3)
			query += "where ";
		
		if(tennv != null)
			query += "tenNhanVien like :ten ";
		
		if(email != null) {
			if(tennv == null )
				query += "email like :em ";
			else
				query += "and email like :em ";
		}
		if(sdt != null) {
			if(tennv == null && email == null)
				query += "soDienThoai like :sdt ";
			else
				query += "and soDienThoai like :sdt ";
		}
		if(ngayvaolam != null) {
			if(tennv == null && email == null && sdt == null)
				query += "ngayVaoLam = :ngayvaolam ";
			else
				query += "and ngayVaoLam = :ngayvaolam ";
		}
		if(loainv != 2) {
			if(tennv == null && email == null && sdt == null && ngayvaolam == null)
				query += "loaiNhanVien = :loainhanvien ";
			else
				query += "and loaiNhanVien = :loainhanvien ";
		}
		
		if(trangThai != 3) {
			if(tennv == null && loainv == 2 && email == null && sdt == null && ngayvaolam == null)
				query += "trangThai = :trangthai ";
			else
				query += "and trangThai = :trangthai ";
		}
		
		try {
			
			NativeQuery<NhanVien> nativeQuery = session.createNativeQuery(query, NhanVien.class);
			if(tennv != null)
				nativeQuery.setParameter("ten", "%" + tennv + "%");
			
			if(email != null)
				nativeQuery.setParameter("em", "%" + email + "%");
			if(sdt != null)
				nativeQuery.setParameter("sdt","%"+ sdt +"%");
			if(ngayvaolam != null)
				nativeQuery.setParameter("ngayvaolam", ngayvaolam);
			if(loainv != 2)
				nativeQuery.setParameter("loainhanvien", loainv);
			if(trangThai != 3)
				nativeQuery.setParameter("trangthai", trangThai);
			
			list = nativeQuery.getResultList();
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return list;
	}
	
public List<NhanVien> findNhanVienTrung(String email, String sdt){
		
		List<NhanVien> list = new ArrayList<NhanVien>();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();

		if(email.trim().equals(""))
			email = null;
		if(sdt.trim().equals(""))
			sdt = null;
		
		String query = "select * from NhanVien ";
		
		if(email !=null || sdt !=null )
			query += "where ";
		
		
		if(email != null) {

				query += "email like :em ";

		}
		if(sdt != null) {
			if(email == null)
				query += "soDienThoai like :sdt ";
			else
				query += "and soDienThoai like :sdt ";
		}
		
		try {
			
			NativeQuery<NhanVien> nativeQuery = session.createNativeQuery(query, NhanVien.class);

			
			if(email != null)
				nativeQuery.setParameter("em", email );
			if(sdt != null)
				nativeQuery.setParameter("sdt","%"+ sdt +"%");
			
			list = nativeQuery.getResultList();
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return list;
	}
}
