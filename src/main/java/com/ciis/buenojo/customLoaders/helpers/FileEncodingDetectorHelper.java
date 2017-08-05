package com.ciis.buenojo.customLoaders.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileEncodingDetectorHelper {
	private static final Logger log = LoggerFactory.getLogger(FileEncodingDetectorHelper.class);

	public static final String guessEncoding(final byte[] bytes) {
		final UniversalDetector detector;
		final String encoding, output, DEFAULT_ENCODING;

		DEFAULT_ENCODING = "UTF-8";
		detector = new UniversalDetector(null);
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		encoding = detector.getDetectedCharset();
		detector.reset();
		if (encoding == null)
			output = DEFAULT_ENCODING;
		else
			output = encoding;
		return output;
	}

	public static final Charset guessEncodingAndGetCharset(final byte[] bytes) {
		final Charset output;
		output = Charset.forName(guessEncoding(bytes));
		return output;
	}
	
	public static final Charset guessEncodingAndGetCharset(final URL resource) {
		final Charset output;
		output = Charset.forName(guessEncoding(fetchRemoteFile(resource)));
		return output;
	}
	
	public static final byte[] fetchRemoteFile(final URL url) {
		InputStream is = null;
		byte[] bytes = null;
		try {
			is = url.openStream();
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			log.error(String.format("Problem with the file:[%s]", url.getFile()), e);
		} finally {
			IOUtils.closeQuietly(is);

		}
		return bytes;
	}

	public static final byte[] fetchRemoteFile(final File file) {
		InputStream is = null;
		byte[] bytes = null;
		try {
			is = new FileInputStream(file);
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			log.error(String.format("Problem with the file:[%s]", file.getAbsolutePath()), e);
		} finally {
			IOUtils.closeQuietly(is);

		}
		return bytes;
	}
	public static final Charset getCharsetFromURL(final URL resource) {
		final Charset output;
		final byte[] fileContent;
		final String charSetName;
		fileContent = fetchRemoteFile(resource);
		charSetName = guessEncoding(fileContent);
		output = Charset.forName(charSetName);
		return output;
	}
	
	public static final Charset getCharsetFromURL(final File resource) {
		final Charset output;
		final byte[] fileContent;
		final String charSetName;
		fileContent = fetchRemoteFile(resource);
		charSetName = guessEncoding(fileContent);
		output = Charset.forName(charSetName);
		return output;
	}
}
