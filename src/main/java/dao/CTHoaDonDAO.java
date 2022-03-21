package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.CTHoaDon;
import entity.HoaDon;

public class CTHoaDonDAO {
	private SessionFactory sessionFactory;

	public CTHoaDonDAO() {
		sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
	}

	public boolean themCTHoaDon(CTHoaDon ctHoaDon) {
		boolean result = false;

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			session.save(ctHoaDon);

			tran.commit();
			result = true;

		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}
	
	public List<CTHoaDon> getDsCTHD(HoaDon hoaDon) {
		List<CTHoaDon> result = new ArrayList<CTHoaDon>();

		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();

		try {
			result = session.createNativeQuery("select * from CTHoaDon where maHoaDon = :x", CTHoaDon.class)
				.setParameter("x", hoaDon.getMaHoaDon())
				.getResultList();
			
			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
	}
	

	
	
}
