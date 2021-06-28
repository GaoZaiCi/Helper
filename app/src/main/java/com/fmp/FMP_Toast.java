package com.fmp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fmp.util.SizeUtil;

import net.fmp.helper.R;

public class FMP_Toast {
    private static View BM_Layout;
    private static ImageView BM_ImageView;
    private static TextView BM_TextView;
    private static Toast DefaultToast;
    private static long time;
    private static StringBuilder ToastString = new StringBuilder();

//    public FMP_Toast(Context context, CharSequence text, int duration) {
//        super(context);
//    }

    public static void BM_Toast(Context Context, String Msg, boolean State) {
        if (BM_Layout == null) {
            BM_Layout = LayoutInflater.from(Context).inflate(R.layout.fmp_app_toast_tip, null);
        }
        if (BM_ImageView == null) {
            BM_ImageView = BM_Layout.findViewWithTag("image");
        }
        if (BM_TextView == null) {
            BM_TextView = BM_Layout.findViewWithTag("text");
            //BM_TextView.setBackground(getAssetsNinePatchDrawable(Context, "FMP_Resources/Image/ic_tip_content.9.png"));
            BM_TextView.setBackground(getBase64NinePatchDrawable("iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6/NlyAAAAGG5wT2wCAAAAAAAAAAAAAAAAAAAAQYLaQeYAAAAAv7tmAAAAPG5wVGMAAgIDIAAAACgAAAAAAAAXAAAADwAAAAsAAAALMAAAAAAAAB8AAAAkAAAAAAAAADwAAAAB5gAAAAAAAAF3ntyjAAACAElEQVRo3u3bsWsTURwH8E9aGgexQzsogW5COim4CBKUDnVRWvwTHDq7ODqpi+Cig6KbYHUQwQjOImQRpINbQBwM1qkdIlksgsMlcASbXNIS7t6975bjOPjwfneXe+/9Ko6eGm6ggTqWUDW79PALP9HCe3QOO7lyROgdXMOCfOUd7mP3uMDreIKT8psebqOZPjg/xYW28GjGZTtNqriOLnamBW/ioWJlDd/QnrSka/iU8zIeVd5XsDvJCD/AOcVMFafxIesIr+Cz4ufiXMYTN4SRjazgRiDgRlZwPRBwPSt4KRDwclZwNRDwwpySJYIjOIIjOIIjOILzDb4XErgy5u/k44A+DUeCF/FMMi0idHANL7Ea4j08DF7FK5wpw0PrkmTGPlhsGrzZH9nFMryWtvA0oI/8seCuZKK6FBk8tM7ieahP5nQGKw/7eI1TuFAGMPzFR8nC02WcKMN7eJCVfomfD3mE0+niTYglnmUxbR0vygTmP3sl4gRABEdwBEdwBEdwBEdwBE8H/hOI9yAreD8Q8F5WcDsQcDsruBUIuBU3lx6SjqGt9AVME524QXxEfuOHpI+gaLmFL0ze89CWTPCtFQh7F9uDH9N0tezgK67K93pUrz+y2+mD81Ne7DveSvoI8rg808TNQRmnUzmGiw+34i2bbafaAfb6t9vYVrx/msNPM376M/gAAAAASUVORK5CYII="));
            BM_TextView.setTextSize((int) SizeUtil.dp2px(Context, 4.4f));//13
            BM_TextView.setGravity(Gravity.CENTER);
            BM_TextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        if (State) {
//            try {
//                BM_ImageView.setImageDrawable(getAssetsImage(Context, "FMP_Resources/Image/ic_tip_positive.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            BM_ImageView.setImageDrawable(getBase64Image("iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAMAAAANIilAAAAAk1BMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACKqXJLWz4CAgEHCAVNXkB5lGQpMiIRFQ6GpG9sg1gkLB0YHhSBnmp1kGFwil0wOicLDglzjV9ieFBGVzpEUzg+TDMsNiUfJhl+mmhfdE9WaUdSZUQ5Ry80QCtkelLbpxlRAAAAEXRSTlMAHvOnd/nkWLm4cE4g+9ciIZ8dCwUAAAF6SURBVEjH7dfJboMwEAZgsyak6TKOMWZLIPuevv/TtQkgxBIb2z3k0P9o8ckympFmUBnDHrsODIj54dlfhamo9Q4SIZZR2zcTJGOOKjshIB0yKeyosLL6cbdhglLM+7stUIyFkEFUMTGQDcqxkaeOPeSqYxc56thBoJG/xUu2T6Enhz3zRTjPMJ5TAq345wjjmPHxDT8St+2qOGc8HOIyO2iElscRD18rnDULcV6d8zDFZeak8RPxELzDZRacm4Vv3kIjF/Gb66sz0vrbQXGeczFcfz+Lzn6ndE73t+SiCvOTpLfC0iTplM4LNcY/lsTJjM56Q7cHEWaYk5sAr3n4W4ApD1MBTnn4IMCQPbcrEOH9c8yEGE7P7BrE2F/028AfgCEM+uwxHFaey7hr43BwbW+ilt3INEa4CWp5pKlkVxG2Xa+CYBFfcv8F+1lnoNEbpcbq2NMbH6fqg+tUZ2TWGtZ11gSdBUVvNaqXMiIG3aVMeh103LH9WaIfSnDtYZzWgL4AAAAASUVORK5CYII="));

        } else {
//            try {
//                BM_ImageView.setImageDrawable(getAssetsImage(Context, "FMP_Resources/Image/ic_tip_negative.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            BM_ImageView.setImageDrawable(getBase64Image("iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAMAAAANIilAAAAAk1BMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACKqXIEBgSHpW+DoW1uh1swOyccIheAnWp7mGZ0jmBrglhTZUQkLB15lGRXakdGVjomLh8WGxISFg9DUjc4RS4tOCYqMyJ9mWdccUxOYEA/TTQ8SjIzPypogFYMDwrOFJ7rAAAAEXRSTlMAHvvzd+Sn+bm4cFf4WU7Xpf5WFuwAAAFnSURBVEjH7ZfnasMwFEblGTtJhyzHe+/M9v2frvFQsVxQLIlCCzk/P+7BFleCe8GEpBraHq5AeTNVCcyRdBkyIOsz/VWBjCgv2N3KkJ3t6O4gF7vhvAqfrPTnfoec6ABIMq8sS0CF3KjA5JdNoPHLGtjzyxsABXjKorJbXzz4E+9Su49kL7TuxHBJ1sehR5Ura+S8qIrH2K5o8smaKMn/wXFGk31cdSXiGsdHmnzAVTkR5zg+0OQQV3VkB3Ds0OTbVJSSReg45S21VdlQ45fLLo/niR9cEjcKgriCS8o4CD7cP3y3nzK/XHU3t/jkkdE1Gt/CKWGWu9T6JkNscmPNcRCLXFgkEYOM/N6w/dAJfXuwm/VyM7hO3hZt7gyvMUCrZad3o6QXUBIN3y5Wy+d7dYo7lKR9w8r/cz1/VxYaaMRGKYNfNoXGR7HBFei8si40rIusCSILiuBqJL6U4XVws+pOaoaK1S+gCuu4pj4rYQAAAABJRU5ErkJggg=="));
        }
        Toast BM_Toast = new Toast(Context);
        BM_Toast.setView(BM_Layout);
        BM_Toast.setGravity(81, 0, (int) SizeUtil.dp2px(Context, 58.0f));
        BM_TextView.setText(Msg);
        BM_Toast.setDuration(State ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        BM_Toast.show();
    }

    public static Drawable getBase64Image(String base64Arr) {
        byte[] bytes = Base64.decode(base64Arr, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return new BitmapDrawable(bitmap);
    }

    public static NinePatchDrawable getBase64NinePatchDrawable(String base64Arr) {
        byte[] bytes = Base64.decode(base64Arr, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        byte[] chunk = bitmap.getNinePatchChunk();
        return new NinePatchDrawable(bitmap, chunk, new Rect(), null);
    }

    public static void Show_Toast(Context ctx, String str) {
        Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
        //获取系统时间
        long systemTime = System.currentTimeMillis();
        if (DefaultToast != null) {
            if (System.currentTimeMillis() - time <= 1000) {
                ToastString.append(AppConfig.Newline);
                ToastString.append(str);
                DefaultToast.setText(ToastString.toString());
                DefaultToast.setDuration(Toast.LENGTH_LONG);
                time = systemTime;
                DefaultToast.show();
            }
        }
        //清除字符串
        ToastString.setLength(0);
        ToastString = new StringBuilder();
        DefaultToast = Toast.makeText(ctx, str, Toast.LENGTH_LONG);
        ToastString.append(str);
        time = systemTime;
        DefaultToast.show();
    }


}
