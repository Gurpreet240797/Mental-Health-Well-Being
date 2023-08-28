package com.soen6841.backend.controller;

import com.soen6841.backend.dto.account.AccountDto;
import com.soen6841.backend.dto.account.EditDto;
import com.soen6841.backend.dto.account.RegisterDto;
import com.soen6841.backend.dto.UserData;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.QuestionnaireResult;
import com.soen6841.backend.mapper.AccountMapper;
import com.soen6841.backend.repository.questionnaire.QuestionnaireResultRepository;
import com.soen6841.backend.service.AccountService;
import com.soen6841.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ManagerController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private QuestionnaireResultRepository questionnaireResultRepository;

    @PostMapping("/adduser")
    public void addUser(@RequestBody RegisterDto registerDto, HttpServletResponse response) throws IOException {
        try{
            accountService.createAccount(registerDto);
            response.setStatus(HttpStatus.CREATED.value());
        }catch (Exception e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam("email_address") String emailAddress, HttpServletResponse response) throws IOException {
        try{
            Account user = accountService.getAccountByEmail(emailAddress);
            accountService.deleteAccount(emailAddress);
            response.setStatus(HttpStatus.OK.value());

            if (user != null) {
                String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\n"
                        + "Your account has been removed by the manager. Please register a new account.\n\n"
                        + "Thank you,\n"
                        + "The MoodSpace Team";
                emailService.sendEmail(user.getEmail(), "Your Account has been removed", body);
            }
        }
        catch (Exception e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

    @GetMapping("/fetch_all_users")
    public List<UserData> fetchUserData(HttpServletResponse httpServletResponse) throws IOException {
        try{
            List<UserData> userDataList = accountService.findAllAccounts();
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return userDataList;
        }
        catch (Exception e){
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
        return null;
    }

    @PostMapping("/edit")
    public AccountDto edit(@RequestBody EditDto editDto, HttpServletResponse response) throws IOException{
        try{
            Account account = accountService.edit(editDto);
            response.setStatus(HttpStatus.OK.value());
            return AccountMapper.INSTANCE.map(account);
        }catch (IllegalArgumentException e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return null;
        }
        catch (Exception e){
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return null;
        }
    }

    @GetMapping("/statistics")
    public Map<String, Integer> statistics(){
        Map<String, Integer> map = new HashMap<>();
        List<QuestionnaireResult> questionnaireResults = questionnaireResultRepository.findAll();
        Date current = new Date();
        int daily = (int)questionnaireResults.stream().filter(x -> {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            return fmt.format(x.getSubmissionDate()).equals(fmt.format(current));
        }).count();
        map.put("daily", daily);
        int week = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        if(c.get(Calendar.DAY_OF_WEEK)==1) week = 7;
        else week = c.get(Calendar.DAY_OF_WEEK)-1;
        c.add(Calendar.DAY_OF_YEAR, -week+1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date start = c.getTime();
        int weekly = (int)questionnaireResults.stream().filter(x -> x.getSubmissionDate().before(current) && x.getSubmissionDate().after(start)).count();
        map.put("weekly", weekly);
        int monthly = (int)questionnaireResults.stream().filter(x -> {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
            return fmt.format(x.getSubmissionDate()).equals(fmt.format(current));
        }).count();
        map.put("monthly", monthly);
        return map;
    }
}
