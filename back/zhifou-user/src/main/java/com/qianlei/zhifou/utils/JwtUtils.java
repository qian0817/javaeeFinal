package com.qianlei.zhifou.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.qianlei.zhifou.vo.UserVo;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

/** @author qianlei */
@Slf4j
public class JwtUtils {
  public static String createJwt(UserVo userVo, RSAKey key) {
    try {
      RSASSASigner signer = new RSASSASigner(key);
      var claimsSet =
          new JWTClaimsSet.Builder()
              .claim("user", userVo)
              .subject(userVo.getUsername())
              .notBeforeTime(Date.from(Instant.now()))
              .expirationTime(Date.from(ZonedDateTime.now().plusDays(7).toInstant()))
              .build();

      var header =
          new JWSHeader.Builder(JWSAlgorithm.RS256)
              .type(JOSEObjectType.JWT)
              .keyID(key.getKeyID())
              .build();

      var signedJwt = new SignedJWT(header, claimsSet);
      signedJwt.sign(signer);
      return signedJwt.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

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
