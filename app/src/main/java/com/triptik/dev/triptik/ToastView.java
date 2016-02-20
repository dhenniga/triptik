package com.triptik.dev.triptik;

        import android.app.Activity;
        import android.graphics.Typeface;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;


public class ToastView extends Activity {

    Typeface RalewayMedium, RalewayLight;

    public void Toast (String toastTextString) {

        RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView toastText = (TextView) layout.findViewById(R.id.tvCustomToast);
        toastText.setTypeface(RalewayLight);
        toastText.setText(toastTextString);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
