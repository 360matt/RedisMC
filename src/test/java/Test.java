import fr.i360matt.redismc.RedisClient;
import fr.i360matt.redismc.Storage;

public class Test {

    public static void main(String[] args) {

        RedisClient.create((auth) -> {
            auth.setHost("localhost");
            auth.setPort(6379);
            auth.setPassword("");
            auth.setName("rouge");
            auth.setGroup("couleur");
        });

        /*
        RedisHashMap<Integer, String> map = Storage.getMap("liste");
        map.put(1, "rouge");
        map.put(2, "bleu");

        System.out.println(map.get(1));
        System.out.println(map.get(2));

        map.clear();*/


        Storage.getMap("liste", (map2) -> {
            map2.put(1, "rouge");
            map2.put(2, "bleu");

            System.out.println(map2.get(1));
            System.out.println(map2.get(2));

            map2.clear();
        });


    }

}
