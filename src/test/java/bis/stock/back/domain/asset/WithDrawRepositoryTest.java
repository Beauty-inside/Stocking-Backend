package bis.stock.back.domain.asset;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import bis.stock.back.domain.asset.dto.Withdraw;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WithDrawRepositoryTest {

	@Autowired
	private WithdrawRepository withdrawRepository;
	Date now = new Date();
	Withdraw withdraw = Withdraw.builder()
							.user_id("wooseong")
							.contents("Salary")
							.amount(2500000L)
							.date(now)
							.build();

	Withdraw saveWithdraw;

    @BeforeEach
    void 테스트지출기록생성() {
    	saveWithdraw = withdrawRepository.save(withdraw);
    }

    @Test
    void 지출기록저장() {
        assertThat(saveWithdraw).isEqualTo(withdraw);
    }

//    @Test
//    void 지출기록삭제() {
//    	withdrawRepository.delete(withdraw);
//        assertThat(withdrawRepository.findById("wooseong").orElse(null)).isNull();
//    }
}
