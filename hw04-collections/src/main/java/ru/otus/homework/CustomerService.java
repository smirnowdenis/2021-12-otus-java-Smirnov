package ru.otus.homework;


import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> map = new TreeMap<>((o1, o2) -> (int) (o1.getScores() - o2.getScores()));

    //todo: 3. надо реализовать методы этого класса

    public Map.Entry<Customer, String> getSmallest() {
        var customer = map.firstKey();

        var id = customer.getId();
        var name = customer.getName();
        var scores = customer.getScores();

        return new AbstractMap.SimpleEntry<>(new Customer(id, name, scores), map.firstEntry().getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = map.higherEntry(customer);

        if (entry == null) {
            return null;
        }

        var id = entry.getKey().getId();
        var name = entry.getKey().getName();
        var scores = entry.getKey().getScores();

        return new AbstractMap.SimpleEntry<>(new Customer(id, name, scores), entry.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
