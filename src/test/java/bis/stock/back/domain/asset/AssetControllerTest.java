package bis.stock.back.domain.asset;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import bis.stock.back.domain.asset.dto.DepositSaveDto;
import bis.stock.back.domain.asset.dto.WithdrawSaveDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AssetControllerTest {

	@Autowired
	public AssetController assetController;

	protected MockMvc mockMvc;

	@Autowired
    ObjectMapper objectMapper;

	@BeforeEach
	public void createController() {
		mockMvc = MockMvcBuilders.standaloneSetup(assetController).build();
	}

	@Test
    void 입금추가() throws Exception {
		java.util.Date date = new Date();

        String content = objectMapper.writeValueAsString(
        		DepositSaveDto.builder()
                        .user_id("wooseong")
                        .contents("입금")
                        .amount((long) 30000)
                        .date(date)
                        .build()
        );

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/asset/deposit/post")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(print());
    }

	@Test
    void 입금조회() throws Exception {
		Long id = 3L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/asset/deposit/"+id);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(print());
    }

	@Test
    void 지출추가() throws Exception {
		java.util.Date date = new Date();

        String content = objectMapper.writeValueAsString(
        		WithdrawSaveDto.builder()
                        .user_id("wooseong")
                        .contents("출금")
                        .amount((long) 30000)
                        .date(date)
                        .build()
        );

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/asset/withdraw/post")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(print());
    }

	@Test
    void 지출조회() throws Exception {
		Long id = 3L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/asset/withdraw/"+id);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(print());
    }

}
