package bis.stock.back.domain.asset.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Entity(name="deposit")
@Getter
public class Deposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String user_id;

	@Column
	private String contents;

	@Column
	private Long amount;

	@Column
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private java.util.Date date;

	protected Deposit() {
	}

    @Builder
    public Deposit(String user_id, String contents, Long amount, Date date) {
    	this.user_id = user_id;
    	this.contents = contents;
    	this.amount = amount;
    	this.date = date;
    }


}
