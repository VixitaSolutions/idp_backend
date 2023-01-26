package com.oversoul.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.oversoul.entity.CompetencyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oversoul.Helper.ExcelHelper;
import com.oversoul.repository.CompetencyMapRepository;

@Service
public class CompentencyMapServiceImpl{
    @Autowired
    CompetencyMapRepository repository;

    private CompetencyMap competence;


    public void save(MultipartFile file, String tenantId) {
        try {
            List<CompetencyMap> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
            for(int i=0;i<tutorials.size();i++){
                tutorials.get(i).setTenantId(tenantId);
            }
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<CompetencyMap> getAllTutorials() {
        return repository.findAll();
    }
}
