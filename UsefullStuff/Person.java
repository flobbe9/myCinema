public class Person {
    
    private String name;

    private int age;


    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Person && this.name.equals(((Person) obj).getName());
    }


    @Override
    public int hashCode() {
        return this.age;
    }


    @Override
    public String toString() {
        return "Person: " + this.name;
    }
    
    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getAge() {
        return age;
    }
    

    public void setAge(int age) {
        this.age = age;
    }
}