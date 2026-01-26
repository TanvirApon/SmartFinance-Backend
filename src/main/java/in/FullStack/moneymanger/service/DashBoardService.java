package in.FullStack.moneymanger.service;

import in.FullStack.moneymanger.dto.ExpenseDTO;
import in.FullStack.moneymanger.dto.IncomeDTO;
import in.FullStack.moneymanger.dto.RecentTransactionDTO;
import in.FullStack.moneymanger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;


@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final ProfileService profileService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public Map<String, Object> getDashBoardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncome = incomeService.getLatest5IncomeForCurrentUser();
        List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpenseForCurrentUser();
        List<RecentTransactionDTO> recentTransactions = concat(latestIncome.stream().map(income ->
                        RecentTransactionDTO.builder()
                                .id(income.getId())
                                .profileId(profile.getId())
                                .name(income.getName())
                                .icon(income.getIcon())
                                .amount(income.getAmount())
                                .date(income.getDate())
                                .createdAt(income.getCreatedAt())
                                .updatedAt(income.getUpdatedAt())
                                .type("income")
                                .build()),
                latestExpense.stream().map(expense ->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .name(expense.getName())
                                .icon(expense.getIcon())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()))
                .sorted((RecentTransactionDTO a, RecentTransactionDTO b) -> {

                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());

        returnValue.put("Total Balance", incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()));
        returnValue.put("Total Income", incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("Total Expense", expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("Total 5 Expenses", latestExpense);
        returnValue.put("Total 5 Incomes", latestIncome);
        returnValue.put("Recent Transactions", recentTransactions);

        return returnValue;
    }

}