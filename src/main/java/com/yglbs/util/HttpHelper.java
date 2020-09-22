package com.yglbs.util;

import static java.lang.System.out;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http请求方法类
 * @author liuxd
 */
public class HttpHelper {

	private static final Logger log = LoggerFactory.getLogger(HttpHelper.class);

	/**
	 * get 请求
	 */
	public static String httpGet(String url, Header[] headers) {
		HttpUriRequest uriRequest = new HttpGet(url);
		if (null != headers){
			uriRequest.setHeaders(headers);
		}
		try (CloseableHttpClient httpClient = declareHttpClientSsl(url)) {
			CloseableHttpResponse httpresponse = httpClient.execute(uriRequest);
			HttpEntity httpEntity = httpresponse.getEntity();
			return EntityUtils.toString(httpEntity, REQ_ENCODEING_UTF8);
		} catch (ClientProtocolException e) {
			log.info("http请求失败，uri{},exception{}", url, e);
		} catch (IOException e) {
			log.info("IO Exception，uri{},exception{}", url, e);
		}
		return null;
	}

	/**
	 * post 请求
	 */
	public static String httpPost(String url, String params) {
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json;charset=" + REQ_ENCODEING_UTF8);
		// 设置传输编码格式
		StringEntity stringEntity = new StringEntity(params, REQ_ENCODEING_UTF8);
		stringEntity.setContentEncoding(REQ_ENCODEING_UTF8);
		post.setEntity(stringEntity);
		HttpResponse httpresponse;
		try (CloseableHttpClient httpClient = declareHttpClientSsl(url)) {
			httpresponse = httpClient.execute(post);
			HttpEntity httpEntity = httpresponse.getEntity();
			return EntityUtils.toString(httpEntity, REQ_ENCODEING_UTF8);
		} catch (ClientProtocolException e) {
			log.info("http请求失败，uri{},exception{}", url, e);
		} catch (IOException e) {
			log.info("IO Exception，uri{},exception{}", url, e);
		}
		return null;
	}

	private static CloseableHttpClient declareHttpClientSsl(String url) {
		if (url.startsWith("https://")) {
			return sslClient();
		} else {
			return HttpClientBuilder.create().setConnectionManager(httpClientConnectionManager).build();
		}
	}

	/**
	 * 设置SSL请求处理
	 */
	private static CloseableHttpClient sslClient() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String str) {
				}
				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String str) {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
			return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException(e);
		}
	}

	private static final String REQ_ENCODEING_UTF8 = "utf-8";
	private static PoolingHttpClientConnectionManager httpClientConnectionManager;

	public HttpHelper() {
		httpClientConnectionManager = new PoolingHttpClientConnectionManager();
		httpClientConnectionManager.setMaxTotal(100);
		httpClientConnectionManager.setDefaultMaxPerRoute(20);
	}

	public static String httpGet(String url) {
		return httpGet(url, null);
	}
}
