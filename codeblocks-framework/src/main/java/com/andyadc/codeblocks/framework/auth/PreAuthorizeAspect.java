package com.andyadc.codeblocks.framework.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PreAuthorizeAspect {

	public static final String POINTCUT_SIGN = "@annotation(com.andyadc.codeblocks.framework.auth.RequiresLogin)"
		+ " || @annotation(com.andyadc.codeblocks.framework.auth.RequiresPermissions)"
		+ " || @annotation(com.andyadc.codeblocks.framework.auth.RequiresRoles)";

	@Pointcut(POINTCUT_SIGN)
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed();
	}
}
