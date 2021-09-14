package bis.stock.back.domain.HoldingStock;

import bis.stock.back.domain.holdingStock.*;
import bis.stock.back.domain.holdingStock.dto.StockDto;
import bis.stock.back.domain.user.User;
import bis.stock.back.domain.user.UserRepository;
import bis.stock.back.domain.user.UserRole;
import bis.stock.back.global.config.security.JwtTokenProvider;
import bis.stock.back.global.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HoldingStockTest {

    protected MockMvc mockMvc;

    @Autowired
    HoldingStockController holdingStockController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    private Long userId;
    private Cookie accessCookie;

    @BeforeEach
    public void createController() {

        mockMvc = MockMvcBuilders.standaloneSetup(holdingStockController).build();

        // 매수, 매도 할 유저 생성
        userRepository.save(User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .nickname("test")
                .cash(0L)
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build());

        User user = userRepository.findByEmail("test@test.com").orElse(null);
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles());
        accessCookie = new Cookie("accessToken", accessToken);
        userId = user.getId();
    }

    public StockDto setStockDto(int amount) {

        return StockDto.builder()
                .code("005930")
                .amount(amount)
                .price(80000)
                .date(LocalDateTime.of(2021, 8, 23, 12, 0))
                .build();
    }

    @Test
    public void 주식매수() throws Exception {

        String content = objectMapper.writeValueAsString(setStockDto(10));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/" + userId + "/stock/buy")
                .content(content)
                .cookie(accessCookie)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("code", "005930").exists())
                .andExpect(jsonPath("amount", 10).exists())
                .andDo(print());

        // 유저 존재하지 않을 때
        requestBuilder = MockMvcRequestBuilders
                .post("/user/" + -1 + "/stock/buy")
                .content(content)
                .cookie(accessCookie)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void 주식매도() throws Exception {

        String content = objectMapper.writeValueAsString(setStockDto(8));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/" + userId + "/stock/sell")
                .content(content)
                .cookie(accessCookie)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void 거래내역조회() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + userId + "/stock/tradeList")
                .characterEncoding("utf-8")
                .cookie(accessCookie);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void 보유주식조회() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + userId + "/stock/stockList")
                .characterEncoding("utf-8")
                .cookie(accessCookie);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
