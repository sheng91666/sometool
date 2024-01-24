package io.github.sheng91666;

public class JenkinsClient {

    private String ip;

    private String port;

    private String token;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setToken(String token) {
        this.token = token;
    }


    //http://10.0.15.6:8080/api/json?pretty=true

    //http://10.0.15.6:8080/job/cnzrz/api/json

    //http://10.0.15.6:8080/job/cnzrz/17/consoleText/api/json

    //http://10.0.15.6:8080/job/cnzrz/17/api/json

    //http://10.0.15.6:8080/job/cnzrz/17/api/json?depth=3

}
