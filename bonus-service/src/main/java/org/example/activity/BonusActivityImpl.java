package org.example.activity;

import org.example.activity.BonusActivity;

public class BonusActivityImpl implements BonusActivity {

    @Override
    public String checkBonus(String accountId) {
        System.out.println("Check Bonus works! + " + accountId);
        return accountId;
    }

}
