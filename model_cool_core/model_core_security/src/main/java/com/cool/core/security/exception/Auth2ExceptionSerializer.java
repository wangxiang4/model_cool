package com.cool.core.security.exception;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * @author 菜王
 * @create 2020-11-03
 * 定义Json格式化处理类
 * 返回错误给前端封装统一
 */
public class Auth2ExceptionSerializer extends StdSerializer<Auth2Exception> {

    protected Auth2ExceptionSerializer() {
        super(Auth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(Auth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject();
        gen.writeObjectField("code", "1");
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getOAuth2ErrorCode());
        gen.writeEndObject();
    }

}
