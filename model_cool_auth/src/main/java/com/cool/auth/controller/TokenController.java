package com.cool.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.core.base.api.R;
import com.cool.core.base.constant.CommonConstants;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *<p>
 * token 管理
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final TokenStore tokenStore;
    private final RedisTemplate redisTemplate;

    /**
     * 退出token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return R.error("退出失败，token 为空");
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
            return R.error("退出失败，token 无效");
        }

        // 清空access token
        tokenStore.removeAccessToken(accessToken);

        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return R.ok();
    }

    /**
     * 令牌删除
     *
     * @param token token
     * @return
     */
    @PreAuthorize("@ps.hasPerm('token_del')")
    @DeleteMapping("/{token}")
    public R<Boolean> delToken(@PathVariable("token") String token) {
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        if (oAuth2AccessToken == null || StrUtil.isBlank(oAuth2AccessToken.getValue())) {
            return R.ok(Boolean.TRUE, "操作失败，token 无效");
        }
        // 清空access token
        tokenStore.removeAccessToken(oAuth2AccessToken);
        // 清空 refresh token
        OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return new R<>();
    }

    /**
     * 令牌查询
     *
     * @param current
     * @param size
     * @return
     */
    @PreAuthorize("@ps.hasPerm('token_view')")
    @GetMapping("/list")
    public R tokenList(int current, int size) {
        //根据分页参数获取对应数据
        String key = CommonConstants.PREFIX + "access:*";

        List<String> pages = findKeysForPage(key, current, size);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        Page result = new Page(current, size);
        result.setRecords(redisTemplate.opsForValue().multiGet(pages));
        result.setTotal(Long.valueOf(redisTemplate.keys(key).size()));
        return R.ok(result.getRecords(), result.getTotal());
    }

    private List<String> findKeysForPage(String patternKey, int current, int size) {
        ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        int tmpIndex = 0;
        int startIndex = (current - 1) * size;
        int end = current * size;

        assert cursor != null;
        while (cursor.hasNext()) {
            if (tmpIndex >= startIndex && tmpIndex < end) {
                result.add(cursor.next().toString());
                tmpIndex++;
                continue;
            }
            if (tmpIndex >= end) {
                break;
            }
            tmpIndex++;
            cursor.next();
        }
        return result;
    }

}
