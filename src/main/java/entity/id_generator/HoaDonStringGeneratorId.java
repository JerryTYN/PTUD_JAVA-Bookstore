package entity.id_generator;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.spi.QueryImplementor;

public class HoaDonStringGeneratorId implements IdentifierGenerator {

		private String prefix = "HD";

		@Override
		public Serializable generate(SharedSessionContractImplementor session, Object object) {
			QueryImplementor<String> query = session.createQuery("SELECT hd.maHoaDon FROM HoaDon hd", String.class);
			try (Stream<String> stream = query.stream()) {
				Long max = stream.map(t -> t.replace(prefix, ""))
						.mapToLong(Long::parseLong)
						.max()
						.orElse(0L);
				return prefix + String.format("%03d", max + 1);
			}
		}
	}