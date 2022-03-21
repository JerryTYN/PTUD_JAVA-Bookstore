package dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseConnection {
	private SessionFactory sessionFactory;
	private static DatabaseConnection databaseConnection = null;
	
	private DatabaseConnection() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure("config/hibernate.cfg.xml")
				.build();
		
		Metadata metadata = new MetadataSources(registry)
			.addAnnotatedClass(entity.NhaCungCap.class)
			.addAnnotatedClass(entity.KhachHang.class)
			.addAnnotatedClass(entity.NhanVien.class)
			.addAnnotatedClass(entity.SanPham.class)
			.addAnnotatedClass(entity.HoaDon.class)
			.addAnnotatedClass(entity.CTHoaDon.class)
			.addAnnotatedClass(entity.CTHoaDon_PK.class)

			.getMetadataBuilder()
			.build();
		
		this.sessionFactory = metadata.getSessionFactoryBuilder().build();
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public synchronized static DatabaseConnection getInstance() {
		if(databaseConnection == null)
			databaseConnection = new DatabaseConnection();
		
		return databaseConnection;
	}
}
