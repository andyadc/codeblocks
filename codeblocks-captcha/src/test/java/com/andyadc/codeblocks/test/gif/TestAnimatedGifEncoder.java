package com.andyadc.codeblocks.test.gif;

import com.andyadc.codeblocks.gif.AnimatedGifEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.awt.Color.*;

public class TestAnimatedGifEncoder {

	private ByteArrayOutputStream outputStream;
	private AnimatedGifEncoder encoder;
	private BufferedImage sonic1;
	private BufferedImage sonic2;
	private BufferedImage agif;
	private BufferedImage bgif;

	@BeforeEach
	public void setUp() throws IOException {
		sonic1 = getImage("/gif/sonic1.png");
		sonic2 = getImage("/gif/sonic2.png");

		agif = getImage("/gif/a.gif");
		bgif = getImage("/gif/b.gif");

		outputStream = new ByteArrayOutputStream();
		encoder = new AnimatedGifEncoder();
		encoder.start(outputStream);
	}

	@Test
	public void testBasicOutput() throws Exception {
		encodeSampleSonicFrames();

		assertEncodedImageIsEqualTo("/gif/sonic-normal.gif");
	}

	@Test
	public void testNullBackgroundWorks() throws Exception {
		encoder.setTransparent(null);
		encodeSampleSonicFrames();

		assertEncodedImageIsEqualTo("/gif/sonic-normal.gif");
	}

	@Test
	public void testBackgroundColorWorksOnOversizeImage() throws Exception {
		encoder.setSize(600, 600);
		encoder.setBackground(RED);
		encodeSampleSonicFrames();

		assertEncodedImageIsEqualTo("/gif/sonic-big-and-red.gif");
	}

	@Test
	public void testTransparentColor() throws Exception {
		encoder.setTransparent(BLUE);
		encodeSampleSonicFrames();

		assertEncodedImageIsEqualTo("/gif/sonic-blue-transparent.gif");
	}

	@Test
	public void testTransparentColorExactMAGENTA() throws Exception {
		encoder.setTransparent(MAGENTA, true);
		encodeSampleExactFrames();
		Assertions.assertFalse(encoder.isColorUsed(MAGENTA)); // Called before finishing the encoder
		encoder.finish();
		// As there is no MAGENTA color, the result should be a GIF with no visible transparency
		assertEncodedImageIsEqualTo("/gif/AandB.gif");
	}

	@Test
	public void testTransparentColorCloseToBlue() throws Exception {
		encoder.setTransparent(BLUE);
		encodeSampleExactFrames();
		encoder.finish();
		// As there are pixels close to blue, the result should be a GIF with a partial transparency on blue color
		assertEncodedImageIsEqualTo("/gif/AandBCloseToBlue.gif");
	}

	@Test
	public void testTransparentColorExactBLACK() throws Exception {
		encoder.setTransparent(BLACK, true);
		encodeSampleExactFrames();
		Assertions.assertTrue(encoder.isColorUsed(BLACK)); // Called before finishing the encoder
		encoder.finish();
		// As there is a BLACK Background color, the result should be a GIF mostly transparent
		assertEncodedImageIsEqualTo("/gif/AandB-transparent.gif");
	}

	@Test
	public void testBackgroundAndTransparent() throws Exception {
		encoder.setSize(600, 600);
		encoder.setBackground(GREEN);
		encoder.setTransparent(BLUE);
		encodeSampleSonicFrames();

		assertEncodedImageIsEqualTo("/gif/sonic-green-bg-blue-transparent.gif");
	}

	private void encodeSampleSonicFrames() {
		encoder.setRepeat(0);
		encoder.setDelay(400);
		encoder.addFrame(sonic1);
		encoder.addFrame(sonic2);
		encoder.finish();
	}

	private void encodeSampleExactFrames() {
		encoder.setRepeat(0);
		encoder.setDelay(1000);
		encoder.addFrame(agif);
		encoder.addFrame(bgif);
	}

	private void assertEncodedImageIsEqualTo(String name) throws IOException {
		byte[] expectedBytes = getExpectedBytes(name);
		byte[] actualBytes = outputStream.toByteArray();

		Assertions.assertEquals(actualBytes.length, expectedBytes.length);
		Assertions.assertEquals(actualBytes, expectedBytes);
	}

	private byte[] getExpectedBytes(String name) throws IOException {
		File expectedFile = new File(getClass().getResource(name).getFile());
		byte[] expectedBytes = new byte[(int) expectedFile.length()];
		FileInputStream inputStream = new FileInputStream(expectedFile);
		int readBytes = inputStream.read(expectedBytes);

//        assertThat(readBytes).isGreaterThan(0);
		Assertions.assertEquals(readBytes, expectedBytes.length);

		inputStream.close();
		return expectedBytes;
	}

	private BufferedImage getImage(String name) throws IOException {
		return ImageIO.read(new File(getClass().getResource(name).getFile()));
	}
}
