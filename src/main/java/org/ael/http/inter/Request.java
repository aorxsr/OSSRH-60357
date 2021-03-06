package org.ael.http.inter;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.CharsetUtil;
import lombok.NonNull;
import org.ael.constant.HttpConstant;
import org.ael.http.MultiPartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: aorxsr
 * @Date: 2019/7/18 18:26
 */
public interface Request {

    String getMethod();

    String getUri();

    String getHost();

    String getRemoteAddress();

    String getUrl();

    Object getParameter(String name);

    Map<String, List<String>> getParameters();

    Object getPathParam(String name);

    MultiPartFile getMultiPartFile(String name);

    boolean isUseGZIP();

    Session getSession();

    boolean isASESSION();

    void setASESSION(boolean asession);

    void initRequest(String remoteAddress);

    default String contentType() {
        String contentType = header(HttpConstant.CONTENT_TYPE);
        return null != contentType ? contentType : "Unknown";
    }

    default boolean isIE() {
        String ua = userAgent();
        return ua.contains("MSIE") || ua.contains("TRIDENT");
    }

    default boolean isAjax() {
        return "XMLHttpRequest".equals(header("X-Requested-With")) || "XMLHttpRequest".equals(header("x-requested-with"));
    }

    default String userAgent() {
        return header(HttpConstant.USER_AGENT);
    }

    Map<String, Cookie> cookies();

    default String getCookieValue(@NonNull String name) {
        Cookie cookie = cookies().get(name);
        return null == cookie ? null : cookie.value();
    }

    default Cookie getCookie(@NonNull String name) {
        return cookies().get(name);
    }

    Request setCookie(Cookie cookie);

    Map<String, String> getHeaders();

    default String header(@NonNull String name) {
        String header = "";
        if (getHeaders().containsKey(name)) {
            header = getHeaders().get(name);
        } else if (getHeaders().containsKey(name.toLowerCase())) {
            header = getHeaders().get(name.toLowerCase());
        }
        return header;
    }

    boolean isKeepAlive();

    boolean isMultipart();

    ByteBuf body();

    default String bodyToString() {
        return this.body().toString(CharsetUtil.UTF_8);
    }

}
