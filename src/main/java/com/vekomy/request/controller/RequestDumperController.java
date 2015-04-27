package com.vekomy.request.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


@Controller
public class RequestDumperController {

	@RequestMapping(value = "/dumpRequest", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody Object getAllProfileTypes(@RequestBody String str) {

		try {
			parseJson(new JSONObject(str.trim()));

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public void parseJson(JSONObject json) throws Exception {

		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");
		String formattedDate = df2.format(new Date());
		String pathToWrite = System.getenv("CATALINA_BASE");

		if (pathToWrite == null || pathToWrite.isEmpty()) {
			pathToWrite = System.getProperty("java.io.tmpdir");
		} else {
			pathToWrite = pathToWrite + File.separator + "logs";
		}

		File file = new File(pathToWrite + File.separator + formattedDate
				+ ".xlsx");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
													// exceeding rows will be
													// flushed to disk
		Sheet sh = wb.createSheet();
		
		Row row = sh.createRow(++rownum);
		handleRow(sh, row, json, 0);

		FileOutputStream out = new FileOutputStream(file);
		wb.write(out);

		out.close();

		// dispose of temporary files backing this workbook on disk
		wb.dispose();
		wb.close();

	}

	int rownum = -1;

	private void handleRow(Sheet sh, Row row, JSONObject json, int column) {
		for (Object key : json.keySet()) {
			String strKey = key.toString();
			Object value = json.get(strKey);
			if (value instanceof JSONObject) {
				Cell cell = row.createCell(column);
				cell.setCellValue(strKey);
//				System.out.println(new CellReference(cell).formatAsString()
//						+ "-----" + strKey);
				handleRow(sh, row, (JSONObject) value, column + 1);
				row = sh.createRow(rownum);
			} else {
				Cell cell = row.createCell(column);
				cell.setCellValue(strKey);
//				System.out.println(new CellReference(cell).formatAsString()
//						+ "-----" + strKey);
				Cell cell2 = row.createCell(column + 1);
				cell2.setCellValue(value.toString());
//				System.out.println(new CellReference(cell2).formatAsString()
//						+ "-----" + value.toString());
				row = sh.createRow(++rownum);
			}
		}
	}

//	public static void main(String[] args) {
//		RequestDumperController rdc = new RequestDumperController();
//		try {
//			JSONObject json = new JSONObject(
//					"{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}");
//			rdc.parseJson(json);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
