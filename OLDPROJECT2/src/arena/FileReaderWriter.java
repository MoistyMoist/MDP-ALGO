package arena;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileReaderWriter {

	// private InputStreamReader input;
	private OutputStreamWriter output;
	private Path file;
	private static String ENCODER = "UTF-8";
	// private static BufferedReader
	private BufferedReader input;

	public FileReaderWriter(Path dir) throws IOException {
		file = dir;
		if (!Files.exists(dir)) {
			Files.createDirectories(file.getParent());
			Files.createFile(file);
		}
	}

	public void fileWriter(String str) throws IOException {
		OutputStream fout = Files.newOutputStream(file, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		output = new OutputStreamWriter(fout, ENCODER);
		output.write(str);
		output.close();
	}

	public String fileReader() throws IOException {
		InputStream fin = Files.newInputStream(file);
		input = new BufferedReader(new InputStreamReader(fin, ENCODER));

		String str = "";
		String line;

		while ((line = input.readLine()) != null) {
			str += line;
		}
		input.close();
		return str;
	}
}
