package com.csahmad.moodcloud;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-07.
 */

public class AccountController {

    public static boolean isUsernameUnique(String username) {

        return AccountController.getAccountFromUsername(username) == null;
    }

    public static Account getAccountFromUsername(String username) {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("username",
                username));

        ElasticSearch<Account> elasticSearch = AccountController.getElasticSearch();
        elasticSearch.setFilter(filter);

        ArrayList<Account> result = elasticSearch.getNext(0);

        if (result.size() == 0) return null;
        return result.get(0);
    }

    public static ArrayList<Account> getAccounts(SearchFilter filter, int from) {

        ElasticSearch<Account> elasticSearch = AccountController.getElasticSearch();
        elasticSearch.setFilter(filter);
        return elasticSearch.getNext(from);
    }

    public static void addOrUpdateAccounts(Account... accounts) {

        AccountController.getElasticSearch().addOrUpdate(accounts);
    }

    public static void deleteAccounts(Account... accounts) {

        AccountController.getElasticSearch().delete(accounts);
    }

    public static ElasticSearch<Account> getElasticSearch() {

        return new ElasticSearch<Account>(Account.class, Account.typeName);
    }
}
