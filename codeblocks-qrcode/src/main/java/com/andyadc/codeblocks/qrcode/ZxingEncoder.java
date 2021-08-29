package com.andyadc.codeblocks.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/6/28
 */
public class ZxingEncoder {

	private static final Logger logger = LoggerFactory.getLogger(ZxingEncoder.class);

	public InputStream encode2InputStream(ZxingEntity zxingEntity) {
		byte[] bytes = encode2Bytes(zxingEntity);
		return new ByteArrayInputStream(bytes);
	}

	public byte[] encode2Bytes(ZxingEntity zxingEntity) {
		BarcodeFormat barcodeFormat = zxingEntity.getBarcodeFormat();
		String text = zxingEntity.getText();
		String format = zxingEntity.getFormat();
		String encoding = zxingEntity.getEncoding();
		ErrorCorrectionLevel correctionLevel = zxingEntity.getLevel();
		int width = zxingEntity.getWidth();
		int height = zxingEntity.getHeight();
		int margin = zxingEntity.getMargin();
		int foregroundColor = zxingEntity.getForegroundColor();
		int backgroundColor = zxingEntity.getBackgroundColor();
		boolean deleteWhiteBorder = zxingEntity.isDeleteWhiteBorder();
		boolean hasLogo = zxingEntity.hasLogo();

		if (barcodeFormat == null) {
			throw new ZxingException("Barcode format is null");
		}

		if (ZxingUtil.isBlank(text)) {
			throw new ZxingException("Text is null or empty");
		}

		if (ZxingUtil.isBlank(format)) {
			throw new ZxingException("Format is null or empty");
		}

		if (ZxingUtil.isBlank(encoding)) {
			throw new ZxingException("Encoding is null or empty");
		}

		if (correctionLevel == null) {
			throw new ZxingException("Correction level is null");
		}

		if (width <= 0) {
			throw new ZxingException("Invalid width=" + width);
		}

		if (height <= 0) {
			throw new ZxingException("Invalid height=" + height);
		}

		if (margin < 0 || margin > 4) {
			throw new ZxingException("Invalid margin=" + margin + ", it must be [0, 4]");
		}

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			Map<EncodeHintType, Object> hints = createHints(encoding, correctionLevel, margin);
			MultiFormatWriter formatWriter = new MultiFormatWriter();

			MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
			BitMatrix bitMatrix = formatWriter.encode(text, barcodeFormat, width, height, hints);

			// 删除二维码四周的白边
			if (barcodeFormat == BarcodeFormat.QR_CODE && deleteWhiteBorder) {
				bitMatrix = deleteWhiteBorder(bitMatrix);
			}

			// 先输出Logo
			if (barcodeFormat == BarcodeFormat.QR_CODE && hasLogo) {
				BufferedImage logoImage = createLogoImage(bitMatrix, foregroundColor, backgroundColor, zxingEntity);

				if (!ImageIO.write(logoImage, format, outputStream)) {
					throw new ZxingException("Failed to write logo image");
				}
			}

			// 再输出二维码/条形码
			MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream, imageConfig);

