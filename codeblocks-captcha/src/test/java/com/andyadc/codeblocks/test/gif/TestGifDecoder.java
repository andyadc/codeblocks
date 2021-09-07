package com.andyadc.codeblocks.test.gif;


import com.andyadc.codeblocks.gif.GifDecoder;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

//import static com.google.common.io.ByteStreams.toByteArray;

public class TestGifDecoder {

	@Test
	public void testDecodingGifWithDeferredClearCodesInLZWCompression() throws Exception {
		GifDecoder decoder = new GifDecoder();
		decoder.read(getClass().getResourceAsStream("/gif/brucelee.gif"));
		BufferedImage image = decoder.getImage();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "gif", outputStream);
		byte[] actualBytes = outputStream.toByteArray();
//        byte[] expectedBytes = toByteArray(getClass().getResourceAsStream("/brucelee-frame.gif"));
//		Assertions.assertEquals(actualBytes, expectedBytes);
	}
}
