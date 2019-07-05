package com.lector.lectorexcel.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lector.lectorexcel.DatosCliente;

@Controller
public class MainController {

	@GetMapping(value={"", "/", "/formulario"})
	public String formulario() {
		return "formulario";
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping(value = "/procesar")
	public String procesar(HttpServletRequest request, 
		    HttpServletResponse response, MultipartFile file,
		    @RequestParam (required = false, value = "mail") String emailBuscado,
		    @RequestParam (required = false, value = "nombre") String nombreBuscado,
		    @RequestParam (required = false, value = "telefono") String telefonoBuscado,
		    @RequestParam (required = false, defaultValue = "false") Boolean contactosMexico,
		    @RequestParam (required = false, defaultValue = "false") Boolean incluirMail) 
		    		throws IOException, EncryptedDocumentException, InvalidFormatException, ParseException {
			
		File excelFolder = new File(request.getRealPath("") + "/excel");
		cleanFolder(excelFolder);
		
		File archivoOriginal = new File(request.getRealPath("") + "/excel/" + new Date().getTime() + file.getOriginalFilename()
				.replaceAll("á", "a")
				.replaceAll("é", "e")
				.replaceAll("í", "i")
				.replaceAll("ó", "o")
				.replaceAll("ú", "u")
				.replaceAll("Á", "A")
				.replaceAll("É", "E")
				.replaceAll("Í", "I")
				.replaceAll("Ó", "O")
				.replaceAll("Ú", "U"));
		
		file.transferTo(archivoOriginal);
		Workbook workbookOriginal = WorkbookFactory.create(archivoOriginal);
		Sheet sheet = workbookOriginal.getSheetAt(0);

		Calendar c = Calendar.getInstance();
		
		List<DatosCliente> listaDatosCliente = new ArrayList<DatosCliente>();
		DatosCliente datosCliente;
		
		Integer columnaNombre = null;
		Integer columnaTelefono = null;
		Integer columnaMail = null;
		
		Row primeraFila = sheet.getRow(0);
		
		Integer index = 0;
		
		for(Cell cell : primeraFila) {
			
			if(cell.getStringCellValue().equals("full_name"))
				columnaNombre = index;
			else if(cell.getStringCellValue().equals("phone_number"))
				columnaTelefono = index;
			else if(cell.getStringCellValue().equals("email"))
				columnaMail = index;
			
			index++;
		}
		
		for (Row row : sheet) {
			Cell cell;
			if(row.getRowNum() == 0)
				continue;
			
			String fecha = (c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH))
							+ "-" + 
							((c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1));
			
			//TRATAMIENTO
			String tratamiento = "Clientes Potenciales";
			
			//NOMBRE
			cell = row.getCell(columnaNombre);
			String nombre = cell.getStringCellValue();
			nombre = nombre
						.replaceAll("á", "a")
						.replaceAll("é", "e")
						.replaceAll("í", "i")
						.replaceAll("ó", "o")
						.replaceAll("ú", "u")
						.replaceAll("Á", "A")
						.replaceAll("É", "E")
						.replaceAll("Í", "I")
						.replaceAll("Ó", "O")
						.replaceAll("Ú", "U");
						
			nombre = nombre.replaceAll("[^A-Za-z0-9\\s\\ñ\\Ñ]", "");
			nombre = capitalize(nombre);
			
			//TELEFONO
			cell = row.getCell(columnaTelefono);
			String tel;
			
			if(cell.getCellTypeEnum() == CellType.STRING)
				tel = cell.getStringCellValue();
			else 
				tel = NumberToTextConverter.toText(cell.getNumericCellValue());
			
			if(contactosMexico) {
				tel = tel.replaceAll("p:", "");
				
				if(!tel.startsWith("+"))
					tel = "+" + tel;
				
				if(telefonoBuscado != null && !telefonoBuscado.equals("")) {
					telefonoBuscado = telefonoBuscado.replaceAll("p:", "");
					
					if(!telefonoBuscado.startsWith("+"))
						telefonoBuscado = "+" + telefonoBuscado;
				}
			}
			else {
				tel = tel.replaceAll("\\+", "").replaceAll("p:", "");
				
				if(tel.length() >= 12){
					if(tel.startsWith("549") || tel.startsWith("540"))
						tel = tel.substring(3);
					else if(tel.startsWith("54"))
						tel = tel.substring(2);
				}
				
				if(telefonoBuscado != null && !telefonoBuscado.equals("")) {
					telefonoBuscado = telefonoBuscado.replaceAll("\\+", "").replaceAll("p:", "");
					
					if(telefonoBuscado.length() >= 12){
						if(telefonoBuscado.startsWith("549") ||telefonoBuscado.startsWith("540"))
							telefonoBuscado = telefonoBuscado.substring(3);
						else if(telefonoBuscado.startsWith("54"))
							telefonoBuscado = telefonoBuscado.substring(2);
					}
				}
				
			}
			
			//MAIL
			cell = row.getCell(columnaMail);
			String mail = cell.getStringCellValue().toLowerCase();
			
//			if(emailBuscado != null && !emailBuscado.equals(""))
//				if(emailBuscado.toLowerCase().equals(mail))
//					break;
//			
//			if(nombreBuscado != null && !nombreBuscado.equals(""))
//				if(nombreBuscado.toLowerCase().trim().equals(nombre.toLowerCase().trim()))
//					break;
			
			if(telefonoBuscado != null && !telefonoBuscado.equals(""))
				if(telefonoBuscado.trim().equals(tel.trim()))
					break;
			
			datosCliente = new DatosCliente();
			datosCliente.setFecha(fecha);
			datosCliente.setNombre(nombre);
			datosCliente.setObservacion("-");
			datosCliente.setOrigen("Facebook");
			datosCliente.setTelefono(tel);
			datosCliente.setTratamiento(tratamiento);
			datosCliente.setMail(mail);
			
			listaDatosCliente.add(datosCliente);
		}
		
		try {
			Calendar d = Calendar.getInstance();
			cleanFolder(excelFolder);
			
			String fileName = request.getRealPath("") + "/excel/contactos"+(d.get(Calendar.YEAR) +"_"+d.get(Calendar.MONTH)+"_"+d.get(Calendar.DAY_OF_MONTH)+"_"+d.get(Calendar.HOUR_OF_DAY)+"_"+d.get(Calendar.MINUTE))+".xls";
	        HSSFWorkbook workbookFinal = new HSSFWorkbook();
	        HSSFSheet sheet2 = workbookFinal.createSheet("CONTACTOS");
	        
	        CellStyle cs = workbookFinal.createCellStyle();
	        cs.setFillForegroundColor(IndexedColors.PINK.getIndex());
	        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        cs.setAlignment(HorizontalAlignment.CENTER);
	        
	        CellStyle cs2 = workbookFinal.createCellStyle();
	        cs2.setAlignment(HorizontalAlignment.CENTER);
	        
	        Integer rowNumber = 0;
	        Integer cellNumber = 0;
	        
	        HSSFRow row = sheet2.createRow(rowNumber);
	        Cell cell = row.createCell(cellNumber++);
	        cell.setCellStyle(cs);
	        cell.setCellValue("FECHA");
	        
	        cell = row.createCell(cellNumber++);
	        cell.setCellStyle(cs);
	        cell.setCellValue("TRATAMIENTO");
	        
	        cell = row.createCell(cellNumber++);
	        cell.setCellStyle(cs);
	        cell.setCellValue("NOMBRE");
	        
	        cell = row.createCell(cellNumber++);
	        cell.setCellStyle(cs);
	        cell.setCellValue("TELEFONO");
	        
	        if(incluirMail) {
	        	cell = row.createCell(cellNumber++);
		        cell.setCellStyle(cs);
		        cell.setCellValue("EMAIL");
	        }
	        
	        cell = row.createCell(cellNumber++);
	        cell.setCellStyle(cs);
	        cell.setCellValue("ORIGEN");
	        
	        cell = row.createCell(cellNumber++);
	        cell.setCellStyle(cs);
	        cell.setCellValue("OBSERVACIONES");
	        
	        rowNumber++;
	        
	        Cell nuevaCelda;
	        
	        for(DatosCliente dt : listaDatosCliente) {
	        	cellNumber = 0;
	        	
	        	row = sheet2.createRow(rowNumber);
	        	
	        	nuevaCelda = row.createCell(cellNumber++);
	        	nuevaCelda.setCellValue(dt.getFecha());
	        	nuevaCelda.setCellStyle(cs2);
	        	
	        	nuevaCelda = row.createCell(cellNumber++);
	        	nuevaCelda.setCellValue(dt.getTratamiento());
	        	nuevaCelda.setCellStyle(cs2);
	        	
	        	nuevaCelda = row.createCell(cellNumber++);
	        	nuevaCelda.setCellValue(dt.getNombre() != null && !dt.getNombre().equals("") ? dt.getNombre() : dt.getMail());
	        	nuevaCelda.setCellStyle(cs2);
	        	
	        	nuevaCelda = row.createCell(cellNumber++);
	        	nuevaCelda.setCellValue(dt.getTelefono());
	        	nuevaCelda.setCellStyle(cs2);
	        	
		        if(incluirMail) {
		        	nuevaCelda = row.createCell(cellNumber++);
		        	nuevaCelda.setCellValue(dt.getMail());
		        	nuevaCelda.setCellStyle(cs2);
		        }
		        
		        nuevaCelda = row.createCell(cellNumber++);
	        	nuevaCelda.setCellValue("Facebook");
	        	nuevaCelda.setCellStyle(cs2);
	        	
	        	nuevaCelda = row.createCell(cellNumber++);
	        	nuevaCelda.setCellValue(dt.getObservacion());
	        	nuevaCelda.setCellStyle(cs2);
	        	
		        rowNumber++;
	        }
	
	        autoSizeColumns(workbookFinal);
	        
	        FileOutputStream fileOut = new FileOutputStream(fileName);
	        workbookFinal.write(fileOut);
	      
	        fileOut.close();
	
	        //Code to download
	        File fileToDownload = new File(fileName);
	        InputStream in = new FileInputStream(fileToDownload);
	
	        // Gets MIME type of the file
	        String mimeType = new MimetypesFileTypeMap().getContentType(fileName);
	
	        if (mimeType == null) {
	            // Set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	
	        // Modifies response
	        response.setContentType(mimeType);
	        response.setContentLength((int) fileToDownload.length());
	
	        // Forces download
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"", fileToDownload.getName());
	        response.setHeader(headerKey, headerValue);
	
	        // obtains response's output stream
	        OutputStream outStream = response.getOutputStream();
	
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	
	        while ((bytesRead = in.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	        
	        in.close();
	        outStream.close();
	    } catch (Exception ex) {
	        System.out.println(ex);
	    }
		
		cleanFolder(excelFolder);
		
		return "redirect:/formulario";
	}
	
	private String capitalize(final String texto) {
		String nuevoTexto = "";
		String[] palabras = texto.split(" ");
	
		for(String palabra : palabras)
			if(!palabra.equals(""))
				nuevoTexto += Character.toUpperCase(palabra.charAt(0)) + palabra.substring(1).toLowerCase() + " ";
		
		return nuevoTexto;
	}
	
	public void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	            Row row = sheet.getRow(sheet.getFirstRowNum());
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	            }
	        }
	    }
	}
	
	public static void cleanFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File f: files)
	        	if(!f.getName().equals("foo.txt"))
	        		f.delete();
	    }
	}
}