			return outputStream.toByteArray();
		} catch (Exception e) {
			logger.error("Encode stream error", e);
			throw new ZxingException("Encode stream error", e);
		}
	}

	public File encode2File(ZxingEntity zxingEntity) {
		BarcodeFormat barcodeFormat = zxingEntity.getBarcodeFormat();
		String text = zxingEntity.getText();
		String format = zxingEntity.getFormat();
		String encoding = zxingEntity.getEncoding();
		ErrorCorrectionLevel correctionLevel = zxingEntity.getLevel();
		int width = zxingEntity.getWidth();
		int height = zxingEntity.getHeight();
		int margin = zxingEntity.getMargin();
		int foregroundColor = zxingEntity.getForegroundColor();
		int backgroundColor = zxingEntity.getBackgroundColor();
		boolean deleteWhiteBorder = zxingEntity.isDeleteWhiteBorder();
		boolean hasLogo = zxingEntity.hasLogo();
		File outputFile = zxingEntity.getOutputFile();

		if (barcodeFormat == null) {
			throw new ZxingException("Barcode format is null");
		}

		if (ZxingUtil.isBlank(text)) {
			throw new ZxingException("Text is null or empty");
		}

		if (ZxingUtil.isBlank(format)) {
			throw new ZxingException("Format is null or empty");
		}

		if (ZxingUtil.isBlank(encoding)) {
			throw new ZxingException("Encoding is null or empty");
		}

		if (correctionLevel == null) {
			throw new ZxingException("Correction level is null");
		}

		if (width <= 0) {
			throw new ZxingException("Invalid width=" + width);
		}

		if (height <= 0) {
			throw new ZxingException("Invalid height=" + height);
		}

		if (margin < 0 || margin > 4) {
			throw new ZxingException("Invalid margin=" + margin + ", it must be [0, 4]");
		}

		if (outputFile == null) {
			throw new ZxingException("Output file is null");
		}

		try {
			Map<EncodeHintType, Object> hints = createHints(encoding, correctionLevel, margin);
			MultiFormatWriter formatWriter = new MultiFormatWriter();

			MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
			BitMatrix bitMatrix = formatWriter.encode(text, barcodeFormat, width, height, hints);

			// 删除二维码四周的白边
			if (barcodeFormat == BarcodeFormat.QR_CODE && deleteWhiteBorder) {
				bitMatrix = deleteWhiteBorder(bitMatrix);
			}

			ZxingUtil.createDirectory(outputFile);

			// 先输出二维码/条形码
			MatrixToImageWriter.writeToPath(bitMatrix, format, outputFile.toPath(), imageConfig);

			// 再输出Logo
			if (barcodeFormat == BarcodeFormat.QR_CODE && hasLogo) {
				BufferedImage logoImage = createLogoImage(bitMatrix, foregroundColor, backgroundColor, zxingEntity);

				if (!ImageIO.write(logoImage, format, outputFile)) {
					throw new ZxingException("Failed to write logo image");
				}
			}
		} catch (Exception e) {
			logger.error("Encode file=[{}] error", outputFile.getPath(), e);
			throw new ZxingException("Encode file=[" + outputFile.getPath() + "] error", e);
		}

		return outputFile;
	}

	private BitMatrix deleteWhiteBorder(BitMatrix bitMatrix) {
		int[] rectangle = bitMatrix.getEnclosingRectangle();
		int width = rectangle[2] + 1;
		int height = rectangle[3] + 1;

		BitMatrix matrix = new BitMatrix(width, height);
		matrix.clear();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (bitMatrix.get(i + rectangle[0], j + rectangle[1]))
					matrix.set(i, j);
			}
		}
		return matrix;
	}

	private BufferedImage createLogoImage(BitMatrix bitMatrix, int foregroundColor, int backgroundColor, ZxingEntity zxingEntity) throws IOException {
		BufferedImage image = createBufferedImage(bitMatrix, foregroundColor, backgroundColor);
		Graphics2D g2d = image.createGraphics();

		int ratioWidth = image.getWidth() * 2 / 10;
		int ratioHeight = image.getHeight() * 2 / 10;

		if (ratioWidth > 100) {
			logger.warn("Ratio width=[{}] for logo image is more than 100", ratioWidth);
		}

		if (ratioHeight > 100) {
			logger.warn("Ratio height=[{}] for logo image is more than 100", ratioHeight);
		}

		// 载入Logo
		File logoFile = zxingEntity.getLogoFile();
		InputStream logoInputStream = zxingEntity.getLogInputStream();

		Image logoImage;
		if (logoInputStream != null) {
			logoImage = ImageIO.read(logoInputStream);
		} else {
			logoImage = ImageIO.read(logoFile);
		}

		int logoWidth = logoImage.getWidth(null) > ratioWidth ? ratioWidth : logoImage.getWidth(null);
		int logoHeight = logoImage.getHeight(null) > ratioHeight ? ratioHeight : logoImage.getHeight(null);

		int x = (image.getWidth() - logoWidth) / 2;
		int y = (image.getHeight() - logoHeight) / 2;

		g2d.drawImage(logoImage, x, y, logoWidth, logoHeight, null);
		g2d.setColor(Color.black);
		g2d.setBackground(Color.WHITE);
		g2d.dispose();

		logoImage.flush();

		return image;
	}

	private BufferedImage createBufferedImage(BitMatrix bitMatrix, int foregroundColor, int backgroundColor) {
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? foregroundColor : backgroundColor);
			}
		}

		return image;
	}


	private Map<EncodeHintType, Object> createHints(String encoding, ErrorCorrectionLevel level, int margin) {
		Map<EncodeHintType, Object> hints = new HashMap<>(8);
		hints.put(EncodeHintType.CHARACTER_SET, encoding); // 设置编码
		hints.put(EncodeHintType.ERROR_CORRECTION, level); // 指定纠错等级
		hints.put(EncodeHintType.MARGIN, margin); // 设置白边

		return hints;
	}
}
