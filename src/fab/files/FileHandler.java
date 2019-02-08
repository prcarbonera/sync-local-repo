package fab.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHandler {
	
	public static int copy(String origem, String destino) {
		File of = new File(origem);
		File df = new File(destino);
		// sai se destino igual à origem
		if (checkFilesEquals(of, df)) {
			//System.out.println("=> Não copiei arquivo: " + origem);
			//System.out.println(destino +  " <= Existe idêntico no destino");
			return 0;
		}
		// copia aruivo
		try {
			// cria diretório destino se não existe
			String pathDestino = getPath(destino);
			File dPath = new File(pathDestino);
			if (!dPath.isDirectory() || !dPath.exists()) {
				dPath.mkdirs();
				System.out.println("=> criei o diretório: " + pathDestino);
			}
			//
			Files.copy(of.toPath(), df.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("=> copiei o arquivo: " + origem + " - para => " + destino);
			return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRO ao copiar arquivo: " + origem);
			System.out.println(e.toString());
			return -1;
		}
		
	}
	
	public static int checkForDelete(String origem, String destino, boolean excluir) {
		// TODO Auto-generated method stub
		File of = new File(origem);
		File df = new File(destino);
		if (!df.exists()) {
			if (excluir) {
				boolean delete = of.delete();
				if (delete) {
					System.out.println("=> arquivo excluido: " + origem);
					return 1;
				} else {
					System.out.println("=> NAO excluido (erro): " + origem);
					return -1;
				}
			} else {
				System.out.println("=> excluir arquivo: " + origem);
				return 1;
			}
		}
		return 0;
	}	


	private static String getPath(String fileName) {
		int lastIndexOf = fileName.lastIndexOf(File.separator);
		String path = fileName.substring(0, lastIndexOf + 1);
		return path;
	}

	private static boolean checkFilesEquals(File a1, File a2) {
		String hash1 = getHashFile(a1);
		String hash2 = getHashFile(a2);
		if (hash1.equals(hash2)) return true;
		return false;
	}

    private static String getHashFile (File arq) {
        String s = "error";
        try {
            MessageDigest dgst = MessageDigest.getInstance ("SHA1");
            FileInputStream fis = new FileInputStream (arq); 
            byte[] buffer = new byte[20480];
            int nBytes;
            dgst.reset();
            while ((nBytes = fis.read (buffer)) > 0) {
                dgst.update (buffer, 0, nBytes);
            }
            byte[] bytes = dgst.digest();
            fis.close();
            BigInteger bd = new BigInteger (bytes);
            s = bd.toString (16);
        } catch (NoSuchAlgorithmException ex) {
        } catch (IOException ex) {
        }
        return s;
    }

}
