package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-07.
 */

public class AccountController {

    private static ElasticSearch<Account> elasticSearch =
            new ElasticSearch<Account>(Account.class, Account.typeName);

    public static boolean isUsernameUnique(String username) throws TimeoutException {

        return AccountController.getAccountFromUsername(username) == null;
    }

    public static Account getAccountFromId(String id) throws TimeoutException {

        return AccountController.elasticSearch.getById(id);
    }

    public static Account getAccountFromUsername(String username) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("username",
                username));

        AccountController.elasticSearch.setFilter(filter);

        ArrayList<Account> result = AccountController.elasticSearch.getNext(0);
        AccountController.elasticSearch.setFilter(null);

        if (result.size() == 0) return null;
        return result.get(0);
    }

    public static ArrayList<Account> getAccounts(SearchFilter filter, int from)
        throws TimeoutException{

        AccountController.elasticSearch.setFilter(filter);
        ArrayList<Account> result = elasticSearch.getNext(from);
        AccountController.elasticSearch.setFilter(null);
        return result;
    }

    public static void addOrUpdateAccounts(Account... accounts) {

        AccountController.elasticSearch.addOrUpdate(accounts);
    }

    public static void deleteAccounts(Account... accounts) {

        AccountController.elasticSearch.delete(accounts);
    }
}
