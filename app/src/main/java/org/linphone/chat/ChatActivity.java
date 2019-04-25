package org.linphone.chat;

/*
ChatActivity.java
Copyright (C) 2019 Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import org.linphone.R;
import org.linphone.core.Address;
import org.linphone.core.Factory;
import org.linphone.main.MainActivity;

public class ChatActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment == null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();
                if (isTablet() || !extras.containsKey("RemoteSipUri")) {
                    showChatRooms();
                }

                if (extras.containsKey("RemoteSipUri")) {
                    String remoteSipUri = extras.getString("RemoteSipUri", null);
                    String localSipUri = extras.getString("LocalSipUri", null);
                    showChatRoom(localSipUri, remoteSipUri);
                }
            } else {
                ChatRoomsFragment fragment = new ChatRoomsFragment();
                changeFragment(fragment, "Chat rooms", false);
                if (isTablet()) {
                    fragment.displayFirstChat();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mChatSelected.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void goBack() {
        if (!isTablet()) {
            if (popBackStack()) {
                return;
            }
        }
        super.goBack();
    }

    private void showChatRooms() {
        ChatRoomsFragment fragment = new ChatRoomsFragment();
        changeFragment(fragment, "Chat rooms", false);
    }

    private void showChatRoom(String localSipUri, String remoteSipUri) {
        Address localAddress = null;
        Address remoteAddress = null;
        if (localSipUri != null) {
            localAddress = Factory.instance().createAddress(localSipUri);
        }
        if (remoteSipUri != null) {
            remoteAddress = Factory.instance().createAddress(remoteSipUri);
        }
        showChatRoom(localAddress, remoteAddress, null, false);
    }

    public void showChatRoom(
            Address localAddress, Address peerAddress, Bundle extras, boolean isChild) {
        if (extras == null) {
            extras = new Bundle();
        }
        if (localAddress != null) {
            extras.putSerializable("LocalSipUri", localAddress.asStringUriOnly());
        }
        if (peerAddress != null) {
            extras.putSerializable("RemoteSipUri", peerAddress.asStringUriOnly());
        }
        ChatMessagesFragment fragment = new ChatMessagesFragment();
        fragment.setArguments(extras);
        changeFragment(fragment, "Chat room", isChild);
    }

    public void showChatRoom(Address localAddress, Address peerAddress, Bundle extras) {
        showChatRoom(localAddress, peerAddress, extras, false);
    }

    public void showChatRoom(Address localAddress, Address peerAddress) {
        showChatRoom(localAddress, peerAddress, null);
    }

    public void showImdn(Address localAddress, Address peerAddress, String messageId) {
        Bundle extras = new Bundle();
        if (localAddress != null) {
            extras.putSerializable("LocalSipUri", localAddress.asStringUriOnly());
        }
        if (peerAddress != null) {
            extras.putSerializable("RemoteSipUri", peerAddress.asStringUriOnly());
        }
        extras.putString("MessageId", messageId);

        ImdnFragment fragment = new ImdnFragment();
        fragment.setArguments(extras);
        changeFragment(fragment, "Chat message imdn", true);
    }

    public void showDevices(Address localAddress, Address peerAddress) {
        Bundle extras = new Bundle();
        if (localAddress != null) {
            extras.putSerializable("LocalSipUri", localAddress.asStringUriOnly());
        }
        if (peerAddress != null) {
            extras.putSerializable("RemoteSipUri", peerAddress.asStringUriOnly());
        }

        DevicesFragment fragment = new DevicesFragment();
        fragment.setArguments(extras);
        changeFragment(fragment, "Chat room devices", true);
    }

    public void showChatRoomCreation() {
        Bundle extras = new Bundle();
        ImdnFragment fragment = new ImdnFragment();
        fragment.setArguments(extras);
        changeFragment(fragment, "Chatroom creation", true);
    }
}
