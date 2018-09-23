package com.codesurfers.xpto.tasklets;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class FileTaskletTest {

	private FileTasklet tasklet = new FileTasklet();

	private StepContribution stepContribution;

	private ChunkContext chunkContext;

	private static final String WORKING_DIRECTORY = "src/test/resources/";
	private static final String FILENAME = "transacoes";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8090); // No-args constructor

	private File workingDirectoryFolder;

	@Before
	public void setUp() {
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
		this.stepContribution = stepExecution.createStepContribution();
		this.chunkContext = new ChunkContext(new StepContext(stepExecution));
		workingDirectoryFolder = new File(WORKING_DIRECTORY);
	}

	@Test
	public void executeTasklet() throws IOException {
		stubFor(get(urlMatching("/.*")).willReturn(aResponse().withStatus(200).withBodyFile("transacoes.zip")));

		tasklet.setHackathonFileUrl("http://localhost:8090/transacoes.zip");
		tasklet.setWorkingDirectory(WORKING_DIRECTORY);
		tasklet.setFileName(FILENAME);
		tasklet.setFileExtension(".csv");
		tasklet.setFilePath(String.format("%s%s", FILENAME, ".csv"));
		tasklet.setZipFilePath(String.format("%s%s", FILENAME, ".zip"));
		tasklet.setNumberOfLines(4);

		try {
			tasklet.execute(stepContribution, chunkContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File[] files = workingDirectoryFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.matches(String.format("%s_part.*", FILENAME));
			}

		});

		Assert.assertEquals(4, files.length);
	}

	@After
	public void tearDown() {
		File[] files = workingDirectoryFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.matches(String.format("%s.*", FILENAME));
			}

		});

		for (final File file : files) {
			if (!file.delete()) {
				System.err.println("Nao foi possivel remover o arquivo " + file.getAbsolutePath());
			}
		}

		Assert.assertFalse(files.length == 0);
	}
}
