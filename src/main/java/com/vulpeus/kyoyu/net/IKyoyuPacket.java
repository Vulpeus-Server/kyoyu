package com.vulpeus.kyoyu.net;

import com.vulpeus.kyoyu.CompatibleUtils;

public abstract class IKyoyuPacket {

    public byte[] encode() {
        return new byte[0];
    }

    public void onServer(CompatibleUtils.KyoyuPlayer player) {
        // nothing to do
    }

    public void onClient() {
        // nothing to do
    }
}
