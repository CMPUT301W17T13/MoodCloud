package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Get {@link Account}s using elasticsearch or add/update accounts using elasticsearch.
 *
 * @see ElasticSearchController
 */
public class AccountController {

    private ElasticSearch<Account> elasticSearch =
            new ElasticSearch<Account>(Account.class, Account.typeName);

    public boolean isUsernameUnique(String username) throws TimeoutException {

        return this.getAccountFromUsername(username) == null;
    }

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    public Account getAccountFromId(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public Account getAccountFromUsername(String username) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("username",
                username));

        this.elasticSearch.setFilter(filter);

        Account result = this.elasticSearch.getSingleResult();
        this.elasticSearch.setFilter(null);
        return result;
    }

    public ArrayList<Account> getAccounts(SearchFilter filter, int from)
        throws TimeoutException{

        this.elasticSearch.setFilter(filter);
        ArrayList<Account> result = elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    public void addOrUpdateAccounts(Account... accounts) {

        this.elasticSearch.addOrUpdate(accounts);
    }

    public void deleteAccounts(Account... accounts) {

        this.elasticSearch.delete(accounts);
    }
}
