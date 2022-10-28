package fr.i360matt.redismc;


public class RedisAuth {

    private String host;
    private int port;
    private String password;

    public RedisAuth () {
        // nothing
    }

    public RedisAuth (String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public RedisAuth (String host, int port) {
        this(host, port, null);
    }

    public String getHost () {
        return host;
    }

    public int getPort () {
        return port;
    }

    public String getPassword () {
        return password;
    }

    public void setHost (String host) {
        this.host = host;
    }

    public void setPort (int port) {
        this.port = port;
    }

    public void setPassword (String password) {
        this.password = password;
    }

}
