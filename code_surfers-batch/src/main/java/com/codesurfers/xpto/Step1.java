package com.codesurfers.xpto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Step1 {

	@Value("${hackathon.zipfile.url}")
	public String hackathonFileUrl;

	@Value("${hackathon.working.directory}")
	public String workingDirectory;

	@Value("${hackathon.file.name}")
	public String fileName;

	@Value("${hackathon.file.extension}")
	public String fileExtension;

	@Value("${hackathon.csvfile.path}")
	public String filePath;

	@Value("${hackathon.zipfile.path}")
	public String zipFilePath;

	@Value("${partitioner.lines.per.file}")
	private Integer numberOfLines;

	private File zipFile;
	
	@Bean
	public Tasklet baixarEDescompactarArquivo() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Baixar e descompactar");

				try {
					// TODO Verificar se existe uma possibilidade melhor.
					trustAllCertsManager();
					
					zipFile = new File(String.format("%s%s%s", workingDirectory, File.separator, zipFilePath));
					FileUtils.copyURLToFile(new URL(hackathonFileUrl), zipFile);
					
					unzipFile();
					partitionateFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return RepeatStatus.FINISHED;
			}
		};
	}

	/**
	 * Descompacta o arquivo baixado
	 * 
	 * @throws IOException
	 */
	public void unzipFile() throws IOException {
		if(zipFile != null) {
			byte[] buffer = new byte[1024];
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.getPath()));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				File newFile = new File(String.format("%s%s%s", workingDirectory, File.separator, filePath));
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		}
		
	}

	/**
	 *  
	 */
	private void trustAllCertsManager() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {

		}
	}

	// TODO Refatorar metodo
	private void partitionateFile() throws IOException {

		File inputFile = new File(String.format("%s%s%s", workingDirectory, File.separator, filePath));

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
				FileUtils.writeLines(new File(String.format("%s%s%s_partition%d%s", workingDirectory,File.separator,
						fileName, fileCount, fileExtension)), lines);
				lines = new ArrayList<String>();
				fileCount++;
			}
			lines.add(line);
			lineCount++;
		}

		if (!lines.isEmpty()) {
			FileUtils.writeLines(new File(String.format("%s%s%s_partition%d%s", workingDirectory,File.separator,
					fileName, fileCount, fileExtension)), lines);
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
