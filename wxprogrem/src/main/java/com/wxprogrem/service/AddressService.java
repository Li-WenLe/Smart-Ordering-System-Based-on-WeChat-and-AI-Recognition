package com.wxprogrem.service;

import com.wxprogrem.pojo.AddressBook;

import java.util.List;

public interface AddressService {
    void update(int id);

    void add(AddressBook addressBook);

    AddressBook getById(int id);

    List<AddressBook> getAll(int id);

    AddressBook getinfoById(int id);

    void updateInfo(AddressBook addressBook);
}
