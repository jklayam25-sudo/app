package lumi.insert.app.service;
 
import java.io.OutputStream;
import java.util.List;

import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.dto.response.TransactionResponse;

public interface XlsxService {
    void exportTransactions(List<TransactionResponse> data, OutputStream output);

    void exportSupplies(List<SupplyResponse> data, OutputStream output);
}
