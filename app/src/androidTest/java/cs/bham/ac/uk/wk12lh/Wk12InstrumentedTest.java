package cs.bham.ac.uk.wk12lh;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Wk12InstrumentedTest {
    @Rule
    public ActivityScenarioRule<WelcomeActivity> act = new ActivityScenarioRule<>(WelcomeActivity.class);

    /* If the welcomeLabel test runs successfully, you will be awarded two marks. */
    @Test
    public void welcomeLabel(){
        onView(withId(R.id.welcomeLabel)).check(matches(instanceOf(TextView.class)));
        onView(withId(R.id.welcomeLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.welcomeLabel)).check(matches(withText("Welcome to this cool app!")));
    }

    /* If the welcomeLayout test runs successfully, you will be awarded 5 marks. */
    @Test
    public void welcomeLayout(){
        onView(withId(R.id.firstChoice)).check(matches(instanceOf(Spinner.class)));
        onView(withId(R.id.secondChoice)).check(matches(instanceOf(EditText.class)));
        onView(withId(R.id.goButton)).check(matches(instanceOf(Button.class)));

        onView(withId(R.id.firstChoice)).check(matches(isDisplayed()));
        onView(withId(R.id.secondChoice)).check(matches(isDisplayed()));
        onView(withId(R.id.goButton)).check(matches(isDisplayed()));

        onView(withId(R.id.welcomeLabel)).check(isCompletelyAbove(withId(R.id.firstChoice)));
        onView(withId(R.id.firstChoice)).check(isCompletelyAbove(withId(R.id.secondChoice)));
        onView(withId(R.id.goButton)).check(isCompletelyBelow(withId(R.id.secondChoice)));
    }

    /* If the spinnerOptionsCorrect test runs successfully, you will be awarded 6 marks. */
    @Test
    public void spinnerOptionsCorrect(){
        //Spinner options are NOT pulled from the API.
        Context appContext = getInstrumentation().getTargetContext();
        String[] spinOpts = appContext.getResources().getStringArray(R.array.spinneroptions);
        assertEquals(spinOpts[0], "Funky");
        assertEquals(spinOpts[1], "Retro");
        assertEquals(spinOpts[2], "Chill");
    }

    /* If the makeChoicesGoToActivity test runs successfully you will be awarded 15 marks. */
    @Test
    public void makeChoices() {
        String choice1 = "Retro";
        String choice2 = "EGG";

        onView(withId(R.id.firstChoice)).perform(click());
        onData(is(choice1)).perform(click());
        Object o = getInstrumentation().getContext().getClass();
        onView(withId(R.id.secondChoice)).perform(typeText(choice2));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.goButton)).perform(click());
        /*The line below you may not be familiar with: it tests that the current activity is "DetailsActivity.
         In other words, once the goButton is clicked, it needs to open the DetailsActivity activity so that this passes.*/
        assertEquals(getActivityInstance().getLocalClassName(),"DetailsActivity");
        onView(withId(R.id.firstChoiceLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.firstChoiceLabel)).check(matches(instanceOf(TextView.class)));
        onView(withId(R.id.secondChoiceLabel)).check(matches(instanceOf(TextView.class)));
        onView(withId(R.id.firstChoiceLabel)).check(matches(withText(choice1)));
        onView(withId(R.id.secondChoiceLabel)).check(matches(withText(choice2)));
    }

    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });
        return currentActivity[0];
    }

    /* If the getItems test runs successfully you will be awarded 20 marks */
    @Test
    public void getItems() {
        //Starts off like makeChoices
        String choice1 = "Chill";
        String choice2 = "CAT";

        onView(withId(R.id.firstChoice)).perform(click());
        onData(is(choice1)).perform(click());
        Object o = getInstrumentation().getContext().getClass();
        onView(withId(R.id.secondChoice)).perform(typeText(choice2));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());

        // Note that the ApiCountingIdlingResources class has been left in-place for you.
        // You do not need to implement it  yourself, only to make the appropriate calls in your code
        // to ensure that the test does not 'rush' through before a result is returned from the API.
        IdlingRegistry.getInstance().register(ApiCountingIdlingResources.getIdlingResource());

        onView(withId(R.id.goButton)).perform(click());
        /*The line below you may not be familiar with: it tests that the current activity is "DetailsActivity.
         In other words, once the goButton is clicked, it needs to open the DetailsActivity activity so that this passes.*/
        assertEquals(getActivityInstance().getLocalClassName(),"DetailsActivity");

        onView(withId(R.id.itemList)).check(matches(isDisplayed()));
        onView(withId(R.id.itemList)).check(matches(instanceOf(RecyclerView.class)));
        onView(ViewMatchers.withId(R.id.itemList)).perform(RecyclerViewActions.scrollTo(withText("And a large, silver "+choice2)));

        IdlingRegistry.getInstance().unregister(ApiCountingIdlingResources.getIdlingResource());
    }
}