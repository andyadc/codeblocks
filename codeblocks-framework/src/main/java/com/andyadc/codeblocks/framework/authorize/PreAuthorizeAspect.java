package com.andyadc.codeblocks.framework.authorize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PreAuthorizeAspect {

	public static final String POINTCUT_SIGN = " @annotation(com.andyadc.codeblocks.framework.authorize.RequiresLogin)"
		+ " || @annotation(com.andyadc.codeblocks.framework.authorize.RequiresPermissions)"
		+ " || @annotation(com.andyadc.codeblocks.framework.authorize.RequiresRoles)";

	@Pointcut(POINTCUT_SIGN)
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed();
	}
}
