package com.ait.staffmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ait.staffmanagement.model.Skill;
import com.ait.staffmanagement.model.Staff;
import com.ait.staffmanagement.model.DTO.CreateDTO;


@Repository
public interface IStaffRepository extends JpaRepository<Staff,Long> {
	
	@Query("SELECT NEW com.ait.staffmanagement.model.DTO.CreateDTO (s.id, s.email) FROM Staff s WHERE s.email = ?1")
    CreateDTO findStaffByEmail(String email);

    Optional<Staff> findByEmail(String email);

    @Query("SELECT NEW com.ait.staffmanagement.model.DTO.displayDTO (s.id, s.fullName, s.email, s.address, s.dateOfBirth, s.entryCompanyDate, s.office.name) FROM Staff s WHERE s.isDeleted =?1")
    Iterable<Staff> findAllByDeleted(Boolean isdeleted);

//    Iterable<Staff> findAllByDeletedIsTrue();

}
