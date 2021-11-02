package com.ait.staffmanagement.service.staff;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ait.staffmanagement.model.Staff;
import com.ait.staffmanagement.model.DTO.CreateDTO;
import com.ait.staffmanagement.repository.IStaffRepository;

@Service
public class StaffService implements  IStaffService {
	
	@Autowired
	private IStaffRepository staffRepository;

	@Override
	public Iterable<Staff> findAll() {
		return 	staffRepository.findAll();
		
	}

	@Override
	public Optional<Staff> findById(long id) {
		return 	staffRepository.findById(id);
		
	}
	@Override
	public Staff save(Staff t) {
		return 	staffRepository.save(t);
		
	}

	@Override
	public void deleteById(long id) {
		staffRepository.deleteById(id);
		
	}
	

	@Override
    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    @Override
    public CreateDTO findStaffByEmail(String email) {
        return staffRepository.findStaffByEmail(email);
    }

	@Override
	public Iterable<Staff> findAllByDeleted(Boolean isdeleted) {
		return staffRepository.findAllByDeleted(isdeleted);
	}

	//	@Override
//	public Iterable<Staff> findAllByDeletedIsTrue() {
//		return staffRepository.findAllByDeletedIsTrue();
//	}
}
