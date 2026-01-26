package in.FullStack.moneymanger.controller;


import in.FullStack.moneymanger.dto.IncomeDTO;
import in.FullStack.moneymanger.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO income = incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(income);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getIncome() {
        List<IncomeDTO> expenses = incomeService.getCurrentMonthIncomesForCurrentCustomer();
        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteIncome(@PathVariable Long id){
        incomeService.deleteIncome(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
