package com.wishare.finance.infrastructure.support;

import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.interfaces.ApiBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public class ApiData {

   public static ApiBase API = new ApiBase() {
        @Override
        public Optional<IdentityInfo> getIdentityInfo() {
            return ApiBase.super.getIdentityInfo();
        }

        @Override
        public IdentityInfo curIdentityInfo() {
            return ApiBase.super.curIdentityInfo();
        }

        @Override
        public Optional<String> getClient() {
            return ApiBase.super.getClient();
        }

        @Override
        public Optional<String> getGateway() {
            return ApiBase.super.getGateway();
        }

        @Override
        public Optional<String> getTenantId() {
            return ApiBase.super.getTenantId();
        }

        @Override
        public Optional<String> getUserId() {
            return ApiBase.super.getUserId();
        }

        @Override
        public Optional<String> getUserName() {
            return ApiBase.super.getUserName();
        }

        @Override
        public Optional<List<Long>> getOrgIds() {
            return ApiBase.super.getOrgIds();
        }

        @Override
        public Optional<List<String>> getRoleIds() {
            return ApiBase.super.getRoleIds();
        }

        @Override
        public Optional<Long> getExternalSysId() {
            return ApiBase.super.getExternalSysId();
        }

        @Override
        public String urlDecode(String urlParam) {
            return ApiBase.super.urlDecode(urlParam);
        }

        @Override
        public void downloadFile(HttpServletRequest req, HttpServletResponse resp, String fileName, InputStream inputStream) {
            ApiBase.super.downloadFile(req, resp, fileName, inputStream);
        }

        @Override
        public void downloadFile(HttpServletRequest req, HttpServletResponse resp, String fileName, byte[] bytes) {
            ApiBase.super.downloadFile(req, resp, fileName, bytes);
        }

        @Override
        public String encodeFileName(HttpServletRequest req, String fileName) throws UnsupportedEncodingException {
            return ApiBase.super.encodeFileName(req, fileName);
        }
    };

}
