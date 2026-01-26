package in.FullStack.moneymanger.controller;

import in.FullStack.moneymanger.dto.ExpenseDTO;
import in.FullStack.moneymanger.dto.FilterDTO;
import in.FullStack.moneymanger.dto.IncomeDTO;
import in.FullStack.moneymanger.service.ExpenseService;
import in.FullStack.moneymanger.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Filter")
public class FilterController {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter){
    LocalDate startDate = filter.getStartDate()!=null?filter.getStartDate():LocalDate.now();
    LocalDate endDate = filter.getEndDate()!=null?filter.getEndDate():LocalDate.now();
    String keyword = filter.getKeyword()!=null?filter.getKeyword():"";
    String sortField = filter.getSortField() !=null?filter.getSortField():"date";
    Sort.Direction direction = "desc".equalsIgnoreCase(sortField)?Sort.Direction.DESC:Sort.Direction.ASC;
    Sort sort = Sort.by(direction,sortField);
    if("income".equalsIgnoreCase(filter.getType())){
        List<IncomeDTO> incomes = incomeService.filterIncomes(startDate,endDate,keyword,sort);
        return ResponseEntity.status(HttpStatus.OK).body(incomes);
      }
    else if("expense".equalsIgnoreCase(filter.getType())){
        List<ExpenseDTO> expense =  expenseService.filterExpense(startDate,endDate,keyword,sort);
        return ResponseEntity.status(HttpStatus.OK).body(expense);
      }
    else{

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("income or expense not found");
      }

    }
}
