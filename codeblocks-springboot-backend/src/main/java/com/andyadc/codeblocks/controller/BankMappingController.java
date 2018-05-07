package com.andyadc.codeblocks.controller;

import com.andyadc.codeblocks.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author andy.an
 * @since 2018/5/7
 */
@Controller
public class BankMappingController {

    @Autowired
    private BankMapper bankMapper;

    @ResponseBody
    @RequestMapping("bankMappings")
    public Object bankMappings() {
        return bankMapper.selectBankMapping();
    }
}
