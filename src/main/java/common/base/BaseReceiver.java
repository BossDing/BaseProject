package common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

/**
 * ******************(^_^)***********************<br>
 * User: 11776610771@qq.com<br>
 * Date: 2018/6/2<br>
 * Time: 12:40<br>
 * <P>DESC:
 * 广播接收者的通用类，可批量给该接收者添加要接收的对应的actions
 * </p>
 * ******************(^_^)***********************
 */
public abstract class BaseReceiver<I extends BaseReceiver<I>> extends BroadcastReceiver {
    protected final String TAG = getClass().getSimpleName();
    /**
     * 本广播接收者基类，只有一个IntentFilter，但广播接收者是可以有多个IntentFilter的
     */
    protected IntentFilter mIntentFilter;

    public I withAction(String action,String dataType) {
        if (action != null) {
            if (mIntentFilter == null) {
                mIntentFilter = new IntentFilter();
            }
            mIntentFilter.addAction(action);
            if (!TextUtils.isEmpty(dataType)) {
                try {
                    mIntentFilter.addDataType(dataType);
                } catch (IntentFilter.MalformedMimeTypeException e) {
                    e.printStackTrace();
                }
            }
        }
        return self();
    }

    public I withAction(String action) {
        return withAction(action, null);
    }
    public I withActions(String... actions) {
        if (actions != null && actions.length > 0) {
            for (String theAction : actions) {
                withAction(theAction, null);
            }
        }
        return self();
    }
    public void register(Context context) {
        context.registerReceiver(this, mIntentFilter);
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }

    public IntentFilter getInnerIntentFilter() {
        return mIntentFilter;
    }

    protected final I self() {
        return (I) this;
    }

}
