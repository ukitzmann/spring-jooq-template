package de.evas.sample.jooq;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class SpringTransactionProvider implements TransactionProvider {
    private static final JooqLogger LOGGER = JooqLogger.getLogger(SpringTransactionProvider.class);

    @Autowired
    DataSourceTransactionManager txmgr;

    @Override
    public void begin(TransactionContext ctx) throws DataAccessException {
        LOGGER.info("Begin transaction");
        TransactionStatus transaction = txmgr.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_NESTED));
        ctx.transaction(new SpringTransaction(transaction));
    }

    @Override
    public void commit(TransactionContext ctx) throws DataAccessException {
        LOGGER.info("Commit transaction");
        txmgr.commit(((SpringTransaction) ctx.transaction()).transactionStatus);
    }

    @Override
    public void rollback(TransactionContext ctx) throws DataAccessException {
        LOGGER.info("Rollback transaction");
        txmgr.rollback(((SpringTransaction) ctx.transaction()).transactionStatus);
    }
}
