package bis.stock.back.domain.holdingStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingStockRepository extends JpaRepository<HoldingStock, Long> {
}
