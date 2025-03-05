package com.altice.salescommission.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.entity.TranslateMasterEntity;
import com.altice.salescommission.repository.KpiMasterRepository;
import com.altice.salescommission.repository.TranslateTableRepository;

//@SpringBootTest
class TranslateTableServiceTest {

	@Autowired
    private TranslateTableService translateTableService;

    @MockBean
    private TranslateTableRepository translateTableRepository;

    @BeforeEach
    void setUp() {
    	TranslateMasterEntity translateTableModel =
    			TranslateMasterEntity.builder()
    			.id(1L)
                .field_name("Measure Type")
                .field_value("Commission Override")
                .description("Commission Override")
                .field_short_name("CommOvrd")
                .effective_date(new Date())
                .effective_status("Y")
                .build();

        //Mockito.when(translateTableRepository.getFieldValuesList("Measure Type"))
                //.thenReturn(translateTableModel);

    }

    @Test
    @DisplayName("Get Data based on Valida KPI Name")
    public void whenValidDepartmentName_thenDepartmentShouldFound() {
        String fiedName = "Measure Type";
       // TranslateTableModel found =
        		//translateTableService.getFieldValuesList(fiedName);

       //assertEquals(fiedName, found.getFieldValuesList());
    }
}