/**
 * Copyright (c) 2015, Powered by stefli. All Rights Reserved. 
 */
package com.stefli.qrcode;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author stefli
 * @version 1.0
 * @date 2015年10月31日 上午10:42:16
 *
 */
public class QRCodeKit {

	public static final String QRCODE_DEFAULT_CHARSET = "UTF-8";

	public static final int QRCODE_DEFAULT_HEIGHT = 320;

	public static final int QRCODE_DEFAULT_WIDTH = 320;

	/**
	 * Create qrcode with default settings
	 *
	 * @author stefli
	 * @param data
	 * @return
	 */
	public static BufferedImage createQRCode(String data) {
		return createQRCode(data, QRCODE_DEFAULT_WIDTH, QRCODE_DEFAULT_HEIGHT);
	}

	/**
	 * Create qrcode with default charset
	 *
	 * @author stefli
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage createQRCode(String data, int width, int height) {
		return createQRCode(data, QRCODE_DEFAULT_CHARSET, width, height);
	}

	/**
	 * Create qrcode with specified charset
	 *
	 * @author stefli
	 * @param data
	 * @param charset
	 * @param width
	 * @param height
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static BufferedImage createQRCode(String data, String charset, int width, int height) {
		Map hint = new HashMap();
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hint.put(EncodeHintType.MARGIN, 0);
		hint.put(EncodeHintType.CHARACTER_SET, charset);

		return createQRCode(data, charset, hint, width, height);
	}

	/**
	 * Create qrcode with specified hint
	 *
	 * @author stefli
	 * @param data
	 * @param charset
	 * @param hint
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage createQRCode(String data, String charset, Map<EncodeHintType, ?> hint, int width,
			int height) {
		BitMatrix matrix;
		try {
			matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE,
					width, height, hint);
			return MatrixToImageWriter.toBufferedImage(matrix);
		} catch (WriterException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Create qrcode with default settings and logo
	 *
	 * @author stefli
	 * @param data
	 * @param logoFile
	 * @return
	 */
	public static BufferedImage createQRCodeWithLogo(String data, File logoFile) {
		return createQRCodeWithLogo(data, QRCODE_DEFAULT_WIDTH, QRCODE_DEFAULT_HEIGHT, logoFile);
	}

	/**
	 * Create qrcode with default charset and logo
	 *
	 * @author stefli
	 * @param data
	 * @param width
	 * @param height
	 * @param logoFile
	 * @return
	 */
	public static BufferedImage createQRCodeWithLogo(String data, int width, int height, File logoFile) {
		return createQRCodeWithLogo(data, QRCODE_DEFAULT_CHARSET, width, height, logoFile);
	}

	/**
	 * Create qrcode with specified charset and logo
	 *
	 * @author stefli
	 * @param data
	 * @param charset
	 * @param width
	 * @param height
	 * @param logoFile
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static BufferedImage createQRCodeWithLogo(String data, String charset, int width, int height, File logoFile) {
		Map hint = new HashMap();
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hint.put(EncodeHintType.MARGIN, 1);
		hint.put(EncodeHintType.CHARACTER_SET, charset);

		return createQRCodeWithLogo(data, charset, hint, width, height, logoFile);
	}

	/**
	 * Create qrcode with specified hint and logo
	 *
	 * @author stefli
	 * @param data
	 * @param charset
	 * @param hint
	 * @param width
	 * @param height
	 * @param logoFile
	 * @return
	 */
	public static BufferedImage createQRCodeWithLogo(String data, String charset, Map<EncodeHintType, ?> hint,
			int width, int height, File logoFile) {
		try {
			BufferedImage qrcode = createQRCode(data, charset, hint, width, height);
			BufferedImage logo = ImageIO.read(logoFile);
			int deltaHeight = height - logo.getHeight();
			int deltaWidth = width - logo.getWidth();

			BufferedImage combined = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();
			g.drawImage(qrcode, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.drawImage(logo, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

			return combined;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Read qrcode from file
	 *
	 * @author stefli
	 * @param filePath
	 * @param hint
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static String readQRCode(String filePath, Map<DecodeHintType, ?> hint) throws FileNotFoundException,
			IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(
				ImageIO.read(new FileInputStream(filePath)))));
		Result result = new MultiFormatReader().decode(binaryBitmap, hint);

		return result.getText();
	}

}
