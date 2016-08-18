package cn.superid.webapp.utils;


import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.core.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;


public class TransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, BeanFactoryAware, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionManager.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    protected Object doGetTransaction() throws TransactionException {
        Session session = sessionFactory.currentSession();
        AbstractSession.setDefaultSessionFactory(sessionFactory);
        return session;
    }

    @Override
    protected void doBegin(Object o, TransactionDefinition transactionDefinition) throws TransactionException {
        sessionFactory.currentSession().begin();
    }

    @Override
    protected void doCommit(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
//        LOG.info("db session commit");
        sessionFactory.currentSession().commit();
    }

    @Override
    protected void doRollback(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
//        LOG.info("db session rollback");
        sessionFactory.currentSession().rollback();
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        Session session = (Session) transaction;
        if(session == null) {
            return;
        }
        session.close();

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        LOG.info("set bean factory of tx manager");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("after properties set of tx manager");
        AbstractSession.setDefaultSessionFactory(sessionFactory);
    }

    @Override
    public Object getResourceFactory() {
        return sessionFactory;
    }
}