package com.andyadc.permission.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author andaicheng
 * @since 2018/7/29
 */
public class DeptVo {

    private Long id;

    @NotBlank(message = "部门名称不能为空")
    @Length(max = 30, message = "部门名称长度太长")
    private String name;

    private Long parentId;
    @NotNull(message = "显示顺序不能为空")

    private int seq;
    @Length(max = 150, message = "备注长度太长")

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DeptVo{" +
                "id=" + id +
                ", name=" + name +
                ", parentId=" + parentId +
                ", seq=" + seq +
                ", remark=" + remark +
                "}";
    }
}
