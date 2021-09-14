package bis.stock.back.domain.stock.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import bis.stock.back.domain.stock.StockPrice;
import bis.stock.back.domain.user.UserRole;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {

	@JsonIgnore
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	@CsvBindByName(column = "한글 종목약명")
	private String name;
	
	@Column(unique = true)
	@CsvBindByName(column = "단축코드")
	private String code;
	
	@Column
	@CsvBindByName(column = "주식종류")
	private String category;

	// 상장 된 주식 수
	@Column
	@CsvBindByName(column = "상장주식수")
	private Long listedAmount;

	// 상장된 시장 구분 : KOSPI, KOSDAQ 등
	@Column
	@CsvBindByName(column = "시장구분")
	private String marketName;

	@Column
	@CsvBindByName(column = "상장일")
	@CsvDate("yyyy/MM/dd")
	private LocalDate listedDate;

	// 액면가
	@Column
	@CsvBindByName(column = "액면가")
	private String faceValue;

	@JsonManagedReference
	@OneToOne
	@JoinColumn(name = "stock_price_id")
	private StockPrice price;

    @Builder
    public Stock(String name, String code, String category) {
    	this.name = name;
    	this.code = code;
    	this.category = category;
    }

	public void setStockPrice(StockPrice stockPrice) {
		this.price = stockPrice;
	}
}
