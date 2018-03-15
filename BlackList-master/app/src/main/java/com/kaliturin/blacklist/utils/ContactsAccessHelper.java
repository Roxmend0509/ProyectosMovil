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

package com.kaliturin.blacklist.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kaliturin.blacklist.utils.DatabaseAccessHelper.Contact;
import com.kaliturin.blacklist.utils.DatabaseAccessHelper.ContactNumber;
import com.kaliturin.blacklist.utils.DatabaseAccessHelper.ContactSource;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Contacts/Calls list access helper
 */
public class ContactsAccessHelper {
    private static final String TAG = ContactsAccessHelper.class.getName();
    private static volatile ContactsAccessHelper sInstance = null;
    private ContentResolver contentResolver = null;

    private ContactsAccessHelper(Context context) {
        contentResolver = context.getApplicationContext().getContentResolver();
    }

    public static ContactsAccessHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ContactsAccessHelper.class) {
                if (sInstance == null) {
                    sInstance = new ContactsAccessHelper(context);
                }
            }
        }
        return sInstance;
    }

    private boolean validate(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) return false;
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }
        return true;
    }

    // Types of contact sources
    public enum ContactSourceType {
        FROM_CONTACTS,
        FROM_CALLS_LOG,
        FROM_BLACK_LIST,
    }

    @Nullable
    public static String getPermission(ContactSourceType sourceType) {
        switch (sourceType) {
            case FROM_CONTACTS:
                return Permissions.READ_CONTACTS;
            case FROM_CALLS_LOG:
                return Permissions.READ_CALL_LOG;
        }
        return null;
    }

    // Returns contacts from specified source
    @Nullable
    public Cursor getContacts(Context context, ContactSourceType sourceType, @Nullable String filter) {
        // check permission
        final String permission = getPermission(sourceType);
        if (permission == null || !Permissions.isGranted(context, permission)) {
            return null;
        }
        // return contacts
        switch (sourceType) {
            case FROM_CONTACTS:
                return getContacts(filter);
            case FROM_CALLS_LOG:
                return getContactsFromCallsLog(filter);
            case FROM_BLACK_LIST: {
                DatabaseAccessHelper db = DatabaseAccessHelper.getInstance(context);
                if (db != null) {
                    return db.getContacts(DatabaseAccessHelper.Contact.TYPE_BLACK_LIST, filter);
                }
            }
        }
        return null;
    }

    // Selects contacts from contacts list
    @Nullable
    private ContactCursorWrapper getContacts(@Nullable String filter) {
        filter = (filter == null ? "%%" : "%" + filter + "%");
        Cursor cursor = contentResolver.query(
                Contacts.CONTENT_URI,
                new String[]{Contacts._ID, Contacts.DISPLAY_NAME},
                Contacts.IN_VISIBLE_GROUP + " != 0 AND " +
                        Contacts.HAS_PHONE_NUMBER + " != 0 AND " +
                        Contacts.DISPLAY_NAME + " IS NOT NULL AND " +
                        Contacts.DISPLAY_NAME + " LIKE ? ",
                new String[]{filter},
                Contacts.DISPLAY_NAME + " ASC");

        return (validate(cursor) ? new ContactCursorWrapper(cursor) : null);
    }

    // Selects contact from contacts list by id
    @Nullable
    private ContactCursorWrapper getContactCursor(long contactId) {
        Cursor cursor = contentResolver.query(
                Contacts.CONTENT_URI,
                new String[]{Contacts._ID, Contacts.DISPLAY_NAME},
                Contacts.DISPLAY_NAME + " IS NOT NULL AND " +
                        Contacts.IN_VISIBLE_GROUP + " != 0 AND " +
                        Contacts.HAS_PHONE_NUMBER + " != 0 AND " +
                        Contacts._ID + " = " + contactId,
                null,
                null);

        return (validate(cursor) ? new ContactCursorWrapper(cursor) : null);
    }

    @Nullable
    private Contact getContact(long contactId) {
        Contact contact = null;
        ContactCursorWrapper cursor = getContactCursor(contactId);
        if (cursor != null) {
            contact = cursor.getContact(false);
            cursor.close();
        }

        return contact;
    }

    // Selects contact from contacts list by phone number
    @Nullable
    private ContactCursorWrapper getContactCursor(String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        Cursor cursor = contentResolver.query(lookupUri,
                new String[]{Contacts._ID, Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        return (validate(cursor) ? new ContactCursorWrapper(cursor) : null);
    }

    @Nullable
    private Contact getContact(String number) {
        Contact contact = null;
        ContactCursorWrapper cursor = getContactCursor(number);
        if (cursor != null) {
            contact = cursor.getContact(false);
            cursor.close();
        }

        return contact;
    }

    @Nullable
    public Contact getContact(Context context, String number) {
        if (!Permissions.isGranted(context, Permissions.READ_CONTACTS)) {
            return null;
        }

        return getContact(number);
    }

    // Contact's cursor wrapper
    private class ContactCursorWrapper extends CursorWrapper implements ContactSource {
        private final int ID;
        private final int NAME;

        private ContactCursorWrapper(Cursor cursor) {
            super(cursor);
            cursor.moveToFirst();
            ID = getColumnIndex(Contacts._ID);
            NAME = getColumnIndex(Contacts.DISPLAY_NAME);
        }

        @Override
        public Contact getContact() {
            return getContact(true);
        }

        Contact getContact(boolean withNumbers) {
            long id = getLong(ID);
            String name = getString(NAME);
            List<ContactNumber> numbers = new LinkedList<>();
            if (withNumbers) {
                ContactNumberCursorWrapper cursor = getContactNumbers(id);
                if (cursor != null) {
                    do {
                        // normalize the phone number (remove spaces and brackets)
                        String number = normalizePhoneNumber(cursor.getNumber());
                        // create and add contact number instance
                        ContactNumber contactNumber =
                                new ContactNumber(cursor.getPosition(), number, id);
                        numbers.add(contactNumber);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }

            return new Contact(id, name, 0, numbers);
        }
    }

    // Contact's number cursor wrapper
    private static class ContactNumberCursorWrapper extends CursorWrapper {
        private final int NUMBER;

        private ContactNumberCursorWrapper(Cursor cursor) {
            super(cursor);
            cursor.moveToFirst();
            NUMBER = cursor.getColumnIndex(Phone.NUMBER);
        }

        String getNumber() {
            return getString(NUMBER);
        }
    }

    // Selects all numbers of specified contact
    @Nullable
    private ContactNumberCursorWrapper getContactNumbers(long contactId) {
        Cursor cursor = contentResolver.query(
                Phone.CONTENT_URI,
                new String[]{Phone.NUMBER},
                Phone.NUMBER + " IS NOT NULL AND " +
                        Phone.CONTACT_ID + " = " + contactId,
                null,
                null);

        return (validate(cursor) ? new ContactNumberCursorWrapper(cursor) : null);
    }

//-------------------------------------------------------------------------------------

    // SMS data URIs
    private static final Uri URI_CONTENT_SMS = Uri.parse("content://sms");
    private static final Uri URI_CONTENT_SMS_INBOX = Uri.parse("content://sms/inbox");
    private static final Uri URI_CONTENT_SMS_CONVERSATIONS = Uri.parse("content://sms/conversations");
    private static final Uri URI_CONTENT_CALLS = Uri.parse("content://call_log/calls");

    // SMS data columns
    public static final String ID = "_id";
    public static final String ADDRESS = "address";
    public static final String BODY = "body";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String DATE_SENT = "date_sent";
    public static final String PROTOCOL = "protocol";
    public static final String REPLY_PATH_PRESENT = "reply_path_present";
    public static final String SERVICE_CENTER = "service_center";
    public static final String SUBJECT = "subject";
    public static final String READ = "read";
    public static final String SEEN = "seen";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String DELIVERY_DATE = "delivery_date";
    public static final String THREAD_ID = "thread_id";
    public static final String MSG_COUNT = "msg_count";
    public static final String NAME = "name";

//-------------------------------------------------------------------------------------



    // Selects contacts from SMS list filtering by contact name or number
    @Nullable
    private ContactFromSMSCursorWrapper getContactsFromSMSList(@Nullable String filter) {
        filter = (filter == null ? "" : filter.toLowerCase());

        // filter by address (number) if person (contact id) is null
        Cursor cursor = contentResolver.query(
                URI_CONTENT_SMS,
                new String[]{"DISTINCT " + ID, ADDRESS, PERSON},
                ADDRESS + " IS NOT NULL " +
                        ") GROUP BY (" + ADDRESS,
                null,
                DATE + " DESC");

        // now we need to filter contacts by names and fill matrix cursor
        if (validate(cursor)) {
            cursor.moveToFirst();
            MatrixCursor matrixCursor = new MatrixCursor(new String[]{ID, ADDRESS, PERSON});
            final int _ID = cursor.getColumnIndex(ID);
            final int _ADDRESS = cursor.getColumnIndex(ADDRESS);
            final int _PERSON = cursor.getColumnIndex(PERSON);
            // set is used to filter repeated data
            Set<String> set = new HashSet<>();
            do {
                String address = cursor.getString(_ADDRESS);
                address = normalizePhoneNumber(address);
                if (!set.add(address)) {
                    continue;
                }
                String id = cursor.getString(_ID);
                String person = address;
                Contact contact = null;
                if (!cursor.isNull(_PERSON)) {
                    // get contact by id
                    long contactId = cursor.getLong(_PERSON);
                    contact = getContact(contactId);
                }
                if (contact == null) {
                    // find contact by address
                    contact = getContact(address);
                }
                // get person name from contact
                if (contact != null) {
                    person = contact.name;
                }
                // filter contact
                if (person.toLowerCase().contains(filter)) {
                    matrixCursor.addRow(new String[]{id, address, person});
                }
            } while (cursor.moveToNext());
            cursor.close();
            cursor = matrixCursor;
        }

        return (validate(cursor) ? new ContactFromSMSCursorWrapper(cursor) : null);
    }

    // Contact from SMS cursor wrapper
    private class ContactFromSMSCursorWrapper extends CursorWrapper implements ContactSource {
        private final int _ID;
        private final int _ADDRESS;
        private final int _PERSON;

        private ContactFromSMSCursorWrapper(Cursor cursor) {
            super(cursor);
            cursor.moveToFirst();
            _ID = getColumnIndex(ID);
            _ADDRESS = getColumnIndex(ADDRESS);
            _PERSON = getColumnIndex(PERSON);
        }

        @Override
        public Contact getContact() {
            long id = getLong(_ID);
            String name = getString(_PERSON);
            String number = getString(_ADDRESS);
            List<ContactNumber> numbers = new LinkedList<>();
            numbers.add(new ContactNumber(0, number, id));

            return new Contact(id, name, 0, numbers);
        }
    }

//-------------------------------------------------------------------------------------

    // Selects contacts from calls log
    @Nullable
    private ContactFromCallsCursorWrapper getContactsFromCallsLog(@Nullable String filter) {
        filter = (filter == null ? "%%" : "%" + filter + "%");

        // filter by name or by number
        Cursor cursor = contentResolver.query(
                URI_CONTENT_CALLS,
                new String[]{Calls._ID, Calls.NUMBER, Calls.CACHED_NAME},
                Calls.NUMBER + " IS NOT NULL AND (" +
                        Calls.CACHED_NAME + " IS NULL AND " +
                        // leave out private numbers
                        Calls.NUMBER + " NOT LIKE '-%' AND " +
                        Calls.NUMBER + " LIKE ? OR " +
                        Calls.CACHED_NAME + " LIKE ? )",
                new String[]{filter, filter},
                Calls.DATE + " DESC");

        if (validate(cursor)) {
            cursor.moveToFirst();
            // Because we cannot query distinct calls - we have queried all.
            // Then we getting rid of repeated data.
            MatrixCursor matrixCursor = new MatrixCursor(
                    new String[]{Calls._ID, Calls.NUMBER, Calls.CACHED_NAME});
            final int ID = cursor.getColumnIndex(Calls._ID);
            final int NUMBER = cursor.getColumnIndex(Calls.NUMBER);
            final int NAME = cursor.getColumnIndex(Calls.CACHED_NAME);
            Set<String> set = new HashSet<>();
            do {
                String number = cursor.getString(NUMBER);
                number = normalizePhoneNumber(number);
                String name = cursor.getString(NAME);
                if (name == null) {
                    name = number;
                }
                if (set.add(number + name)) {
                    String id = cursor.getString(ID);
                    matrixCursor.addRow(new String[]{id, number, name});
                }
            } while (cursor.moveToNext());
            cursor.close();
            cursor = matrixCursor;
        }

        return (validate(cursor) ? new ContactFromCallsCursorWrapper(cursor) : null);
    }

    // Contact from calls cursor wrapper
    private class ContactFromCallsCursorWrapper extends CursorWrapper implements ContactSource {
        private final int ID;
        private final int NUMBER;
        private final int NAME;

        private ContactFromCallsCursorWrapper(Cursor cursor) {
            super(cursor);
            cursor.moveToFirst();
            ID = cursor.getColumnIndex(Calls._ID);
            NUMBER = cursor.getColumnIndex(Calls.NUMBER);
            NAME = cursor.getColumnIndex(Calls.CACHED_NAME);
        }

        @Override
        public Contact getContact() {
            long id = getLong(ID);
            String number = getString(NUMBER);
            String name = getString(NAME);
            List<ContactNumber> numbers = new LinkedList<>();
            numbers.add(new ContactNumber(0, number, id));

            return new Contact(id, name, 0, numbers);
        }
    }

    // Deletes last Call log record that was written since "duration" time
    public boolean deleteLastRecordFromCallLog(Context context, String number, long duration) {
        if (!Permissions.isGranted(context, Permissions.WRITE_CALL_LOG)) {
            return false;
        }

        // get id of the last record has been written since duration ago
        long id = getLastRecordIdFromCallLog(context, number, duration);
        if (id < 0) {
            return false;
        }

        // delete record from log by id
        int count = contentResolver.delete(
                URI_CONTENT_CALLS,
                ID + " = ? ",
                new String[]{String.valueOf(id)});

        return (count > 0);
    }

    // Returns last call record from the Call log that was written since "duration" time
    private long getLastRecordIdFromCallLog(Context context, String number, long duration) {
        if (!Permissions.isGranted(context, Permissions.READ_CALL_LOG)) {
            return -1;
        }

        // We should not search call just by number because it can be not normalized.
        // Therefore select all records have been written since passed time duration,
        // normalize every number and then compare.
        long time = System.currentTimeMillis() - duration;

        Cursor cursor = contentResolver.query(
                URI_CONTENT_CALLS,
                new String[]{Calls._ID, Calls.NUMBER},
                Calls.DATE + " > ? ",
                new String[]{String.valueOf(time)},
                Calls.DATE + " DESC");

        long id = -1;
        if (validate(cursor)) {
            cursor.moveToFirst();
            final int ID = cursor.getColumnIndex(Calls._ID);
            final int NUMBER = cursor.getColumnIndex(Calls.NUMBER);
            boolean isPrivate = isPrivatePhoneNumber(number);

            // get the first equal
            do {
                String _number = cursor.getString(NUMBER);

                if (isPrivate) {
                    // searching for private number
                    if (isPrivatePhoneNumber(_number)) {
                        id = cursor.getLong(ID);
                        break;
                    }
                } else {
                    // searching for normal number
                    if (_number != null) {
                        _number = normalizePhoneNumber(_number);
                        if (_number.equals(number)) {
                            id = cursor.getLong(ID);
                            break;
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return id;
    }


//-------------------------------------------------------------------------------------





//---------------------------------------------------------------------

    // For the sake of performance we don't use comprehensive phone number pattern.
    // We just want to detect whether a phone number is digital but not symbolic.
    private static final Pattern digitalPhoneNumberPattern = Pattern.compile("[+]?[0-9-() ]+");
    // Is used for normalizing a phone number, removing from it brackets, dashes and spaces.
    private static final Pattern normalizePhoneNumberPattern = Pattern.compile("[-() ]");

    /**
     * If passed phone number is digital and not symbolic then normalizes
     * it, removing brackets, dashes and spaces.
     */
    public static String normalizePhoneNumber(@NonNull String number) {
        number = number.trim();
        if (digitalPhoneNumberPattern.matcher(number).matches()) {
            number = normalizePhoneNumberPattern.matcher(number).replaceAll("");
        }
        return number;
    }

    /**
     * Checks whether passed phone number is private
     */
    public static boolean isPrivatePhoneNumber(@Nullable String number) {
        try {
            if (number == null) {
                return true;
            }
            number = number.trim();
            if (number.isEmpty() || Long.valueOf(number) < 0) {
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

//---------------------------------------------------------------------

    private static void debug(Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String s = cursor.getString(i);
            String n = cursor.getColumnName(i);
            sb.append("[").append(n).append("]=").append(s);
        }
        Log.d(TAG, sb.toString());
    }


}
