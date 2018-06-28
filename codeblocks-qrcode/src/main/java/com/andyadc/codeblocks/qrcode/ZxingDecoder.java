package com.andyadc.codeblocks.qrcode;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/6/28
 */
public class ZxingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ZxingDecoder.class);

    private static Map<DecodeHintType, Object> createHints(String encoding) {
        Map<DecodeHintType, Object> hints = new HashMap<>(4);
        hints.put(DecodeHintType.CHARACTER_SET, encoding); // 设置编码

        return hints;
    }

    public Result decodeByFile(File file, String encoding) {
        BufferedImage image;

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            logger.error("Decode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Decode file=[" + file.getPath() + "] error", e);
        }

        try {
            return decode(image, encoding);
        } catch (NotFoundException e) {
            logger.error("Decode stream error", e);
            throw new ZxingException("Decode stream error", e);
        }
    }

    public Result decodeByInputStream(InputStream inputStream, String encoding) {
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            logger.error("Decode stream error", e);
            throw new ZxingException("Decode stream error", e);
        }

        try {
            return decode(image, encoding);
        } catch (NotFoundException e) {
            logger.error("Decode stream error", e);
            throw new ZxingException("Decode stream error", e);
        }
    }

    public Result decodeByURL(URL url, String encoding) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            logger.error("Decode url error", e);
            throw new ZxingException("Decode url error", e);
        }

        try {
            return decode(image, encoding);
        } catch (NotFoundException e) {
            logger.error("Decode url error", e);
            throw new ZxingException("Decode url error", e);
        }
    }

    public Result decodeByBytes(byte[] bytes, String encoding) {
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(bytes);
            return decodeByInputStream(inputStream, encoding);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private Result decode(BufferedImage image, String encoding) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        Map<DecodeHintType, Object> hints = createHints(encoding);

        MultiFormatReader reader = new MultiFormatReader();

        return reader.decode(binaryBitmap, hints);
    }
}
