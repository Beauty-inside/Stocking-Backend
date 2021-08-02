package bis.stock.back.domain.asset;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import bis.stock.back.domain.asset.dto.Withdraw;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long>{
	List<Withdraw> findAllById(String user_id);
}
