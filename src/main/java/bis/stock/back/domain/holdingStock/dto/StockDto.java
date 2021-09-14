package bis.stock.back.domain.holdingStock.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {

    private String code;
    private LocalDateTime date;
    private int price;
    private int amount;
}
