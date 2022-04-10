package com.andyadc.bms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@Controller
public class RegistrationController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private MessageSource messageSource;
	
	@Inject
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@GetMapping("/login")
	public ModelAndView login(final HttpServletRequest request,
							  final ModelMap model,
							  @RequestParam("messageKey") final Optional<String> messageKey,
							  @RequestParam("error") final Optional<String> error) {
		Locale locale = request.getLocale();
		model.addAttribute("lang", locale.getLanguage());
		messageKey.ifPresent(key -> {
				String message = messageSource.getMessage(key, null, locale);
				model.addAttribute("message", message);
			}
		);

		error.ifPresent(e -> model.addAttribute("error", e));

		return new ModelAndView("login", model);
	}

	@PostMapping(value = "/login")
	public void login(HttpServletRequest request) {

	}
}
