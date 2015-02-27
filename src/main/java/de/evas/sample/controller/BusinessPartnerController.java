package de.evas.sample.controller;

import de.evas.sample.jooq.tables.pojos.BusinessPartner;
import de.evas.sample.jooq.tables.records.BusinessPartnerRecord;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static de.evas.sample.jooq.Tables.BUSINESS_PARTNER;


@Controller
@RequestMapping("/rest")
public class BusinessPartnerController {

    @Autowired
    DSLContext context;

    private ModelMapper modelMapper;

    public BusinessPartnerController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
    }

    @RequestMapping(value = "/businesspartners/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getBusinessPartnerByName(@PathVariable String name) {
        return context.selectFrom(BUSINESS_PARTNER).where(BUSINESS_PARTNER.NAME.eq(name)).fetch().getValues(BUSINESS_PARTNER.NAME);
    }

    @RequestMapping(value = "/businesspartner/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BusinessPartner getBusinessPartnerById(@PathVariable("id") Integer id) {
        BusinessPartnerRecord businessPartnerRecord = context.selectFrom(BUSINESS_PARTNER).where(BUSINESS_PARTNER.ID.eq(id)).fetchOne();
        return modelMapper.map(businessPartnerRecord, BusinessPartner.class);
    }
}
