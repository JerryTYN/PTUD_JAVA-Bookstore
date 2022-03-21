package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entity.NhaCungCap;

public class NhaCungCapDAO {
	private SessionFactory sessionFactory;

	public NhaCungCapDAO() {
		this.sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
	}
	
	public List<NhaCungCap> get20NCC() {
		
		List<NhaCungCap> list = new ArrayList<NhaCungCap>();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			list = session.createNativeQuery("select  * from NhaCungCap order by maNCC desc", NhaCungCap.class).getResultList();
			
			transaction.commit();
			
		} catch (Exception e) {
			
			transaction.rollback();
			e.printStackTrace();
		}
		
		return list;
	}
	
	public boolean addNhaCungCap(NhaCungCap nhaCC) {
		boolean flag = false;
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			session.save(nhaCC);
			transaction.commit();
			
			flag = true;
			
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean deleteNhaCungCap(String maNhaCC) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			String sqlQuery = "delete from NhaCungCap where maNCC = :x";
			session.createNativeQuery(sqlQuery).setParameter("x", maNhaCC).executeUpdate();

			transaction.commit();
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean updateNhaCungCap(NhaCungCap nhaCC) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			
			session.update(nhaCC);

			transaction.commit();
			
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public List<NhaCungCap> findNhaCungCap(String tenncc, String diachi, String sofax){
		List<NhaCungCap> list = new ArrayList<NhaCungCap>();
		
		if(tenncc.trim().equals(""))
			tenncc = null;
		if(diachi.trim().equals(""))
			diachi = null;
		if(sofax.trim().equals(""))
			sofax = null;
		
		String query = "select * from NhaCungCap ";
		
		if(tenncc != null || diachi != null || sofax !=null)
			query += "where ";
		
		if(tenncc != null)
			query += "tenNCC like :ten ";
		if(diachi != null) {
			if(tenncc == null)
				query += "diaChi like :dc ";
			else
				query += "and diaChi like :dc ";
		}
		if(sofax != null) {
			if(tenncc == null && diachi == null)
				query += "soFax like :fax ";
			else
				query += "and soFax like :fax ";
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			
			NativeQuery<NhaCungCap> nativeQuery = session.createNativeQuery(query, NhaCungCap.class);
			if(tenncc != null)
				nativeQuery.setParameter("ten", "%" + tenncc + "%");
			if(diachi != null)
				nativeQuery.setParameter("dc", "%" + diachi + "%");
			if(sofax != null)
				nativeQuery.setParameter("fax", "%" + sofax + "%");
			
			list = nativeQuery.getResultList();
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<NhaCungCap> findNhaCungCapTheoTen(String tenncc, String diachi, String sofax){
		List<NhaCungCap> list = new ArrayList<NhaCungCap>();
		
		if(tenncc.trim().equals(""))
			tenncc = null;
		if(diachi.trim().equals(""))
			diachi = null;
		if(sofax.trim().equals(""))
			sofax = null;
		
		String query = "select * from NhaCungCap ";
		
		if(tenncc != null || diachi != null || sofax !=null)
			query += "where ";
		
		if(tenncc != null)
			query += "tenNCC like :ten ";
		if(diachi != null) {
			if(tenncc == null)
				query += "diaChi like :dc ";
			else
				query += "and diaChi like :dc ";
		}
		if(sofax != null) {
			if(tenncc == null && diachi == null)
				query += "soFax like :fax ";
			else
				query += "and soFax like :fax ";
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			
			NativeQuery<NhaCungCap> nativeQuery = session.createNativeQuery(query, NhaCungCap.class);
			if(tenncc != null)
				nativeQuery.setParameter("ten",  tenncc);
			if(diachi != null)
				nativeQuery.setParameter("dc", diachi );
			if(sofax != null)
				nativeQuery.setParameter("fax", sofax);
			
			list = nativeQuery.getResultList();
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return list;
	}
	
}
