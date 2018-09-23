package com.codesurfers.xpto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
}
