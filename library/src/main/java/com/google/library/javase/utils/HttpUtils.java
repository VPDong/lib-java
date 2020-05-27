package com.google.library.javase.utils;

import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class HttpUtils {
    private HttpUtils() throws Exception {
        throw new IllegalAccessException("can not to init instance for http utils");
    }

    public static <T> T doGetJson(String url, final Map<String, String> params, Map<String, String> headers, final Type type) {
        if (url == null) throw new IllegalArgumentException("url can not be null");
        if (params != null) {
            url = url.endsWith("?") ? url : String.format("%s?", url);
            StringBuilder argument = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                argument.append(String.format("%s=%s&", param.getKey(), param.getValue()));
            }
            url = url + argument.toString();
        }
        return doHttp(url, "GET", headers, new HttpProcessor<T>() {
            @Override
            public T onConnected(HttpURLConnection conn) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    for (String line; (line = br.readLine()) != null; ) {
                        sb.append(line);
                    }
                    return JsonUtils.fromJson(sb.toString(), type);
                } catch (Exception e) {
                    return onFailed(-2, e.getMessage());
                } finally {
                    FileUtils.close(br);
                }
            }
        });
    }

    public static boolean doGetFile(String url, Map<String, String> headers, final File file) {
        if (url == null || file == null) throw new IllegalArgumentException("url or file can not be null");
        return HttpUtils.doHttp(url, "GET", headers, new HttpProcessor<Boolean>() {
            @Override
            public Boolean onConnected(HttpURLConnection connection) {
                InputStream is = null;
                try {
                    is = connection.getInputStream();
                    return FileUtils.setContent(file, is, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    FileUtils.close(is);
                }
            }
        });
    }

    public static <T> T doPostJson(String url, Map<String, String> headers, final Object body, final Type type) {
        if (url == null) throw new IllegalArgumentException("url can not be null");
        return doHttp(url, "POST", headers, new HttpProcessor<T>() {
            @Override
            public T onConnected(HttpURLConnection conn) {
                if (body != null) {
                    OutputStream os = null;
                    try {
                        os = conn.getOutputStream();
                        if (body instanceof byte[]) {
                            os.write((byte[]) body);
                        } else if (body instanceof String) {
                            os.write(((String) body).getBytes(Charset.forName("utf-8")));
                        } else {
                            String data = JsonUtils.toJson(body, null);
                            if (data != null) os.write(data.getBytes());
                        }
                        os.flush();
                    } catch (Exception e) {
                        return onFailed(-2, e.getMessage());
                    } finally {
                        FileUtils.close(os);
                    }
                }
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    for (String line; (line = br.readLine()) != null; ) {
                        sb.append(line);
                    }
                    return JsonUtils.fromJson(sb.toString(), type);
                } catch (Exception e) {
                    return onFailed(-2, e.getMessage());
                } finally {
                    FileUtils.close(br);
                }
            }
        });
    }

    public static <T> T doPostForm(String url, Map<String, String> headers, final Map<String, Object> body, final Type type) {
        if (url == null) throw new IllegalArgumentException("url or body can not be null");
        final String boundary = "------------------------" + System.currentTimeMillis();
        if (headers == null) headers = new LinkedHashMap<>();
        headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);
        return doHttp(url, "POST", headers, new HttpProcessor<T>() {
            @Override
            public T onConnected(HttpURLConnection conn) {
                if (body != null) {
                    OutputStream os = null;
                    try {
                        os = conn.getOutputStream();
                        StringBuilder content = new StringBuilder();
                        for (Map.Entry<String, Object> entry : body.entrySet()) {
                            content.delete(0, content.length());
                            if (entry == null || entry.getKey() == null || entry.getValue() == null) continue;
                            if (entry.getValue() instanceof File) {
                                content.append("--").append(boundary).append("\r\n")
                                        .append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"",
                                                entry.getKey().trim(), ((File) entry.getValue()).getName())).append("\r\n")
                                        .append("Content-Type: application/octet-stream").append("\r\n")
                                        .append("\r\n");
                                os.write(content.toString().getBytes());
                                os.write(FileUtils.getContent((File) entry.getValue()).getBytes());
                                os.write("\r\n".getBytes());
                                os.flush();
                            } else {
                                content.append("--").append(boundary).append("\r\n")
                                        .append(String.format("Content-Disposition: form-data; name=\"%s\"", entry.getKey().trim())).append("\r\n")
                                        .append("\r\n")
                                        .append(entry.getValue()).append("\r\n");
                                os.write(content.toString().getBytes());
                                os.flush();
                            }
                        }
                        content.delete(0, content.length());
                        content.append("--").append(boundary).append("--").append("\r\n");
                        os.write(content.toString().getBytes());
                        os.flush();
                    } catch (Exception e) {
                        return onFailed(-2, e.getMessage());
                    } finally {
                        FileUtils.close(os);
                    }
                }
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    for (String line; (line = br.readLine()) != null; ) {
                        sb.append(line);
                    }
                    return JsonUtils.fromJson(sb.toString(), type);
                } catch (Exception e) {
                    return onFailed(-2, e.getMessage());
                } finally {
                    FileUtils.close(br);
                }
            }
        });
    }

    public static <T> T doHttp(String url, String method, Map<String, String> headers, HttpProcessor<T> processor) {
        if (url == null || method == null || processor == null) return null;
        if (!url.startsWith("http")) url = String.format("http://%s", url.trim());
        HttpURLConnection connection = null;
        try {
            // 打开连接
            connection = (HttpURLConnection) (new URL(url)).openConnection();
            if (url.startsWith("https")) {
                doNotVerify((HttpsURLConnection) connection);
                trustAllHosts((HttpsURLConnection) connection);
            }
            // 设置方式
            connection.setRequestMethod(method.trim().toUpperCase());
            // 设置头部
            connection.setUseCaches(false);
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            if (!headers.containsKey("Accept")) {
                headers.put("Accept", "*/*");
            }
            if (!headers.containsKey("Charset")) {
                headers.put("Charset", "UTF-8");
            }
            if (!headers.containsKey("Connection")) {
                headers.put("Connection", "Keep-Alive");
            }
            if (!headers.containsKey("Content-Type")) {
                headers.put("Content-Type", "application/json");
            }
            if (!headers.containsKey("User-Agent")) {
                headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) continue;
                connection.setRequestProperty(entry.getKey().trim(), entry.getValue().trim());
            }
            // 设置通道
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 发送数据
            T result = processor.onConnected(connection);
            int respCode = connection.getResponseCode();
            switch (respCode) {
                case 302:
                case 301:
                    return doHttp(url, method, headers, processor);
                case 200:
                    return result;
                default:
                    return processor.onFailed(respCode, connection.getResponseMessage());
            }
        } catch (Exception e) {
            return processor.onFailed(-1, e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 设置不验证主机
     */
    private static HostnameVerifier doNotVerify(HttpsURLConnection connection) {
        if (connection == null) return null;
        HostnameVerifier oldHostnameVerifier = connection.getHostnameVerifier();
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        return oldHostnameVerifier;
    }

    /**
     * 信任所有主机
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        if (connection == null) return null;
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) {
                    // ignored
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) {
                    // ignored
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }}, new SecureRandom());
            SSLSocketFactory newFactory = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception ignored) {
            // ignored
        }
        return oldFactory;
    }

    public static abstract class HttpProcessor<T> {
        public abstract T onConnected(HttpURLConnection connection);

        public T onFailed(int code, String msg) {
            String error = String.format(Locale.CHINA, "%d:%s", code, msg);
            System.err.println(error);
            return null;
        }
    }
}
