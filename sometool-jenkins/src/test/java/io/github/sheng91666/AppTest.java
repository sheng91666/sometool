package io.github.sheng91666;

import com.alibaba.fastjson.JSON;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import io.github.sheng91666.vo.JenkinsApiVo;
import junit.framework.TestCase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {


    static final String JENKINS_URL = "";
    static final String JENKINS_USERNAME = "";
    static final String JENKINS_PASSWORD = "";
    static final String JENKINS_TOKEN = "";


    /**
     * Rigourous Test :-)
     */
    public void testApp() {
//        Test_JenkinsServer();
//        Test_JenkinsHttpClient();
        Test_GetApi();
    }

    public void Test_JenkinsServer() {
        try {
            JenkinsServer connection = new STJenkinsClient(JENKINS_URL, JENKINS_USERNAME, JENKINS_PASSWORD, JENKINS_TOKEN).GetJenkinsServer();
            Map<String, Job> jobs = connection.getJobs();
            System.out.println(JSON.toJSONString(jobs));
            List<Build> cnzrzBuilds = jobs.get("cnzrz").details().getBuilds();
            String cnzrz1 = jobs.get("cnzrz").details().getBuilds().get(0).details().getConsoleOutputText();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Test_GetApi() {
        try {
            JenkinsApiVo jenkinsApiVo = new STJenkinsClient(JENKINS_URL, JENKINS_USERNAME, JENKINS_PASSWORD, JENKINS_TOKEN).GetApi();
            System.out.println(JSON.toJSONString(jenkinsApiVo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void Test_JenkinsHttpClient() {
        String path = "/api/json?pretty=true";
//         path = "/job/cnzrz/api/json";
//         path = "/job/cnzrz/17/consoleText/api/json";
//         path = "/job/cnzrz/17/api/json";
//         path = "/job/cnzrz/17/api/json?depth=1";

        try {
            JenkinsHttpClient jenkinsHttpClient = new STJenkinsClient(JENKINS_URL, JENKINS_USERNAME, JENKINS_PASSWORD, JENKINS_TOKEN).GetJenkinsHttpClient();
            String s = jenkinsHttpClient.get(path);
            System.out.println(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void testGet() throws Exception {
        String JENKINS_PROJECT_NAME = "cnzrz";
        String url = JENKINS_URL + "/job/" + JENKINS_PROJECT_NAME + "/api/json";
        HttpGet httpGet = new HttpGet(url);
        String res = new STJenkinsClient(JENKINS_URL, JENKINS_USERNAME, JENKINS_PASSWORD, JENKINS_TOKEN).GetHttpMsg(url, httpGet);
        System.out.println(res);
    }

    public void testPost() throws Exception {
        String JENKINS_PROJECT_NAME = "cnzrz";
        String url = JENKINS_URL + "/job/" + JENKINS_PROJECT_NAME + "/api/json";
        HttpPost httpPost = new HttpPost(url);
        String res = new STJenkinsClient(JENKINS_URL, JENKINS_USERNAME, JENKINS_PASSWORD, JENKINS_TOKEN).GetHttpMsg(url, httpPost);
    }


}
