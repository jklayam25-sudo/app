package lumi.insert.app.controller.supply; 

import com.github.f4b6a3.uuid.UuidCreator;
 
import lumi.insert.app.controller.BaseControllerTest;
import lumi.insert.app.dto.response.SupplyResponse; 
 
public abstract class BaseSupplyControllerTest extends BaseControllerTest{
     
    public SupplyResponse supplyResponse = new SupplyResponse(UuidCreator.getTimeOrderedEpochFast(), "INV", UuidCreator.getTimeOrderedEpochFast(), null, null, null, null, null, null, null, null, null, null, null, null, null, null);

}
