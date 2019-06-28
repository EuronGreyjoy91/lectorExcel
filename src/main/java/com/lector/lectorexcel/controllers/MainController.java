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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		    HttpServletResponse response, MultipartFile file) throws IOException, EncryptedDocumentException, InvalidFormatException, ParseException {
			
		File excel = new File(request.getRealPath("") + "/" + new Date().getTime() + file.getOriginalFilename());
		file.transferTo(excel);
		Workbook workbook2222 = WorkbookFactory.create(excel);
		Sheet sheet = workbook2222.getSheetAt(0);

		System.out.print(sheet.getPhysicalNumberOfRows());
		
		DataFormatter dataFormatter = new DataFormatter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		Integer totalFilas = sheet.getPhysicalNumberOfRows() - 1;
		Integer totalContactos = 0;
		List<DatosCliente> listaDatosCliente = new ArrayList<DatosCliente>();
		DatosCliente datosCliente;
		
		for (Row row : sheet) {
			
			if(row.getRowNum() == 0)
				continue;
			
			//FECHA
			Cell cell = row.getCell(1);
			
			Date parsedDate = formatter.parse(dataFormatter.formatCellValue(cell));
			c.setTime(parsedDate);
			
			String fecha = (c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH))
							+ "-" + 
							((c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1));
			
			//TRATAMIENTO
			cell = row.getCell(7);
			String tratamiento = cell.getStringCellValue();
			
			//NOMBRE
			cell = row.getCell(12);
			String nombre = cell.getStringCellValue();
			nombre = nombre.replaceAll("[^A-Za-z0-9\\s\\ñ]", "");
			nombre = capitalize(nombre);
			
			//TELEFONO
			cell = row.getCell(13);
			String tel = cell.getStringCellValue();
			tel = tel.replaceAll("\\+", "");
			
			if(tel.length() >= 12){
				if(tel.startsWith("54"))
					tel = tel.substring(2);
			}
			
			//ORIGEN
			cell = row.getCell(11);
			String origen = cell.getStringCellValue().equals("ig") ? "Instagram" : "Facebook";
			
			//MAIL
			cell = row.getCell(14);
			String mail = cell.getStringCellValue();
			
			//IMPRESION
//			System.out.println(fecha + " * " + tratamiento + " * " + nombre + " * " + tel + " * " + origen);
			totalContactos++;
			
			datosCliente = new DatosCliente();
			datosCliente.setFecha(fecha);
			datosCliente.setNombre(nombre);
			datosCliente.setObservacion("-");
			datosCliente.setOrigen(origen);
			datosCliente.setTelefono(tel);
			datosCliente.setTratamiento(tratamiento);
			datosCliente.setMail(mail);
			
			listaDatosCliente.add(datosCliente);
			
//			sdf.format(new Date(dataFormatter.formatCellValue(cell)));
		}
		
//		System.out.println("Total filas: " + totalFilas);
//		System.out.println("Total clientes: " + totalContactos);
		
		
		try {
			
			Calendar d = Calendar.getInstance();
			
			String fileName = "contactos"+new Date().getTime()+".xls";
	        HSSFWorkbook workbook2 = new HSSFWorkbook();
	        HSSFSheet sheet2 = workbook2.createSheet("CONTACTOS");
	        
	        CellStyle cs = workbook2.createCellStyle();
	        cs.setFillForegroundColor(IndexedColors.PINK.getIndex());
	        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	
	        Integer rowNumber = 0;
	        Integer cellNumber = 0;
	        
	        HSSFRow row = sheet2.createRow(rowNumber);
	        Cell cell = row.createCell(0);
	        cell.setCellStyle(cs);
	        cell.setCellValue("FECHA");
	        
	        cell = row.createCell(1);
	        cell.setCellStyle(cs);
	        cell.setCellValue("TRATAMIENTO");
	        
	        cell = row.createCell(2);
	        cell.setCellStyle(cs);
	        cell.setCellValue("NOMBRE");
	        
	        cell = row.createCell(3);
	        cell.setCellStyle(cs);
	        cell.setCellValue("TELEFONO");
	        
	        cell = row.createCell(4);
	        cell.setCellStyle(cs);
	        cell.setCellValue("ORIGEN");
	        
	        cell = row.createCell(5);
	        cell.setCellStyle(cs);
	        cell.setCellValue("OBSERVACIONES");
	        
	        rowNumber++;
	        
	        for(DatosCliente dt : listaDatosCliente) {
	        	cellNumber = 0;
	        	
	        	row = sheet2.createRow(rowNumber);
		        row.createCell(cellNumber++).setCellValue(dt.getFecha());
		        row.createCell(cellNumber++).setCellValue(dt.getTratamiento());
		        row.createCell(cellNumber++).setCellValue(dt.getNombre() != null && !dt.getNombre().equals("") ? dt.getNombre() : dt.getMail());
		        row.createCell(cellNumber++).setCellValue(dt.getTelefono());
		        row.createCell(cellNumber++).setCellValue("Facebook");
		        row.createCell(cellNumber++).setCellValue(dt.getObservacion());
	        	
		        rowNumber++;
	        }
	
	        autoSizeColumns(workbook2);
	        
	        FileOutputStream fileOut = new FileOutputStream(fileName);
	        workbook2.write(fileOut);
	      
	        fileOut.close();
	        System.out.println("Your excel file has been generated!");
	
	        //Code to download
	        File fileToDownload = new File(fileName);
	        InputStream in = new FileInputStream(fileToDownload);
	
	        // Gets MIME type of the file
	        String mimeType = new MimetypesFileTypeMap().getContentType(fileName);
	
	        if (mimeType == null) {
	            // Set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	        System.out.println("MIME type: " + mimeType);
	
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
	
	        System.out.println("File downloaded at client successfully");
	    } catch (Exception ex) {
	        System.out.println(ex);
	    }
		
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
}
