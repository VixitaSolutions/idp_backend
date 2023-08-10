package com.oversoul.controller;

import com.oversoul.Helper.ExcelHelperForPreloadeddata;
import com.oversoul.entity.PreloadedData;
import com.oversoul.exception.CommonException;
import com.oversoul.message.ResponseMessage;
import com.oversoul.repository.PreloadedDataRepository;
import com.oversoul.service.PreloadedDataService;
import com.oversoul.service.PreloadedDataServiceImpl;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@Controller
@RequestMapping("/api/excel")
public class PreloadedDataComtroller {
    @Autowired
    PreloadedDataServiceImpl fileService;


    @PostMapping("/uploadPreloadedData")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelHelperForPreloadeddata.hasExcelFormat(file)) {
            try {
                fileService.saveData(file);

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


    @GetMapping("/preloadedData")
    public ResponseEntity<List<PreloadedData>> getPreloadedData() {
        try {
            List<PreloadedData> preloadedData = fileService.getPreloadedData();

            if (preloadedData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(preloadedData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
