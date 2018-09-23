package com.codesurfers.xpto.tasklets;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
import org.springframework.stereotype.Component;

import com.codesurfers.xpto.util.MyFileUtils;

@Component
public class FileTasklet implements Tasklet {
	@Value("${hackathon.zipfile.url}")
	private String hackathonFileUrl;

	@Value("${hackathon.working.directory}")
	private String workingDirectory;

	@Value("${hackathon.file.name}")
	private String fileName;

	@Value("${hackathon.file.extension}")
	private String fileExtension;

	@Value("${hackathon.csvfile.path}")
	private String filePath;

	@Value("${hackathon.zipfile.path}")
	private String zipFilePath;

	@Value("${partitioner.lines.per.file}")
	private Integer numberOfLines;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String pathToZip = String.format("%s%s%s", workingDirectory, File.separator, zipFilePath);
		String pathToFile = String.format("%s%s", workingDirectory, File.separator);
		try {
			trustAllCertsManager();

			File zipFile = new File(pathToZip);

			FileUtils.copyURLToFile(new URL(hackathonFileUrl), zipFile);

			MyFileUtils.unzipFile(pathToZip, pathToFile);
			MyFileUtils.partitionFile(fileName, fileExtension, workingDirectory, numberOfLines);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return RepeatStatus.FINISHED;
	}

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

	public void setHackathonFileUrl(String hackathonFileUrl) {
		this.hackathonFileUrl = hackathonFileUrl;
	}

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public void setNumberOfLines(Integer numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

}
