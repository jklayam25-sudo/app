package lumi.insert.app.controller.transactionpayment;

import java.util.UUID;
  
import lumi.insert.app.controller.BaseControllerTest;
import lumi.insert.app.dto.response.TransactionPaymentResponse; 
 
public abstract class BaseTransactionPaymentControllerTest extends BaseControllerTest{
  
    TransactionPaymentResponse transactionPaymentResponse = new TransactionPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "CLIENT", "LUMI", false);

    TransactionPaymentResponse transactionRefundResponse = new TransactionPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "LUMI", "CLIENT", true);

}
