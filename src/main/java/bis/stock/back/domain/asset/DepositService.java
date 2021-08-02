package bis.stock.back.domain.asset;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import bis.stock.back.domain.asset.dto.Deposit;
import bis.stock.back.domain.asset.dto.DepositResponseDto;
import bis.stock.back.domain.asset.dto.DepositSaveDto;
import bis.stock.back.domain.asset.dto.DepositUpdateDto;
import bis.stock.back.domain.asset.dto.WithdrawResponseDto;
import bis.stock.back.domain.asset.dto.WithdrawSaveDto;
import bis.stock.back.domain.asset.dto.WithdrawUpdateDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepositService {
	private final DepositRepository depositRepository;

	@Transactional
	public Long save(DepositSaveDto requestDto) {
		return depositRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional
	public Long update(Long id, DepositUpdateDto requestDto) {
		Deposit deposit = depositRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("\"해당 id에 대한 정보가 존재하지 않습니다. id="+ id));

		deposit.update(requestDto.getUser_id(), requestDto.getContents(), requestDto.getAmount(), requestDto.getDate());
		return id;
	}

	public DepositResponseDto findById(Long id) {
		Deposit entity = depositRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id에 대한 정보가 존재하지 않습니다. id="+ id));
		return new DepositResponseDto(entity);
	}
//
//	public List<Deposit> findAllById(String id) {
//		List<Deposit> entityList = depositRepository.findAllById(id);
//		return new DepositResponseDto(entityList);
//	}
}
