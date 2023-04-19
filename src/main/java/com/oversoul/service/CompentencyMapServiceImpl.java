package com.oversoul.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.oversoul.entity.Competency;
import com.oversoul.entity.CompetencyMap;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.CompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import com.oversoul.Helper.ExcelHelper;
import com.oversoul.repository.CompetencyMapRepository;

@Service
@Slf4j
public class CompentencyMapServiceImpl{
    @Autowired
    CompetencyMapRepository repository;
    CompetencyRepository competencyRepo;
    private CompetencyMap competence;
    private boolean noDuplicates = false;


    public void save(MultipartFile file, String tenantId) {
        try {
            List<CompetencyMap> dummyList = new ArrayList<CompetencyMap>();
            List<CompetencyMap> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
            List<CompetencyMap> TutorialsList = repository.findAll();
            //List<Competency> globalData = ExcelHelper.excelToGlobalCompetency(file.getInputStream());
            for(int i=0;i<tutorials.size();i++){
                if (tutorials.get(i).getcName().isEmpty() && tutorials.get(i).getgName().isEmpty()) {
                    throw new CommonException("Client Competency or Global Competency are Empty");
                }
                tutorials.get(i).setTenantId(tenantId);
            }
            if(TutorialsList.size() > 0){
                dummyList = this.createSharedListViaStream(tutorials, TutorialsList);
                tutorials.removeAll(dummyList);
                repository.saveAll(tutorials);
            }
            else {
                repository.saveAll(tutorials);
            }

           //competencyRepo.saveAll(globalData);
        } catch (IOException | CommonException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public static List<CompetencyMap> createSharedListViaStream(List<CompetencyMap> listOne, List<CompetencyMap> listTwo)
    {
        // We create a stream of elements from the first list.
        List<CompetencyMap> listOneList = listOne.stream()
                // We select any elements such that in the stream of elements from the second list
                .filter(two -> listTwo.stream()
                        // there is an element that has the same name and school as this element,
                        .anyMatch(one -> one.getcName().equals(two.getcName())
                                && two.getTenantId().equals(one.getTenantId())))
                // and collect all matching elements from the first list into a new list.
                .collect(Collectors.toList());
        // We return the collected list.
        return listOneList;
    }
    public List<CompetencyMap> getAllTutorials() {
        return repository.findAll();
    }
}
