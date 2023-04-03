package com.ctecltd.bravebruhs;

import java.util.AbstractList;

/**
 * Created by scoot on 3/24/2023.
 */

public class SMSObserver {
    private static SMSObserver smsObserverInstance;
    private String sms_message;
    private AbstractList<SMSListener> smsListeners;

    public static SMSObserver getSMSObserver() {
        if (smsObserverInstance == null) {
            smsObserverInstance = new SMSObserver();
        }
        return smsObserverInstance;
    }

    public void updateSMS(String sms_message) {
        this.sms_message = sms_message;
        fireNewSMS();
    }

    public void addSMSListener(SMSListener listener){
        smsListeners.add(listener);
    }

    private void fireNewSMS() {
        for(SMSListener listener: smsListeners){
            listener.fireNewSMS(sms_message);
        }
    }
}
