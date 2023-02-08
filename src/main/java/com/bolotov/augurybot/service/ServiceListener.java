package com.bolotov.augurybot.service;

import com.bolotov.augurybot.model.ServiceEvent;

public interface ServiceListener {
    public void event(ServiceEvent e);
}
