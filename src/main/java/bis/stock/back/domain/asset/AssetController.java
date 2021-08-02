package bis.stock.back.domain.asset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bis.stock.back.domain.asset.dto.Deposit;
import bis.stock.back.domain.asset.dto.DepositResponseDto;
import bis.stock.back.domain.asset.dto.DepositSaveDto;
import bis.stock.back.domain.asset.dto.DepositUpdateDto;
import bis.stock.back.domain.asset.dto.Withdraw;
import bis.stock.back.domain.asset.dto.WithdrawResponseDto;
import bis.stock.back.domain.asset.dto.WithdrawSaveDto;
import bis.stock.back.domain.asset.dto.WithdrawUpdateDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/asset")
@RestController
public class AssetController {

	private final DepositRepository depositRepository;
	private final DepositService depositService;

	private final WithdrawRepository withrdrawRepository;
	private final WithdrawService withdrawService;

	@PostMapping("/deposit/post") // 입금내역 추가
	public Long insert(@RequestBody DepositSaveDto requestDto) {
		return depositService.save(requestDto);
	}
	@GetMapping("/deposit/{id}") // 유저별 수입내역 전체 조회
	public List<Deposit> findAllById(@PathVariable String id) {
		return depositRepository.findAllById(id);
	}

	@PutMapping("/deposit/{id}")  // 입금내역 변경 - 코드 잘못된듯
	public Long update(@PathVariable Long id, @RequestBody DepositUpdateDto requestDto) {
		return depositService.update(id, requestDto);
	}
//	@GetMapping("/deposit/{id}")
//	public DepositResponseDto findById(@PathVariable Long id) {
//		return depositService.findById(id);
//	}


	@PostMapping("/withdraw/post") // 지출내역
	public Long insert(@RequestBody WithdrawSaveDto requestDto) {
		return withdrawService.save(requestDto);
	}
	@GetMapping("/withdraw/{id}") // 유저별 지출내역 전체 조회
	public List<Withdraw> withdrawfindAllById(@PathVariable String id) {
		return withrdrawRepository.findAllById(id);
	}

	@PutMapping("/withdraw/{id}") // 지출내역 변경 - 소스 잘못됨
	public Long update(@PathVariable Long id, @RequestBody WithdrawUpdateDto requestDto) {
		return withdrawService.update(id, requestDto);
	}
//	@GetMapping("/withdraw/{id}")
//	public WithdrawResponseDto WithdrawfindById(@PathVariable Long id) {
//		return withdrawService.findById(id);
//	}
}
