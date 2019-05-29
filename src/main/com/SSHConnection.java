package main.com;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.Properties;

public class SSHConnection {

    public static Session session;
    public static String host;
    public static String username;
    public static String password;

    public static void connection() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, 22);
            Properties config = new Properties();
            config.setProperty("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public static void sendingCommands(String command) {
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);

            channelExec.connect();

            channelExec.disconnect();

        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}