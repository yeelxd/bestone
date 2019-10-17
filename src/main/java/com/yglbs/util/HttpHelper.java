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

public class HttpHelper {

	// get 请求
	public static String httpGet(String url, Header[] headers) {
		HttpUriRequest uriRequest = new HttpGet(url);
		if (null != headers)
			uriRequest.setHeaders(headers);
		try (CloseableHttpClient httpClient = declareHttpClientSSL(url)) {
			CloseableHttpResponse httpresponse = httpClient.execute(uriRequest);
			HttpEntity httpEntity = httpresponse.getEntity();
			return EntityUtils.toString(httpEntity, REQ_ENCODEING_UTF8);
		} catch (ClientProtocolException e) {
			out.println(String.format("http请求失败，uri{%s},exception{%s}", url, e));
		} catch (IOException e) {
			out.println(String.format("IO Exception，uri{%s},exception{%s}", url, e));
		}
		return null;
	}

	// post 请求
	public static String httpPost(String url, String params) {
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json;charset=" + REQ_ENCODEING_UTF8);
		// 设置传输编码格式
		StringEntity stringEntity = new StringEntity(params, REQ_ENCODEING_UTF8);
		stringEntity.setContentEncoding(REQ_ENCODEING_UTF8);
		post.setEntity(stringEntity);
		HttpResponse httpresponse;
		try (CloseableHttpClient httpClient = declareHttpClientSSL(url)) {
			httpresponse = httpClient.execute(post);
			HttpEntity httpEntity = httpresponse.getEntity();
			return EntityUtils.toString(httpEntity, REQ_ENCODEING_UTF8);
		} catch (ClientProtocolException e) {
			out.println(String.format("http请求失败，uri{%s},exception{%s}", url, e));
		} catch (IOException e) {
			out.println(String.format("IO Exception，uri{%s},exception{%s}", url, e));
		}
		return null;
	}

	private static CloseableHttpClient declareHttpClientSSL(String url) {
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
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] xcs, String str) {
				}

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

	// this is config
	private static final String REQ_ENCODEING_UTF8 = "utf-8";
	private static PoolingHttpClientConnectionManager httpClientConnectionManager;

	public HttpHelper() {
		httpClientConnectionManager = new PoolingHttpClientConnectionManager();
		httpClientConnectionManager.setMaxTotal(100);
		httpClientConnectionManager.setDefaultMaxPerRoute(20);
	}

	// get 请求
	public static String httpGet(String url) {
		return httpGet(url, null);
	}
}
