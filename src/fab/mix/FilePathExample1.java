package fab.mix;

import java.io.File;
import java.io.IOException;

public class FilePathExample1 {
	
	public static void run() {
	  try {

		String filename = "newFile.txt";
		String workingDirectory = System.getProperty("user.dir");
			
		//****************//
			
		String absoluteFilePath = "";
			
		//absoluteFilePath = workingDirectory + System.getProperty("file.separator") + filename;
		absoluteFilePath = workingDirectory + File.separator + filename;

		System.out.println("Final filepath : " + absoluteFilePath);
			
		//****************//
			
		File file = new File(absoluteFilePath);

		if (file.createNewFile()) {
			System.out.println("File is created!");
		} else {
			System.out.println("File is already existed!");
		}

	  } catch (IOException e) {
		e.printStackTrace();
	  }
	}
}