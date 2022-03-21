package dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entity.NhaCungCap;
import entity.SanPham;



public class SanPhamDao {

	private SessionFactory sessionFactory;

	public SanPhamDao() {
		super();
		this.sessionFactory= DatabaseConnection.getInstance().getSessionFactory();
	}
	
	public List<SanPham> lay20SanPhamGanDay(){
		List<SanPham> list = new ArrayList<SanPham>();
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {

			list = session.createNativeQuery("select  * from SanPham order by maSanPham desc", SanPham.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return list;
		
	}
	public List<String> getDSPhanLoai() {
		List<String> list = new ArrayList<String>();

		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {

			List<?> resultList = session.createNativeQuery("select phanLoai from SanPham where phanLoai not like '' group by phanLoai").getResultList();
			for(Object item : resultList) {
				list.add((String)item);
			}

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return list;
	}
	
	
	public boolean xoaSanPham(String maSanPham) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			String sqlQuery = "delete from SanPham where maSanPham = :x";
			session.createNativeQuery(sqlQuery).setParameter("x", maSanPham).executeUpdate();

			transaction.commit();
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;	
	}
	public boolean themSanPham(SanPham sanpham) {
		
		boolean flag = false;

		Session session = sessionFactory.getCurrentSession();

		Transaction transaction = session.beginTransaction();

		try {
			session.save(sanpham);
			transaction.commit();

			flag = true;


		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return flag;
	}
	public boolean capNhatSanPham(SanPham sanPham) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();

		Transaction transaction = session.beginTransaction();

		try {
			session.update(sanPham);

			transaction.commit();

			flag = true;

		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return flag;
	}
		
	
	public List<SanPham> timSanPham(String tenSanPham, String nhaCungCap, String tacGia, String theLoai){
		List<SanPham> result = new ArrayList<SanPham>();
		if(tenSanPham.trim().equals(""))
				tenSanPham= null;
		if(nhaCungCap.trim().equals(""))
			nhaCungCap= null;
		if(tacGia.trim().equals(""))
			tacGia= null;
		if(theLoai.trim().equals(""))
			theLoai= null;
		
		
		String queryNCC= "select * from SanPham INNER JOIN NhaCungCap ON SanPham.maNCC = NhaCungCap.maNCC ";
		
		if(tenSanPham != null || nhaCungCap != null || tacGia!= null || theLoai != null ) {
			
			queryNCC+="where ";
			
			if(tenSanPham != null)
				queryNCC += "tenSanPham like :ten ";
			if(nhaCungCap != null) {
				if(tenSanPham == null) 
				     queryNCC+= "tenNCC like :ncc ";
				else
				     queryNCC+= "and tenNCC like :ncc ";
			}
			if(tacGia != null) {
				if(tenSanPham == null && nhaCungCap == null)
					queryNCC += "tacGia like :tg ";
				else
					queryNCC += "and tacGia like :tg ";
			}
			if(theLoai != null) {
				if(tenSanPham == null && nhaCungCap == null && tacGia == null)
					queryNCC += "theLoai like :tl ";
				else
					queryNCC +="and theLoai like :tl ";	
			}
			
		}
		
		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();	
		
		try {
			NativeQuery<SanPham> nativeQuery = session.createNativeQuery(queryNCC, SanPham.class);
			if(tenSanPham != null)
				nativeQuery.setParameter("ten", "%" + tenSanPham + "%");
			if(nhaCungCap != null)
				nativeQuery.setParameter("ncc", "%" + nhaCungCap + "%");
			if(tacGia != null)
				nativeQuery.setParameter("tg", "%" + tacGia + "%");
			if(theLoai != null)
				nativeQuery.setParameter("tl", "%" + theLoai + "%");
			
			result = nativeQuery.getResultList();
			tran.commit();
					
		}catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
		
	}
	
	public Object[] thongKeSanPham(boolean isHetHang, boolean isSapHet, boolean isChuaBanLanNao) {
		Object[] result = new Object[4];

		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			int soHetHang = 0;
			int soSapHet = 0;
			int soChuaBanLanNao = 0;

			if(isHetHang)
				soHetHang = (int)session.createNativeQuery("select COUNT(maSanPham) from SanPham where maSanPham in (select maSanPham from SanPham where soLuong = 0)").getSingleResult();
                
			if(isSapHet)
				soSapHet = (int)session.createNativeQuery("select COUNT(maSanPham) from SanPham where maSanPham in (select maSanPham from SanPham where soLuong > 0 and soLuong <= 10)").getSingleResult();
                 
			if(isChuaBanLanNao)
				soChuaBanLanNao = (int)session.createNativeQuery("select COUNT(maSanPham) from SanPham where maSanPham not in (select maSanPham from CTHoaDon)").getSingleResult();

			String query = "select * from SanPham where ";
			if(isHetHang)
				query += "maSanPham in (select maSanPham from SanPham where soLuong = 0)";

			if(isSapHet) {
				if(!isHetHang)
					query += "maSanPham in (select maSanPham from SanPham where soLuong > 0 and soLuong <= 10)";
			}
			if(isChuaBanLanNao) {
				if(isHetHang || isSapHet)
					query += "or maSanPham not in (select maSanPham from CTHoaDon)";
				else
					query += "maSanPham not in (select maSanPham from CTHoaDon)";
				
			}
			
			List<SanPham> resultList = session.createNativeQuery(query, SanPham.class).getResultList();

			result[0] = soHetHang;
			result[1] = soSapHet;
			result[2] = soChuaBanLanNao;
			result[3] = resultList;

			transaction.commit();

		} catch (Exception e) {
			transaction.rollback();
		}

		return result;
	}

	public boolean nhapSanPhamTuExcel(File file) {
		boolean result = false;

		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();

		try {
			FileInputStream excelFile = new FileInputStream(file);

			XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> iterator = sheet.iterator();

			NhaCungCap nhaCungCap = new NhaCungCap();

			Row tenNCCRow = iterator.next();
			nhaCungCap.setTenNCC(tenNCCRow.getCell(1).getStringCellValue());

			Row dcNCCRow = iterator.next();
			nhaCungCap.setDiaChi(dcNCCRow.getCell(1).getStringCellValue());

			Row faxNCCRow = iterator.next();
			nhaCungCap.setSoFax(faxNCCRow.getCell(1).getStringCellValue());

			List<NhaCungCap> resultList = session.createNativeQuery("select * from NhaCungCap where tenNCC = :ten and diaChi = :dc and soFax = :fax", NhaCungCap.class)
					.setParameter("ten", nhaCungCap.getTenNCC())
					.setParameter("dc", nhaCungCap.getDiaChi())
					.setParameter("fax", nhaCungCap.getSoFax())
					.getResultList();

			if(resultList.size() == 0)
				session.save(nhaCungCap);
			else
				nhaCungCap = resultList.get(0);

			iterator.next();
			iterator.next();

			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				SanPham sp = new SanPham();

				sp.setTenSanPham(currentRow.getCell(2).getStringCellValue());
				sp.setTacGia(currentRow.getCell(3).getStringCellValue());
				sp.setGiaThanh(currentRow.getCell(4).getNumericCellValue());
				sp.setPhanLoai(currentRow.getCell(5).getStringCellValue());
				sp.setSoLuong((int) currentRow.getCell(6).getNumericCellValue());
				sp.setTheLoai(currentRow.getCell(7).getStringCellValue());
				sp.setNhaCungCap(nhaCungCap);
				sp.setHinhAnh(null);
				sp.setNhaCungCap(nhaCungCap);


				session.save(sp);
			}

			transaction.commit();
			result = true;
			workbook.close();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return result;
	}
	
	
}
