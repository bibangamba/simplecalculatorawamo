package com.bibangamba.simplecalculatorawamo;

import android.os.SystemClock;

import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.bibangamba.simplecalculatorawamo.view.SimpleCalculatorActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class CalculatorScreenTest {
    private static final String numberOne = "99";
    private static final String numberTwo = "99";
    private static final String numberOneText = "Number One: " + numberOne;

    @Rule
    public ActivityTestRule<SimpleCalculatorActivity> activityTestRule
            = new ActivityTestRule<>(SimpleCalculatorActivity.class);

    @Test
    public void submitCalculationExpression_sameActivity() {
        onView(withId(R.id.numberOneET))
                .perform(typeText(numberOne));
        onView(withId(R.id.numberTwoET))
                .perform(typeText(numberTwo));
        closeSoftKeyboard();

        // api request and database save
        onView(withId(R.id.calculateBtn)).perform(click());
        SystemClock.sleep(2000); //TODO replace these calls with IdlingResourceImplementation

        onView(withId(R.id.resultsRecyclerView)).check(matches(isDisplayed()));

        onView(withId(R.id.resultsRecyclerView))
                .perform(scrollTo(hasDescendant(withText(numberOneText))));
    }

    @Test
    public void deleteExistingResult_sameActivity() {
        //delete result item that was just added
        onView(withId(R.id.deleteBtn))
                .check(matches(hasSibling(withText(numberOneText))))
                .perform(click());
    }

    // TODO: 2019-12-19
    //  - add mock product flavour with mock database that can be dumped
    //  - add mock web server call to fake api calls
    //  - add IdlingResource implementation to avoid System.clock() calls

}
