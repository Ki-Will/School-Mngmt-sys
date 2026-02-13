package com.schoolmgmt.io;

import com.schoolmgmt.data.SchoolRecords;

public interface SchoolRecordsStore {
    SchoolRecords load();

    void save(SchoolRecords records);
}
