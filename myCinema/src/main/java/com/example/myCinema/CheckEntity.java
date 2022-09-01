package com.example.myCinema;


public class CheckEntity {

    public boolean objectNullOrEmpty(Object obj) {

        // null
        if (obj == null) return true;

        // empty string
        if (obj instanceof String && obj.equals("")) return true;

        return false;
    }


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