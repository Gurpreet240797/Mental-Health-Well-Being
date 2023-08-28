package com.soen6841.backend.controller;

import com.soen6841.backend.dto.account.LoginDto;
import com.soen6841.backend.dto.account.RegisterDto;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.mapper.AccountMapper;
import com.soen6841.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private AccountService accountService;

    @CrossOrigin(origins = {"http://localhost:3000", "https://blog.csdn.net"})
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            Account account = accountService.login(loginDto.getEmail(), loginDto.getPassword(), request);
            response.setStatus(HttpStatus.OK.value());
            //response.setHeader("Access-Control-Allow-Credentials", "true");
            //response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            Map<String, Object> map = new HashMap<>();
            map.put("userInfo", AccountMapper.INSTANCE.map(account));
            map.put("sessionId", Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getSessionId());
            return map;
        }catch (Exception e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return null;
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            accountService.logout(request);
            response.setStatus(HttpStatus.OK.value());
        } catch (ServletException e) {
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterDto registerDto, HttpServletResponse response) throws IOException {
        try{
            accountService.createAccount(registerDto);
            response.setStatus(HttpStatus.CREATED.value());
        }catch (Exception e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

}
