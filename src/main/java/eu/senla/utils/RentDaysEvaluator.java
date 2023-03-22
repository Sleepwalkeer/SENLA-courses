package eu.senla.utils;

public class RentDaysEvaluator {

    private static final long SECONDS_IN_DAY = 86400L;

    public static int count(long seconds) {
        double rentSeconds = seconds;
        return (int) Math.ceil(rentSeconds / SECONDS_IN_DAY);
    }
}
