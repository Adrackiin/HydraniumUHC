package fr.adrackiin.hydranium.uhc.utils;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.management.redis.RedisAccess;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.UUID;

public class PubSub {

    private static final RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();

    public static void resetUHC(){
        RTopic stringRTopic = redissonClient.getTopic("ResetUHC");
        long clientsReceivedMessage = stringRTopic.publish("");
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: ResetUHC");
        }
    }

    public static void commandSendHost(String player){
        RTopic<String> stringRTopic = redissonClient.getTopic("CommandSendHost");
        long clientsReceivedMessage = stringRTopic.publish(player);
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: CommandSendHost");
        }
    }

    public static void addHost(APPlayer player){
        RTopic<String> stringRTopic = redissonClient.getTopic("AddHost");
        long clientsReceivedMessage = stringRTopic.publish(player.getUUID().toString());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: AddHost");
        }
    }

    public static void removeHost(APPlayer player){
        RTopic<String> stringRTopic = redissonClient.getTopic("RemoveHost");
        long clientsReceivedMessage = stringRTopic.publish(player.getUUID().toString());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: RemoveHost");
        }
    }

    public static void sendWhitelist(){
        RTopic<String> stringRTopic = redissonClient.getTopic("SendWhitelist");
        StringBuilder message = new StringBuilder();
        for(UUID uuid : Game.getGame().getWhitelistedPlayers().copy()){
            message.append(uuid).append("²");
        }
        long clientsReceivedMessage = stringRTopic.publish(message.toString());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: SendWhitelist");
        }
    }

    public static void changeGameStatus(){
        RTopic<String> stringRTopic = redissonClient.getTopic("ChangeStatus");
        long clientsReceivedMessage = stringRTopic.publish(Game.getGame().getGameStateManager().getGameState().toString());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: ChangeStatus");
        }
    }

    public static void playersUpdate(){
        RTopic<Integer> integerRTopic = redissonClient.getTopic("PlayersUpdate");
        long clientsReceivedMessage = integerRTopic.publish(Game.getGame().getAlivePlayers().getSize());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: PlayersUpdate");
        }
    }

    public static void maxPlayersUpdate(){
        RTopic<Integer> integerRTopic = redissonClient.getTopic("MaxPlayersUpdate");
        long clientsReceivedMessage = integerRTopic.publish((int)Game.getGame().getSettings().getMaxPlayers());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: MaxPlayersUpdate");
        }
    }

    public static void pvpUpdate(){
        RTopic<Integer> integerRTopic = redissonClient.getTopic("PvPUpdate");
        long clientsReceivedMessage = integerRTopic.publish((int)Game.getGame().getSettings().getPvp());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: PvPUpdate");
        }
    }

    public static void borderUpdate(){
        RTopic<Integer> integerRTopic = redissonClient.getTopic("BorderUpdate");
        long clientsReceivedMessage = integerRTopic.publish((int)Game.getGame().getSettings().getBorder());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: BorderUpdate");
        }
    }

    public static void teamUpdate(){
        RTopic<Integer> integerRTopic = redissonClient.getTopic("TeamUpdate");
        long clientsReceivedMessage = integerRTopic.publish((int)Game.getGame().getSettings().getTeam());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: TeamUpdate");
        }
    }

    public static void addScenario(Scenario sc){
        RTopic<String> stringRTopic = redissonClient.getTopic("AddScenario");
        long clientsReceivedMessage = stringRTopic.publish(sc.getName());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: AddScenario");
        }
    }

    public static void removeScenario(Scenario sc){
        RTopic<String> stringRTopic = redissonClient.getTopic("RemoveScenario");
        long clientsReceivedMessage = stringRTopic.publish(sc.getName());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: RemoveScenario");
        }
    }

}
