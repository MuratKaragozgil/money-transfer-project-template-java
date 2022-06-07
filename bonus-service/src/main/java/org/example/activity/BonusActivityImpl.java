package org.example.activity;

import io.temporal.activity.Activity;

public class BonusActivityImpl implements BonusActivity {

    @Override
    public String checkBonus(String accountId) {
        System.out.println("Check Bonus works! + " + accountId);
//        return accountId + " OK ";
        throw Activity.wrap(new NullPointerException("NPE"));
    }

    @Override
    public void rollbackBonus(String accountId) {
        System.out.println("Rollback Bonus works! + " + accountId);
    }

}
