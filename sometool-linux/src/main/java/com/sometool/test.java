package com.sometool;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.JSchException;
import com.sometool.util.R;

public class test {
    public static void main(String[] args) {
        testSSHClient("ls");
    }

    public static void testSSHClient(String shellStr) {
        SSHClient sshClient = null;
        try {
            sshClient = new SSHClient();
            sshClient.setHost("").setPort(22).setUsername("root").setPassword("");
            sshClient.login();

            R r = sshClient.sendCmd(shellStr);
            System.out.println("###############################");
            System.out.println(JSON.toJSONString(r));
            System.out.println("###############################");

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            sshClient.logout();
        }
    }
}
