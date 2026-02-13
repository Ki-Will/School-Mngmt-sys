package com.schoolmgmt.service;

import com.schoolmgmt.model.Staff;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StaffService {
    void addStaffMember(Staff staff);

    Optional<Staff> findStaff(String staffId);

    Collection<Staff> listStaff();

    List<Staff> findAllStaff();
}
