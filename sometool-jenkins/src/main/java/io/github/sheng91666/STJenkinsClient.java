package io.github.sheng91666;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.sometool.exception.STException;
import io.github.sheng91666.vo.JenkinsApiVo;
import io.github.sheng91666.vo.WorkflowJobVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class STJenkinsClient {

    private String JENKINS_URL;
    private String JENKINS_USERNAME;
    private String JENKINS_PASSWORD;
    private String JENKINS_TOKEN;

    private JenkinsHttpClient jenkinsHttpClient = null;
    private JenkinsServer jenkinsServer = null;


    public STJenkinsClient(String JENKINS_URL, String JENKINS_USERNAME, String JENKINS_PASSWORD, String JENKINS_TOKEN) {
        this.JENKINS_URL = JENKINS_URL;
        this.JENKINS_USERNAME = JENKINS_USERNAME;
        this.JENKINS_PASSWORD = JENKINS_PASSWORD;
        this.JENKINS_TOKEN = JENKINS_TOKEN;
    }

    /**
     * 获取 JenkinsHttpClient 调用RESTFul风格的api
     *
     * @return
     */
    public JenkinsHttpClient GetJenkinsHttpClient() {
        if (jenkinsHttpClient == null) {
            try {
                jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return jenkinsHttpClient;
    }

    /**
     * 获取 JenkinsServer。
     * JenkinsServer底层也是通过JenkinsHttpClient去调Jenkins
     *
     * @return
     */
    public JenkinsServer GetJenkinsServer() {
        if (jenkinsServer == null) {
            try {
                jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return jenkinsServer;
    }


    /**
     * 通过url调用，不用jenkins的client包。
     *
     * @param url         请求地址
     * @param httpRequest 请求方法对象
     * @return
     */
    public String GetHttpMsg(String url, HttpRequest httpRequest) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort());
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 这边需要注意一下是使用的token代替了密码
        credentialsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(JENKINS_USERNAME, JENKINS_TOKEN));
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicScheme = new BasicScheme();
        authCache.put(host, basicScheme);
        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()) {
            HttpClientContext httpClientContext = HttpClientContext.create();
            httpClientContext.setAuthCache(authCache);
            CloseableHttpResponse response = httpClient.execute(host, httpRequest, httpClientContext);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }


    // 获取job列表、view列表  /api/json?pretty=true
    public JenkinsApiVo GetApi() throws STException {
        JenkinsApiVo resultVo = new JenkinsApiVo();
        try {
            String str = GetJenkinsHttpClient().get("/api/json?pretty=true");
            JSONObject jsonObject = JSONObject.parseObject(str);

            if (!ObjectUtils.isEmpty(jsonObject.get("jobs"))) {
                List<WorkflowJobVo> jobs = JSON.parseArray(JSON.toJSONString(jsonObject.get("jobs")), WorkflowJobVo.class);
                resultVo.setJobs(jobs);
            }

            if (!ObjectUtils.isEmpty(jsonObject.get("views"))) {
                List<WorkflowJobVo> jobs = JSON.parseArray(JSON.toJSONString(jsonObject.get("views")), WorkflowJobVo.class);
                resultVo.setViews(jobs);
            }

            resultVo.setUrl(String.valueOf(jsonObject.get("url")));
            resultVo.setUseCrumbs(Boolean.valueOf(String.valueOf(jsonObject.get("useCrumbs"))));
            resultVo.setUseSecurity(Boolean.valueOf(String.valueOf(jsonObject.get("useSecurity"))));
        } catch (Exception e) {
            throw new STException("STJenkins--GetApi--error", e);
        }
        return resultVo;
    }


    // 获取指定job的详细信息 build 集合 /job/cnzrz/api/json

    //  获取指定job，buildNumber的build详细信息 /job/cnzrz/17/api/json

    // 获取指定job，buildNumber的日志 /job/cnzrz/17/consoleText/api/json


    /**
     * 获取特定job和build信息的API请求。 /job/cnzrz/17/api/json?depth=3
     * depth=3：这是一个查询参数，表示返回结果的深度。在这个例子中，深度为3，意味着返回的结果将包含所有级别的信息，例如构建步骤、构建参数等。
     */
}
