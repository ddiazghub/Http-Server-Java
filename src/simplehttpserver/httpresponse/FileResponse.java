/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver.httpresponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import simplehttpserver.exceptions.NotFoundException;

/**
 *
 * @author david
 */
public class FileResponse extends HttpResponse<File> {
    public FileResponse(String filepath, int status) {
        super(new File(filepath), status);
        
        if (!this.body.exists())
            throw new NotFoundException("Could not find file " + this.body.getAbsolutePath());
        
        this.setHeader("Content-Type", this.getFileType(this.body.getName()));
    }
    
    private String getFileType(String filename) {
        String[] nameParts = filename.split("\\.");
        
        if (nameParts.length == 0)
            throw new NotFoundException("Could not find file " + this.body.getAbsolutePath());
        
        switch (nameParts[nameParts.length - 1]) {
            case "html":
                return "text/html; charset=utf-8";
                
            case "js":
                return "application/javascript; charset=utf-8";
            
            case "css":
                return "text/css; charset=utf-8";
                
            case "xml":
                return "text/xml; charset=utf-8";
                
            case "csv":
                return "text/xml; charset=utf-8";
                
            case "json":
                return "application/json; charset=utf-8";
            
            case "png":
            case "PNG":
                return "image/png";
                
            case "jpg":
            case "jpeg":
            case "JPG":
            case "JPEG":
                return "image/jpeg";
            
            case "gif":
            case "GIF":
                return "image/gif";
                
            case "ogg":
                return "application/ogg";
                
            case "pdf":
                return "application/pdf";
                
            case "zip":
                return "application/zip";
            
            case "mpeg":
                return "video/mpeg";
            
            case "mp4":
                return "video/mp4";
                
            case "webm":
                return "video/webm";
                
            default:
                return "application/octet-stream";
        }
    }
    
    @Override
    public byte[] raw() {
        try {
            return this.serialize(Files.readAllBytes(this.body.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Could not read from file " + this.body.getAbsolutePath());
        }
    }
}
