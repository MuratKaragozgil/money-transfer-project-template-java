package org.example.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BonusActivity {

    @ActivityMethod
    String checkBonus(String accountId);

}
