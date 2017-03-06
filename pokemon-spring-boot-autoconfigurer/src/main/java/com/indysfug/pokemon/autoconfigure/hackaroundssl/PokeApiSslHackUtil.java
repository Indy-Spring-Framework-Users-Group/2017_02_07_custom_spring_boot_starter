package com.indysfug.pokemon.autoconfigure.hackaroundssl;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import kotlin.jvm.functions.Function1;
import me.sargunvohra.lib.pokekotlin.client.ClientConfig;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @author russell.scheerer
 */
public class PokeApiSslHackUtil {

    public static ClientConfig clientConfig() {
        // Begin ridiculous hack to get around my employer's man in the middle cert hack
        HttpUrl pokeV2ApiUrl = HttpUrl.parse("https://pokeapi.co/api/v2/");

        ClientConfig config = new ClientConfig(pokeV2ApiUrl, new Function1<OkHttpClient.Builder, OkHttpClient.Builder>() {
            @Override
            public OkHttpClient.Builder invoke(OkHttpClient.Builder builder) {
                try {
                    final TrustManager[] trustAllCerts = new TrustManager[] {
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return new java.security.cert.X509Certificate[0];
                                }
                            }
                    };
                    // Install the all-trusting trust manager
                    final SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                    // Create an ssl socket factory with our all-trusting manager
                    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                    return new OkHttpClient.Builder()
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String s, SSLSession sslSession) {
                                    return true;
                                }
                            })
                            .sslSocketFactory(sslSocketFactory)
                            .retryOnConnectionFailure(false)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // End ridiculous hack to get around my employer's man in the middle cert hack
        return config;
    }

}
