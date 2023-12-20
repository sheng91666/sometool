package com.sometool;


import com.jcraft.jsch.*;
import com.sometool.util.R;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;


public class SSHClient {

    private String host = "localhost";

    private Integer port = 22;

    private String username = "";

    private String password = "";

    /**
     * SSH Login pubKeyPath，这里如果要用密钥登录的话，是你本机的或者项目所在服务器的私钥地址
     * private String pubKeyPath = "/root/.ssh/id_rsa";
     */
    private String pubKeyPath = "";

    private JSch jsch = null;

    private Session session = null;

    private Channel channel = null;

    private final Integer SESSION_TIMEOUT = 60000;

    private final Integer CHANNEL_TIMEOUT = 60000;

    private final Integer CYCLE_TIME = 100;

    public SSHClient() throws JSchException {
        jsch = new JSch();
    }

    public SSHClient setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
        return this;
    }

    public SSHClient setHost(String host) {
        this.host = host;
        return this;
    }

    public SSHClient setPort(Integer port) {
        this.port = port;
        return this;
    }

    public SSHClient setUsername(String username) {
        this.username = username;
        return this;
    }

    public SSHClient setPassword(String password) {
        this.password = password;
        return this;
    }

    public Session getSession() {
        return this.session;
    }

    public Channel getChannel() {
        return this.channel;
    }

    /**
     * login to server
     */
    public void login(String username, String password, String host, Integer port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        try {
            if (null == session) {
                session = jsch.getSession(this.username, this.host, this.port);

                if (!ObjectUtils.isEmpty(pubKeyPath) && new File(pubKeyPath).exists()) {
                    jsch.addIdentity(pubKeyPath);
                } else {
                    session.setPassword(this.password);
                }
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("userauth.gssapi-with-mic", "no");
            }
            session.connect(SESSION_TIMEOUT);
        } catch (JSchException e) {
            e.printStackTrace();
            this.logout();
        }
    }


    public void login() {
        this.login(this.username, this.password, this.host, this.port);
    }


    public void logout() {
        this.session.disconnect();
    }

    public synchronized R sendCmd(String command) {
        if (!session.isConnected()) {
            this.login();
        }
        if (this.session == null || !session.isConnected()) {
            return R.error("sendCmd--session连接失败！");
        }

        R r = new R();

        Channel channel = null;

        try {
            channel = this.session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            InputStream errStream = ((ChannelExec) channel).getErrStream();

            channel.connect();

            //成功返回值
            r = doExecResponse(in);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error("sendCmd--失败：" + e.getMessage());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        return r;
    }


    private R doExecResponse(InputStream inputStream) {
        String resp = "";
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                resp += line + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("读取shell执行结果失败：" + e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return R.ok(resp);
    }

//    public synchronized R uploadFile(LinuxServerCommand command) {
//        log.info("SSHClient--uploadFile--command:{}", JSON.toJSONString(command));
//        if (!session.isConnected()) {
//            this.login();
//        }
//        if (this.session == null) {
//            return null;
//        }
//
//        ChannelSftp channelSftp = null;
//        try {
//            channelSftp = (ChannelSftp) session.openChannel("sftp");
//            channelSftp.connect();
//            channelSftp.cd("/");
//            if (command.getFilePath() != null && command.getFilePath().trim().length() > 0) {
//                String[] split = command.getFilePath().split("/");
//
//                for (String mulu : split) {
//                    if (!"".equals(mulu)) {
//                        try {
//                            channelSftp.mkdir(mulu);
//                            channelSftp.cd(mulu);
//                        } catch (SftpException e) {
//                            channelSftp.cd(mulu);
//                        }
//                    }
//                }
//
//            }
//
//            FileInputStream fileInputStream = new FileInputStream(new File(command.getNginxPath()));
//            channelSftp.put(fileInputStream, command.getNginxFileName());
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("SSHClient--uploadFile--error:{}", e.getMessage());
//            return R.error("SSHClient--uploadFile--失败：" + e.getMessage());
//        } finally {
//            if (channelSftp != null) {
//                channelSftp.disconnect();
//            }
//        }
//
//        return R.ok();
//
//    }
//
//    public synchronized R dowloadFile(ConfigsXiafaInfo info) {
//        log.info("SSHClient--downLoadFile--info:{}", JSON.toJSONString(info));
//        if (!session.isConnected()) {
//            this.login();
//        }
//        if (this.session == null) {
//            return null;
//        }
//
//        ChannelSftp channelSftp = null;
//        try {
//            channelSftp = (ChannelSftp) session.openChannel("sftp");
//            channelSftp.connect();
//            channelSftp.cd("/");
//
//            //判断备份路径是否存在
//            if (!FileUtil.exist(info.getBakPath())) {
//                FileUtil.mkdir(info.getBakPath());
//            }
//
//            String localPath = info.getBakPath() + "/" + info.getCreateTime() + "_" + info.getFileName();
//            String linuxPath = info.getFileTargetPath() + "/" + info.getFileName();
//
//            channelSftp.get(linuxPath, localPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("SSHClient--downLoadFile--失败:{}", e.getMessage());
//            return R.error("下载配置文件失败" + e.getMessage());
//        } finally {
//            if (channelSftp != null) {
//                channelSftp.disconnect();
//            }
//        }
//
//        return R.ok();
//    }
}
