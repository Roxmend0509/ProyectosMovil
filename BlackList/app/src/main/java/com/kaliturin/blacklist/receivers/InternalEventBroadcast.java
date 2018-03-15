/*
 * Copyright (C) 2017 Anton Kaliturin <kaliturin@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.kaliturin.blacklist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * Broadcast receiver/sender is used for notifications about internal events
 */
public class InternalEventBroadcast extends BroadcastReceiver {
    public static final String TAG = InternalEventBroadcast.class.getName();
    public static final String JOURNAL_WAS_WRITTEN = "JOURNAL_WAS_WRITTEN";

    private static final String EVENT_TYPE = "EVENT_TYPE";
    private static final String CONTACT_NUMBER = "CONTACT_NUMBER";
    private static final String THREAD_ID = "THREAD_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        // get action type
        String actionType = intent.getStringExtra(EVENT_TYPE);
        if (actionType == null) {
            return;
        }
        // invoke the callback correspondent to the received event
        switch (actionType) {
            case JOURNAL_WAS_WRITTEN:
                onJournalWasWritten();
                break;
        }
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(TAG);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    /**
     * Method is called if some record was written to the Journal (Event log)
     **/
    public void onJournalWasWritten() {
    }


    /**
     * Envia un evento interno que deberia recibir una respuesta
     * de llamada de un registro
     **/
    public static void send(Context context, String eventType) {
        Intent intent = new Intent(TAG);
        intent.putExtra(EVENT_TYPE, eventType);
        context.sendBroadcast(intent, null);
    }


}
