package com.andyadc.codeblocks.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.InputStream;

/**
 * @author andy.an
 * @since 2018/6/28
 */
public class ZxingEntity {

    private BarcodeFormat barcodeFormat;
    private String text;
    private String format = "jpg";
    private String encoding = "UTF-8";
    private ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
    private int width;
    private int height;
    private int margin = 1;
    private int foregroundColor = 0xFF000000;
    private int backgroundColor = 0xFFFFFFFF;
    private boolean deleteWhiteBorder = false;

    private File outputFile;
    private File logoFile;
    private InputStream logInputStream;

    // 默认 logo
    private File localLogo = new File("src/main/resources/speedy.jpg");

    public ZxingEntity withDefaultLogo() {
        this.logoFile = localLogo;
        return this;
    }

    public boolean hasLogo() {
        return logoFile != null || logInputStream != null;
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public InputStream getLogInputStream() {
        return logInputStream;
    }

    public void setLogInputStream(InputStream logInputStream) {
        this.logInputStream = logInputStream;
    }

    public File getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(File logoFile) {
        this.logoFile = logoFile;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public ErrorCorrectionLevel getLevel() {
        return level;
    }

    public void setLevel(ErrorCorrectionLevel level) {
        this.level = level;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isDeleteWhiteBorder() {
        return deleteWhiteBorder;
    }

    public void setDeleteWhiteBorder(boolean deleteWhiteBorder) {
        this.deleteWhiteBorder = deleteWhiteBorder;
    }
}
