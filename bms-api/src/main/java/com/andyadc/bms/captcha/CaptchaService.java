package com.andyadc.bms.captcha;

import com.andyadc.codeblocks.patchca.color.GradientColorFactory;
import com.andyadc.codeblocks.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.andyadc.codeblocks.patchca.filter.predefined.DiffuseRippleFilterFactory;
import com.andyadc.codeblocks.patchca.filter.predefined.DoubleRippleFilterFactory;
import com.andyadc.codeblocks.patchca.filter.predefined.MarbleRippleFilterFactory;
import com.andyadc.codeblocks.patchca.filter.predefined.WobbleRippleFilterFactory;
import com.andyadc.codeblocks.patchca.service.Captcha;
import com.andyadc.codeblocks.patchca.service.ConfigurableCaptchaService;
import com.andyadc.codeblocks.patchca.utils.encoder.EncoderHelper;
import com.andyadc.codeblocks.patchca.word.AdaptiveRandomWordFactory;
import com.andyadc.codeblocks.patchca.word.RandomWordFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

@Service
public class CaptchaService {

	private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);

	private static final RemovalListener<String, String> listener = (notification) ->
		logger.info("Removed captcha cache key: {}", notification.getKey()
		);

	private static final Cache<String, String> captchaCache = CacheBuilder.newBuilder()
		.expireAfterWrite(61L, TimeUnit.SECONDS)
		.removalListener(RemovalListeners.asynchronous(listener, Executors.newSingleThreadExecutor()))
		.build();

	private final LongAdder adder = new LongAdder();

	@Value("${captcha.image.length:6}")
	private Integer length;
	@Value("${captcha.image.height:70}")
	private Integer height;
	@Value("${captcha.image.width:160}")
	private Integer width;

	public CaptchaDTO gen() {
		return create(height, width, length);
	}

	public CaptchaDTO gen(CaptchaDTO dto) {
		if (dto == null) {
			dto = new CaptchaDTO();
		}
		Integer _height = dto.getHeight() != null ? dto.getHeight() : height;
		Integer _width = dto.getWidth() != null ? dto.getWidth() : width;
		Integer _length = dto.getLength() != null ? dto.getLength() : length;
		return create(_height, _width, _length);
	}

	private CaptchaDTO create(Integer height, Integer width, Integer length) {
		CaptchaDTO dto = new CaptchaDTO();
		Captcha captcha = createCaptcha(height, width, length);
		dto.setCaptchaId(adder.longValue() + "");
		captchaCache.put(dto.getCaptchaId(), captcha.getChallenge());

		dto.setCaptchaCode(captcha.getChallenge());
		String captchaImage = EncoderHelper.genCaptchaImage(captcha);
		dto.setCaptchaPngImage(captchaImage);
		return dto;
	}

	public boolean validate(CaptchaDTO dto) {
		Objects.requireNonNull(dto);
		if (dto.getCaptchaId() == null || dto.getCaptchaCode() == null) {
			return false;
		}
		String key = dto.getCaptchaId();
		String value = captchaCache.getIfPresent(key);
		captchaCache.invalidate(key);
		return value != null && value.equalsIgnoreCase(dto.getCaptchaCode());
	}

	private Captcha createCaptcha(Integer height, Integer width, Integer length) {
		ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
		if (height != null && height > 0) {
			cs.setHeight(height);
		}
		if (width != null && width > 0) {
			cs.setWidth(width);
		}

		RandomWordFactory wordFactory = new AdaptiveRandomWordFactory();
		if (length != null && length > 0) {
			wordFactory.setMinLength(length);
			wordFactory.setMaxLength(length);
		}
		cs.setWordFactory(wordFactory);

		cs.setColorFactory(new GradientColorFactory());
		switch (adder.intValue() % 5) {
			case 0:
				cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
				break;
			case 1:
				cs.setFilterFactory(new MarbleRippleFilterFactory());
				break;
			case 2:
				cs.setFilterFactory(new DoubleRippleFilterFactory());
				break;
			case 3:
				cs.setFilterFactory(new WobbleRippleFilterFactory());
				break;
			case 4:
				cs.setFilterFactory(new DiffuseRippleFilterFactory());
				break;
		}
		adder.increment();
		return cs.getCaptcha();
	}
}
