package com.andyadc.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class ServletIntegrationTest {

	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;
}
