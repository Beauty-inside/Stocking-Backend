package bis.stock.back.domain.asset;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import bis.stock.back.domain.asset.dto.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Long>{
	List<Deposit> findAllById(String user_id);
}
