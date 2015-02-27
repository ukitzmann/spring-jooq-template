package de.evas.sample;

import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static de.evas.sample.jooq.tables.BusinessPartner.BUSINESS_PARTNER;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-context.xml"})
public class TestBusinessPartner {
    @Autowired
    DSLContext context;

    @Autowired
    DataSourceTransactionManager txmgr;

    @Before
    public void cleanUpDatabase() {
        context.delete(BUSINESS_PARTNER).execute();
    }

    @Test
    public void testDslContextIsValid() {
        Assert.assertNotNull(context);
    }

    @Test
    public void testBusinessPartnerInsert() {
        int count = context.fetchCount(context.select(BUSINESS_PARTNER.NAME).from(BUSINESS_PARTNER));
        context.insertInto(BUSINESS_PARTNER).set(BUSINESS_PARTNER.NAME, "Max Mustermann").execute();
        Assert.assertEquals(context.fetchCount(context.select(BUSINESS_PARTNER.NAME).from(BUSINESS_PARTNER)), count + 1);
    }

    @Test
    public void testBusinessPartnerTransaction() {
        boolean rollbackHappened = false;
        context.insertInto(BUSINESS_PARTNER).set(BUSINESS_PARTNER.NAME, "Max Mustermann").execute();
        int count = context.fetchCount(context.select(BUSINESS_PARTNER.NAME).from(BUSINESS_PARTNER));

        TransactionStatus tx = txmgr.getTransaction(new DefaultTransactionDefinition());
        try {
            context.insertInto(BUSINESS_PARTNER).set(BUSINESS_PARTNER.NAME, "Max Mustermann").execute();
        }
        catch (DataAccessException e) {
            txmgr.rollback(tx);
            rollbackHappened = true;
        }

        Assert.assertEquals(context.fetchCount(context.select(BUSINESS_PARTNER.NAME).from(BUSINESS_PARTNER)), count);
        Assert.assertTrue(rollbackHappened);
    }
}
