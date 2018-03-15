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

package com.kaliturin.blacklist.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kaliturin.blacklist.receivers.InternalEventBroadcast;
import com.kaliturin.blacklist.utils.ContactsAccessHelper;
import com.kaliturin.blacklist.utils.DatabaseAccessHelper;
import com.kaliturin.blacklist.utils.DatabaseAccessHelper.Contact;
import com.kaliturin.blacklist.utils.Notifications;
import com.kaliturin.blacklist.utils.Settings;

/**
 * SMS/Call blocking events processing service
 */
public class BlockEventProcessService extends IntentService {
    private static final String TAG = BlockEventProcessService.class.getName();

    private static final String NUMBER = "NUMBER";
    private static final String NAME = "NAME";
    private static final String BODY = "BODY";

    public BlockEventProcessService() {
        super(BlockEventProcessService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String number = intent.getStringExtra(NUMBER);
            String name = intent.getStringExtra(NAME);
            String body = intent.getStringExtra(BODY);
            processEvent(this, number, name, body);
        }
    }

    // Processes the event
    private void processEvent(Context context, String number, String name, String body) {
        //No puede no ser Null Todo
        if (name == null && number == null) {
            Log.w(TAG, "Numero y nombre no puede ser Null");
            return;
        }

        if (name == null) {
            //Trae el nombre del contacto
            ContactsAccessHelper db = ContactsAccessHelper.getInstance(context);
            Contact contact = db.getContact(context, number);
            name = (contact != null ? contact.name : number);
        }

        //Escribe en el Historial
        writeToJournal(context, number, name, body);

        //Si no esta, no entro la llamada
        if (body == null) {
            //Notifica al usuario
            Notifications.onCallBlocked(context, name);
            //Remueve la ultima llamada de el historial
            if (Settings.getBooleanValue(context, Settings.REMOVE_FROM_CALL_LOG)) {
                removeFromCallLog(context, number);
            }
        }
    }

    //Escribe en el historial la llamada bloqueada
    private void writeToJournal(Context context, String number, @NonNull String name, String body) {
        if (ContactsAccessHelper.isPrivatePhoneNumber(number)) {
            number = null;
        }
        long time = System.currentTimeMillis();
        DatabaseAccessHelper db = DatabaseAccessHelper.getInstance(context);
        if (db != null && db.addJournalRecord(time, name, number, body) >= 0) {
            //Envia un mensaje
            InternalEventBroadcast.send(context, InternalEventBroadcast.JOURNAL_WAS_WRITTEN);
        }
    }

    //Remueve los numeros de el Historial
    private void removeFromCallLog(Context context, String number) {
        //Espera por la llamada se escriba en el Historial
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        //Y los remueve
        ContactsAccessHelper db = ContactsAccessHelper.getInstance(context);
        db.deleteLastRecordFromCallLog(context, number, 10000);
    }

    //Inicia el servidor
    public static void start(Context context, String number, String name, String body) {
        Intent intent = new Intent(context, BlockEventProcessService.class);
        intent.putExtra(NUMBER, number);
        intent.putExtra(NAME, name);
        intent.putExtra(BODY, body);
        context.startService(intent);
    }
}
