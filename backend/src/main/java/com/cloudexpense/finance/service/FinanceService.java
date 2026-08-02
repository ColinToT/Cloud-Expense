package com.cloudexpense.finance.service;

import com.cloudexpense.finance.dto.FinanceExpenseDetailResponse;
import com.cloudexpense.finance.dto.FinanceExpenseResponse;

import java.util.List;

/**
 * ClassName: FinanceService
 * Package: com.cloudexpense.finance.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:09
 * @Version: v1.0
 */
public interface FinanceService {

    List<FinanceExpenseResponse> getExpenses();

    FinanceExpenseDetailResponse getExpenseDetail(Long id);

}
