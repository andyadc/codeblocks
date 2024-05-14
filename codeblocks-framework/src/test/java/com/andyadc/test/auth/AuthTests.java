package com.andyadc.test.auth;

import com.andyadc.codeblocks.framework.auth.Logical;
import com.andyadc.codeblocks.framework.auth.annotation.RequiresPermissions;
import com.andyadc.codeblocks.framework.auth.annotation.RequiresRoles;

public class AuthTests {

	@RequiresRoles
	public void act001() {

	}

	@RequiresPermissions(value = {"p1", "p2"}, logical = Logical.AND)
	public void act002() {

	}
}
