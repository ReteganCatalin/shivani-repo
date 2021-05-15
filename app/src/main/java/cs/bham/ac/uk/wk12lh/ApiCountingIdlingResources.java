package cs.bham.ac.uk.wk12lh;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class ApiCountingIdlingResources {
    private static CountingIdlingResource cIR = new CountingIdlingResource("api-call-idling-resource");
    public static void increment() {
        cIR.increment();
    }
    public static void decrement() {
        cIR.decrement();
    }
    public static IdlingResource getIdlingResource() {
        return cIR;
    }
}
