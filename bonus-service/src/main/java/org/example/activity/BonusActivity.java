package org.example.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BonusActivity {

    @ActivityMethod(name = "checkBonus")
    String checkBonus(String accountId);

    @ActivityMethod(name = "rollbackBonus")
    void rollbackBonus(String accountId);

}
