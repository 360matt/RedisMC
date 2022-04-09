import fr.i360matt.redismc.RedisClient;
import fr.i360matt.redismc.RedisConnection;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        RedisConnection redis = new RedisConnection("localhost", 6379);
        RedisClient clientRouge = RedisClient.create(redis, "rouge", "couleur");
        RedisClient clientFer = RedisClient.create(redis, "fer", "metal");



        clientFer.getMessagingManager().on("hello", String.class, (message) -> {
            System.out.println("Message reçu : " + message);
        });


        Thread.sleep(100);

        System.out.println("Envoi de hello");
        clientRouge.getMessagingManager().send("fer", "hello", "bonsoir");
        System.out.println("Envoyé");
    }

}
