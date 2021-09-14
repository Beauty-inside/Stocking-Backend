package bis.stock.back.domain.holdingStock.dto;

import bis.stock.back.domain.holdingStock.BuyOrSell;
import bis.stock.back.domain.holdingStock.HoldingStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class StockResponseDto {

    private Long id;
    private Long userId;
    private String stockCode;
    private int amount;
    private LocalDateTime date;
    private int price;
    private BuyOrSell buyOrSell;

    public StockResponseDto(HoldingStock stock) {

        this.id = stock.getId();
        this.userId = stock.getUser().getId();
        this.stockCode = stock.getStockCode();
        this.amount = stock.getAmount();
        this.date = stock.getDate();
        this.price = stock.getPrice();
        this.buyOrSell = stock.getBuyOrSell();
    }
}
