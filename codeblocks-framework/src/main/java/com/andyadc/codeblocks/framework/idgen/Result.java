package com.andyadc.codeblocks.framework.idgen;

public class Result {

    private String id;
    private Status status;

    public Result() {
    }

    public Result(String id, Status status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result {" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
