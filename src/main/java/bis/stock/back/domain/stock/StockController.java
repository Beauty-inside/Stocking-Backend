package bis.stock.back.domain.stock;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bis.stock.back.domain.stock.dto.Stock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockController {

   private final StockService stockService;

   @ResponseBody
   @RequestMapping(value="/detail")               //defaultValue 지워도 됨 아직 편의상 넣어둠
   public String detail(@RequestParam(value="itemname", defaultValue="삼성전자") String itemname, RedirectAttributes redirect) {

      String itemcode = stockService.findcode(itemname);

      //실제로는 필요없음 -> 어차피 주식 코드 넘겨줄 때(totalList에서)다 변환해서 옴. 하지만 아직 받아서 오는게 아니므로 넣어뒀다.
      try {
         itemcode = String.format("%06d", Integer.parseInt(itemcode)).toString();
      } catch (Exception e) {

      }

      return stockService.detail(itemcode, itemname);
   }

   @GetMapping("/detail")
   public String getStockDetailByCode(@RequestParam(value="itemCode") String itemCode) {

      String itemName = stockService.findName(itemCode);
      return stockService.detail(itemCode, itemName);
   }

   // ...:8080/stock/005930 (Item code)
   @GetMapping("/{code}")
   public ResponseEntity<Stock> getStockByCode(@PathVariable String code) {

      return ResponseEntity.ok(stockService.getStock(code));
   }

   @ResponseBody
   @RequestMapping(value="/totalList")
   public Object totalList() {
      List<Stock> totalList = stockService.totalList();

      JSONObject res = new JSONObject();
      List<JSONObject> stockList = new ArrayList<JSONObject>();

      for(int i = 0; i < totalList.size(); i++) {
         JSONObject stock_info = new JSONObject();
         String itemcode = totalList.get(i).getCode();

         try {
            itemcode = String.format("%06d", Integer.parseInt(itemcode)).toString();
         } catch (Exception e) {
            //코드 뒤에 K , L 등 붙은 코드일 경우
         }
         stock_info.put("stockID", totalList.get(i).getId()); // 쓸지말지 결정 후 변경.
         stock_info.put("itemcode", itemcode);
         stock_info.put("itemname", totalList.get(i).getName());
         stock_info.put("category", totalList.get(i).getCategory());

         stockList.add(stock_info);

      }

      return res.put("stockList", stockList);
   }

   @GetMapping("/list")
   public ResponseEntity<List> list() {

      return ResponseEntity.ok(stockService.list());
   }

   @ResponseBody
   @RequestMapping(value="/myList")
   public String myList() {
      return "";
   }

}
