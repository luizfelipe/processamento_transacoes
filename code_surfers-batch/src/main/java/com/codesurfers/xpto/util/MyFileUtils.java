package com.codesurfers.xpto.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

public class MyFileUtils {

	public static void unzipFile(String zipFilePath, String unzippedFilePath) throws IOException {
		File fileDir = new File(unzippedFilePath);
		if (!fileDir.exists())
			fileDir.mkdirs();
		FileInputStream fileInputStream;
		byte[] buffer = new byte[1024];
		try {
			fileInputStream = new FileInputStream(zipFilePath);
			ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				File newFile = new File(String.format("%s%s", unzippedFilePath, fileName));
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fileOutputStream = new FileOutputStream(newFile);
				int length;
				while ((length = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, length);
				}
				fileOutputStream.close();
				zipInputStream.closeEntry();
				zipEntry = zipInputStream.getNextEntry();
			}
			zipInputStream.closeEntry();
			zipInputStream.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void partitionFile(String fileName, String fileExtension, String dir, Integer numberOfLines)
			throws IOException {
		File inputFile = new File(String.format("%s%s%s%s", dir, File.separator, fileName, fileExtension));

		int lineCount = 1;
		int fileCount = 1;
		List<String> lines = new ArrayList<>();

		String line = "";

		FileInputStream fileInputStream = new FileInputStream(inputFile);
		InputStreamReader inputStreamFileReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
		BufferedReader inputBufferedReader = new BufferedReader(inputStreamFileReader);

		while ((line = inputBufferedReader.readLine()) != null) {
			if ((lineCount == 1 && fileCount == 1)) {
				lineCount++;
				continue;
			}

			if (lineCount == numberOfLines) {
				lineCount = 0;
				FileUtils.writeLines(new File(
						String.format("%s%s%s_partition%d%s", dir, File.separator, fileName, fileCount, fileExtension)),
						lines);
				lines = new ArrayList<String>();
				fileCount++;
			}
			lines.add(line);
			lineCount++;
		}

		if (!lines.isEmpty()) {
			FileUtils.writeLines(new File(
					String.format("%s%s%s_partition%d%s", dir, File.separator, fileName, fileCount, fileExtension)),
					lines);
		}

		try {
			if (inputBufferedReader != null) {
				inputBufferedReader.close();
			}
		} catch (final IOException ioe) {
			// ignore
		}
	}

}
