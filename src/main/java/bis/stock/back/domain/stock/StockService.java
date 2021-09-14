package bis.stock.back.domain.stock;

import java.io.*;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import bis.stock.back.global.exception.NotFoundException;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import bis.stock.back.domain.stock.dto.Stock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class StockService {

   private final StockRepository stockRepository;

   private final StockPriceRepository stockPriceRepository;

   @PersistenceContext
   private EntityManager em;

   @PostConstruct // init
   public void stockListUpdate() throws UnsupportedEncodingException {

      // resource에서 input.csv(다운받아서 넣어놈)을 읽어와서 Stock객체의 배열로 변환함
      InputStream is = getClass().getResourceAsStream("/input.csv");
      List<Stock> stockList = new CsvToBeanBuilder<Stock>(new InputStreamReader(is, "EUC-KR"))
              .withType(Stock.class)
              .build()
              .parse();

      if(stockRepository.count() != stockList.size()) {
         // 한꺼번에 저장
         stockRepository.saveAll(stockList);
      }

   }

   public List<Stock> totalList() {

      return em.createQuery("select s from Stock s", Stock.class)
            .getResultList();
   }

   public String findcode(String itemname) {

      return em.createQuery("select s from Stock s where s.name = :name", Stock.class)
            .setParameter("name", itemname)
            .getSingleResult().getCode();
   }

   public String findName(String itemCode) {
      return stockRepository.findByCode(itemCode)
              .orElseThrow(() -> new NotFoundException("해당 이름의 주식이 존재하지 않습니다.")).getName();
   }

   public String detail(String itemcode, String itemname) {

      String line ="";
      String result = "";
      ObjectMapper objectMapper = new ObjectMapper();
      JSONObject res = new JSONObject();

      try {
         String urlstr = "https://api.finance.naver.com/service/itemSummary.nhn?itemcode=" + itemcode;
         URL url = new URL(urlstr);

         BufferedReader br;
         br = new BufferedReader(new InputStreamReader(url.openStream()));
         while((line = br.readLine())!=null) {
            result = result.concat(line);
         }

         JSONParser parser = new JSONParser();
         JSONObject obj = (JSONObject) parser.parse(result);
         System.out.println(obj.toString());
         String amount = obj.get("amount").toString();//거래량
         String high = obj.get("high").toString(); //고점
         String rate = obj.get("rate").toString(); //등락비율
         String low = obj.get("low").toString(); //저점
         String now = obj.get("now").toString(); //현재가
         String diff = obj.get("diff").toString();//등락폭

         res.put("itemcode", itemcode);
         res.put("itemname", itemname);
         res.put("now", now);
         res.put("diff", diff);
         res.put("high", high);
         res.put("low", low);
         res.put("rate", rate);
         res.put("amount", amount);

         br.close();
      }catch (Exception e) {

      }

      return res.toJSONString();

   }

   public Stock getStock(String itemCode) {

      Stock stock = stockRepository.findByCode(itemCode).orElseThrow(() -> new NotFoundException("종목 코드 오류"));

      stockPriceChecker(stock);

      return stock;
   }

   @Transactional
   public void stockPriceChecker(Stock stock) {

      // 만약 주식 가격 조회 값이 없으면 조회하기
      if(stock.getPrice() == null) {
         StockPrice stockPrice = updateStockPrice(stock.getCode());
         saveStockPrice(stockPrice);
         stock.setStockPrice(stockPrice);
      } else {
         LocalDate nowDate = LocalDate.now();
         DayOfWeek nowDay = nowDate.getDayOfWeek();
         int nowHour = LocalDateTime.now().getHour();

         // 주말일 경우
         if(nowDay == DayOfWeek.SATURDAY || nowDay == DayOfWeek.SUNDAY) {
            LocalDate recentMarketDate = LocalDate.now().minusDays((nowDay == DayOfWeek.SATURDAY ? 1 : 2));
            LocalDateTime recentMarketTime = LocalDateTime.of(
                    recentMarketDate.getYear(),
                    recentMarketDate.getMonth(),
                    recentMarketDate.getDayOfMonth(),
                    16,
                    00
            );
            if(!stock.getPrice().getRecentUpdateTime().isAfter(recentMarketTime)) {
               stock.getPrice().setStockPrice(updateStockPrice(stock.getCode()));
            }
         } else { // 평일일 경우
            // 장이 열려있는 경우
            if(nowHour > 9 && nowHour < 16) {
               // 가장 최근에 조회한 주식 가격이 5초 이전이면 다시 받아오기.
               Duration dur = Duration.between(stock.getPrice().getRecentUpdateTime(), LocalDateTime.now());

               if(dur.getSeconds() > 5) {
                  stock.getPrice().setStockPrice(updateStockPrice(stock.getCode()));
               }
            } else { // 장이 마감된 경우
               LocalDate recentMarketDate = LocalDate.now();
               if(nowHour < 9) {
                  if(nowDay == DayOfWeek.MONDAY) {
                     recentMarketDate = LocalDate.now().minusDays(3);
                  } else {
                     recentMarketDate = LocalDate.now().minusDays(1);
                  }
               }
               LocalDateTime recentMarketTime = LocalDateTime.of(
                       recentMarketDate.getYear(),
                       recentMarketDate.getMonth(),
                       recentMarketDate.getDayOfMonth(),
                       16,
                       00
               );
               if(!stock.getPrice().getRecentUpdateTime().isAfter(recentMarketTime)) {
                  stock.getPrice().setStockPrice(updateStockPrice(stock.getCode()));
               }
            }
         }
      }
   }

   public void saveStockPrice(StockPrice stockPrice) {
      stockPriceRepository.save(stockPrice);
   }

   public StockPrice updateStockPrice(String itemCode) {

      String line ="";
      String result = "";
      StockPrice stockPrice = null;
      Stock stock = stockRepository.findByCode(itemCode).orElseThrow(() -> new NotFoundException("종목 코드 오류"));

      try {
         String urlstr = "https://api.finance.naver.com/service/itemSummary.nhn?itemcode=" + itemCode;
         URL url = new URL(urlstr);

         BufferedReader br;
         br = new BufferedReader(new InputStreamReader(url.openStream()));
         while((line = br.readLine())!=null) {
            result = result.concat(line);
         }

         JSONParser parser = new JSONParser();
         JSONObject obj = (JSONObject) parser.parse(result);
         String amount = obj.get("amount").toString();//거래량
         String high = obj.get("high").toString(); //고점
         String rate = obj.get("rate").toString(); //등락비율
         String low = obj.get("low").toString(); //저점
         String now = obj.get("now").toString(); //현재가
         String diff = obj.get("diff").toString();//등락폭

         stockPrice = StockPrice.builder()
                 .stock(stock)
                 .amount(Integer.parseInt(amount))
                 .rate(Float.parseFloat(rate))
                 .now(Integer.parseInt(now))
                 .high(Integer.parseInt(high))
                 .low(Integer.parseInt(low))
                 .diff(Integer.parseInt(diff))
                 .recentUpdateTime(LocalDateTime.now())
                 .build();

         br.close();
      }catch (Exception e) {
         System.out.println("종목 불러오기 오류");
      }

      return stockPrice;
   }

   public List<Stock> list() {

      // TODO : 추후에 Pageable 구현
      return stockRepository.findAll();
   }
}