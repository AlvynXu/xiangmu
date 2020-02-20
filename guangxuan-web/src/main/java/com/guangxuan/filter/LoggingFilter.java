//package com.guangxuan.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * @author zhuolin
// * @Date 2019/12/6
// */
//@Component
//public class LoggingFilter extends OncePerRequestFilter {
//
//    private Logger logger = LoggerFactory.getLogger("requestLogger");
//
//    private static final String REQUEST_PREFIX = "Request: ";
//    private static final String RESPONSE_PREFIX = "Response: ";
//    private AtomicLong id = new AtomicLong(1);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
//        long requestId = id.incrementAndGet();
//        request = new RequestWrapper(requestId, request);
//        logRequest(request);
//        response = new ResponseWrapper(requestId, response);
//        try {
//            filterChain.doFilter(request, response);
////            response.flushBuffer();
//        } finally {
//            logResponse((ResponseWrapper) response);
//        }
//
//    }
//
//    private void logRequest(final HttpServletRequest request) {
//        logger.info("================================================================================================================start");
//        StringBuilder msg = new StringBuilder();
//        msg.append(REQUEST_PREFIX);
//        if (request instanceof RequestWrapper) {
//            msg.append("request id=").append(((RequestWrapper) request).getId()).append("; ");
//        }
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            msg.append("session id=").append(session.getId()).append("; ");
//        }
//        if (request.getMethod() != null) {
//            msg.append("method=").append(request.getMethod()).append("; ");
//        }
//        if (request.getContentType() != null) {
//            msg.append("content type=").append(request.getContentType()).append("; ");
//        }
//        msg.append("uri=").append(request.getRequestURI());
//        if (request.getQueryString() != null) {
//            msg.append('?').append(request.getQueryString());
//        }
//
//        if (request instanceof RequestWrapper && !isMultipart(request) && !isBinaryContent(request)) {
//            RequestWrapper requestWrapper = (RequestWrapper) request;
//            try {
//                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() :
//                        "UTF-8";
//                msg.append("; payload=").append(new String(requestWrapper.toByteArray(), charEncoding));
//            } catch (UnsupportedEncodingException e) {
//                logger.warn("Failed to parse request payload", e);
//            }
//
//        }
//        logger.debug(msg.toString());
//    }
//
//    private boolean isBinaryContent(final HttpServletRequest request) {
//        if (request.getContentType() == null) {
//            return false;
//        }
//        return request.getContentType().startsWith("image") || request.getContentType().startsWith("video") || request.getContentType().startsWith("audio");
//    }
//
//    private boolean isMultipart(final HttpServletRequest request) {
//        return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
//    }
//
//    private void logResponse(final ResponseWrapper response) {
//        StringBuilder msg = new StringBuilder();
//        msg.append(RESPONSE_PREFIX);
//        msg.append("request id=").append((response.getId()));
//        try {
//            msg.append("; payload=").append(new String(response.toByteArray(), response.getCharacterEncoding()));
//        } catch (UnsupportedEncodingException e) {
//            logger.warn("Failed to parse response payload", e);
//        }
//        logger.debug(msg.toString());
//        logger.info("================================================================================================================end");
//
//    }
//
//
//}
