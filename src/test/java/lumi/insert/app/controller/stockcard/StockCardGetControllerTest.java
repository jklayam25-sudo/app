package lumi.insert.app.controller.stockcard;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.StockCardGetByFilter;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.exception.NotFoundEntityException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

public class StockCardGetControllerTest extends BaseStockCardControllerTest{
    
    @Test
    void getStockCardAPI_foundEntity_returnOkAndDTO() throws Exception{
        when(stockCardService.getStockCard(stockCardResponse.id())).thenReturn(stockCardResponse);

        mockMvc.perform(
            get("/api/stockcards/" + stockCardResponse.id())
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.productId").value(stockCardResponse.productId()))
        .andExpect(jsonPath("$.data.id").value(stockCardResponse.id().toString()))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    void getStockCardAPI_notFoundEntity_returnNotFound() throws Exception{
        when(stockCardService.getStockCard(stockCardResponse.id())).thenThrow(new NotFoundEntityException("StockCard with ID " + stockCardResponse.id() + " was not found"));

        mockMvc.perform(
            get("/api/stockcards/" + stockCardResponse.id())
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isNotFound()) 
        .andExpect(jsonPath("$.errors").value("StockCard with ID " + stockCardResponse.id() + " was not found"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getStockCardsAPI_foundEntity_returnOkAndDTO() throws Exception{
        when(stockCardService.getStockCards(eq(stockCardResponse.id()), any(PaginationRequest.class))).thenReturn(new SliceImpl<>(List.of(stockCardResponse)));

        mockMvc.perform(
            get("/api/stockcards?lastId=" + stockCardResponse.id() + "&size=20")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content.length()").value(1))
        .andExpect(jsonPath("$.data.content[0].id").value(stockCardResponse.id().toString()))
        .andExpect(jsonPath("$.errors").isEmpty());

        verify(stockCardService, times(1)).getStockCards(any(), argThat(arg -> arg.getSize() == 20 && arg.getSortBy().equals("createdAt")));
    }

    @Test
    void getStockCardsAPI_notFoundEntity_returnOkAndDTO() throws Exception{
        when(stockCardService.getStockCards(any(), any(PaginationRequest.class))).thenReturn(new SliceImpl<>(List.of()));

        mockMvc.perform(
            get("/api/stockcards")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content.length()").value(0))
        .andExpect(jsonPath("$.data.empty").value(true))
        .andExpect(jsonPath("$.errors").isEmpty());

        verify(stockCardService, times(1)).getStockCards(any(), argThat(arg -> arg.getSize() == 10));
    }

    @Test
    void searchStockCardsAPI_foundEntity_returnOkAndDTO() throws Exception{
        when(stockCardService.searchStockCards(any(StockCardGetByFilter.class))).thenReturn(new SliceImpl<>(List.of(stockCardResponse)));

        mockMvc.perform(
            get("/api/stockcards/search?lastId=" + stockCardResponse.id() + "&productId=1&type=CUSTOMER_IN&size=20")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content.length()").value(1))
        .andExpect(jsonPath("$.data.content[0].id").value(stockCardResponse.id().toString()))
        .andExpect(jsonPath("$.errors").isEmpty());

        verify(stockCardService, times(1)).searchStockCards(argThat(arg -> arg.getType() == StockMove.CUSTOMER_IN && arg.getProductId() == 1L));
    }
}
