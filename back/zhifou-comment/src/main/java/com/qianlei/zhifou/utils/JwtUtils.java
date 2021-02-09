package com.qianlei.zhifou.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.qianlei.zhifou.vo.UserVo;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/** @author qianlei */
@Slf4j
public class JwtUtils {
  public static UserVo checkJwt(String jwt, RSAKey key) {
    try {
      SignedJWT signedJwt = SignedJWT.parse(jwt);
      var verifier = new RSASSAVerifier(key.toRSAPublicKey());
      if (signedJwt.verify(verifier)) {
        if (signedJwt.getJWTClaimsSet().getExpirationTime().toInstant().isBefore(Instant.now())) {
          return null;
        }
        if (signedJwt.getJWTClaimsSet().getNotBeforeTime().toInstant().isAfter(Instant.now())) {
          return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
            mapper.writeValueAsString(signedJwt.getJWTClaimsSet().getJSONObjectClaim("user")),
            UserVo.class);
      }
    } catch (Exception e) {
      log.debug("解析 jwt 出错", e);
      e.printStackTrace();
    }
    return null;
  }
}
