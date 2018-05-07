package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.BackendStartup;
import com.andyadc.codeblocks.mapper.BankMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author andy.an
 * @since 2018/5/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BackendStartup.class)
public class MapperTest {

    @Autowired
    private BankMapper bankMapper;

    @Test
    public void testGetBankMappings() {
        bankMapper.selectBankMapping();
    }

    @Test
    public void testInsertBankMapping() {

    }
}
