package com.modosa.switchnightui.service;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.uitl.OpUtil;
import com.modosa.switchnightui.uitl.SwitchUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchForceDark extends TileService {

    private OpUtil opUtil;
    private SwitchUtil switchUtil;

    @Override
    public void onStartListening() {
        super.onStartListening();
        opUtil = new OpUtil(this);
        switchUtil = new SwitchUtil(this, null);
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();

        switchForceDark();
        refreshState();
    }

    private void switchForceDark() {
        String msg = "";
        boolean isSu, isForceDark;
        if (opUtil.isOp()) {
            isSu = opUtil.switchForceDark();
            if (!isSu) {
                switchUtil.showToast(getString(R.string.no_root));
                return;
            }
            isForceDark = opUtil.isForceDark();
        } else {
            isSu = switchUtil.switchForceDark();
            if (!isSu) {
                msg = getString(R.string.no_root) + "\n";
            }
            isForceDark = switchUtil.isForceDark();
        }
        if (isForceDark) {
            switchUtil.showToast(msg + getString(R.string.ForceDarkOn));
        } else {
            switchUtil.showToast(msg + getString(R.string.ForceDarkOff));
        }
    }

    private void refreshState() {
        Tile qsTile = getQsTile();
        try {
            boolean isForceDark;
            if (opUtil.isOp()) {
                isForceDark = opUtil.isForceDark();
            } else {
                isForceDark = switchUtil.isForceDark();
            }
            if (isForceDark) {
                qsTile.setState(Tile.STATE_ACTIVE);
            } else {
                qsTile.setState(Tile.STATE_INACTIVE);
            }
            qsTile.updateTile();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
        }

    }

}
