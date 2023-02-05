package com.oversoul.service;

import java.io.IOException;
import java.util.List;

import com.oversoul.entity.Competency;
import com.oversoul.entity.CompetencyMap;
import com.oversoul.repository.CompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oversoul.Helper.ExcelHelper;
import com.oversoul.repository.CompetencyMapRepository;

@Service
public class CompentencyMapServiceImpl{
    @Autowired
    CompetencyMapRepository repository;
    CompetencyRepository competencyRepo;
    private CompetencyMap competence;


    public void save(MultipartFile file, String tenantId) {
        try {
            List<CompetencyMap> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
            //List<Competency> globalData = ExcelHelper.excelToGlobalCompetency(file.getInputStream());
            for(int i=0;i<tutorials.size();i++){
                tutorials.get(i).setTenantId(tenantId);
            }
            repository.saveAll(tutorials);
           //competencyRepo.saveAll(globalData);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<CompetencyMap> getAllTutorials() {
        return repository.findAll();
    }
}
