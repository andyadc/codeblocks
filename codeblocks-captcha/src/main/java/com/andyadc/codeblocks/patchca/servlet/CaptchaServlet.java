package com.andyadc.codeblocks.patchca.servlet;

import com.andyadc.codeblocks.kit.text.StringUtil;
import com.andyadc.codeblocks.patchca.background.BackgroundFactory;
import com.andyadc.codeblocks.patchca.filter.ConfigurableFilterFactory;
import com.andyadc.codeblocks.patchca.filter.library.AbstractImageOp;
import com.andyadc.codeblocks.patchca.filter.library.WobbleImageOp;
import com.andyadc.codeblocks.patchca.font.RandomFontFactory;
import com.andyadc.codeblocks.patchca.service.Captcha;
import com.andyadc.codeblocks.patchca.service.ConfigurableCaptchaService;
import com.andyadc.codeblocks.patchca.text.renderer.BestFitTextRenderer;
import com.andyadc.codeblocks.patchca.text.renderer.TextRenderer;
import com.andyadc.codeblocks.patchca.word.RandomWordFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andaicheng
 * @version 2016/4/16
 */
public final class CaptchaServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaServlet.class);
    private static final long serialVersionUID = 1L;

    private static final String CHARACTERS = "AaBbCcDdEeFfGgHhJjKkMmNnQqXxYyPpWwSsTtRrUui123456789";
    private static final String CAPTCHA_SESSION = "_captcha";
    private static final String CAPTCHA_SIZE = "_size";
    private static final String CAPTCHA_HEIGHT = "_height";
    private static final String CAPTCHA_WIDTH = "_width";
    private static final String CAPTCHA_LENGTH = "_length";

    private ConfigurableCaptchaService configurableCaptchaService = null;
    private RandomWordFactory wordFactory = null;
    private RandomFontFactory fontFactory = null;
    private TextRenderer textRenderer = null;

    public static boolean validateCaptcha(HttpServletRequest request, String userCaptcha) {
        return request != null
                && StringUtil.isNotBlank(userCaptcha)
                && userCaptcha.equalsIgnoreCase(String.valueOf(request.getSession().getAttribute(CAPTCHA_SESSION)));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("image/png");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");

        set(req);

        HttpSession session = req.getSession(true);

        Captcha captcha = configurableCaptchaService.getCaptcha();
        String code = captcha.getChallenge();
        session.setAttribute(CAPTCHA_SESSION, code);

        BufferedImage bufferedImage = captcha.getImage();

        try (OutputStream outputStream = resp.getOutputStream()) {
            ImageIO.write(bufferedImage, "png", outputStream);
            outputStream.flush();
        } catch (Exception e) {
            logger.error("CaptchaServlet handle error", e);
        }
    }

    /**
     * 常见参数设置
     */
    private void set(HttpServletRequest request) {
        String size = request.getParameter(CAPTCHA_SIZE);
        String height = request.getParameter(CAPTCHA_HEIGHT);
        String width = request.getParameter(CAPTCHA_WIDTH);
        String length = request.getParameter(CAPTCHA_LENGTH);

        fontFactory = new RandomFontFactory();
        int fontSize = Integer.parseInt(StringUtil.defaultIfBlank(size, "25"));
        fontFactory.setMaxSize(fontSize);
        fontFactory.setMinSize(fontSize);
        configurableCaptchaService.setFontFactory(fontFactory);

        wordFactory = new RandomWordFactory();
        wordFactory.setCharacters(CHARACTERS);
        int l = Integer.parseInt(StringUtil.defaultIfBlank(length, "4"));
        wordFactory.setMaxLength(l);
        wordFactory.setMinLength(l);
        configurableCaptchaService.setWordFactory(wordFactory);

        configurableCaptchaService.setHeight(Integer.parseInt(StringUtil.defaultIfBlank(height, "30")));
        configurableCaptchaService.setWidth(Integer.parseInt(StringUtil.defaultIfBlank(width, "100")));
    }

    @Override
    public void destroy() {
        configurableCaptchaService = null;
        wordFactory = null;
        fontFactory = null;
        textRenderer = null;
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) {
        String cs = config.getInitParameter("captchaSession");
        logger.info("CaptchaServlet init param: {}", cs);

        configurableCaptchaService = new ConfigurableCaptchaService();

        // 颜色创建工厂
        configurableCaptchaService.setColorFactory((int x) -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int[] c = new int[3];
            int i = random.nextInt(c.length);
            for (int fi = 0; fi < c.length; fi++) {
                if (fi == i) {
                    c[fi] = random.nextInt(71);
                } else {
                    c[fi] = random.nextInt(256);
                }
            }
            return new Color(c[0], c[1], c[2]);
        });

        // 自定义验证码图片背景
        configurableCaptchaService.setBackgroundFactory(new MyCustomeBackgroundFactory());

        // 图片滤镜设置
        ConfigurableFilterFactory configurableFilterFactory = new ConfigurableFilterFactory();
        java.util.List<BufferedImageOp> filters = new ArrayList<>();
        WobbleImageOp wobbleImageOp = new WobbleImageOp();
        wobbleImageOp.setEdgeMode(AbstractImageOp.EDGE_CLAMP);
        wobbleImageOp.setxAmplitude(1.0);
        wobbleImageOp.setyAmplitude(2.0);
        filters.add(wobbleImageOp);
        configurableFilterFactory.setFilters(filters);
        configurableCaptchaService.setFilterFactory(configurableFilterFactory);

        // 文字渲染器设置
        textRenderer = new BestFitTextRenderer();
        textRenderer.setBottomMargin(1);
        textRenderer.setTopMargin(1);
        configurableCaptchaService.setTextRenderer(textRenderer);
    }

    /**
	 * 自定义验证码图片背景,主要画一些噪点和干扰线
	 */
	private static class MyCustomeBackgroundFactory implements BackgroundFactory {

		@Override
		public void fillBackground(BufferedImage bufferedImage) {
			Graphics graphics = bufferedImage.getGraphics();

			// 验证码图片的宽高
			int imgWidth = bufferedImage.getWidth();
			int imgHeight = bufferedImage.getHeight();

			// 填充为白色背景
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, imgWidth, imgHeight);

            ThreadLocalRandom random = ThreadLocalRandom.current();
            // 画100个噪点(颜色及位置随机)
            for (int i = 0; i < 25; i++) {
                // 随机颜色
                int rInt = random.nextInt(255);
                int gInt = random.nextInt(255);
                int bInt = random.nextInt(255);

                graphics.setColor(new Color(rInt, gInt, bInt));

                // 随机位置
                int xInt = random.nextInt(imgWidth - 3);
                int yInt = random.nextInt(imgHeight - 2);

                // 随机旋转角度
                int sAngleInt = random.nextInt(360);
                int eAngleInt = random.nextInt(360);

                // 随机大小
                int wInt = random.nextInt(6);
                int hInt = random.nextInt(6);

                graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);

                // 画干扰线
                if (i % 5 == 0) {
                    int xInt2 = random.nextInt(imgWidth);
                    int yInt2 = random.nextInt(imgHeight);
                    graphics.drawLine(xInt, yInt, xInt2, yInt2);
                }
            }
        }
    }
}
