package org.oauth.authoriaztion.authority;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分装的权限关系的类型,在
 * @see org.oauth.authoriaztion.authority.AuthorityResource
 * 中使用的时候会被转化为
 *@see org.oauth.authoriaztion.authority.AuthorityAttr
 *
 * */
public class AuthorityEntity {

    private String path;

    private String authority;

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