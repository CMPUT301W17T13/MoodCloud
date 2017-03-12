package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-07.
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

        ArrayList<Account> result = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        if (result.size() == 0) return null;
        return result.get(0);
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
