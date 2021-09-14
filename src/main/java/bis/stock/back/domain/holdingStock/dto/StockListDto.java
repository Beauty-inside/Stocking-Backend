package bis.stock.back.domain.holdingStock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StockListDto {

    private String code;
    private String stockName;
    private int amount;
    private int avgPrice;

    public void update(int amount, int price) {
        this.amount += amount;
        this.avgPrice += (price * amount);
    }

    public void calAvgPrice() {
        this.avgPrice = (this.avgPrice / this.amount);
    }
}
