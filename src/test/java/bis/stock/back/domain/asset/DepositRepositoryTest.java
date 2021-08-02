package bis.stock.back.domain.asset;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import bis.stock.back.domain.asset.dto.Deposit;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepositRepositoryTest {

	@Autowired
	private DepositRepository depositRepository;
	Date now = new Date();
	Deposit deposit = Deposit.builder()
							.user_id("wooseong")
							.contents("Salary")
							.amount(2500000L)
							.date(now)
							.build();

	Deposit saveDeposit;

    @BeforeEach
    void 테스트수입기록생성() {
    	saveDeposit = depositRepository.save(deposit);
    }

    @Test
    void 수입저장() {
        assertThat(saveDeposit).isEqualTo(deposit);
    }

//    @Test
//    void 수입삭제() {
//    	depositRepository.delete(deposit);
//        assertThat(depositRepository.findById("wooseong").orElse(null)).isNull();
//    }
}
