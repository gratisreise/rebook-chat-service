package com.example.rebookchatservice.global.passport;

import com.example.rebookchatservice.global.exception.UnAuthorizedException;
import com.rebook.passport.PassportDecoder;
import com.rebook.passport.PassportProto.Passport;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class PassportUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final PassportDecoder passportDecoder;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PassportUser.class)
                && parameter.getParameterType().equals(Passport.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String passport = request.getHeader("X-Passport");
        log.info("passport: {}", passport);

        if (passport == null) {
            throw new UnAuthorizedException("Passport가 존재하지 않습니다.");
        }
        Passport decodedPassport = passportDecoder.decode(passport);
        log.info("decoded: {}", decodedPassport);

        return decodedPassport;
    }
}
