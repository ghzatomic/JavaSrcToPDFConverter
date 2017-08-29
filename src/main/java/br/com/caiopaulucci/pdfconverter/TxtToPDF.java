package br.com.caiopaulucci.pdfconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class TxtToPDF {

	public static boolean convertTextToPDF(File fileOrigem, File fileDestino) throws Exception {
		System.out.println(fileDestino.getAbsolutePath());
		BufferedReader br = null;

		try {
			String fileDestinoCompletePathPDF = "";
			int i = fileDestino.getAbsolutePath().lastIndexOf('.');
			if (i > 0) {
				fileDestinoCompletePathPDF = fileDestino.getAbsolutePath().substring(0,i + 1) + "pdf";
			} else {
				fileDestinoCompletePathPDF = fileDestino.getAbsolutePath() + ".pdf";
			}

			Document pdfDoc = new Document(PageSize.A4);
			// String output_file = fileOrigin.getName().replace(".txt",
			// ".pdf").replace(".java", ".pdf").replace(".html",
			// ".pdf").replace(".js", ".pdf").replace(".py", ".pdf");
			PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileDestinoCompletePathPDF))
					.setPdfVersion(PdfWriter.VERSION_1_7);

			pdfDoc.open();

			Font myfont = new Font();
			myfont.setStyle(Font.NORMAL);
			myfont.setSize(11);

			pdfDoc.add(new Paragraph("\n"));

			if (fileOrigem.exists()) {

				br = new BufferedReader(new FileReader(fileOrigem));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					Paragraph para = new Paragraph(strLine + "\n", myfont);
					// para.setAlignment(Element.ALIGN_JUSTIFIED);
					pdfDoc.add(para);
				}
			} else {
				System.out.println("no such file exists!");
				return false;
			}
			pdfDoc.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				br.close();
		}

		return true;
	}

	public static boolean convertFolderTextToPDF(String folder, String folderDest) throws Exception {
		folder = folder.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"));
		folderDest = folderDest.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"));

		String folderRepl = folder.replaceAll("\\\\|/", File.pathSeparator);
		String folderDestRepl = folderDest.replaceAll("\\\\|/", File.pathSeparator);

		File folderOrigem = new File(folder);
		File folderDestFileCreation = new File(folderDest);
		if (folderOrigem.exists()) {
			if (folderOrigem.isDirectory()) {
				if (!folderDestFileCreation.exists()) {
					folderDestFileCreation.mkdirs();
				}
				for (File file : folderOrigem.listFiles()) {
					String strFolderDestFile = (folderDestRepl + file.getAbsolutePath()
							.replaceAll("\\\\|/", File.pathSeparator).replaceAll(folderRepl, ""))
									.replace(File.pathSeparator, "\\\\");
					File folderDestFile = new File(strFolderDestFile);
					if (strFolderDestFile.endsWith(".git")||
							strFolderDestFile.endsWith(".settings")||
							strFolderDestFile.endsWith(".project")||
							strFolderDestFile.endsWith(".mvn")||
							strFolderDestFile.endsWith("node_modules")||
							strFolderDestFile.endsWith("node")||
							strFolderDestFile.endsWith(".idea")||
							strFolderDestFile.endsWith(".classpath")||
							strFolderDestFile.endsWith("target")||
							strFolderDestFile.endsWith("lib")||
							strFolderDestFile.endsWith(".gitignore")){
						continue;
					}
					if (strFolderDestFile.endsWith("Dockerfile")||
						strFolderDestFile.endsWith("Dockerfile~")){
						convertTextToPDF(file, new File(strFolderDestFile.replace("\\\\", "\\")));
					}else{
						File fileFolderDest = new File(strFolderDestFile);
						if (file.isDirectory()) {
							if (!fileFolderDest.exists()) {
								fileFolderDest.mkdirs();
							}
							convertFolderTextToPDF(file.getAbsolutePath(), strFolderDestFile.replace("\\\\", "\\"));
						} else {
							convertTextToPDF(file, new File(strFolderDestFile.replace("\\\\", "\\")));
						}
					}
				}
			} else {
				convertTextToPDF(folderOrigem, new File(folderDest));
			}
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		TxtToPDF.convertFolderTextToPDF(
				"E:/Desenvolvimento/Python/Itau/ocr-confidence",
				"E:/Desenvolvimento/Python/Itau/ocr-confidencePDF"
				/*"E:/Desenvolvimento/WorkspaceFinch/WorkspaceItau/ItauProject",
				"E:/Desenvolvimento/WorkspaceFinch/WorkspaceItau/ItauProjectPDF"*/
				/*"E:/Desenvolvimento/WorkspaceXGracco/X-Gracco",
				"E:/Desenvolvimento/WorkspaceXGracco/X-GraccoPDF"*/);
	}

}
