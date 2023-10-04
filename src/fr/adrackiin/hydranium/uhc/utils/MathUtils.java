package fr.adrackiin.hydranium.uhc.utils;

import java.util.Collection;

public class MathUtils {

    public static int getMaxInt(Collection<Integer> ints){
        int max = 0;
        for(int integer : ints){
            if(integer > max){
                max = integer;
            }
        }
        return max;
    }

    public static float getValue(String value){
        return Float.parseFloat(value);
    }

    public static float surround(float toSurround, float min, float max){
        if(toSurround < min){
            return min;
        } else if(toSurround > max){
            return max;
        }
        return toSurround;
    }

    public static boolean isInteger(float modifier){
        return ((int) modifier == modifier);
    }

    public static int roundMin(int toRound, byte roundUnit){
        return toRound - ((int) (toRound % Math.pow(10, roundUnit - 1)));
    }

    public static int addOrMinus(int result, int toAddOrRemove){
        if(result < 0){
            return (result - toAddOrRemove);
        }
        return (result + toAddOrRemove);
    }

}
