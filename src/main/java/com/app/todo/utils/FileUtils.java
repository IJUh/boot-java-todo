package com.app.todo.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.io.PrintWriter;
import java.util.Objects;

public class FileUtils {

    public static void  createTextFile(HttpServletResponse response, String fileName, String data) throws IOException {
        String docName = URLEncoder.encode(fileName.concat(".txt"),"UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
        response.setContentType("text/plain;charset=UTF-8");

        PrintWriter txtPrinter = response.getWriter();
        txtPrinter.print(data);
        response.flushBuffer();
    }

    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.createNewFile();
        fosWrite(file, multipartFile);
        return file;
    }

    private static void fosWrite(File file, MultipartFile multipartFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
    }



}
