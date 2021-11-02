package com.ait.staffmanagement.service.staff;


import java.util.Optional;

import com.ait.staffmanagement.model.Staff;
import com.ait.staffmanagement.model.DTO.CreateDTO;
import com.ait.staffmanagement.service.IGeneralService;


public interface IStaffService  extends IGeneralService<Staff>{
	
	Optional<Staff> findByEmail(String email);

    CreateDTO findStaffByEmail(String email);

//    Iterable<Staff> findAllByDeletedIsTrue();

    Iterable<Staff> findAllByDeleted(Boolean isdeleted);
}
