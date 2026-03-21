package lumi.insert.app.controller.supplypayment;

import java.util.UUID; 

import lumi.insert.app.controller.BaseControllerTest;
import lumi.insert.app.dto.response.SupplyPaymentResponse; 
 
public abstract class BaseSupplyPaymentControllerTest extends BaseControllerTest{
 
    SupplyPaymentResponse supplyPaymentResponse = new SupplyPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "CLIENT", "LUMI", false);

    SupplyPaymentResponse supplyRefundResponse = new SupplyPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "LUMI", "CLIENT", true);

}
