package com.oversoul.service;

import com.oversoul.entity.ClientCompetency;
import com.oversoul.entity.Competency;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.ClientCompetencyRepository;
import com.oversoul.repository.CompetencyRepository;
import com.oversoul.repository.TenantDetailsRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.util.CommonUtils;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.CompetencyDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class CompetencyServiceImpl implements CompetencyService {

    private final CompetencyRepository competencyRepo;

    private final TenantDetailsRepository tenantDetailsRepo;

    private final ClientCompetencyRepository clientCompetencyRepo;


    CompetencyServiceImpl(CompetencyRepository competencyRepo, TenantDetailsRepository tenantDetailsRepo, ClientCompetencyRepository clientCompetencyRepo) {
        this.competencyRepo = competencyRepo;
        this.tenantDetailsRepo = tenantDetailsRepo;
        this.clientCompetencyRepo = clientCompetencyRepo;
    }

    @Override
    public ApiReturn getCompetencies(UUID tenantId) {
        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                clientCompetencyRepo.findByTenantId(tenantId));
    }

    @Override
    public ApiReturn competencyUpload(MultipartFile fileToUpload, UUID tenantId, HttpServletRequest request, HttpServletResponse response) throws CommonException, IOException {
        if (!(Optional.ofNullable(fileToUpload).isPresent() && fileToUpload.getSize() != 0)) {
            throw new CommonException("Upload File should not empty");
        }
        Long createdBy = (Long) request.getAttribute("userId");
        tenantDetailsRepo.findById(tenantId).orElseThrow(() -> new CommonException("Invalid Tenant Details"));

        XSSFSheet spreadsheet = CommonUtils.convertCsvToXlsx(fileToUpload).getSheet("sheet1");
        XSSFRow currentRow;

        if (spreadsheet.getLastRowNum() < 1) {
            throw new CommonException("Sheet doesn't have any record");
        }
        Map<Integer, String> map = new HashMap<Integer, String>();
        Map<String, Object> uploadInfo = new HashMap<String, Object>();
        List<CompetencyDetails> competencyDetailsList = new ArrayList<>();
        int uploadedCount = 0;
        for (int rowNo = 1; rowNo <= spreadsheet.getLastRowNum(); rowNo++) {
            currentRow = spreadsheet.getRow(rowNo);
            if (currentRow == null)
                continue;
            boolean skipUpload = false;
            String reason = null;
            String clientCompetencyName = null;
            Long clientCompetencyLevel = null;
            String globalCompetencyName = null;
            Long globalCompetencyLevel = null;
            //String keyword = null;
            if (currentRow.getCell(1) != null && currentRow.getCell(1).getCellType() == CellType.STRING) {
                clientCompetencyName = currentRow.getCell(1).getStringCellValue().trim();
            } else {
                reason = "Client Competency Name Should not empty";
                skipUpload = true;
            }
            if (currentRow.getCell(2) != null && currentRow.getCell(2).getCellType() == CellType.STRING) {
                clientCompetencyLevel = Long.valueOf(currentRow.getCell(2).getStringCellValue().trim());
            } else {
                reason = reason != null ? reason + "/ Client Competency Level Should not empty" : "Client Competency Level Should not empty";
                skipUpload = true;
            }
            if (currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == CellType.STRING) {
                globalCompetencyName = currentRow.getCell(3).getStringCellValue().trim();
            } else {
                reason = reason != null ? reason + "/ Global Competency Name Should not empty" : "Global Competency Name Should not empty";
                skipUpload = true;
            }
            if (currentRow.getCell(4) != null && currentRow.getCell(4).getCellType() == CellType.STRING) {
                globalCompetencyLevel = Long.valueOf(currentRow.getCell(4).getStringCellValue().trim());
            } else {
                reason = reason != null ? reason + "/ Global Competency Level Should not empty" : "Global Competency Level Should not empty";
                skipUpload = true;
            }
            /*if (currentRow.getCell(5) != null && currentRow.getCell(5).getCellType() == CellType.STRING) {
                keyword = currentRow.getCell(5).getStringCellValue().trim();
            }*/

            if (skipUpload) {
                map.put(rowNo, reason);
            } else {
                Competency competency = competencyRepo.findByNameAllIgnoreCaseAndLevel(globalCompetencyName, globalCompetencyLevel);
                if (competency != null) {
                    CompetencyDetails competencyDetails = new CompetencyDetails();
                    competencyDetails.setClientCompetencyName(clientCompetencyName);
                    competencyDetails.setClientCompetencyLevel(clientCompetencyLevel);
                    competencyDetails.setGlobalCompetencyName(globalCompetencyName);
                    competencyDetails.setGlobalCompetencyLevel(globalCompetencyLevel);
                    //competencyDetails.setKeyword(keyword);
                    competencyDetailsList.add(competencyDetails);
                } else {
                    reason = reason != null ? reason + "/ Global Competency Not Existed in the system" : "Global Competency Not Existed in the system";
                    map.put(rowNo, reason);
                }
            }
        }
        if (map.isEmpty() && !competencyDetailsList.isEmpty()) {
            for (CompetencyDetails competency : competencyDetailsList) {
                Competency globalCompetency = competencyRepo.findByNameAllIgnoreCaseAndLevel(competency.getGlobalCompetencyName(), competency.getGlobalCompetencyLevel());
                if (globalCompetency != null) {
                    log.info("globalCompetency details id {}", globalCompetency.getId());
                    saveClientCompetencyDetails(tenantId, competency, globalCompetency, createdBy);
                }
            }
        }
        uploadInfo.put("newSkippedUser", map);
        uploadInfo.put("uploadedUser", uploadedCount);
        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), uploadInfo);
    }

    private void saveClientCompetencyDetails(UUID tenantId, CompetencyDetails competency, Competency globalCompetency, Long createdBy) {
        ClientCompetency clientCompetencyDetails = clientCompetencyRepo.findByNameIgnoreCaseAndLevelAndTenantId(competency.getClientCompetencyName(), competency.getClientCompetencyLevel(), tenantId);
        if (clientCompetencyDetails != null) {
            saveClientCompetency(tenantId, globalCompetency, competency, clientCompetencyDetails, createdBy);
        } else {
            clientCompetencyDetails = new ClientCompetency();
            saveClientCompetency(tenantId, globalCompetency, competency, clientCompetencyDetails, createdBy);
        }
    }

    private void saveClientCompetency(UUID tenantId, Competency globalCompetency, CompetencyDetails clientCompetency, ClientCompetency clientCompetencyDetails, Long createdBy) {
        clientCompetencyDetails.setGlobalCompetency(globalCompetency);
        clientCompetencyDetails.setCreatedBy(createdBy);
        clientCompetencyDetails.setLevel(clientCompetency.getClientCompetencyLevel());
        clientCompetencyDetails.setName(clientCompetency.getClientCompetencyName());
        clientCompetencyDetails.setTenantId(tenantId);
        clientCompetencyRepo.save(clientCompetencyDetails);
    }

}
