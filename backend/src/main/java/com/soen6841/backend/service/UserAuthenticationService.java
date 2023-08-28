package com.soen6841.backend.service;

import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.CurrentUser;
import com.soen6841.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserAuthenticationService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).setAttribute("loginAccount", account, RequestAttributes.SCOPE_REQUEST);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority(account.getRole().name()));
            return new CurrentUser(account, authorityList);
        }
        throw new UsernameNotFoundException("Account not found.");
    }
}
