package fr.adrackiin.hydranium.uhc.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;

public class StringUtils {

    public static String[] cutString(String message){
        boolean adjust = (float)message.length() / 2F != message.length() / 2;
        return new String[]{message.substring(0, message.length() / 2 + (adjust ? 1 : 0)), message.substring(message.length() / 2 + (adjust ? 1 : 0))};
    }

    public static String[] optimizeCutString(String message, int max){
        if(message.length() < max){
            return cutString(message);
        }
        return new String[]{message.substring(0, max), message.substring(max)};
    }

    public static IChatBaseComponent getIChatBaseComponent(String toConvert){
        return IChatBaseComponent.ChatSerializer.a("{\"APText\":\"" + toConvert + "\"}");
    }

    public static String toIntegerString(float modifier){
        if(String.valueOf(modifier).contains(".")){
            if(MathUtils.isInteger(modifier)){
                return String.valueOf(modifier).split("\\.")[0];
            }
        }
        return String.valueOf(modifier);
    }

}