package com.barack.securebanksystem.service.Impl;

import com.barack.securebanksystem.dto.EmailDetails;

public interface EmailService {
    public void sendEmailAlert(EmailDetails emailDetails);
    public void sendEmailWithAttachment(EmailDetails emailDetails);
}
