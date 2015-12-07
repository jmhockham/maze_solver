package mazeTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class MazeTestMain {

	private static final String INPUT_TEXT_FILE = "large_input.txt";

	public static void main(String[] args) {

		ArrayList<String> inputFileLines = getInputFileLines();

		CreatePathCommand cpc = new CreatePathCommand(inputFileLines);
		cpc.createPath();
		cpc.printPath();
	}

	private static ArrayList<String> getInputFileLines() {
		BufferedReader reader = null;
		ArrayList<String> fileLines = new ArrayList<String>();

		Path filePath = new File(INPUT_TEXT_FILE).toPath();
		System.out.println("reading input file: " + filePath);

		try {
			Charset charset = Charset.forName("US-ASCII");
			reader = Files.newBufferedReader(filePath, charset);
			String line = "";
			while ((line = reader.readLine()) != null) {
				fileLines.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileLines;
	}
}
