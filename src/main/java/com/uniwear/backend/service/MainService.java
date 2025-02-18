package com.uniwear.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.uniwear.backend.dto.CategoryDto;
import com.uniwear.backend.dto.ColorDto;
import com.uniwear.backend.dto.MembershipDto;
import com.uniwear.backend.dto.PrintTypeDto;
import com.uniwear.backend.dto.SizeDto;
import com.uniwear.backend.dto.SupplierDto;
import com.uniwear.backend.enums.DeliveryCaseType;
import com.uniwear.backend.repository.CategoryRepository;
import com.uniwear.backend.repository.MembershipRepository;
import com.uniwear.backend.repository.PrintTypeRepository;
import com.uniwear.backend.repository.SupplierRepository;
import com.uniwear.backend.util.RedisUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MainService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PrintTypeRepository printTypeRepository;

    public List<CategoryDto.Menu.Res> getMenuList() {
        return categoryRepository.findByParentCategoryIsNull().stream().map(p -> modelMapper.map(p, CategoryDto.Menu.Res.class)).collect(Collectors.toList());
    }

    public Object getHolidays(String year) {
        List<String> date = new ArrayList<String>();
        DateTimeFormatter format = new DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

        if (year == null || year.equals("")) year = LocalDate.now().format(format);
        LocalDate localDate = LocalDate.parse(year, format);

        date.addAll(redisUtil.getDataList("holiday", "date:" + year));

        year = localDate.plusYears(1).format(format);
        
        date.addAll(redisUtil.getDataList("holiday", "date:" + year));
        
        return date;
    }

    public List<MembershipDto.Res> getMembership() {
        return membershipRepository.findAll().stream().map(p -> modelMapper.map(p, MembershipDto.Res.class)).collect(Collectors.toList());
    }

    public CategoryDto.Default.Res getCategory(Long categoryId) {
        return modelMapper.map(categoryRepository.findById(categoryId).get(), CategoryDto.Default.Res.class);
    }

    public List<CategoryDto.Root.Res> getRootCategories() {
        return categoryRepository.findByParentCategoryIsNull().stream().map(p -> modelMapper.map(p, CategoryDto.Root.Res.class)).collect(Collectors.toList());
    }

    public List<SupplierDto.Res> getSuppliers() {
        return supplierRepository.findAll().stream().map(p -> modelMapper.map(p, SupplierDto.Res.class)).collect(Collectors.toList());
    }

    public List<PrintTypeDto.Res> getPrintTypeList() {
        return printTypeRepository.findAll().stream().map(p -> modelMapper.map(p, PrintTypeDto.Res.class)).collect(Collectors.toList());
    }

    public Object getCaseTypeList() {
        return DeliveryCaseType.values();
    }

    public List<ColorDto.Res> getColorsByPrintType(Long printTypeId) {
        return printTypeRepository.findById(printTypeId).get().getPrintColors().stream().map(p -> modelMapper.map(p.getColor(), ColorDto.Res.class)).collect(Collectors.toList());
    }

    public List<SizeDto.Res> getSizesByPrintType(Long printTypeId) {
        return printTypeRepository.findById(printTypeId).get().getPrintSizes().stream().map(p -> modelMapper.map(p, SizeDto.Res.class)).collect(Collectors.toList());
    }

    public Object getPrintPositionList() {
        
        return null;
    }
}