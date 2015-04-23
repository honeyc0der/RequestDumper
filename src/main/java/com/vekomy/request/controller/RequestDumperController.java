package com.vekomy.request.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vekomy.request.util.JsonParserUtil;

@Controller
public class RequestDumperController {

	@RequestMapping(value = "/dumpRequest", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody Object getAllProfileTypes(@RequestBody String str) {

		FileOutputStream fop = null;
		try {
			Object obj = null;
			if (str.trim().startsWith("{")) {
				JSONObject json = new JSONObject(str.trim());
				obj = JsonParserUtil.jsonToMap(json);
			} else if (str.trim().startsWith("[")) {
				JSONArray json = new JSONArray(str.trim());
				obj = JsonParserUtil.toList(json);
			} else {
				obj = str.trim();
			}

			System.out.println(System.getProperty("java.io.tmpdir"));

			DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");
			String formattedDate = df2.format(new Date());

			File file = new File(System.getProperty("java.io.tmpdir")
					+ File.separator + formattedDate + ".txt");

			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = obj.toString().getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("Done");
			System.out.println("absolute path:" + file.getAbsolutePath());

			return obj;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fop;
	}

}
