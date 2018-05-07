package com.andyadc.codeblocks.mapper;

import com.andyadc.codeblocks.entity.BankMapping;

import java.util.List;

/**
 * @author andy.an
 * @since 2018/5/3
 */
public interface BankMapper {

    List<BankMapping> selectBankMapping();
}
