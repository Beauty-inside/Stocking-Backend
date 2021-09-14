package bis.stock.back.domain.holdingStock;

import bis.stock.back.domain.holdingStock.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/{userId}/stock")
public class HoldingStockController {

    private final HoldingStockService holdingStockService;

    // 주식 구매 입력
    @PostMapping("buy")
    public ResponseEntity<StockDto> buyStock(@PathVariable Long userId,
                                             @RequestBody StockDto stockDto) {

        return ResponseEntity.ok(holdingStockService.stockTrade(userId, stockDto, true));
    }

    // 주식 판매 입력
    @PostMapping("sell")
    public ResponseEntity<StockDto> sellStock(@PathVariable Long userId,
                                             @RequestBody StockDto stockDto) {

        return ResponseEntity.ok(holdingStockService.stockTrade(userId, stockDto, false));
    }

    // 현재 보유 주식
    @GetMapping("stockList")
    public ResponseEntity<List> stockList(@PathVariable Long userId) {

        return ResponseEntity.ok(holdingStockService.stockList(userId));
    }


    // 전체 주식 거래 내역
    @GetMapping("tradeList")
    public ResponseEntity<List> stockTradeList(@PathVariable Long userId) {

        return ResponseEntity.ok(holdingStockService.tradeList(userId));
    }
}
