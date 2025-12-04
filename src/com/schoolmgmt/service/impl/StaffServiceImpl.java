package com.schoolmgmt.service.impl;

import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.Staff;
import com.schoolmgmt.repository.InMemoryRepository;
import com.schoolmgmt.service.StaffService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class StaffServiceImpl implements StaffService {
    private final SchoolRecords records;

    public StaffServiceImpl(SchoolRecords records) {
        this.records = records;
    }

    @Override
    public void addStaffMember(Staff staff) {
        InMemoryRepository<String, Staff> repository = records.getStaff();
        if (repository.existsById(staff.getId())) {
            throw new IllegalArgumentException("Staff already exists: " + staff.getId());
        }
        repository.save(staff);
    }

    @Override
    public Optional<Staff> findStaff(String staffId) {
        return records.getStaff().findById(staffId);
    }

    @Override
    public Collection<Staff> listStaff() {
        return new ArrayList<>(records.getStaff().findAll());
    }
}
