package lumi.insert.app.controller.transactionitem;

import java.util.UUID;
  
import lumi.insert.app.controller.BaseControllerTest;
import lumi.insert.app.dto.response.TransactionItemResponse; 
 
public abstract class BaseTransactionItemControllerTest extends BaseControllerTest{
  
    public TransactionItemResponse transactionItemResponse = new TransactionItemResponse(UUID.randomUUID(), UUID.randomUUID(), 1L, null, null, 10L, 5L, null, null);

}