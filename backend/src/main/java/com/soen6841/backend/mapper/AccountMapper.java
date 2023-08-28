package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.account.AccountDto;
import com.soen6841.backend.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto map(Account source);

    List<AccountDto> mapList(List<Account> accountList);

}
