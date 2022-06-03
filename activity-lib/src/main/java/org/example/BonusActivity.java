package org.example;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BonusActivity {

    @ActivityMethod
    void checkBonus(String accountId);

}
