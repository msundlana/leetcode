package main;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListExample
{
    public static void main(String[] args) throws Exception
    {
        ArrayList<String> listOne = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "f"));

        ArrayList<String> listTwo = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));

        listOne.addAll(listTwo);    //Merge both lists

        System.out.println(listOne);
    }
}