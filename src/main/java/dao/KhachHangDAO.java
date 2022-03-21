package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entity.KhachHang;


public class KhachHangDAO {
	private SessionFactory sessionFactory;

	public KhachHangDAO() {
		super();
		this.sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
	}

	public List<KhachHang> layDSKhachHangGanDay(){
		List<KhachHang> result = new ArrayList<KhachHang>();

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			result = session.createNativeQuery("select  * from KhachHang order by maKhachHang desc", KhachHang.class).getResultList();

			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}
	public boolean addKhachHang(KhachHang kh) {
		boolean flag = false;
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			session.save(kh);
			transaction.commit();
			
			flag = true;
			
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean xoaKhachHang(String maKH) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			String sqlQuery = "delete from KhachHang where maKhachHang = :x";
			session.createNativeQuery(sqlQuery).setParameter("x", maKH).executeUpdate();

			transaction.commit();
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	public boolean capNhatKhachHang(KhachHang khachHang) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			session.update(khachHang);

			transaction.commit();
			
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	public List<KhachHang> timKhachHang(String tenKH, String diaChi, String email, String sdt) {
		List<KhachHang> result = new ArrayList<KhachHang>();
		
		if(tenKH.trim().equals(""))
			tenKH = null;
		if(diaChi.trim().equals(""))
			diaChi = null;
		if(email.trim().equals(""))
			email = null;
		if(sdt.trim().equals(""))
			sdt = null;
		
		
		String query = "select * from KhachHang ";
		
		if(tenKH != null || diaChi != null || email !=null || sdt !=null)
			query += "where ";
		
		if(tenKH != null)
			query += "tenKhachHang like :ten ";
		if(diaChi != null) {
			if(tenKH == null)
				query += "diaChi like :dc ";
			else
				query += "and diaChi like :dc ";
		}
		if(email != null) {
			if(tenKH == null && diaChi == null)
				query += "email like :email ";
			else
				query += "and email like :email ";
		}
		if(sdt != null) {
			if(tenKH == null && diaChi == null && email == null)
				query += "soDienThoai like :sdt ";
			else
				query += "and soDienThoai like :sdt ";
		}
		
		
		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			NativeQuery<KhachHang> nativeQuery = session.createNativeQuery(query, KhachHang.class);
			if(tenKH != null)
				nativeQuery.setParameter("ten", "%" + tenKH + "%");
			if(diaChi != null)
				nativeQuery.setParameter("dc", "%" + diaChi + "%");
			if(email != null)
				nativeQuery.setParameter("email", "%" + email + "%");
			if(sdt != null)
				nativeQuery.setParameter("sdt", "%" + sdt + "%");
			
			result = nativeQuery.getResultList();
					
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}
	
	public List<KhachHang> timKhachHang1(String tenKH, String diaChi, String email, String sdt) {
		List<KhachHang> result = new ArrayList<KhachHang>();
		
		if(tenKH.trim().equals(""))
			tenKH = null;
		if(diaChi.trim().equals(""))
			diaChi = null;
		if(email.trim().equals(""))
			email = null;
		if(sdt.trim().equals(""))
			sdt = null;
		
		
		String query = "select * from KhachHang ";
		
		if(tenKH != null || diaChi != null || email !=null || sdt !=null)
			query += "where ";
		
		if(tenKH != null)
			query += "tenKhachHang like :ten ";
		if(diaChi != null) {
			if(tenKH == null)
				query += "diaChi like :dc ";
			else
				query += "and diaChi like :dc ";
		}
		if(email != null) {
			if(tenKH == null && diaChi == null)
				query += "email like :email ";
			else
				query += "and email like :email ";
		}
		if(sdt != null) {
			if(tenKH == null && diaChi == null && email == null)
				query += "soDienThoai like :sdt ";
			else
				query += "and soDienThoai like :sdt ";
		}
		
		
		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			NativeQuery<KhachHang> nativeQuery = session.createNativeQuery(query, KhachHang.class);
			if(tenKH != null)
				nativeQuery.setParameter("ten", "%" + tenKH + "%");
			if(diaChi != null)
				nativeQuery.setParameter("dc", "%" + diaChi + "%");
			if(email != null)
				nativeQuery.setParameter("email",  email );
			if(sdt != null)
				nativeQuery.setParameter("sdt", "%" + sdt + "%");
			
			result = nativeQuery.getResultList();
					
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}
	
	
}
