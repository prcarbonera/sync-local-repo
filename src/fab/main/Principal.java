package fab.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fab.files.FileHandler;
import fab.files.Filewalker;

public class Principal {

	private static List<String> exceptList = null;
	private static String origem;
	private static String destino;
	private static String modo;
	private static boolean excluir;
	
	public static void main(String[] args) {
		
		modo = "normal";
		excluir = false;
		
		for (int i = 0 ; i < args.length ; i++) {
			if (args[i].equalsIgnoreCase("reverso")) modo = "reverso";
			if (args[i].equalsIgnoreCase("-r")) modo = "reverso";
			if (args[i].equalsIgnoreCase("del")) excluir = true;
			if (args[i].equalsIgnoreCase("-d")) excluir = true;
		}
		
		checkCopy();
		
		//inverte o modo
		if (modo.equalsIgnoreCase("normal")) modo = "reverso";
		else modo = "normal";
		
		checkDel();
		
	}
	
	private static void checkCopy() {
		int copiados = 0;
		int erros = 0;
		int mantidos = 0;
		int ignorados = 0;
		
		System.out.println(">>>>>>>>> Iniciando check para copia");
		System.out.println(">>>>>>>>> ");
		
		carregaConfigFile();
		
		Filewalker fw = new Filewalker();
		List<String> filesNames = new ArrayList<String>();
		fw.walk(origem, filesNames);
		
		for (String fileName : filesNames) {
			if (checkExceptionFile(fileName)) {
				ignorados++;
			} else {
				String destinoName = fileName.replace(origem, destino);
				int copy = FileHandler.copy(fileName, destinoName);
				if (copy == 1) copiados++;
				if (copy == 0) mantidos++;
				if (copy == -1) erros++;
			}
		}
		System.out.println(">>>>>>>>> ");
		System.out.println("> Arquivos copiados : " + copiados);
		System.out.println("> Arquivos mantidos : " + mantidos);
		System.out.println("> Erros ao copiar   : " + erros);
		System.out.println("> Arquivos ignorados: " + ignorados);
		System.out.println(">>>>>>>>> Finalizado");
	}
	
	private static void checkDel() {
		System.out.println(">>>>>>>>> Iniciando check para exclusão");
		System.out.println(">>>>>>>>> ");
		System.out.println(">> Origem: " + origem);
		System.out.println(">> Destino: " + destino);

		exceptList = null;
		carregaConfigFile();
		Filewalker fw = new Filewalker();
		List<String> filesNames = new ArrayList<String>();
		fw.walk(origem, filesNames);
		
		int excluidos = 0;
		int ignorados = 0;
		int erros = 0;
		for (String fileName : filesNames) {
			if (checkExceptionFile(fileName)) {
				ignorados++;
			} else {
				String destinoName = fileName.replace(origem, destino);
				int check = FileHandler.checkForDelete(fileName, destinoName, excluir);
				if (check == 1) excluidos++;
				if (check == -1) erros++;
			}
		}
		System.out.println(">>>>>>>>> ");
		if (excluir) 
		System.out.println("> Arquivos excluidos        : " + excluidos);
		else 
		System.out.println("> Arquivos a serem excluidos: " + excluidos);
		System.out.println("> Erros ao excluir          : " + erros);
		System.out.println("> Arquivos ignorados        : " + ignorados);
		System.out.println(">>>>>>>>> Finalizado");		
	}
	
	private static boolean checkExceptionFile(String fileName) {
		for (String excf : exceptList) {
			if (fileName.contains(excf)) {
				return true;
			}
		}
		return false;
	}

	private static void carregaConfigFile() {
		String fileConfig = "";
		if (modo.equalsIgnoreCase("normal")) fileConfig = "configOrigin.txt";
		if (modo.equalsIgnoreCase("reverso")) fileConfig = "configReverso.txt";
		System.out.println("MODO: " + modo);
		ArrayList<String> lista = new ArrayList<String>();
		String workingDirectory = System.getProperty("user.dir");
		String fileName = workingDirectory + File.separator + File.separator + fileConfig;
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				if (!sCurrentLine.startsWith("#")) {
					if (sCurrentLine.startsWith("$")) {
						if (sCurrentLine.contains("ORIGEM")) {
							origem = sCurrentLine.replace("$ORIGEM=", "");
						}
						if (sCurrentLine.contains("DESTINO")) {
							destino = sCurrentLine.replace("$DESTINO=", "");
						}
					} else if(sCurrentLine != null && !sCurrentLine.trim().isEmpty()) {
						lista.add(sCurrentLine);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		exceptList = lista;
	}

}
