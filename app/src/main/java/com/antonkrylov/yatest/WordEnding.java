package com.antonkrylov.yatest;

/*
    WordEnding - класс, содержащий 2 статических метода, необходимых для создания окончаний слов "альбомы" и "песни".
*/
public class WordEnding {
    public static String albumEnding(Integer a) {
        int i = Math.abs(a);
        String ending = "";
        if (i == 1) ending = "";
        else if (i == 0) ending = "ов";
        else if (i >=2 & i<=4) ending = "а";
        else if (i >=5 & i<=20) ending = "ов";
        else if (i % 10 == 1) ending = "";
        else if (i % 10 >= 2 & i % 10 <= 4) ending = "а";
        else ending = "ов";
        return ending;
    }

    public static String songEnding(Integer a) {
        int i = Math.abs(a);
        String ending = "";
        if (i == 1) ending = " ня";
        else if (i == 0) ending = "ен";
        else if (i >=2 & i<=4) ending = "ни";
        else if (i >=5 & i<=20) ending = "ен";
        else if (i % 10 == 1) ending = "ня";
        else if (i % 10 >= 2 & i % 10 <= 4) ending = "ни";
        else ending = "ен";
        return ending;
    }
}
