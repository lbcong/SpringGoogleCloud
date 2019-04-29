package com.spring.Demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.spring.Demo.GoogleAuthorizeUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.spring.Demo.ReadFile;

@SpringBootApplication
@RestController
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "hello world!";
	}

	@GetMapping("/test")
	public String test() {

		return System.getProperty("user.home") + "--" + System.getProperty("user.dir");
	}

	@GetMapping("/uploadget")
	public String uploadget() {

		return "<form method='POST' action='/uploadpost' enctype='multipart/form-data'>"
				+ "<input type='file' name='file' /><br/><br/>" + "<input type='submit' value='Submit' />" + "</form>";
	}

	@PostMapping("/uploadpost")
	public String uploadpost(@RequestParam("file") MultipartFile file) throws IOException {

		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(System.getProperty("user.home") + "\\" + file.getOriginalFilename());
			Files.write(path, bytes);
			return "ok";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	@GetMapping("/getUrlAuthorize")
	public String test2() {

		List<String> rs = ReadFile.readFile(System.getProperty("user.home") + "\\temp.txt");
		String temp = "";
		if (rs != null) {
			for (int i = 0; i < rs.size(); i++) {
				temp += rs.get(i) + "\n";
			}
		}
		return temp;
	}

	@GetMapping("/getSheet")
	public String getSheet() throws IOException, GeneralSecurityException {
		Sheets service = GoogleAuthorizeUtil.getSheetsService();

		// Prints the names and majors of students in a sample spreadsheet:
		// https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		String temp = "";
		String spreadsheetId = "1I-575yBsH2xoAcEebKAvW19fo0KbO00xaG1ljxk2AEg";
		String range = "A1:E";
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		List<List<Object>> values = response.getValues();
		if (values == null || values.size() == 0) {
			System.out.println("No data found.");
		} else {
			System.out.println("Name, Major");
			for (List row : values) {
				// Print columns A and E, which correspond to indices 0 and 4.
				System.out.printf("%s, %s\n", row.get(0), row.get(0));
				temp = row.get(0).toString();
			}
		}
		return "hello world!" + temp;
	}

}
