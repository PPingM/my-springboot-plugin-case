package com.rxx.springdataredis.util;

import com.rxx.springdataredis.Entry.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/3/21 8:16
 */
public class PersonUtil {

    public static Person getOnePerson(String name){
        return new Person(name, 1, "男");
    }

    public static List<Person> getPersons(String name, int size){
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            persons.add(new Person(name + i, i, "男"));
        }
        return persons;
    }
}
