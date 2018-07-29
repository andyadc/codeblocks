package com.andyadc.permission.service;

import com.andyadc.permission.exception.PermissionException;
import com.andyadc.permission.mapper.AuthDeptMapper;
import com.andyadc.permission.util.BeanValidator;
import com.andyadc.permission.vo.DeptVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author andaicheng
 * @since 2018/7/29
 */
@Service
public class DeptService {

    @Resource
    private AuthDeptMapper deptMapper;

    @Transactional
    public void save(DeptVo vo) {
        BeanValidator.check(vo);
        if (checkExist(vo.getParentId(), vo.getId(), vo.getName())) {
            throw new PermissionException("同一层级下存在相同名称的部门");
        }

    }

    private boolean checkExist(Long parentId, Long deptId, String deptName) {

        // TODO
        return true;
    }
}
