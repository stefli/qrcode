/**
 * Copyright (c) 2015, Powered by stefli. All Rights Reserved. 
 */
package com.stefli.qrcode;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author stefli
 * @version 1.0
 * @date 2015年10月31日 下午1:15:30
 *
 */
public class QRCodeKitTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGenerateWithOnlyData() throws IOException, NotFoundException {
		String data = "http://www.stefli.com";
		BufferedImage image = QRCodeKit.createQRCode(data);
		ImageIO.write(image, "png", new File("result.png"));
		Map hint = new HashMap();
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hint.put(EncodeHintType.MARGIN, 1);
		hint.put(EncodeHintType.CHARACTER_SET, QRCodeKit.QRCODE_DEFAULT_CHARSET);
		String result = QRCodeKit.readQRCode("result.png", hint);
		assertEquals(data, result);
	}

	@Test
	public void testBase64Encode() throws IOException {
		String data = "http://www.stefli.com";
		BufferedImage image = QRCodeKit.createQRCode(data);
		String result = QRCodeKit.getImageBase64String(image);
		System.out.println(result);
		QRCodeKit.convertBase64DataToImage(result, new File("decode.png"));
	}

}
