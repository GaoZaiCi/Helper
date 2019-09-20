package com.fmp.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

public class BlockmanDialog extends AlertDialog {
    private Context mContext;
    private LinearLayout.LayoutParams PARAMS_DIALOG = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private String bg_new_dialog = "iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6/NlyAAAAGG5wT2wKAAAACAAAAAoAAAAMAAAAQYJaQv8AAADK0dDaAAAAVG5wVGMAAgIJIAAAACgAAAAAAAACAAAAAgAAAAAAAAAEMAAAAAAAAB4AAAAfAAAAHAAAAB0AAAABAAAAAQAAAAEAAAAB/86/ngAAAAEAAAABAAAAAQAAAAFPe6ZwAAAESklEQVRo3u1bwW4rNRQ9vrbTpqVqn/gAxC90wZpKbBESO7ZlxYJfQO8fQEIs6BI+gC1SWbPIDyAWLJAQ8HivSps0Y/teFvFIk8QTO8q8JGrnSqNEiT3xsa/v8Zl7o5A3BUD9+vP3LwGAJSgckJHSAgAffPT5SwASr1YzhYCN85XmwMQiBwZYCWniiMV3AZgA2OrRD2auMoGZDgmwJuIjO/AALACO1/YrPHOVcVWwLjh9SICttgGoaixVrn0pYArM5ILTzHJQK+zgQKxC9ERV4q7PynrAT91UjmdDCOScNx9+/OW3AIYxGq7s8dHtzehtDvTy6voyRmBZ2cbA9Jefvv7CWuO11ryOp02OZ0VESQBFjpsCmCUC3/EOFicAeIzjaBoD8BJADt54H2QdT5sczyqlxRJxPZMJnjve0daoAIwj6OVtabwP2lVsZclDl3na5HjWahtgjW+s8PIMC4DTHQCuf3+S8LChY6bgvEmOv8HTJsezDg5kVKhdJ0HuNne66chqL0sdLlgkqNbxN3i6p6Ue8BMzU7R5GDS6vflvXZvx/fS34L0JklZT7744f29d/1ev7/5IqiFFrI3xo9ub39f1//OvV990A1ignKuy7URYzWlMyep3ZRo61bd577UiwlUGkhcPWSCBmUo0sIgoEAnxasBmVQaWkABMJCUTVjlvNRG3TdpGgNkHU+AIqh7gypiZC4KJklTfhXuv23Y+GBh4o3XYOmj5kqccHF0aIs1r88N9oj/nfcQXPonpaakH/Bx5uMQuLs7e32f/foV7wD3gHnAPuAf8nHm4Tc9uq4dL++8c8Ho9u60e7i4nbboEm9Sz2+rhwv47B7xOz26rh0v678Wlaz27qXjvsn8fpXvAPQ8nGhHx5dX1J5hnCpfzwwKAR7c3P75NPXx5df0Z0nUcDsDjD9999WkngDURw8ADeBPbU8JL7A4WZxzBLYdsBuDJaK/nad3tAWtNAcDfmGcPl296BOBsB4D/iaBniQk3A2scBCpXOJd3aQWx84TyHeb5WbfU4mRH228M4F+s5octgKG1A1+SfSjaw0RgzDPvE6Tzs7MdAJ7F339Y+nwAQMUx9lG6B/zcAKu46U8AnMfXQYLnWuugZrPKPtw/nrx+Mz5/mE5PnPedUpQ1xp0Oh5MXF2d3p+8cT46OBgtBs6COrIp7/w7AxMSDg28EnirFc211UM55EwITEVgb7XPpyo1dUFMgAofA5Jw3Ka2cqSOrsXnEwrS6OuY+RuLUwaK1DooZ5ILTSlMYDuzMd1xPbYhYaQouOI1HdTSrnF3U0dk6srr6yCHWaUnjQFEljm5r66BqDVefyAbovpxaE3FbJUJhHZlEfGKWPmiz1jqo5hOLXDJ6G2MRlTpUFNSR9bTUA37qPJylwgxP79sWeDYhbjYWDzme3rct8GwXK0xxYmzLA4B9W5NnfYZtigDXfxOgxvtDsgWeza3y/5kubUQ6HFifAAAAAElFTkSuQmCC";

    public BlockmanDialog(Context context) {
        super(context);
        this.mContext = context;
        PARAMS_DIALOG.setMargins(20, 20, 20, 20);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private NinePatchDrawable getBase64NinePatchDrawable(String base64Arr) {
        byte[] bytes = Base64.decode(base64Arr.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        byte[] chunk = bitmap.getNinePatchChunk();
        return new NinePatchDrawable(bitmap, chunk, new Rect(), null);
    }

    @Override
    public void setView(View view) {
        view.setLayoutParams(PARAMS_DIALOG);
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackground(getBase64NinePatchDrawable(bg_new_dialog));
        layout.addView(view);
        super.setView(layout);
    }

    @Override
    public void setContentView(View view) {
        view.setLayoutParams(PARAMS_DIALOG);
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackground(getBase64NinePatchDrawable(bg_new_dialog));
        layout.addView(view);
        super.setContentView(layout);
    }
}
