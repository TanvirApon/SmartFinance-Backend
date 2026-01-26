package in.FullStack.moneymanger.service;

import in.FullStack.moneymanger.dto.ExpenseDTO;
import in.FullStack.moneymanger.entity.ProfileEntity;
import in.FullStack.moneymanger.repository.ExpenseRepository;
import in.FullStack.moneymanger.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontEndUrl;

    @Scheduled(cron="0 0 21 * * *",zone="Asia/Dhaka")
    public void SendDailyIncomeExpenseReminder(){
        log.info("Job started: Send Daily income expense reminder");
       List<ProfileEntity> profiles = profileRepository.findAll();
       for (ProfileEntity profile : profiles){
            String body = "Hi " + profile.getFullName() + ",<br><br>" +
                    "This is a friendly reminder to add your income and expense for today in Money Manager.<br><br>" +

                    "<a href='" + frontEndUrl + "' " +
                    "style='display:inline-block; padding:10px 20px; background:#4f46e5; " +
                    "color:#ffffff; text-decoration:none; border-radius:5px;'>" +
                    "Add Income & Expense" +
                    "</a>" +
                    "<br><br>Best regards,<br>" +
                    "Money Manager Team";
            emailService.sendEmail(profile.getEmail(), "Daily Reminder: Add your Incomes and Expenses",body);
       }
        log.info("Job Finished: Sent Daily income expense reminder");
    }

    @Scheduled(cron="0 0 23 * * *",zone="Asia/Dhaka")
    public void senDailyExpenseSummary(){
        log.info("Job started: Send Daily income expense reminder");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles){
           List<ExpenseDTO> todayExpense=expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now(ZoneId.of("Asia/Dhaka")));
           if(!todayExpense.isEmpty()){
            StringBuilder table = new StringBuilder();
               table.append("<table style='width:100%; border-collapse:collapse; font-family:Arial;'>")
                       .append("<tr style='background:#f2f2f2;'>")
                       .append("<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Expense</th>")
                       .append("<th style='border:1px solid #ddd; padding:8px; text-align:right;'>Amount</th>")
                       .append("</tr>");
               int i = 1;
               for(ExpenseDTO expense : todayExpense){
                 table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(i++).append("</td>");
                 table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(expense.getName()).append("</td>");
                 table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(expense.getAmount()).append("</td>");
                 table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(expense.getCategoryId()!=null ? expense.getCategoryId():"N/A").append("</td>");
                 table.append("</tr>");
               }
               table.append("</table>");
               String body =
                       "Hi " + profile.getFullName() + ",<br><br>" +

                               "Here is your <b>daily expense summary</b> for today:<br><br>" +

                               table.toString() +

                               "<br><br>" +
                               "<a href='https://your-frontend-url.com' " +
                               "style='display:inline-block;padding:10px 18px;" +
                               "background:#4f46e5;color:#ffffff;text-decoration:none;" +
                               "border-radius:6px;font-weight:bold;'>" +
                               "Open Money Manager" +
                               "</a>" +

                               "<br><br>" +
                               "Thanks & regards,<br>" +
                               "<b>Money Manager Team</b>";

               emailService.sendEmail(profile.getEmail(), "Daily Expense Summary",body);
           }

            log.info("Job Ended: Sent Daily income expense reminder");
        }
    }
}
