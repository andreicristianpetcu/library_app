package com.cegeka.infrastructure;

import java.util.LinkedList;
import java.util.List;

public class EmailTO {
    private List<String> tos = new LinkedList<String>();
    private String body;
    private String subject;
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTos() {
        return tos;
    }

    public void addTo(String email) {
        this.tos.add(email);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
