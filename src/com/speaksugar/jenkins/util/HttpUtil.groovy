package com.speaksugar.jenkins.util

import com.speaksugar.jenkins.exception.HttpUtilException
import groovy.json.JsonException
import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class HttpUtil {

    static Object post(String url, Object data) {
        CloseableHttpClient httpClient = HttpClients.createDefault()
        try {
            HttpPost httpPost = new HttpPost(url)
            httpPost.setEntity(new StringEntity(JsonOutput.toJson(data), ContentType.APPLICATION_JSON))
            HttpResponse response = httpClient.execute(httpPost)
            int statusCode = response.getStatusLine().statusCode
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8")
            if (statusCode >= 300) {
                throw new HttpUtilException("status code = ${statusCode}, res msg = ${responseBody}")
            }
            try {
                return new JsonSlurperClassic().parseText(responseBody)
            } catch (JsonException | IllegalArgumentException e) {
                return responseBody
            }
        } finally {
            httpClient.close()
        }
    }

    static Object get(URIBuilder uriBuilder) {
        CloseableHttpClient httpClient = HttpClients.createDefault()
        try {
            HttpGet httpGet = new HttpGet(uriBuilder.build())
            HttpResponse response = httpClient.execute(httpGet)
            int statusCode = response.getStatusLine().statusCode
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8")
            if (statusCode >= 300) {
                throw new HttpUtilException("status code = ${statusCode}, res msg = ${responseBody}")
            }
            try {
                return new JsonSlurperClassic().parseText(responseBody)
            } catch (JsonException | IllegalArgumentException e) {
                return responseBody
            }
        } finally {
            httpClient.close()
        }
    }

    static Object delete(URIBuilder uriBuilder) {
        CloseableHttpClient httpClient = HttpClients.createDefault()
        try {
            HttpDelete httpDelete = new HttpDelete(uriBuilder.build())
            HttpResponse response = httpClient.execute(httpDelete)
            int statusCode = response.getStatusLine().statusCode
            HttpEntity entity = response.getEntity()
            String responseBody = "{}"
            if (entity != null) {
                responseBody = EntityUtils.toString(entity, "UTF-8")
            }
            if (statusCode >= 300) {
                throw new HttpUtilException("status code = ${statusCode}, res msg = ${responseBody}")
            }
            try {
                return new JsonSlurperClassic().parseText(responseBody)
            } catch (JsonException | IllegalArgumentException e) {
                return responseBody
            }
        } finally {
            httpClient.close()
        }
    }

}