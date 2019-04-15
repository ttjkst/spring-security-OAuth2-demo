package org.github.securityDemo.core.authority;

import java.io.Serializable;

/**
 * 中使用的时候会被转化为
 *@see AuthorityAttr
 *
 * */
public class AuthorityEntity implements Serializable {

    private String path;

    private String authority;

    private Boolean caseSensitive;

    private String httpMethod;

    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    private Boolean any;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Boolean getAny() {
        return any==null?false:any;
    }

    public void setAny(Boolean any) {
        this.any = any;
    }
}