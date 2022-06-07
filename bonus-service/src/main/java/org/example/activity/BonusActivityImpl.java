package org.example.activity;

public class BonusActivityImpl implements BonusActivity {

    @Override
    public String checkBonus(String accountId) {
        System.out.println("Check Bonus works! + " + accountId);
        return accountId + " OK ";
//        throw Activity.wrap(new NullPointerException("NPE"));
    }

}
