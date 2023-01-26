package com.oversoul.controller;

import java.util.List;
import java.util.UUID;

import com.oversoul.entity.CompetencyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.oversoul.Helper.ExcelHelper;
import com.oversoul.message.ResponseMessage;
import com.oversoul.service.CompentencyMapServiceImpl;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@Controller
@RequestMapping("/api/excel")
public class CompetencyMapComtroller {
    @Autowired
    CompentencyMapServiceImpl fileService;

    @PostMapping("/upload/{tenantId}")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(value = "tenantId", required = true) String tenantId) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                fileService.save(file,tenantId);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping("/competency")
    public ResponseEntity<List<CompetencyMap>> getAllTutorials() {
        try {
            List<CompetencyMap> compentencies = fileService.getAllTutorials();

            if (compentencies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(compentencies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
