// The MIT License (MIT)
//
// Copyright (c) 2014 Jerry Morrison
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
// associated documentation files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge, publish, distribute,
// sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or
// substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
// NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.onefishtwo.bbqtimer.notificationCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Notification Builder for API level 20-, implemented via NotificationCompat.Builder plus
 * additional OS bug workarounds.
 */
class V20Builder implements NotificationBuilder {
    private NotificationCompat.Builder builder;

    public V20Builder(Context context) {
        builder = new NotificationCompat.Builder(context);
    }

    @Override
    public NotificationBuilder setWhen(long when) {
        builder.setWhen(when);
        return this;
    }

    @Override
    public NotificationBuilder setUsesChronometer(boolean b) {
        builder.setUsesChronometer(b);
        return this;
    }

    @Override
    public NotificationBuilder setSmallIcon(int icon) {
        builder.setSmallIcon(icon);
        return this;
    }

    @Override
    public NotificationBuilder setContentTitle(CharSequence title) {
        builder.setContentTitle(title);
        return this;
    }

    @Override
    public NotificationBuilder setContentText(CharSequence text) {
        builder.setContentText(text);
        return this;
    }

    @Override
    public NotificationBuilder setNumber(int number) {
        // WORKAROUND: On Android level 12 HONEYCOMB_MR1, setNumber() will later crash with
        // Resources$NotFoundException "Resource ID #0x1050019" from
        // Resources.getDimensionPixelSize(). It works on API v15.
        //
        // On older builds, calling builder.setContentInfo() would be a workaround but it's a noop.
        //
        // TODO: Test API level 13 - 14. Those emulators take most of an hour to launch, then croak.
        if (Build.VERSION.SDK_INT >= 15) {
            builder.setNumber(number);
        }

        return this;
    }

    @Override
    public NotificationBuilder setContentIntent(PendingIntent intent) {
        builder.setContentIntent(intent);
        return this;
    }

    @Override
    public NotificationBuilder setLargeIcon(Bitmap icon) {
        // WORKAROUND: On Android level 12, setLargeIcon() will later crash with "FATAL EXCEPTION:
        // main android.app.RemoteServiceException: Bad notification posted ...: Couldn't expand
        // RemoteViews for: StatusBarNotification(...)".
        //
        // TODO: What about Android level 13-14?
        if (Build.VERSION.SDK_INT >= 15) {
            builder.setLargeIcon(icon);
        }

        return this;
    }

    @Override
    public NotificationBuilder setSound(Uri sound) {
        builder.setSound(sound);
        return this;
    }

    @Override
    public NotificationBuilder setVibrate(long[] pattern) {
        builder.setVibrate(pattern);
        return this;
    }

    @Override
    public NotificationBuilder setOngoing(boolean ongoing) {
        builder.setOngoing(ongoing);
        return this;
    }

    @Override
    public NotificationBuilder setCategory(String category) {
        builder.setCategory(category);
        return this;
    }

    @Override
    public NotificationBuilder setDefaults(int defaults) {
        builder.setDefaults(defaults);
        return this;
    }

    @Override
    public NotificationBuilder setPriority(int pri) {
        builder.setPriority(pri);
        return this;
    }

    @Override
    public NotificationBuilder addAction(int icon, CharSequence title, PendingIntent intent) {
        builder.addAction(icon, title, intent);
        return this;
    }

    @Override
    public NotificationBuilder setVisibility(int visibility) {
        builder.setVisibility(visibility);
        return this;
    }

    @Override
    public NotificationBuilder setMediaStyleActionsInCompactView(int... actions) {
        // Noop for Android API V20-.
        return this;
    }

    @Override
    public Notification build() {
        return builder.build();
    }
}