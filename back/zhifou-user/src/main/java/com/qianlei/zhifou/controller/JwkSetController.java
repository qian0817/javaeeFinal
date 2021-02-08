package com.qianlei.zhifou.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** @author qianlei */
@RestController
public class JwkSetController {
  @Autowired private RSAKey rsaKey;

  @GetMapping("/.well-known/jwks.json")
  public Map<String, Object> getKey() throws JOSEException {
    RSAKey key = new RSAKey.Builder(rsaKey.toRSAPublicKey()).build();
    return new JWKSet(key).toJSONObject();
  }
}
