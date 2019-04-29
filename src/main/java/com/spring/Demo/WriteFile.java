package com.spring.Demo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class WriteFile {
	public static void writeFile(List<String> contents, String path) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(path);
		// OutputStreamWriter BufferedWriter dung de ghi string
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		for (int i = 0; i < contents.size(); i++) {
            bufferedWriter.write(contents.get(i));
            bufferedWriter.newLine();
        }
		bufferedWriter.close();
	}
}
