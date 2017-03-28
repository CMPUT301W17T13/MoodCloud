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

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    /**
     * Wait for the last task to finish executing.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    /**
     * Return whether the given username is unique (does not yet exist).
     *
     * @param username the username to check
     * @return whether the given username is unique
     * @throws TimeoutException
     */
    public boolean isUsernameUnique(String username) throws TimeoutException {

        return this.getAccountFromUsername(username) == null;
    }

    /**
     * Return the {@link Account} that has the given id.
     *
     * <p>
     * Return null if no {@link Account} has the given id.
     *
     * @param id the id of the desired {@link Account}
     * @return the {@link Account} that has the given id
     * @throws TimeoutException
     */
    public Account getAccountFromId(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    /**
     * Return the {@link Account} that has the given username.
     *
     * <p>
     * Return null if no {@link Account} has the given username.
     *
     * @param username the username of the desired {@link Account}
     * @return the {@link Account} that has the given username
     * @throws TimeoutException
     */
    public Account getAccountFromUsername(String username) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("username",
                username));

        this.elasticSearch.setFilter(filter);

        Account result = this.elasticSearch.getSingleResult();
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Return {@link Account}s that match the given filter.
     *
     * <p>
     * If filter is null or has no restrictions, return all {@link Account}s.
     *
     * @param filter restricts which {@link Account}s will be returned (defines conditions each
     *               {@link Account} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return {@link Account}s from the elasticsearch index
     * @throws TimeoutException
     */
    public ArrayList<Account> getAccounts(SearchFilter filter, int from)
        throws TimeoutException{

        this.elasticSearch.setFilter(filter);
        ArrayList<Account> result = elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Add or update the given {@link Account}s via elasticsearch.
     *
     * <p>
     * If an {@link Account} has a null {@link Account#id}, add it. If an {@link Account} has a
     * non-null {@link Account#id}, update it.
     *
     * @param accounts the {@link Account}s to add or update
     */
    public void addOrUpdateAccounts(Account... accounts) {

        this.elasticSearch.addOrUpdate(accounts);
    }

    /**
     * Delete the given {@link Account}s via elasticsearch.
     *
     * @param accounts
     */
    public void deleteAccounts(Account... accounts) {

        this.elasticSearch.delete(accounts);
    }
}
