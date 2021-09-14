package bis.stock.back.domain.holdingStock;

import bis.stock.back.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class HoldingStock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // TODO : 추후 주식 단방향 1:1 매핑 구현
    // Stock과 Holding Stock이 다대다 관계이기 때문에 굳이 연관관계 설정하지 않고 단순 id만 저장하기로 함.
    // 성능상의 문제가 있다고 하며, 다대다 관계가 나왔다면 설계가 잘못되었다고 함.
    private String stockCode;

    private String stockName;

    @Column
    private int amount;

    // 구매 및 판매 날짜
    @Column
    private LocalDateTime date;

    // 구매 및 판매 가격
    @Column
    private int price;

    @Column
    @Enumerated(EnumType.STRING)
    private BuyOrSell buyOrSell;
}
