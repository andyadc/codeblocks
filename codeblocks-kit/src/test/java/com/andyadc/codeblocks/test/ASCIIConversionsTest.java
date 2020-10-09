package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.image.ASCIIConversions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

/**
 * @author andy.an
 * @since 2018/6/20
 */
public class ASCIIConversionsTest {

    @Test
    public void testConversion() throws Exception {

		BufferedImage image = ImageIO.read(new FileInputStream("D://1.gif"));

        ASCIIConversions conversions = new ASCIIConversions();
        String ascii = conversions.convert(image, false);
        System.out.println(ascii);
    }
}
