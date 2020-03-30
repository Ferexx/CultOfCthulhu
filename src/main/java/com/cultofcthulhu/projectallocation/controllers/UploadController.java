package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.FileParser;
import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import com.cultofcthulhu.projectallocation.storage.StorageFileNotFoundException;
import com.cultofcthulhu.projectallocation.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    private final StorageService storageService;
    public static FileParser parser;

    @Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "")
    @GetMapping("/")
    public String listUploadedFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/user-files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping(value = "/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        storageService.store(file);
        model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        File convFile = new File(String.valueOf(storageService.load(file.getOriginalFilename())));
        try {
            parser = new FileParser(convFile);
            return "uploadStatus";
        } catch (ParseException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
