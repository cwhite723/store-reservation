package com.hayan.reservation.aop;

import com.hayan.reservation.common.annotation.CurrentCustomer;
import com.hayan.reservation.common.annotation.CurrentOwner;
import com.hayan.reservation.common.exception.CustomException;
import com.hayan.reservation.common.response.ErrorCode;
import com.hayan.reservation.user.domain.Customer;
import com.hayan.reservation.user.domain.Owner;
import com.hayan.reservation.user.service.AuthenticationService;
import com.hayan.reservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.IntStream;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Around("@annotation(com.hayan.reservation.common.annotation.LoginCheck)")
    public Object checkLoginAndInject(ProceedingJoinPoint joinPoint) throws Throwable {
        Long loginId = authenticationService.getLoggedInUserId()
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Object user = userService.getById(loginId);

        if (user instanceof Owner) {
            injectArgument(joinPoint, CurrentOwner.class, (Owner) user);
        } else if (user instanceof Customer) {
            injectArgument(joinPoint, CurrentCustomer.class, (Customer) user);
        }

        return joinPoint.proceed(joinPoint.getArgs());

    }

    private void injectArgument(ProceedingJoinPoint joinPoint, Class<?> annotationClass, Object value) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();

        IntStream.range(0, parameterAnnotations.length)
                .filter(i -> Arrays.stream(parameterAnnotations[i])
                        .anyMatch(annotation -> annotation.annotationType().equals(annotationClass)))
                .findFirst()
                .ifPresent(i -> {
                    validateUserByAnnotation(annotationClass, value);
                    args[i] = value;
                });
    }

    private void validateUserByAnnotation(Class<?> annotationClass, Object value) {
        if (annotationClass.equals(CurrentOwner.class) && !(value instanceof Owner)) {
            throw new CustomException(ErrorCode.OWNER_PRIVILEGE_REQUIRED);
        }
        if (annotationClass.equals(CurrentCustomer.class) && !(value instanceof Customer)) {
            throw new CustomException(ErrorCode.CUSTOMER_PRIVILEGE_REQUIRED);
        }
    }
}