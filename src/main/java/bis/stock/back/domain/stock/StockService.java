package bis.stock.back.domain.stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StockService { //

	public String stock(String itemcode) {

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

//			String pbr = obj.get("pbr").toString();//주가순자산비율
//			String risefall = obj.get("risefall").toString();
//			String marketSum = obj.get("marketSum").toString();
//			String eps = obj.get("eps").toString();//주당 순 이익
//			String per = obj.get("per").toString();//주가 수익 비율
//			String quant = obj.get("quant").toString();//거래량


			String itemname = "삼성전자";//db로 받아와야 해서 아직은 임의로 넣음

			res.put("itemcode", itemcode);
			res.put("itemname", itemname);
			res.put("now", now);
			res.put("diff", diff);
			res.put("high", high);
			res.put("low", low);
			res.put("rate", rate);
			res.put("amount", amount);
			System.out.println(res.toString());

			br.close();
		}catch (Exception e) {

		}


		return res.toJSONString();

	}

public String fullStockList() {
		//임시 목업 코드 다른서버에서 구현할 기능
		String line ="";
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject res = new JSONObject();

		try {
//			String urlstr = "우리 전체 리스트 실시간으로 뿌려주는 API";

//			URL url = new URL(urlstr);
//			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//			while((line = br.readLine())!=null) {
//				result = result.concat(line);
//			}
			result="{resultCode:200, data:{loadTime:210623093800, stockList:[{itemcode:005930, itemname:삼성전자, now:80000, diff:0, high:80100, low:79900, rate:0.0, amount:0},{itemcode:035420, itemname:NAVER, now:391000, diff:0, high:399000, low:390000, rate:1.8, amount:31231}]}}";
			JSONParser parser = new JSONParser();
			res = (JSONObject) parser.parse(result);
//			br.close();
		}catch (Exception e) {
			res.clear();
			res.put("resultCode","500");
			res.put("errorMessage","서버 연결이 끊어졌습니다.");
		}
		return res.toJSONString();
	}

}
