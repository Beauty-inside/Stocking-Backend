package bis.stock.back.domain.stock;

import bis.stock.back.domain.stock.dto.Stock;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockPrice {

    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne(mappedBy = "price")
    private Stock stock;

    private float rate;

    private int diff;

    private int amount;

    private int now;

    private int high;

    private int low;

    private LocalDateTime recentUpdateTime;

    public void setStockPrice(StockPrice stockPrice) {
        this.rate = stockPrice.rate;
        this.diff = stockPrice.diff;
        this.amount = stockPrice.amount;
        this.now = stockPrice.now;
        this.high = stockPrice.high;
        this.low = stockPrice.low;
        this.recentUpdateTime = LocalDateTime.now();
    }
}
