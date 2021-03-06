package com.oversoul.service;

import com.oversoul.entity.Role;
import com.oversoul.entity.TenantDetails;
import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.RoleRepository;
import com.oversoul.repository.TenantDetailsRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.util.CommonUtils;
import com.oversoul.util.Constants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserUploadServiceImpl implements UserUploadService {

    private final UserRepository userRepo;

    private final UserRoleRepository userRoleRepo;

    private final TenantDetailsRepository tenantDetailsRepo;

    private final RoleRepository roleRepository;

    public UserUploadServiceImpl(UserRepository userRepo, UserRoleRepository userRoleRepo,
                                 TenantDetailsRepository tenantDetailsRepo, RoleRepository roleRepository) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
        this.tenantDetailsRepo = tenantDetailsRepo;
        this.roleRepository = roleRepository;
    }

    @Override
    public ApiReturn processUserUpload(MultipartFile fileToUpload, UUID tenantId)
            throws IOException, CommonException {

        if (!(Optional.ofNullable(fileToUpload).isPresent() && fileToUpload.getSize() != 0)) {
            throw new CommonException("Upload File should not empty");
        }
        TenantDetails tenantDetails = tenantDetailsRepo.findById(tenantId).orElseThrow(() -> new CommonException("Invalid Tenant Details"));

        XSSFSheet spreadsheet = CommonUtils.convertCsvToXlsx(fileToUpload).getSheet("sheet1");
        XSSFRow currentRow = null;

        if (spreadsheet.getLastRowNum() < 1) {
            throw new CommonException("Sheet doesn't have any record");
        }
        Map<Integer, String> map = new HashMap<Integer, String>();
        Map<String, Object> uploadInfo = new HashMap<String, Object>();
        int uploadedCount = 0;
        for (int rowNo = 1; rowNo <= spreadsheet.getLastRowNum(); rowNo++) {
            currentRow = spreadsheet.getRow(rowNo);
            if (currentRow == null)
                continue;
            boolean skipUpload = false;
            String reason = null;
            String firstName = null;
            String lastName = null;
            String email = null;
            String phone = null;
            if (currentRow.getCell(0) != null && currentRow.getCell(0).getCellType() == CellType.STRING) {
                firstName = currentRow.getCell(0).getStringCellValue().trim();
                System.out.println(firstName);
            } else {
                reason = "First Name Should not empty";
                skipUpload = true;
            }
            if (currentRow.getCell(1) != null && currentRow.getCell(1).getCellType() == CellType.STRING) {
                lastName = currentRow.getCell(1).getStringCellValue().trim();
            } else {
                reason = reason != null ? reason + "/ Last Name Should not empty" : "Last Name Should not empty";
                skipUpload = true;
            }
            if (currentRow.getCell(2) != null && currentRow.getCell(2).getCellType() == CellType.STRING) {
                email = currentRow.getCell(2).getStringCellValue().trim();
                try {
                    if (Boolean.TRUE.equals(userRepo.existsByEmailAndTenantId(email, tenantDetails.getId()))) {
                        reason = reason != null ? reason + "/ Email Already exists in the system"
                                : "Email Already exists in the system";
                        skipUpload = true;
                    }
                } catch (Exception e) {
                    reason = reason != null ? reason + "/ Invalid Email Id" : "Invalid Email Id";
                    skipUpload = true;
                }
            } else {
                reason = reason != null ? reason + "/ Email Id should not empty" : "Email Id should not empty";
                skipUpload = true;
            }
            if (currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == CellType.STRING) {
                phone = currentRow.getCell(3).getStringCellValue().trim();
                if (phone.trim().length() != 10) {
                    reason = reason != null ? reason + "/ Invalid Mobile Number" : "Invalid Mobile Number";
                    skipUpload = true;
                }
            }
            Role role = null;
            if (currentRow.getCell(4) != null && currentRow.getCell(4).getCellType() == CellType.STRING) {
                String EmployeeType = currentRow.getCell(4).getStringCellValue().trim().toUpperCase();

                if (EmployeeType.equalsIgnoreCase("COACH")) {
                    role = roleRepository.findByIdAndActive((long) Constants.COACH, true);
                } else if (EmployeeType.equalsIgnoreCase("EMPLOYEE")) {
                    role = roleRepository.findByIdAndActive((long) Constants.EMPLOYEE, true);
                } else if (EmployeeType.equalsIgnoreCase("MANAGER")) {
                    role = roleRepository.findByIdAndActive((long) Constants.MANAGER, true);
                } else if (EmployeeType.equalsIgnoreCase("ADMIN")) {
                    role = roleRepository.findByIdAndActive((long) Constants.ADMIN, true);
                } else {
                    reason = reason != null ? reason + "/ Employee ROle is invalid" : "Employee ROle is invalid";
                    skipUpload = true;
                }
                if (role == null) {
                    reason = reason != null ? reason + "/ Employee Role is invalid" : "Employee Role is invalid";
                    skipUpload = true;
                }
            } else {
                reason = reason != null ? reason + "/ Employee Type should not empty" : "Employee Type should not empty";
                skipUpload = true;
            }


            if (skipUpload) {
                map.put(rowNo, reason);
            } else {
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setMobile(phone);
                Long userId = Long.parseLong(MDC.get("userId"));
                user.setCreatedBy(userId);
                user.setFromFileUpload(true);
                user.setTenantId(tenantId);
                user = userRepo.save(user);
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(role);
                userRoleRepo.save(userRole);
                uploadedCount = uploadedCount + 1;
            }
        }
        uploadInfo.put("newSkippedUser", map);
        uploadInfo.put("uploadedUser", uploadedCount);
        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), uploadInfo);
    }


}
