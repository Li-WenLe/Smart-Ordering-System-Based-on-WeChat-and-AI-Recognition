package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.AddressMapper;
import com.wxprogrem.pojo.AddressBook;
import com.wxprogrem.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public void update(int id) {
        addressMapper.update(id);
    }

    @Override
    public void add(AddressBook addressBook) {
        addressMapper.add(addressBook);
    }

    @Override
    public AddressBook getById(int id) {
        return addressMapper.getById(id);
    }

    @Override
    public List<AddressBook> getAll(int id) {
        return addressMapper.getAll(id);
    }

    @Override
    public AddressBook getinfoById(int id) {
        return addressMapper.getinfo(id);
    }

    @Override
    public void updateInfo(AddressBook addressBook) {
        addressMapper.updateInfo(addressBook);
    }
}
