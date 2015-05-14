package org.satorysoft.cotton.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.DaggerService;

import java.lang.reflect.ParameterizedType;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static org.satorysoft.cotton.util.DaggerService.createComponent;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
public abstract class MortarActivity<T> extends AppCompatActivity {
    public abstract String getScopeName();
    public void setCustomActionBarTitle(String title){
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_action_bar_title, null);
        ((RobotoTextView) v.findViewById(R.id.text_custom_action_bar_title)).setText(title);
        getSupportActionBar().setCustomView(v);
    }
    private Class<T> mComponentClass;

    /**
     * Get activity component class
     * @return generic class
     */
    @SuppressWarnings("unchecked")
    private Class<T> getComponentClass(){
        final ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Builds a mortar scope
     * @param activityScope mortar scope
     * @return built scope
     */
    private MortarScope buildMortarScope(MortarScope activityScope) {
        this.mComponentClass = getComponentClass();
        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, createComponent(mComponentClass))
                    .build(getScopeName());
        }
        return activityScope;
    }

    /**
     * If we want to build mortar scope for our activity we need override this method in activity
     * @param name name
     * @return object
     */
    @Override
    public Object getSystemService(String name) {
        MortarScope activityScope = findChild(getApplicationContext(), getScopeName());

        activityScope = buildMortarScope(activityScope);

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }
}
