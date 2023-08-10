package com.oversoul.service;

import com.oversoul.Helper.ExcelHelperForPreloadeddata;
import com.oversoul.entity.PreloadedData;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.PreloadedDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PreloadedDataServiceImpl {

    private final  PreloadedDataRepository preloadedDataRepository;
    private boolean noDuplicates = false;

    public PreloadedDataServiceImpl(PreloadedDataRepository preloadedDataRepository) {
        this.preloadedDataRepository = preloadedDataRepository;
    }

    public void saveData(MultipartFile file) {
        try {
            List<PreloadedData> dummyList = new ArrayList<PreloadedData>();
            List<PreloadedData> preloadedData = ExcelHelperForPreloadeddata.excelToData(file.getInputStream());
            List<PreloadedData> TutorialsList = preloadedDataRepository.findAll();
            for(int i=0;i<preloadedData.size();i++){

                if (preloadedData.get(i).getcName().isEmpty() && preloadedData.get(i).getyName().isEmpty() && preloadedData.get(i).getbTitle().isEmpty() && preloadedData.get(i).getOcCourse().isEmpty())  {
                    throw new CommonException("Client Competency or Global Competency are Empty");
                }
//                preloadedData.get(i).setTenantId(tenantId);
            }
            if(TutorialsList.size() > 0){
                dummyList = this.createSharedListViaStream(preloadedData, TutorialsList);
                preloadedData.removeAll(dummyList);
                preloadedDataRepository.saveAll(preloadedData);
            }
            else {
                preloadedDataRepository.saveAll(preloadedData);
            }

            //preloadedDataRepository.saveAll(preloadedData);
        } catch (IOException  e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        } catch (CommonException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PreloadedData> createSharedListViaStream(List<PreloadedData> listOne, List<PreloadedData> listTwo)
    {
        // We create a stream of elements from the first list.
        List<PreloadedData> listOneList = listOne.stream()
                // We select any elements such that in the stream of elements from the second list
                .filter(two -> listTwo.stream()
                        // there is an element that has the same name and school as this element,
                        .anyMatch(one -> one.getcName().equals(two.getcName())
                                && two.getbTitle().equals(one.getbTitle()) && two.getOcCourse().equals(one.getOcCourse()) && two.getyName().equals(one.getyName()) ))
                // and collect all matching elements from the first list into a new list.
                .collect(Collectors.toList());
        // We return the collected list.
        return listOneList;
    }
    public List<PreloadedData> getPreloadedData() {
        return preloadedDataRepository.findAll();
    }
}
