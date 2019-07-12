package org.apache.skywalking.apm.webapp.compont;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Liu-XinYuan
 */
@Component
@ConfigurationProperties(prefix = "sso")
public class SSOConfiguration {
    private String ssologin;
    private String redicturl;
    private String skywalkingCallback;
    private String key;
    private String secret;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSkywalkingCallback() {
        return skywalkingCallback;
    }

    public void setSkywalkingCallback(String skywalkingCallback) {
        this.skywalkingCallback = skywalkingCallback;
    }

    public String getRedicturl() {
        return redicturl;
    }

    public void setRedicturl(String redicturl) {
        this.redicturl = redicturl;
    }

    public String getSsologin() {
        return ssologin;
    }

    public void setSsologin(String ssologin) {
        this.ssologin = ssologin;
    }
}
