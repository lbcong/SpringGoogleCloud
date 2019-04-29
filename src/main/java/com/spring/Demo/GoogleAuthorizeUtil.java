package com.spring.Demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.spring.Demo.WriteFile;

public class GoogleAuthorizeUtil {
	private final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private java.io.File DATA_STORE_DIR = null;

	/** Global instance of the {@link FileDataStoreFactory}. */
	private FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

	public Credential authorize() throws IOException, GeneralSecurityException {
		String p =DemoApplication.class.getClassLoader().getResource("sheets.googleapis.com-java-quickstart/StoredCredential").getPath();
		DATA_STORE_DIR = new java.io.File(p);
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}

		InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream("/google-sheets-client-secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("online").build();

		Credential credential = flow.loadCredential("user");
		if (credential != null && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() == null
				|| credential.getExpiresInSeconds() > 60)) {
			return credential;
		} else {
			GetTextFromGit git = new GetTextFromGit();

			List<String> rs = git.getStringFromGithubRaw(
					"https://raw.githubusercontent.com/lbcong/SpringGoogleCloud/master/src/main/resources/temp.txt");

			String StringCode = rs.get(0);
			LocalServerReceiver localReceiver = null;
			try {
				localReceiver = new LocalServerReceiver.Builder().setPort(46423).setHost("localhost").build();

				TokenResponse response = flow.newTokenRequest(StringCode).setRedirectUri(localReceiver.getRedirectUri())
						.execute();
				credential = flow.createAndStoreCredential(response, "user");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				localReceiver.stop();
			}
			return credential;
		}

	}

	public Sheets getSheetsService() throws IOException, GeneralSecurityException {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

}
