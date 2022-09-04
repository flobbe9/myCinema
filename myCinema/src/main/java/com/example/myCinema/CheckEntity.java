package com.example.myCinema;


/**
 * Contains several check methods for objects as well as iterables.
 */
public class CheckEntity {

    /**
     * Checks if object is null or, if the object is a string, whether the string is empty ("").
     * 
     * @param obj to check.
     * @return false if object is not null and is not an empty string.
     */
    public boolean objectNullOrEmpty(Object obj) {

        // null
        if (obj == null) return true;

        // empty string
        if (obj instanceof String && obj.equals("")) return true;

        return false;
    }


    /**
     * Uses the {@link #objectNullOrEmpty(Object)} method on every object in the iterable.
     * Also checks if iterable itself is null.
     * 
     * @param iterable iterable to check.
     * @return false if no null value or empty string was found.
     */
    public boolean iterableNullOrEmpty(Iterable<?> iterable) {

        // null
        if (iterable == null) return true;

        // making a wrapper object for counts, so they can be altered in different scope
        var countWrapper = new Object() { int count1 = 0; int count2 = 0; };

        // checking single elements
        iterable.forEach(element -> {
            // null or empty
            if (objectNullOrEmpty(element)) countWrapper.count1++;

            // count2 for list not empty
            countWrapper.count2++;
        });

        // contains null element
        if (countWrapper.count1 != 0) return true;

        // iterable emtpy
        if (countWrapper.count2 == 0) return true;

        return false;
    }
}