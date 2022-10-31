import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


class Test {

    public static void main(String[] args) {
        Set<Integer> set = new LinkedHashSet<>();
        List<Person> list = new LinkedList<>();
        Map<Person, String> map = new HashMap<>();
        Deque<Integer> deque = new ArrayDeque<>();
        Queue<Integer> queue = new PriorityQueue<>();

        Person person = new Person("Flo", 21);
        Person person2 = new Person("Flo", 21);


        map.put(person, "Hello ");
        map.put(person2, "World");
        
        System.out.println(map);


        set.add(1);
        set.add(2);
        set.add(4);
        
        System.out.println(set);



        
        
    }
}