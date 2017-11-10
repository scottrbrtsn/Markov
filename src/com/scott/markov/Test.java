package com.scott.markov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Test {


    public static void main(String[] args) throws IOException {

        Set<VoiceAndText> set1 = new TreeSet<>(Comparator.comparing(VoiceAndText::getKey));
                                //key, value
//        set1.add(new VoiceAndText("1", "one"));
//        set1.add(new VoiceAndText("2", "one"));
//        set1.add(new VoiceAndText("3", "one"));
//        set1.add(new VoiceAndText("4", "one"));

//        set1.add(new VoiceAndText("1", "two"));
//        set1.add(new VoiceAndText("2", "two"));
//        set1.add(new VoiceAndText("3", "two"));
//        set1.add(new VoiceAndText("4", "two"));

        List<VoiceAndText> list1 = new ArrayList<>();
        list1.add(new VoiceAndText("1", "three"));
        list1.add(new VoiceAndText("2", "three"));
        list1.add(new VoiceAndText("3", "three"));
        list1.add(new VoiceAndText("4", "three"));

        List<VoiceAndText> list2 = new ArrayList<>();
        list2.add(new VoiceAndText("1", "four"));
        list2.add(new VoiceAndText("2", "four"));
        list2.add(new VoiceAndText("3", "four"));
        list2.add(new VoiceAndText("4", "four"));

        List<VoiceAndText> list3 = new ArrayList<>();
        list3.add(new VoiceAndText("1", "five"));
        list3.add(new VoiceAndText("2", "five"));
        list3.add(new VoiceAndText("3", "five"));
        list3.add(new VoiceAndText("4", "five"));

    //    set1.addAll(list1);
        set1.addAll(list2);
        set1.addAll(list3);

        for (int i = 0; i < 10000; i++) {
            set1.addAll(list3);
            set1.addAll(list2);
            set1.addAll(list3);

        }

        set1.forEach(s -> System.out.println(s.getValue()));

    }

}

