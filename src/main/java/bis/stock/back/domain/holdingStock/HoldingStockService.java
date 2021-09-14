package bis.stock.back.domain.holdingStock;

import bis.stock.back.domain.holdingStock.dto.StockDto;
import bis.stock.back.domain.holdingStock.dto.StockListDto;
import bis.stock.back.domain.holdingStock.dto.StockResponseDto;
import bis.stock.back.domain.stock.StockRepository;
import bis.stock.back.domain.user.User;
import bis.stock.back.domain.user.UserRepository;
import bis.stock.back.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldingStockService {

    private final HoldingStockRepository holdingStockRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    public StockDto stockTrade(Long userId, StockDto stockDto, boolean isBuy) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        if(!isBuy) {
            HashMap<String, StockListDto> stockMap = getStockHashMap(userId);
            if(!stockMap.containsKey(stockDto.getCode())
                    || stockMap.get(stockDto.getCode()).getAmount() < stockDto.getAmount()) {
                throw new NotFoundException("보유한 수량보다 매도하려는 수량이 더 많습니다.");
            }
        }
        BuyOrSell buyOrSell = isBuy ? BuyOrSell.BUY : BuyOrSell.SELL;

        holdingStockRepository.save(HoldingStock.builder()
                .stockName(stockRepository
                        .findByCode(stockDto.getCode())
                        .orElseThrow(() -> new NotFoundException("주식 코드 오류")).getName())
                .buyOrSell(buyOrSell)
                .amount(stockDto.getAmount())
                .price(stockDto.getPrice())
                .date(stockDto.getDate())
                .stockCode(stockDto.getCode())
                .user(user)
                .build());

        return stockDto;
    }

    public List<StockResponseDto> tradeList(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<StockResponseDto> stockList = new ArrayList<>();
        List<HoldingStock> stocks = user.getHoldingStocks();

        if(stocks == null) {
            return stockList;
        }

        for(int i = stocks.size() - 1; i >= 0; i--) {
            stockList.add(new StockResponseDto(stocks.get(i)));
        }

        return stockList;
    }

    public HashMap<String, StockListDto> getStockHashMap(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<HoldingStock> stocks = user.getHoldingStocks();
        HashMap<String, StockListDto> stockMap = new HashMap<>();

        if(stocks == null) {
            return stockMap;
        }

        for(HoldingStock entity : stocks) {
            String code = entity.getStockCode();
            StockListDto value;
            if(entity.getBuyOrSell() == BuyOrSell.valueOf("BUY")) {
                if(stockMap.containsKey(code)) {
                    stockMap.get(code).update(entity.getAmount(), entity.getPrice());
                } else {
                    value = StockListDto.builder()
                            .code(code)
                            .stockName(entity.getStockName())
                            .amount(entity.getAmount())
                            .avgPrice(entity.getPrice() * entity.getAmount())
                            .build();
                    stockMap.put(code, value);
                }
            } else {
                if(stockMap.containsKey(code)) {
                    stockMap.get(code).update(entity.getAmount() * -1, entity.getPrice());
                } else {
                    value = StockListDto.builder()
                            .code(code)
                            .stockName(entity.getStockName())
                            .amount(entity.getAmount() * -1)
                            .avgPrice(entity.getPrice() * entity.getAmount() * -1)
                            .build();
                    stockMap.put(code, value);
                }
            }
        }

        return stockMap;
    }

    public List<StockListDto> stockList(Long userId) {

        List<StockListDto> stockList = new ArrayList<>();
        HashMap<String, StockListDto> stockMap = getStockHashMap(userId);

        if(stockMap == null) {
            return stockList;
        }

        stockMap.forEach((key, value) -> {
            if(value.getAmount() > 0) {
                value.calAvgPrice();
                stockList.add(value);
            }
        });

        return stockList;
    }
}
