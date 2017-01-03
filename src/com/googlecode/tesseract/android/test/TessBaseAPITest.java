/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.tesseract.android.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessBaseAPITest extends AndroidTestCase {
    @SuppressLint("SdCardPath")
    static String TESSBASE_PATH = "/mnt/sdcard/tesseract/";
    static String DEFAULT_LANGUAGE = "vie";
    static String TESSDATA_PATH = TESSBASE_PATH + "tessdata/";
    static String TESS_IMG = TESSBASE_PATH + "vietsample.jpg";

    protected void setUp() throws Exception {
        super.setUp();
        // Check that the data file(s) exist.
        for (String languageCode : DEFAULT_LANGUAGE.split("\\+")) {
            if (!languageCode.startsWith("~")) {
                File expectedFile = new File(TESSDATA_PATH + File.separator + 
                        languageCode + ".traineddata");
                assertTrue("Make sure that you've copied " + languageCode + 
                        ".traineddata to " + TESSDATA_PATH, expectedFile.exists());
            }
        }
    }

    @SmallTest
    public void testGetTextUTF8() {
        final Bitmap bmp = BitmapFactory.decodeFile(TESS_IMG);

        // Attempt to initialize the API.
        final TessBaseAPI baseApi = new TessBaseAPI();
        boolean success = baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        assertTrue(success);

        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_BLOCK);

        baseApi.setImage(bmp);
        long start = System.currentTimeMillis();
        Log.i("trongvu","start getUTF8Text");
        String text = baseApi.getUTF8Text();
        Log.i("trongvu","start getUTF8Text = " + (System.currentTimeMillis() - start) + " ms");
        assertNotNull("Recognized text is null.", text);
        writeToFile(TESSBASE_PATH + "normal.txt", text);
        // Attempt to shut down the API.
        baseApi.end();
        bmp.recycle();
    }

    @SmallTest
    public void testGetTextUTF8OpenMP() {
        final Bitmap bmp = BitmapFactory.decodeFile(TESS_IMG);

        // Attempt to initialize the API.
        final TessBaseAPI baseApi = new TessBaseAPI();
        boolean success = baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        assertTrue(success);

        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_BLOCK);

        baseApi.setImage(bmp);
        long start = System.currentTimeMillis();
        Log.i("trongvu","start getUTF8TextOpenMp");
        String text = baseApi.getUTF8TextOpenMp();
        Log.i("trongvu","start getUTF8TextOpenMp = " + (System.currentTimeMillis() - start) + " ms");

        assertNotNull("Recognized text is null.", text);
        writeToFile(TESSBASE_PATH + "openmp.txt", text);
        // Attempt to shut down the API.
        baseApi.end();
        bmp.recycle();
    }
    
    private void writeToFile(String filename, String content){
    	try {
    		File fileDir = new File(filename);
    			
    		Writer out = new BufferedWriter(new OutputStreamWriter(
    			new FileOutputStream(fileDir), "UTF8"));
    		out.append(content);
    		out.flush();
    		out.close();
    	        
    	    } 
    	   catch (UnsupportedEncodingException e) 
    	   {
    		System.out.println(e.getMessage());
    	   } 
    	   catch (IOException e) 
    	   {
    		System.out.println(e.getMessage());
    	    }
    	   catch (Exception e)
    	   {
    		System.out.println(e.getMessage());
    	   } 
    }
}